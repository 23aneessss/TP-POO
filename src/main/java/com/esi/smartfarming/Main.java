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

        // ── Initialisation de base ─────────────────────────────────────────
        System.out.print("Nom de la zone culture  : ");
        ZoneCulture zoneCulture = new ZoneCulture("ZC-001", sc.nextLine().trim());

        System.out.print("Nom de la zone elevage  : ");
        ProgrammeAlimentation progElevage = new ProgrammeAlimentation("Foin", 5.0);
        ZoneElevage zoneElevage = new ZoneElevage("ZE-001", sc.nextLine().trim(), progElevage);

        System.out.print("Nom de la zone aquacole : ");
        ProgrammeAlimentation progAquacole = new ProgrammeAlimentation("Granules", 0.5);
        ZoneAquacole zoneAquacole = new ZoneAquacole("ZA-001", sc.nextLine().trim(), progAquacole);

        System.out.println("\nZones creees avec succes.");

        // ── Menu principal ─────────────────────────────────────────────────
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

    // =========================================================================
    // ZONE CULTURE
    // =========================================================================

    static void menuCulture(ZoneCulture zone) {
        boolean back = false;
        while (!back) {
            titre("ZONE CULTURE : " + zone.getNom());
            System.out.println("  1. Ajouter une culture");
            System.out.println("  2. Changer le stade d'une culture");
            System.out.println("  3. Ajouter capteur environnemental");
            System.out.println("  4. Ajouter capteur sol");
            System.out.println("  5. Envoyer releves des capteurs");
            System.out.println("  6. Enregistrer un rendement");
            System.out.println("  7. Rapport des cultures");
            System.out.println("  8. Suspendre / Activer la zone");
            System.out.println("  9. Retour");
            System.out.print("Choix : ");

            switch (lireInt()) {
                case 1: ajouterCulture(zone); break;
                case 2: changerStageCulture(zone); break;
                case 3: ajouterCapteurEnv(zone); break;
                case 4: ajouterCapteurSol(zone); break;
                case 5: envoyerRelevesCulture(zone); break;
                case 6: enregistrerRendement(zone); break;
                case 7: System.out.println(zone.genererRapportCultures()); break;
                case 8: toggleZone(zone); break;
                case 9: back = true; break;
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
        System.out.print("Temperature actuelle (°C) : ");  double temp = lireDouble();
        System.out.print("Humidite actuelle (%) : ");       double hum  = lireDouble();
        System.out.print("Pluviometrie actuelle (mm) : ");  double pluv = lireDouble();
        System.out.print("Seuil min temperature : ");       double sMin = lireDouble();
        System.out.print("Seuil max temperature : ");       double sMax = lireDouble();

        CapteurEnvironnemental cap = new CapteurEnvironnemental(code, zone, sMin, sMax, "°C", temp, hum, pluv);
        zone.ajouterCapteurEnv(cap);
        System.out.println("Capteur environnemental [" + code + "] ajoute.");
    }

    static void ajouterCapteurSol(ZoneCulture zone) {
        String code = "CS-" + String.format("%03d", capteurIdCounter++);
        System.out.print("pH actuel : ");              double ph   = lireDouble();
        System.out.print("Humidite sol (%) : ");       double hum  = lireDouble();
        System.out.print("Teneur azote (mg/kg) : ");   double azote = lireDouble();
        System.out.print("Seuil min pH : ");            double sMin = lireDouble();
        System.out.print("Seuil max pH : ");            double sMax = lireDouble();

        CapteurSol cap = new CapteurSol(code, zone, sMin, sMax, "pH", ph, hum, azote);
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

    // =========================================================================
    // ZONE ELEVAGE
    // =========================================================================

    static void menuElevage(ZoneElevage zone) {
        boolean back = false;
        while (!back) {
            titre("ZONE ELEVAGE : " + zone.getNom());
            System.out.println("  1. Ajouter un animal (Ruminant ou Volaille)");
            System.out.println("  2. Enregistrer une production");
            System.out.println("  3. Ajouter un evenement sanitaire");
            System.out.println("  4. Verifier les limites de capacite");
            System.out.println("  5. Ajouter capteur biometrique a un animal");
            System.out.println("  6. Ajouter capteur GPS a un animal");
            System.out.println("  7. Afficher les animaux");
            System.out.println("  8. Suspendre / Activer la zone");
            System.out.println("  9. Retour");
            System.out.print("Choix : ");

            switch (lireInt()) {
                case 1: ajouterAnimal(zone); break;
                case 2: enregistrerProduction(zone); break;
                case 3: ajouterEvenementSanitaire(zone); break;
                case 4: verifierLimites(zone); break;
                case 5: ajouterCapteurBio(zone); break;
                case 6: ajouterCapteurGPS(zone); break;
                case 7: afficherAnimaux(zone); break;
                case 8: toggleZone(zone); break;
                case 9: back = true; break;
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
        System.out.print("Temperature corporelle (°C) : "); double temp = lireDouble();
        System.out.print("Niveau d'activite (0.0-1.0) : "); double act  = lireDouble();
        System.out.print("Seuil min temp : ");               double sMin = lireDouble();
        System.out.print("Seuil max temp : ");               double sMax = lireDouble();

        CapteurBiometrique cap = new CapteurBiometrique(code, zone, sMin, sMax, "°C", animal, temp, act);
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
        CapteurGPS cap = new CapteurGPS(code, zone, 0.0, 180.0, animal);
        animal.setCapteurGPS(cap);

        ReleveGPS r = cap.envoyerReleve();
        System.out.printf("Capteur GPS [%s] ajoute. Position : lat=%.5f lon=%.5f%n",
                code, r.getLatitude(), r.getLongitude());
    }

    static void afficherAnimaux(ZoneElevage zone) {
        if (zone.getAnimaux().isEmpty()) { System.out.println("  Aucun animal."); return; }
        System.out.println("Animaux (" + zone.getAnimaux().size() + ") :");
        int i = 1;
        for (var a : zone.getAnimaux()) {
            String type = (a instanceof Ruminant) ? "Ruminant" : "Volaille";
            System.out.println("  " + i++ + ". #" + a.getNumero() + " " + a.getEspece()
                    + " [" + type + "] | " + a.getPoids() + "kg | " + a.getEtatSante());
        }
    }

    // =========================================================================
    // ZONE AQUACOLE
    // =========================================================================

    static void menuAquacole(ZoneAquacole zone) {
        boolean back = false;
        while (!back) {
            titre("ZONE AQUACOLE : " + zone.getNom());
            System.out.println("  1. Ajouter une espece aquacole");
            System.out.println("  2. Ajouter capteur eau");
            System.out.println("  3. Envoyer releves des capteurs");
            System.out.println("  4. Modifier programme alimentation");
            System.out.println("  5. Afficher especes");
            System.out.println("  6. Suspendre / Activer la zone");
            System.out.println("  7. Retour");
            System.out.print("Choix : ");

            switch (lireInt()) {
                case 1: ajouterEspece(zone); break;
                case 2: ajouterCapteurEau(zone); break;
                case 3: envoyerRelevesAquacole(zone); break;
                case 4: modifierProgAlimentation(zone); break;
                case 5: afficherEspeces(zone); break;
                case 6: toggleZone(zone); break;
                case 7: back = true; break;
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
        System.out.print("Temperature eau (°C) : ");     double temp = lireDouble();
        System.out.print("Taux oxygene (mg/L) : ");      double oxy  = lireDouble();
        System.out.print("pH de l'eau : ");               double ph   = lireDouble();
        System.out.print("Type capteur (ex: Polyvalent) : "); String type = sc.nextLine().trim();
        System.out.print("Seuil min temperature : ");    double sMin = lireDouble();
        System.out.print("Seuil max temperature : ");    double sMax = lireDouble();

        CapteurEau cap = new CapteurEau(code, zone, sMin, sMax, "°C", temp, oxy, ph, type);
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

    static void afficherEspeces(ZoneAquacole zone) {
        if (zone.getEspeces().isEmpty()) { System.out.println("  Aucune espece."); return; }
        System.out.println("Especes (" + zone.getEspeces().size() + ") :");
        for (var e : zone.getEspeces())
            System.out.println("  " + e.getId() + ". " + e.getEspece() + " | " + e.getNombre() + " individus");
    }

    // =========================================================================
    // ALERTES
    // =========================================================================

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

    // =========================================================================
    // UTILITAIRES
    // =========================================================================

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
}
