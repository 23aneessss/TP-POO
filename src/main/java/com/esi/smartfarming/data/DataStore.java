package com.esi.smartfarming.data;

import com.esi.smartfarming.alimentation.ProgrammeAlimentation;
import com.esi.smartfarming.alerte.Alerte;
import com.esi.smartfarming.animal.*;
import com.esi.smartfarming.capteur.*;
import com.esi.smartfarming.culture.Culture;
import com.esi.smartfarming.enums.*;
import com.esi.smartfarming.historique.ProductionRecord;
import com.esi.smartfarming.releve.*;
import com.esi.smartfarming.sanitaire.EvenementSanitaire;
import com.esi.smartfarming.zone.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Singleton central de la ferme intelligente.
 * <p>
 * Responsabilités :
 * <ol>
 *   <li><strong>Persistance</strong> — sérialise / désérialise l'état complet
 *       de la ferme dans {@code data/ferme.dat} via Java Object Serialization.</li>
 *   <li><strong>Accès aux données</strong> — point d'entrée unique pour les trois
 *       zones ({@link com.esi.smartfarming.zone.ZoneCulture ZoneCulture},
 *       {@link com.esi.smartfarming.zone.ZoneElevage ZoneElevage},
 *       {@link com.esi.smartfarming.zone.ZoneAquacole ZoneAquacole}) et la liste
 *       des alertes.</li>
 *   <li><strong>Mutations métier</strong> — toutes les opérations CRUD passent
 *       par DataStore qui appelle {@link #save()} après chaque modification.</li>
 *   <li><strong>Observable JavaFX</strong> — {@link #getObservableAlertes()} expose
 *       une {@link javafx.collections.ObservableList} liée à la liste persistante,
 *       afin que les vues JavaFX se mettent à jour automatiquement.</li>
 * </ol>
 *
 * <h3>Cycle de vie</h3>
 * <pre>
 *   DataStore ds = DataStore.getInstance();  // charge ferme.dat ou buildDefault()
 *   ds.ajouterAnimal(a);                     // mutation + save automatique
 * </pre>
 */
public class DataStore implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String FILE = "data/ferme.dat";

    private static transient DataStore INSTANCE;

    // ── Persistent fields ─────────────────────────────────────────────────────
    private List<ZoneCulture>  zonesCulture  = new ArrayList<>();
    private List<ZoneElevage>  zonesElevage  = new ArrayList<>();
    private List<ZoneAquacole> zonesAquacole = new ArrayList<>();
    private List<Alerte> alertes      = new ArrayList<>();
    private int nextAnimalId          = 6;
    private int nextCultureId         = 3;
    private int nextAlerteId          = 5;
    private int nextCapteurSeq        = 10;
    private int nextEspeceId          = 2;
    private int nextZoneSeq           = 2;
    private List<ProductionRecord> productions = new ArrayList<>();
    private List<String> typesAliment = new ArrayList<>(Arrays.asList(
        "Mais broye + luzerne", "Granules flottants", "Foin", "Granules", "Luzerne", "Ensilage"));

    // ── Transient JavaFX binding (rebuilt on first access) ────────────────────
    private transient ObservableList<Alerte> obsAlertes;

    private DataStore() {}

    // ── Defensive deserialization (schema changes between versions) ───────────

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        if (zonesCulture == null || zonesElevage == null || zonesAquacole == null) {
            throw new InvalidObjectException(
                "ferme.dat provient d'un schema incompatible (anciens champs zoneNord/zoneEst/zoneSud) — reinitialisation requise.");
        }
        if (alertes       == null) alertes       = new ArrayList<>();
        if (productions   == null) productions   = new ArrayList<>();
        if (typesAliment  == null) typesAliment  = new ArrayList<>();
        if (nextZoneSeq < 2) nextZoneSeq = 2;
    }

    // ── Singleton ─────────────────────────────────────────────────────────────

    public static DataStore getInstance() {
        if (INSTANCE == null) INSTANCE = loadOrCreate();
        return INSTANCE;
    }

    // ── Persistence ───────────────────────────────────────────────────────────

    private static DataStore loadOrCreate() {
        File f = new File(FILE);
        if (f.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
                System.out.println("[DataStore] Chargement depuis " + f.getAbsolutePath());
                return (DataStore) in.readObject();
            } catch (Exception e) {
                System.err.println("[DataStore] Chargement echoue (" + e.getMessage() + ") — reinitialisation.");
            }
        }
        DataStore ds = buildDefault();
        ds.save();
        return ds;
    }

    public void save() {
        new File("data").mkdirs();
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE))) {
            out.writeObject(this);
        } catch (IOException e) {
            System.err.println("[DataStore] Sauvegarde echouee : " + e.getMessage());
        }
    }

    // ── Observable alertes (transient – rebuilt after deserialization) ─────────

    public ObservableList<Alerte> getObservableAlertes() {
        if (obsAlertes == null)
            obsAlertes = FXCollections.observableArrayList(alertes);
        return obsAlertes;
    }

    // ── Accessors ─────────────────────────────────────────────────────────────

    /** Zone Culture "principale" — la premiere creee, pour compatibilite avec l'existant. */
    public ZoneCulture  getZoneNord() { return zonesCulture.isEmpty()  ? null : zonesCulture.get(0);  }
    public ZoneElevage  getZoneEst()  { return zonesElevage.isEmpty()  ? null : zonesElevage.get(0);  }
    public ZoneAquacole getZoneSud()  { return zonesAquacole.isEmpty() ? null : zonesAquacole.get(0); }
    public List<Alerte> getAlertes()  { return alertes;  }

    public List<ZoneCulture>  getZonesCulture()  { return zonesCulture;  }
    public List<ZoneElevage>  getZonesElevage()  { return zonesElevage;  }
    public List<ZoneAquacole> getZonesAquacole() { return zonesAquacole; }

    public List<Zone> getZones() {
        List<Zone> all = new ArrayList<>();
        all.addAll(zonesCulture);
        all.addAll(zonesElevage);
        all.addAll(zonesAquacole);
        return all;
    }

    public List<Capteur> getAllCapteurs() {
        List<Capteur> all = new ArrayList<>();
        for (Zone z : getZones()) all.addAll(z.getCapteurs());
        return all;
    }

    // ── Mutations: gestion generique des zones (ajout / renommage / desactivation) ────

    public ZoneCulture ajouterZoneCulture(String nom) {
        ZoneCulture z = new ZoneCulture("ZC-" + String.format("%02d", nextZoneSeq++), nom);
        zonesCulture.add(z);
        save();
        return z;
    }

    public ZoneElevage ajouterZoneElevage(String nom, ProgrammeAlimentation prog) {
        ZoneElevage z = new ZoneElevage("ZE-" + String.format("%02d", nextZoneSeq++), nom, prog);
        zonesElevage.add(z);
        save();
        return z;
    }

    public ZoneAquacole ajouterZoneAquacole(String nom, ProgrammeAlimentation prog) {
        ZoneAquacole z = new ZoneAquacole("ZA-" + String.format("%02d", nextZoneSeq++), nom, prog);
        zonesAquacole.add(z);
        save();
        return z;
    }

    public void renommerZone(Zone z, String nouveauNom) {
        z.setNom(nouveauNom);
        save();
    }

    public List<ProductionRecord> getProductions() {
        if (productions == null) productions = new ArrayList<>();
        return productions;
    }

    public List<String> getTypesAliment() {
        if (typesAliment == null) typesAliment = new ArrayList<>();
        return typesAliment;
    }

    public void ajouterTypeAliment(String type) {
        if (type != null && !type.trim().isEmpty() && !getTypesAliment().contains(type.trim())) {
            typesAliment.add(type.trim());
            save();
        }
    }

    public String[] toProductionRow(ProductionRecord pr) {
        String date = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(pr.getDate());
        String valeur = (pr.getValeur() == Math.floor(pr.getValeur()))
            ? String.valueOf((int) pr.getValeur())
            : String.format("%.2f", pr.getValeur());
        return new String[]{ date, pr.getZone(), pr.getType(), pr.getDetail(), valeur + " " + pr.getUnite() };
    }

    // ── ID / code generators ──────────────────────────────────────────────────

    public int nextAnimalId()  { int id = nextAnimalId++;  save(); return id; }
    public int nextCultureId() { int id = nextCultureId++; save(); return id; }
    public int nextAlerteId()  { int id = nextAlerteId++;  save(); return id; }
    public int nextEspeceId()  { int id = nextEspeceId++;  save(); return id; }

    public String nextCapteurCode(String prefix) {
        String code = prefix + String.format("%03d", nextCapteurSeq++);
        save();
        return code;
    }

    // ── Private: add alerte to both lists ─────────────────────────────────────

    private void pushAlerte(Alerte a) {
        alertes.add(a);
        if (obsAlertes != null) obsAlertes.add(a);
    }

    // ── Mutations: Alertes ────────────────────────────────────────────────────

    public void acquitterAlerte(Alerte a) { a.acquitter(); save(); }

    public void supprimerAlerte(Alerte a) {
        alertes.remove(a);
        if (obsAlertes != null) obsAlertes.remove(a);
        save();
    }

    // ── Mutations: Zones ──────────────────────────────────────────────────────

    public void basculerZone(Zone z) {
        if (z.getStatut() == StatutZone.ACTIVE) z.suspendre(); else z.activer();
        save();
    }

    // ── Mutations: Capteurs ───────────────────────────────────────────────────

    public void basculerCapteur(Capteur c) {
        if (c.getStatut() == StatutCapteur.ACTIF) c.suspendre();
        else if (c.getStatut() == StatutCapteur.SUSPENDU) c.activer();
        save();
    }

    public void marquerDefaillantCapteur(Capteur c) {
        c.marquerDefaillant();
        save();
    }

    public void reactiverCapteur(Capteur c) {
        c.activer();
        save();
    }

    // ── Mutations: Zone Culture ───────────────────────────────────────────────

    public void ajouterCulture(ZoneCulture zone, Culture c) {
        zone.ajouterCulture(c);
        save();
    }

    public void changerStageCulture(Culture c, StageCroissance stade) {
        if (stade.ordinal() < c.getStageCroissance().ordinal()) {
            throw new IllegalArgumentException(
                "Retour en arriere impossible : la culture est deja au stade " + c.getStageCroissance() + ".");
        }
        c.setStageCroissance(stade);
        save();
    }

    public void enregistrerRendement(ZoneCulture zone, double rendement) {
        zone.enregistrerRendement(rendement, new Date());
        getProductions().add(new ProductionRecord(new Date(), zone.getNom(), "Rendement culture", "—", rendement, "t/ha"));
        save();
    }

    public void ajouterCapteurEnv(ZoneCulture zone, CapteurEnvironnemental c) {
        zone.ajouterCapteurEnv(c);
        save();
    }

    public void ajouterCapteurSol(ZoneCulture zone, CapteurSol c) {
        zone.ajouterCapteurSol(c);
        save();
    }

    // ── Mutations: Zone Elevage ───────────────────────────────────────────────

    public void ajouterAnimal(ZoneElevage zone, Animal a) {
        zone.ajouterAnimal(a);
        save();
    }

    public void enregistrerProductionRuminant(ZoneElevage zone, Ruminant r, double litres) {
        r.enregistrerProduction(litres, new Date());
        getProductions().add(new ProductionRecord(new Date(), zone.getNom(), "Lait", r.getEspece(), litres, "L"));
        save();
    }

    public void enregistrerProductionVolaille(ZoneElevage zone, Volaille v, int nbOeufs) {
        v.enregistrerProduction(nbOeufs, new Date());
        getProductions().add(new ProductionRecord(new Date(), zone.getNom(), "Oeufs", v.getEspece(), nbOeufs, "oeufs"));
        save();
    }

    public void ajouterEvenementSanitaire(Animal a, String desc, double nouveauPoids) {
        int id = a.getEvenements().size() + 1;
        a.ajouterEvenement(new EvenementSanitaire(id, a, desc, new Date(), nouveauPoids));
        a.setPoids(nouveauPoids);
        save();
    }

    public void ajouterCapteurBio(ZoneElevage zone, Animal animal, CapteurBiometrique c) {
        animal.setCapteurBiometrique(c);
        zone.getCapteurs().add(c);
        save();
    }

    public void ajouterCapteurGPS(ZoneElevage zone, Animal animal, CapteurGPS c) {
        animal.setCapteurGPS(c);
        zone.getCapteurs().add(c);
        save();
    }

    public void modifierProgAlimentationElevage(ZoneElevage zone, String aliment, double quantite) {
        zone.setProgrammeAlimentation(new ProgrammeAlimentation(aliment, quantite));
        ajouterTypeAliment(aliment);
        save();
    }

    // ── Mutations: Zone Aquacole ──────────────────────────────────────────────

    public void ajouterEspeceAquacole(ZoneAquacole zone, EspeceAquacole e) {
        zone.ajouterEspece(e);
        save();
    }

    public void ajouterCapteurEau(ZoneAquacole zone, CapteurEau c) {
        zone.ajouterCapteurEau(c);
        save();
    }

    public void modifierProgAlimentationAquacole(ZoneAquacole zone, String aliment, double quantite) {
        zone.setProgrammeAlimentation(new ProgrammeAlimentation(aliment, quantite));
        ajouterTypeAliment(aliment);
        save();
    }

    // ── Mutations: Envoyer releves + creation alertes ─────────────────────────

    public List<String> envoyerRelevesZone(Zone zone) {
        List<String> rapport = new ArrayList<>();
        for (Capteur cap : zone.getCapteurs()) {
            if (cap instanceof CapteurNumerique && cap.getStatut() == StatutCapteur.ACTIF) {
                ReleveNumerique r = ((CapteurNumerique) cap).envoyerReleve();
                String line = "[" + cap.getCode() + "] " + String.format("%.2f", r.getValeur())
                    + " " + r.getUnite() + " → " + r.getNiveau();
                if (r.getNiveau() != NiveauReleve.NORMAL) {
                    NiveauGravite g = r.getNiveau() == NiveauReleve.CRITIQUE
                        ? NiveauGravite.CRITIQUE : NiveauGravite.AVERTISSEMENT;
                    Alerte a = new Alerte(nextAlerteId++, r, g, new Date());
                    pushAlerte(a);
                    line += "  ⚠ ALERTE creee";
                }
                rapport.add(line);
            }
        }
        save();
        return rapport;
    }

    // ── Diagnostics ───────────────────────────────────────────────────────────

    public String verifierLimitesElevage(ZoneElevage zone) {
        StringBuilder sb = new StringBuilder();
        for (Animal a : zone.getAnimaux()) {
            boolean ok = a.verifierLimites(zone);
            sb.append("#").append(a.getNumero()).append(" ").append(a.getEspece())
              .append("  →  ").append(ok ? "✔ dans les limites" : "✖ LIMITE DEPASSEE")
              .append("\n");
        }
        return sb.length() == 0 ? "Aucun animal." : sb.toString();
    }

    // ── Stats ─────────────────────────────────────────────────────────────────

    public long countZonesActives() {
        return getZones().stream().filter(z -> z.getStatut() == StatutZone.ACTIVE).count();
    }

    public long countCapteursActifs() {
        return getAllCapteurs().stream().filter(c -> c.getStatut() == StatutCapteur.ACTIF).count();
    }

    public long countAlertesActives() {
        return alertes.stream().filter(a -> !a.isAcquittee() && !a.isSupprimee()).count();
    }

    // ── Row converters (domain object → String[] for TableViews) ─────────────

    public String[] toZoneRow(Zone z) {
        String entites;
        if      (z instanceof ZoneCulture)  entites = ((ZoneCulture)  z).getCultures().size() + " culture(s)";
        else if (z instanceof ZoneElevage)  entites = ((ZoneElevage)  z).getAnimaux().size()  + " animaux";
        else if (z instanceof ZoneAquacole) entites = ((ZoneAquacole) z).getEspeces().size()  + " espece(s)";
        else entites = "—";
        return new String[]{
            z.getCode(), z.getNom(), z.getClass().getSimpleName(),
            z.getStatut().name(), entites, String.valueOf(z.getCapteurs().size())
        };
    }

    public String[] toCultureRow(Culture c) {
        return new String[]{
            String.valueOf(c.getId()), c.getNom(), c.getFamille().name(),
            c.getStageCroissance().name(),
            String.valueOf(c.getPHMin()), String.valueOf(c.getPHMax()),
            (int) c.getHumiditeMin() + "%", (int) c.getHumiditeMax() + "%"
        };
    }

    public String[] toAnimalRow(Animal a) {
        String type = (a instanceof Ruminant) ? "Ruminant" : (a instanceof Volaille) ? "Volaille" : "Animal";
        String capteurs = "";
        if (a.getCapteurBiometrique() != null) capteurs += "BIO ";
        if (a.getCapteurGPS() != null) capteurs += "GPS";
        if (capteurs.isEmpty()) capteurs = "—";
        return new String[]{
            String.valueOf(a.getNumero()), a.getEspece(), type,
            a.getAge() + " an(s)", String.format("%.1f kg", a.getPoids()),
            a.getEtatSante().name(), capteurs.trim()
        };
    }

    public String[] toEspeceRow(EspeceAquacole e) {
        return new String[]{
            String.valueOf(e.getId()), e.getEspece(), String.valueOf(e.getNombre())
        };
    }

    public String[] toCapteurRow(Capteur c) {
        String seuilMin = "—", seuilMax = "—", derniereVal = "—", niveau = "NORMAL";
        if (c instanceof CapteurSol) {
            CapteurSol cs = (CapteurSol) c;
            seuilMin = String.valueOf(cs.getPhMin()); seuilMax = String.valueOf(cs.getPhMax());
            derniereVal = String.format("pH %.1f", cs.getPh());
            niveau = cs.verifierPh() ? "NORMAL" : "CRITIQUE";
        } else if (c instanceof CapteurBiometrique) {
            CapteurBiometrique cb = (CapteurBiometrique) c;
            seuilMin = String.valueOf(cb.getTempCorpMin()); seuilMax = String.valueOf(cb.getTempCorpMax());
            derniereVal = String.format("%.1f °C", cb.getTemperatureCorporelle());
            niveau = cb.verifierTemperatureCorporelle() ? "NORMAL" : "AVERTISSEMENT";
        } else if (c instanceof CapteurEnvironnemental) {
            CapteurEnvironnemental ce = (CapteurEnvironnemental) c;
            seuilMin = String.valueOf(ce.getTempMin()); seuilMax = String.valueOf(ce.getTempMax());
            derniereVal = String.format("%.1f °C", ce.getTemperature());
            niveau = ce.verifierTemperature() ? "NORMAL" : "CRITIQUE";
        } else if (c instanceof CapteurEau) {
            CapteurEau ce = (CapteurEau) c;
            seuilMin = String.valueOf(ce.getTempMin()); seuilMax = String.valueOf(ce.getTempMax());
            derniereVal = String.format("%.1f °C", ce.getTemperateur());
            niveau = ce.verifierTemperature() ? "NORMAL" : "CRITIQUE";
        } else if (c instanceof CapteurGPS) {
            List<ReleveGPS> hist = ((CapteurGPS) c).getHistoriqueGPS();
            if (!hist.isEmpty()) {
                ReleveGPS last = hist.get(hist.size() - 1);
                derniereVal = String.format("%.5f N  %.5f E", last.getLatitude(), last.getLongitude());
            }
        }
        String type = c.getClass().getSimpleName().replace("Capteur", "");
        return new String[]{
            c.getCode(), type, c.getZone().getNom(), c.getStatut().name(),
            seuilMin, seuilMax, derniereVal, niveau
        };
    }

    public String[] toAlerteRow(Alerte a) {
        String capteurCode = "—", zone = "—";
        Releve r = a.getReleve();
        if (r != null && r.getCapteur() != null) {
            capteurCode = r.getCapteur().getCode();
            zone = r.getCapteur().getZone().getNom();
        }
        String date = new SimpleDateFormat("dd/MM HH:mm").format(a.getDateHeure());
        return new String[]{
            "ALT-" + String.format("%03d", a.getId()),
            a.getNiveauGravite().name(),
            capteurCode, zone,
            descriptionAlerte(a),
            date,
            a.isAcquittee() ? "Oui" : "Non"
        };
    }

    public static String descriptionAlerte(Alerte a) {
        Releve r = a.getReleve();
        if (r == null) return "Alerte supprimee";
        Capteur c = r.getCapteur();
        if (c instanceof CapteurSol) {
            CapteurSol cs = (CapteurSol) c;
            return String.format("pH hors seuil (%.1f > %.1f)", cs.getPh(), cs.getPhMax());
        }
        if (c instanceof CapteurBiometrique) {
            CapteurBiometrique cb = (CapteurBiometrique) c;
            return String.format("Temperature elevee (%.1f C)", cb.getTemperatureCorporelle());
        }
        if (c instanceof CapteurEau && c.getStatut() == StatutCapteur.SUSPENDU) {
            return "Capteur suspendu — zone inactive";
        }
        if (c instanceof CapteurEnvironnemental) {
            CapteurEnvironnemental ce = (CapteurEnvironnemental) c;
            return String.format("Anomalie environnementale (T=%.1f C, H=%.0f%%)",
                ce.getTemperature(), ce.getHumidite());
        }
        return "Anomalie detectee";
    }

    // ── Default data factory ──────────────────────────────────────────────────

    private static DataStore buildDefault() {
        DataStore ds = new DataStore();

        // Zone Nord – Culture
        ZoneCulture nord = new ZoneCulture("ZC-01", "Zone Nord");
        Culture ble     = new Culture(1, "Ble",     TypeFamille.CEREALE, new Date(), new Date(), 6.0, 7.0, 40.0, 60.0);
        ble.setStageCroissance(StageCroissance.CROISSANCE);
        Culture tomates = new Culture(2, "Tomates", TypeFamille.LEGUME,  new Date(), new Date(), 5.5, 6.8, 60.0, 80.0);
        tomates.setStageCroissance(StageCroissance.MATURITE);
        nord.ajouterCulture(ble);
        nord.ajouterCulture(tomates);

        CapteurEnvironnemental env01 = new CapteurEnvironnemental(
            "ENV-01", nord, "°C", 22.5, 55.0, 0.0, 15.0, 35.0, 30.0, 70.0, 0.0, 50.0);
        CapteurSol sol01 = new CapteurSol(
            "SOL-01", nord, "pH", 6.8, 55.0, 2.1, 6.0, 7.5, 30.0, 70.0, 1.0, 5.0);
        CapteurSol sol02 = new CapteurSol(
            "SOL-02", nord, "pH", 7.8, 55.0, 2.1, 6.0, 7.5, 30.0, 70.0, 1.0, 5.0);
        sol02.marquerDefaillant();
        nord.ajouterCapteurEnv(env01);
        nord.ajouterCapteurSol(sol01);
        nord.ajouterCapteurSol(sol02);
        ds.zonesCulture.add(nord);

        // Zone Est – Elevage
        ProgrammeAlimentation progEst = new ProgrammeAlimentation("Mais broye + luzerne", 15.0);
        ZoneElevage est = new ZoneElevage("ZE-01", "Zone Est", progEst);

        Ruminant vache1 = new Ruminant(1, "Vache Holstein",     4, 450.0);
        Ruminant vache2 = new Ruminant(2, "Vache Charolaise",   3, 420.0);
        Ruminant vache3 = new Ruminant(3, "Vache Montbeliarde", 5, 480.0);
        vache3.setEtatSante(StatutAnimal.MALADE);
        Volaille poule1 = new Volaille(4, "Poule Leghorn", 1, 2.5);
        Volaille poule2 = new Volaille(5, "Poule Sussex",  2, 2.8);
        poule2.setEtatSante(StatutAnimal.QUARANTAINE);
        est.ajouterAnimal(vache1); est.ajouterAnimal(vache2); est.ajouterAnimal(vache3);
        est.ajouterAnimal(poule1); est.ajouterAnimal(poule2);

        CapteurBiometrique bio01 = new CapteurBiometrique(
            "BIO-01", est, "°C", vache1, 38.5, 70.0, 37.5, 39.5, 30.0, 100.0);
        CapteurBiometrique bio02 = new CapteurBiometrique(
            "BIO-02", est, "°C", vache2, 39.8, 70.0, 37.5, 39.5, 30.0, 100.0);
        CapteurGPS gps01 = new CapteurGPS("GPS-01", est, vache1);
        CapteurGPS gps02 = new CapteurGPS("GPS-02", est, vache2);
        vache1.setCapteurBiometrique(bio01); vache1.setCapteurGPS(gps01);
        vache2.setCapteurBiometrique(bio02); vache2.setCapteurGPS(gps02);
        est.getCapteurs().add(bio01); est.getCapteurs().add(bio02);
        est.getCapteurs().add(gps01); est.getCapteurs().add(gps02);
        ds.zonesElevage.add(est);

        // Zone Sud – Aquacole
        ProgrammeAlimentation progSud = new ProgrammeAlimentation("Granules flottants", 2.5);
        ZoneAquacole sud = new ZoneAquacole("ZA-01", "Zone Sud", progSud);
        sud.ajouterEspece(new EspeceAquacole(1, "Tilapia", 250));
        CapteurEau eau01 = new CapteurEau(
            "EAU-01", sud, "°C", 20.0, 7.0, 7.2, "temperature",
            18.0, 28.0, 5.0, 10.0, 6.5, 8.5);
        sud.ajouterCapteurEau(eau01);
        ds.zonesAquacole.add(sud);

        // Alertes initiales
        ds.alertes.add(new Alerte(1, sol02.envoyerReleve(), NiveauGravite.CRITIQUE,
            new Date(System.currentTimeMillis() - 3 * 3600_000L)));
        ds.alertes.add(new Alerte(2, bio02.envoyerReleve(), NiveauGravite.AVERTISSEMENT,
            new Date(System.currentTimeMillis() - 1 * 3600_000L)));
        ds.alertes.add(new Alerte(3, eau01.envoyerReleve(), NiveauGravite.CRITIQUE,
            new Date(System.currentTimeMillis() - 50 * 3600_000L)));
        Alerte alt4 = new Alerte(4, env01.envoyerReleve(), NiveauGravite.AVERTISSEMENT,
            new Date(System.currentTimeMillis() - 4 * 3600_000L));
        alt4.acquitter();
        ds.alertes.add(alt4);

        return ds;
    }
}
