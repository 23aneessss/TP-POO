package com.esi.smartfarming;

import com.esi.smartfarming.alerte.Alerte;
import com.esi.smartfarming.alimentation.ProgrammeAlimentation;
import com.esi.smartfarming.animal.EspeceAquacole;
import com.esi.smartfarming.animal.Ruminant;
import com.esi.smartfarming.animal.Volaille;
import com.esi.smartfarming.capteur.*;
import com.esi.smartfarming.culture.Culture;
import com.esi.smartfarming.enums.*;
import com.esi.smartfarming.releve.ReleveGPS;
import com.esi.smartfarming.releve.ReleveNumerique;
import com.esi.smartfarming.sanitaire.EvenementSanitaire;
import com.esi.smartfarming.zone.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);
    static List<Alerte> alertes = new ArrayList<>();
    static int alerteIdCounter = 1;
    static int capteurIdCounter = 1;

    public static void main(String[] args) {

        titre("BIENVENUE - SmartFarming ESI");

        System.out.print("Nom de la zone culture  : ");
        ZoneCulture zoneCulture = new ZoneCulture("ZC-001", sc.nextLine().trim());

        System.out.print("Nom de la zone elevage  : ");
        ProgrammeAlimentation progElevage = new ProgrammeAlimentation("Foin", 5.0);
        ZoneElevage zoneElevage = new ZoneElevage("ZE-001", sc.nextLine().trim(), progElevage);

        System.out.print("Nom de la zone aquacole : ");
        ProgrammeAlimentation progAquacole = new ProgrammeAlimentation("Granules", 0.5);
        ZoneAquacole zoneAquacole = new ZoneAquacole("ZA-001", sc.nextLine().trim(), progAquacole);

        System.out.println("\nZones creees avec succes.");

        boolean running = true;
        while (running) {
            titre("MENU PRINCIPAL");
            System.out.println("  1. Zone Culture   [" + zoneCulture.getNom() + "] - " + zoneCulture.getStatut());
            System.out.println("  2. Zone Elevage   [" + zoneElevage.getNom() + "] - " + zoneElevage.getStatut());
            System.out.println("  3. Zone Aquacole  [" + zoneAquacole.getNom() + "] - " + zoneAquacole.getStatut());
            System.out.println("  4. Alertes (" + alertes.size() + ")");
            System.out.println("  5. Quitter");
            System.out.print("Choix : ");

            switch (lireInt()) {
                case 1: menuCulture(zoneCulture); break;
                case 2: menuElevage(zoneElevage); break;
                case 3: menuAquacole(zoneAquacole); break;
                case 4: afficherAlertes(); break;
                case 5: running = false; break;
                default: System.out.println("Choix invalide.");
            }
        }

        System.out.println("\nAu revoir !");
        sc.close();
    }

    static void menuCulture(ZoneCulture zone) {
        boolean back = false;
        while (!back) {
            titre("ZONE CULTURE : " + zone.getNom() + "  [" + zone.getStatut() + "]");
            System.out.println("  1. Afficher cultures");
            System.out.println("  2. Afficher capteurs");
            System.out.println("  3. Ajouter une culture");
            System.out.println("  4. Changer le stade d'une culture");
            System.out.println("  5. Ajouter capteur environnemental");
            System.out.println("  6. Ajouter capteur sol");
            System.out.println("  7. Envoyer releves des capteurs");
            System.out.println("  8. Enregistrer un rendement");
            System.out.println("  9. Suspendre / Activer la zone");
            System.out.println("  0. Retour");
            System.out.print("Choix : ");

            switch (lireInt()) {
                case 1: afficherDetailsCultures(zone); break;
                case 2: afficherDetailsCapteurs(zone); break;
                case 3: ajouterCulture(zone); break;
                case 4: changerStageCulture(zone); break;
                case 5: ajouterCapteurEnv(zone); break;
                case 6: ajouterCapteurSol(zone); break;
                case 7: envoyerRelevesCulture(zone); break;
                case 8: enregistrerRendement(zone); break;
                case 9: toggleZone(zone); break;
                case 0: back = true; break;
                default: System.out.println("Choix invalide.");
            }
        }
    }

    static void ajouterCulture(ZoneCulture zone) {
        System.out.print("Nom de la culture : ");
        String nom = sc.nextLine().trim();

        System.out.println("Famille (1=CEREALE  2=LEGUME  3=FRUIT) : ");
        TypeFamille famille;
        switch (lireInt()) {
            case 2:  famille = TypeFamille.LEGUME; break;
            case 3:  famille = TypeFamille.FRUIT; break;
            default: famille = TypeFamille.CEREALE;
        }

        System.out.print("pH min : ");   double phMin = lireDouble();
        System.out.print("pH max : ");   double phMax = lireDouble();
        System.out.print("Humidite min (%) : "); double hMin = lireDouble();
        System.out.print("Humidite max (%) : "); double hMax = lireDouble();

        Culture c = new Culture(zone.getCultures().size() + 1, nom, famille,
                new Date(), new Date(), phMin, phMax, hMin, hMax);
        zone.ajouterCulture(c);
        System.out.println("Culture '" + nom + "' ajoutee. Exigences : " + c.getExigencesPedologiques());
    }

    static void changerStageCulture(ZoneCulture zone) {
        if (zone.getCultures().isEmpty()) { System.out.println("Aucune culture."); return; }
        listerCultures(zone);
        System.out.print("Numero de la culture : ");
        int idx = lireInt() - 1;
        if (idx < 0 || idx >= zone.getCultures().size()) { System.out.println("Index invalide."); return; }

        Culture c = zone.getCultures().get(idx);
        System.out.println("Stade actuel : " + c.getStageCroissance());
        System.out.println("Nouveau stade (1=SEMIS  2=GERMINATION  3=CROISSANCE  4=MATURITE  5=RECOLTE) :");
        StageCroissance[] stages = StageCroissance.values();
        int s = lireInt() - 1;
        if (s >= 0 && s < stages.length) {
            c.setStageCroissance(stages[s]);
            System.out.println("Stade mis a jour : " + c.getStageCroissance());
        } else {
            System.out.println("Index invalide.");
        }
    }

    static void ajouterCapteurEnv(ZoneCulture zone) {
        String code = "CE-" + String.format("%03d", capteurIdCounter++);
        System.out.print("Temperature actuelle (°C) : ");   double temp = lireDouble();
        System.out.print("Humidite actuelle (%) : ");        double hum  = lireDouble();
        System.out.print("Pluviometrie actuelle (mm) : ");   double pluv = lireDouble();
        System.out.print("Seuil min temperature (°C) : ");   double tMin = lireDouble();
        System.out.print("Seuil max temperature (°C) : ");   double tMax = lireDouble();
        System.out.print("Seuil min humidite (%) : ");       double hMin = lireDouble();
        System.out.print("Seuil max humidite (%) : ");       double hMax = lireDouble();
        System.out.print("Seuil min pluviometrie (mm) : ");  double pMin = lireDouble();
        System.out.print("Seuil max pluviometrie (mm) : ");  double pMax = lireDouble();

        CapteurEnvironnemental cap = new CapteurEnvironnemental(
                code, zone, "°C", temp, hum, pluv,
                tMin, tMax, hMin, hMax, pMin, pMax);
        zone.ajouterCapteurEnv(cap);
        System.out.println("Capteur environnemental [" + code + "] ajoute.");
    }

    static void ajouterCapteurSol(ZoneCulture zone) {
        String code = "CS-" + String.format("%03d", capteurIdCounter++);
        System.out.print("pH actuel : ");                double ph    = lireDouble();
        System.out.print("Humidite sol (%) : ");         double hum   = lireDouble();
        System.out.print("Teneur azote (mg/kg) : ");     double azote = lireDouble();
        System.out.print("Seuil min pH : ");             double phMin = lireDouble();
        System.out.print("Seuil max pH : ");             double phMax = lireDouble();
        System.out.print("Seuil min humidite (%) : ");   double hMin  = lireDouble();
        System.out.print("Seuil max humidite (%) : ");   double hMax  = lireDouble();
        System.out.print("Seuil min azote (mg/kg) : ");  double aMin  = lireDouble();
        System.out.print("Seuil max azote (mg/kg) : ");  double aMax  = lireDouble();

        CapteurSol cap = new CapteurSol(code, zone, "pH", ph, hum, azote,
                phMin, phMax, hMin, hMax, aMin, aMax);
        zone.ajouterCapteurSol(cap);
        System.out.println("Capteur sol [" + code + "] ajoute.");
    }

    static void envoyerRelevesCulture(ZoneCulture zone) {
        if (zone.getCapteurs().isEmpty()) { System.out.println("Aucun capteur dans cette zone."); return; }
        System.out.println("Releves envoyes :");
        for (var cap : zone.getCapteurs()) {
            if (cap instanceof CapteurNumerique) {
                ReleveNumerique r = ((CapteurNumerique) cap).envoyerReleve();
                System.out.println("  [" + cap.getCode() + "] valeur=" + r.getValeur() + r.getUnite() + " | " + r.getNiveau());
                creerAlertesSiNecessaire(r);
            }
        }
    }

    static void enregistrerRendement(ZoneCulture zone) {
        System.out.print("Rendement (t/ha) : ");
        double v = lireDouble();
        zone.enregistrerRendement(v, new Date());
    }

    static void afficherDetailsCultures(ZoneCulture zone) {
        sousTitre("CULTURES DE [" + zone.getNom() + "]");
        if (zone.getCultures().isEmpty()) { System.out.println("  Aucune culture."); return; }
        int i = 1;
        for (Culture c : zone.getCultures()) {
            System.out.println("  " + i++ + ". " + c.getNom());
            System.out.println("     Famille     : " + c.getFamille());
            System.out.println("     Stade       : " + c.getStageCroissance());
            System.out.println("     Exigences   : " + c.getExigencesPedologiques());
        }
    }

    static void afficherDetailsCapteurs(ZoneCulture zone) {
        sousTitre("CAPTEURS DE [" + zone.getNom() + "]");
        if (zone.getCapteurs().isEmpty()) { System.out.println("  Aucun capteur."); return; }
        int i = 1;
        for (var cap : zone.getCapteurs()) {
            System.out.println("  " + i++ + ". [" + cap.getCode() + "] " + cap.getClass().getSimpleName() + " | " + cap.getStatut());
            if (cap instanceof CapteurEnvironnemental ce) {
                System.out.println("     Temperature   : " + ce.getTemperature() + " °C  (seuils: " + ce.getTempMin() + " - " + ce.getTempMax() + ")");
                System.out.println("     Humidite      : " + ce.getHumidite() + " %  (seuils: " + ce.getHumMin() + " - " + ce.getHumMax() + ")");
                System.out.println("     Pluviometrie  : " + ce.getPluviometrie() + " mm  (seuils: " + ce.getPluvMin() + " - " + ce.getPluvMax() + ")");
            } else if (cap instanceof CapteurSol cs) {
                System.out.println("     pH            : " + cs.getPh() + "  (seuils: " + cs.getPhMin() + " - " + cs.getPhMax() + ")");
                System.out.println("     Humidite sol  : " + cs.getHumidite() + " %  (seuils: " + cs.getHumMin() + " - " + cs.getHumMax() + ")");
                System.out.println("     Azote         : " + cs.getTeneurAzote() + " mg/kg  (seuils: " + cs.getAzoteMin() + " - " + cs.getAzoteMax() + ")");
            }
            if (cap instanceof CapteurNumerique cn)
                System.out.println("     Releves enregistres : " + cn.getHistorique().size());
        }
    }

    static void menuElevage(ZoneElevage zone) {
        boolean back = false;
        while (!back) {
            titre("ZONE ELEVAGE : " + zone.getNom() + "  [" + zone.getStatut() + "]");
            System.out.println("  1. Afficher animaux (details)");
            System.out.println("  2. Ajouter un animal (Ruminant ou Volaille)");
            System.out.println("  3. Enregistrer une production");
            System.out.println("  4. Ajouter un evenement sanitaire");
            System.out.println("  5. Verifier les limites de capacite");
            System.out.println("  6. Ajouter capteur biometrique a un animal");
            System.out.println("  7. Ajouter capteur GPS a un animal");
            System.out.println("  8. Suspendre / Activer la zone");
            System.out.println("  0. Retour");
            System.out.print("Choix : ");

            switch (lireInt()) {
                case 1: afficherDetailsAnimaux(zone); break;
                case 2: ajouterAnimal(zone); break;
                case 3: enregistrerProduction(zone); break;
                case 4: ajouterEvenementSanitaire(zone); break;
                case 5: verifierLimites(zone); break;
                case 6: ajouterCapteurBio(zone); break;
                case 7: ajouterCapteurGPS(zone); break;
                case 8: toggleZone(zone); break;
                case 0: back = true; break;
                default: System.out.println("Choix invalide.");
            }
        }
    }

    static void ajouterAnimal(ZoneElevage zone) {
        System.out.println("Type (1=Ruminant  2=Volaille) :");
        int type = lireInt();
        System.out.print("Espece : ");    String espece = sc.nextLine().trim();
        System.out.print("Age (ans) : "); int age = lireInt();
        System.out.print("Poids (kg) : "); double poids = lireDouble();

        int num = zone.getAnimaux().size() + 1;
        if (type == 2) {
            zone.ajouterAnimal(new Volaille(num, espece, age, poids));
            System.out.println("Volaille #" + num + " '" + espece + "' ajoutee.");
        } else {
            zone.ajouterAnimal(new Ruminant(num, espece, age, poids));
            System.out.println("Ruminant #" + num + " '" + espece + "' ajoute.");
        }
    }

    static void enregistrerProduction(ZoneElevage zone) {
        if (zone.getAnimaux().isEmpty()) { System.out.println("Aucun animal."); return; }
        afficherAnimaux(zone);
        System.out.print("Numero de l'animal : ");
        int idx = lireInt() - 1;
        if (idx < 0 || idx >= zone.getAnimaux().size()) { System.out.println("Index invalide."); return; }

        var animal = zone.getAnimaux().get(idx);
        if (animal instanceof Ruminant) {
            System.out.print("Quantite de lait produit (L) : ");
            ((Ruminant) animal).enregistrerProduction(lireDouble(), new Date());
        } else if (animal instanceof Volaille) {
            System.out.print("Nombre d'oeufs produits : ");
            ((Volaille) animal).enregistrerProduction(lireInt(), new Date());
        }
    }

    static void ajouterEvenementSanitaire(ZoneElevage zone) {
        if (zone.getAnimaux().isEmpty()) { System.out.println("Aucun animal."); return; }
        afficherAnimaux(zone);
        System.out.print("Numero de l'animal : ");
        int idx = lireInt() - 1;
        if (idx < 0 || idx >= zone.getAnimaux().size()) { System.out.println("Index invalide."); return; }

        var animal = zone.getAnimaux().get(idx);
        System.out.print("Description de l'evenement : ");
        String desc = sc.nextLine().trim();
        System.out.print("Nouveau poids (kg) : ");
        double poids = lireDouble();

        int id = animal.getEvenements().size() + 1;
        var ev = new EvenementSanitaire(id, animal, desc, new Date(), poids);
        animal.ajouterEvenement(ev);
        animal.setPoids(poids);
        System.out.println("Evenement enregistre. Poids mis a jour : " + poids + " kg");
    }

    static void verifierLimites(ZoneElevage zone) {
        if (zone.getAnimaux().isEmpty()) { System.out.println("Aucun animal."); return; }
        System.out.println("Verification des limites :");
        for (var a : zone.getAnimaux()) {
            boolean ok = a.verifierLimites(zone);
            System.out.println("  #" + a.getNumero() + " " + a.getEspece()
                    + " -> " + (ok ? "dans les limites" : "LIMITE DEPASSEE"));
        }
    }

    static void ajouterCapteurBio(ZoneElevage zone) {
        if (zone.getAnimaux().isEmpty()) { System.out.println("Aucun animal."); return; }
        afficherAnimaux(zone);
        System.out.print("Numero de l'animal : ");
        int idx = lireInt() - 1;
        if (idx < 0 || idx >= zone.getAnimaux().size()) { System.out.println("Index invalide."); return; }

        var animal = zone.getAnimaux().get(idx);
        String code = "CB-" + String.format("%03d", capteurIdCounter++);
        System.out.print("Temperature corporelle (°C) : ");       double temp  = lireDouble();
        System.out.print("Niveau d'activite (0.0-1.0) : ");       double act   = lireDouble();
        System.out.print("Seuil min temperature corp (°C) : ");    double tMin  = lireDouble();
        System.out.print("Seuil max temperature corp (°C) : ");    double tMax  = lireDouble();
        System.out.print("Seuil min niveau activite : ");          double aMin  = lireDouble();
        System.out.print("Seuil max niveau activite : ");          double aMax  = lireDouble();

        CapteurBiometrique cap = new CapteurBiometrique(code, zone, "°C", animal, temp, act, tMin, tMax, aMin, aMax);
        animal.setCapteurBiometrique(cap);

        ReleveNumerique r = cap.envoyerReleve();
        System.out.println("Capteur [" + code + "] ajoute. Releve : " + r.getValeur() + r.getUnite() + " | " + r.getNiveau());
        creerAlertesSiNecessaire(r);
    }

    static void ajouterCapteurGPS(ZoneElevage zone) {
        if (zone.getAnimaux().isEmpty()) { System.out.println("Aucun animal."); return; }
        afficherAnimaux(zone);
        System.out.print("Numero de l'animal : ");
        int idx = lireInt() - 1;
        if (idx < 0 || idx >= zone.getAnimaux().size()) { System.out.println("Index invalide."); return; }

        var animal = zone.getAnimaux().get(idx);
        String code = "CG-" + String.format("%03d", capteurIdCounter++);
        CapteurGPS cap = new CapteurGPS(code, zone, animal);
        animal.setCapteurGPS(cap);

        ReleveGPS r = cap.envoyerReleve();
        System.out.printf("Capteur GPS [%s] ajoute. Position : lat=%.5f lon=%.5f%n",
                code, r.getLatitude(), r.getLongitude());
    }

    static void afficherAnimaux(ZoneElevage zone) {
        if (zone.getAnimaux().isEmpty()) { System.out.println("  Aucun animal."); return; }
        int i = 1;
        for (var a : zone.getAnimaux()) {
            String type = (a instanceof Ruminant) ? "Ruminant" : "Volaille";
            System.out.println("  " + i++ + ". #" + a.getNumero() + " " + a.getEspece()
                    + " [" + type + "] | " + a.getPoids() + "kg | " + a.getEtatSante());
        }
    }

    static void afficherDetailsAnimaux(ZoneElevage zone) {
        sousTitre("ANIMAUX DE [" + zone.getNom() + "]");
        System.out.println("  Programme alimentation : "
                + zone.getProgrammeAlimentation().getTypeAliment()
                + " | " + zone.getProgrammeAlimentation().getQuantiteParRepas() + " kg/repas");
        if (zone.getAnimaux().isEmpty()) { System.out.println("  Aucun animal."); return; }
        int i = 1;
        for (var a : zone.getAnimaux()) {
            String type = (a instanceof Ruminant) ? "Ruminant" : "Volaille";
            System.out.println("\n  " + i++ + ". " + type + " #" + a.getNumero() + " - " + a.getEspece());
            System.out.println("     Age         : " + a.getAge() + " an(s)");
            System.out.println("     Poids       : " + a.getPoids() + " kg");
            System.out.println("     Etat sante  : " + a.getEtatSante());
            if (a.getCapteurBiometrique() != null) {
                var cb = a.getCapteurBiometrique();
                String dernierNiveau = cb.getHistorique().isEmpty() ? "N/A"
                        : cb.getHistorique().get(cb.getHistorique().size()-1).getNiveau().toString();
                System.out.println("     Capteur bio : [" + cb.getCode() + "] "
                        + cb.getTemperatureCorporelle() + cb.getUnite()
                        + " | activite=" + cb.getNiveauActivite()
                        + " | dernier releve=" + dernierNiveau);
            } else {
                System.out.println("     Capteur bio : aucun");
            }
            if (a.getCapteurGPS() != null) {
                var cg = a.getCapteurGPS();
                if (!cg.getHistoriqueGPS().isEmpty()) {
                    var last = cg.getHistoriqueGPS().get(cg.getHistoriqueGPS().size()-1);
                    System.out.printf("     Capteur GPS : [%s] lat=%.5f lon=%.5f%n",
                            cg.getCode(), last.getLatitude(), last.getLongitude());
                } else {
                    System.out.println("     Capteur GPS : [" + cg.getCode() + "] aucun releve");
                }
            } else {
                System.out.println("     Capteur GPS : aucun");
            }
            if (a.getEvenements().isEmpty()) {
                System.out.println("     Evenements  : aucun");
            } else {
                System.out.println("     Evenements  : " + a.getEvenements().size());
                for (var ev : a.getEvenements())
                    System.out.println("       - " + ev.getDescription() + " (" + ev.getNouveauPoids() + " kg)");
            }
        }
    }

    static void menuAquacole(ZoneAquacole zone) {
        boolean back = false;
        while (!back) {
            titre("ZONE AQUACOLE : " + zone.getNom() + "  [" + zone.getStatut() + "]");
            System.out.println("  1. Afficher details (especes + capteurs)");
            System.out.println("  2. Ajouter une espece aquacole");
            System.out.println("  3. Ajouter capteur eau");
            System.out.println("  4. Envoyer releves des capteurs");
            System.out.println("  5. Modifier programme alimentation");
            System.out.println("  6. Suspendre / Activer la zone");
            System.out.println("  0. Retour");
            System.out.print("Choix : ");

            switch (lireInt()) {
                case 1: afficherDetailsAquacole(zone); break;
                case 2: ajouterEspece(zone); break;
                case 3: ajouterCapteurEau(zone); break;
                case 4: envoyerRelevesAquacole(zone); break;
                case 5: modifierProgAlimentation(zone); break;
                case 6: toggleZone(zone); break;
                case 0: back = true; break;
                default: System.out.println("Choix invalide.");
            }
        }
    }

    static void ajouterEspece(ZoneAquacole zone) {
        System.out.print("Nom de l'espece : ");  String nom = sc.nextLine().trim();
        System.out.print("Nombre initial : ");   int nb = lireInt();
        zone.ajouterEspece(new EspeceAquacole(zone.getEspeces().size() + 1, nom, nb));
        System.out.println("Espece '" + nom + "' ajoutee (" + nb + " individus).");
    }

    static void ajouterCapteurEau(ZoneAquacole zone) {
        String code = "CE-AQ-" + String.format("%03d", capteurIdCounter++);
        System.out.print("Temperature eau (°C) : ");          double temp  = lireDouble();
        System.out.print("Taux oxygene (mg/L) : ");           double oxy   = lireDouble();
        System.out.print("pH de l'eau : ");                    double ph    = lireDouble();
        System.out.print("Type capteur (ex: Polyvalent) : ");  String type  = sc.nextLine().trim();
        System.out.print("Seuil min temperature (°C) : ");     double tMin  = lireDouble();
        System.out.print("Seuil max temperature (°C) : ");     double tMax  = lireDouble();
        System.out.print("Seuil min oxygene (mg/L) : ");       double oMin  = lireDouble();
        System.out.print("Seuil max oxygene (mg/L) : ");       double oMax  = lireDouble();
        System.out.print("Seuil min pH : ");                    double phMin = lireDouble();
        System.out.print("Seuil max pH : ");                    double phMax = lireDouble();

        CapteurEau cap = new CapteurEau(code, zone, "°C", temp, oxy, ph, type,
                tMin, tMax, oMin, oMax, phMin, phMax);
        zone.ajouterCapteurEau(cap);
        System.out.println("Capteur eau [" + code + "] ajoute.");
    }

    static void envoyerRelevesAquacole(ZoneAquacole zone) {
        if (zone.getCapteurs().isEmpty()) { System.out.println("Aucun capteur."); return; }
        System.out.println("Releves envoyes :");
        for (var cap : zone.getCapteurs()) {
            if (cap instanceof CapteurNumerique) {
                ReleveNumerique r = ((CapteurNumerique) cap).envoyerReleve();
                System.out.println("  [" + cap.getCode() + "] valeur=" + r.getValeur() + r.getUnite() + " | " + r.getNiveau());
                creerAlertesSiNecessaire(r);
            }
        }
    }

    static void modifierProgAlimentation(ZoneAquacole zone) {
        System.out.print("Type d'aliment : ");
        String type = sc.nextLine().trim();
        System.out.print("Quantite par repas (kg) : ");
        double q = lireDouble();
        zone.setProgrammeAlimentation(new ProgrammeAlimentation(type, q));
        System.out.println("Programme mis a jour.");
    }

    static void afficherDetailsAquacole(ZoneAquacole zone) {
        sousTitre("DETAILS ZONE [" + zone.getNom() + "]");
        var prog = zone.getProgrammeAlimentation();
        System.out.println("  Programme : " + prog.getTypeAliment()
                + " | " + prog.getQuantiteParRepas() + " kg/repas");

        System.out.println("\n  Especes (" + zone.getEspeces().size() + ") :");
        if (zone.getEspeces().isEmpty()) {
            System.out.println("    Aucune espece.");
        } else {
            for (var e : zone.getEspeces())
                System.out.println("    " + e.getId() + ". " + e.getEspece()
                        + " | " + e.getNombre() + " individus");
        }

        System.out.println("\n  Capteurs eau (" + zone.getCapteurs().size() + ") :");
        if (zone.getCapteurs().isEmpty()) {
            System.out.println("    Aucun capteur.");
        } else {
            for (var cap : zone.getCapteurs()) {
                if (cap instanceof CapteurEau ce) {
                    System.out.println("    [" + ce.getCode() + "] " + ce.getTypeCapture() + " | " + ce.getStatut());
                    System.out.println("      Temperature : " + ce.getTemperateur() + " °C  (seuils: " + ce.getTempMin() + " - " + ce.getTempMax() + ")");
                    System.out.println("      Oxygene     : " + ce.getOxygene() + " mg/L  (seuils: " + ce.getOxyMin() + " - " + ce.getOxyMax() + ")");
                    System.out.println("      pH          : " + ce.getPh() + "  (seuils: " + ce.getPhMin() + " - " + ce.getPhMax() + ")");
                    System.out.println("      Releves     : " + ce.getHistorique().size());
                }
            }
        }
    }

    static void creerAlertesSiNecessaire(ReleveNumerique r) {
        if (r.getNiveau() == NiveauReleve.NORMAL) return;
        NiveauGravite gravite = (r.getNiveau() == NiveauReleve.CRITIQUE)
                ? NiveauGravite.CRITIQUE : NiveauGravite.AVERTISSEMENT;
        Alerte a = new Alerte(alerteIdCounter++, r, gravite, new Date());
        alertes.add(a);
        System.out.println("  ⚠  ALERTE generee ! [" + gravite + "] valeur=" + r.getValeur() + r.getUnite());
    }

    static void afficherAlertes() {
        titre("ALERTES (" + alertes.size() + ")");
        if (alertes.isEmpty()) { System.out.println("  Aucune alerte."); return; }
        int i = 1;
        for (Alerte a : alertes) {
            System.out.println("  " + i++ + ". #" + a.getId()
                    + " | " + a.getNiveauGravite()
                    + " | supprimee=" + a.isSupprimee()
                    + " | acquittee=" + a.isAcquittee());
        }
        System.out.println("\n  1. Acquitter une alerte");
        System.out.println("  2. Supprimer une alerte");
        System.out.println("  3. Retour");
        System.out.print("Choix : ");
        switch (lireInt()) {
            case 1: {
                System.out.print("Numero de l'alerte : ");
                int idx = lireInt() - 1;
                if (idx >= 0 && idx < alertes.size()) {
                    alertes.get(idx).acquitter();
                    System.out.println("Alerte acquittee.");
                }
                break;
            }
            case 2: {
                System.out.print("Numero de l'alerte : ");
                int idx = lireInt() - 1;
                if (idx >= 0 && idx < alertes.size()) {
                    alertes.get(idx).supprimer();
                    System.out.println("Alerte supprimee.");
                }
                break;
            }
        }
    }

    static void toggleZone(com.esi.smartfarming.zone.Zone z) {
        if (z.getStatut() == StatutZone.ACTIVE) {
            z.suspendre();
            System.out.println("Zone suspendue.");
        } else {
            z.activer();
            System.out.println("Zone activee.");
        }
    }

    static void listerCultures(ZoneCulture zone) {
        int i = 1;
        for (Culture c : zone.getCultures())
            System.out.println("  " + i++ + ". " + c.getNom() + " [" + c.getStageCroissance() + "]");
    }

    static int lireInt() {
        while (true) {
            try {
                String line = sc.nextLine().trim();
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.print("Entier attendu : ");
            }
        }
    }

    static double lireDouble() {
        while (true) {
            try {
                String line = sc.nextLine().trim().replace(',', '.');
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.print("Nombre attendu : ");
            }
        }
    }

    static void titre(String t) {
        System.out.println("\n" + "=".repeat(55));
        System.out.println("  " + t);
        System.out.println("=".repeat(55));
    }

    static void sousTitre(String t) {
        System.out.println("\n--- " + t + " ---");
    }
}
