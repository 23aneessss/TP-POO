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

import java.util.Date;

public class Main {

    public static void main(String[] args) {

        separateur("INITIALISATION DE LA FERME ESI");

        // ── Programmes d'alimentation ──────────────────────────────────────
        ProgrammeAlimentation progBovins  = new ProgrammeAlimentation("Foin + Concentre", 5.0);
        ProgrammeAlimentation progPoisson = new ProgrammeAlimentation("Granules", 0.5);

        // ── Zones ──────────────────────────────────────────────────────────
        ZoneCulture   zoneCulture  = new ZoneCulture("ZC-001", "Parcelle Ble");
        ZoneElevage   zoneElevage  = new ZoneElevage("ZE-001", "Elevage Bovin-Avicole", progBovins);
        ZoneAquacole  zoneAquacole = new ZoneAquacole("ZA-001", "Bassin Truite", progPoisson);

        System.out.println("Zones creees :");
        System.out.println("  [" + zoneCulture.getCode()  + "] " + zoneCulture.getNom()  + " - " + zoneCulture.getStatut());
        System.out.println("  [" + zoneElevage.getCode()  + "] " + zoneElevage.getNom()  + " - " + zoneElevage.getStatut());
        System.out.println("  [" + zoneAquacole.getCode() + "] " + zoneAquacole.getNom() + " - " + zoneAquacole.getStatut());

        // ═══════════════════════════════════════════════════════════════════
        separateur("ZONE CULTURE : " + zoneCulture.getNom());

        // ── Cultures ───────────────────────────────────────────────────────
        Date today = new Date();
        Culture ble    = new Culture(1, "Ble dur",      TypeFamille.CEREALE, today, today, 6.0, 7.5, 40.0, 70.0);
        Culture tomate = new Culture(2, "Tomate cerise", TypeFamille.LEGUME, today, today, 5.5, 7.0, 50.0, 80.0);

        zoneCulture.ajouterCulture(ble);
        zoneCulture.ajouterCulture(tomate);

        // Avancer le stade de la tomate
        tomate.setStageCroissance(StageCroissance.CROISSANCE);

        System.out.println("Cultures enregistrees :");
        for (Culture c : zoneCulture.getCultures()) {
            System.out.println("  " + c.getNom() + " | Stade: " + c.getStageCroissance());
            System.out.println("    Exigences : " + c.getExigencesPedologiques());
        }

        // ── Capteurs culture ───────────────────────────────────────────────
        CapteurEnvironnemental captEnv = new CapteurEnvironnemental(
                "CE-001", zoneCulture, 10.0, 35.0, "°C",
                22.0, 55.0, 2.0);

        CapteurSol captSol = new CapteurSol(
                "CS-001", zoneCulture, 5.5, 7.5, "pH",
                6.8, 45.0, 120.0);

        zoneCulture.ajouterCapteurEnv(captEnv);
        zoneCulture.ajouterCapteurSol(captSol);

        System.out.println("\nCapteurs zone culture :");
        System.out.println("  [" + captEnv.getCode() + "] Env | Temp=" + captEnv.getTemperature()
                + captEnv.getUnite() + " | Humidite=" + captEnv.getHumidite() + "% | Pluv=" + captEnv.getPluviometrie() + "mm");
        System.out.println("  [" + captSol.getCode() + "] Sol | pH=" + captSol.getPh()
                + " | Humidite=" + captSol.getHumidite() + "% | Azote=" + captSol.getTeneurAzote() + "mg/kg");

        // Envoi de relevés
        ReleveNumerique relEnv = captEnv.envoyerReleve();
        ReleveNumerique relSol = captSol.envoyerReleve();
        System.out.println("\nReleves envoyes :");
        System.out.println("  Env -> valeur=" + relEnv.getValeur() + relEnv.getUnite() + " | niveau=" + relEnv.getNiveau());
        System.out.println("  Sol -> valeur=" + relSol.getValeur() + relSol.getUnite() + " | niveau=" + relSol.getNiveau());

        // Enregistrer le rendement
        System.out.println("\nEnregistrement du rendement :");
        zoneCulture.enregistrerRendement(4.2, today);

        // Rapport cultures
        System.out.println("\n" + zoneCulture.genererRapportCultures());

        // ═══════════════════════════════════════════════════════════════════
        separateur("ZONE ELEVAGE : " + zoneElevage.getNom());

        // ── Animaux ────────────────────────────────────────────────────────
        Ruminant vache = new Ruminant(101, "Bovin", 3, 450.0);
        Volaille poule = new Volaille(201, "Gallus", 1, 2.5);

        zoneElevage.ajouterAnimal(vache);
        zoneElevage.ajouterAnimal(poule);

        System.out.println("Animaux enregistres (" + zoneElevage.getAnimaux().size() + ") :");
        System.out.println("  #" + vache.getNumero() + " " + vache.getEspece()
                + " | Poids=" + vache.getPoids() + "kg | Sante=" + vache.getEtatSante());
        System.out.println("  #" + poule.getNumero() + " " + poule.getEspece()
                + " | Poids=" + poule.getPoids() + "kg | Sante=" + poule.getEtatSante());

        // ── Capteurs animaux ───────────────────────────────────────────────
        CapteurBiometrique captBio = new CapteurBiometrique(
                "CB-001", zoneElevage, 37.5, 39.5, "°C",
                vache, 38.5, 0.7);

        CapteurGPS captGPS = new CapteurGPS("CG-001", zoneElevage, 0.0, 180.0, vache);

        vache.setCapteurBiometrique(captBio);
        vache.setCapteurGPS(captGPS);

        ReleveNumerique relBio = captBio.envoyerReleve();
        ReleveGPS       relGPS = captGPS.envoyerReleve();

        System.out.println("\nCapteurs vache :");
        System.out.println("  Bio  -> tempCorp=" + captBio.getTemperatureCorporelle()
                + captBio.getUnite() + " | niveau=" + relBio.getNiveau());
        System.out.println("  GPS  -> lat=" + String.format("%.5f", relGPS.getLatitude())
                + " lon=" + String.format("%.5f", relGPS.getLongitude()));

        // ── Verification limites ───────────────────────────────────────────
        System.out.println("\nVerification limites :");
        System.out.println("  Vache dans limites : " + vache.verifierLimites(zoneElevage));
        System.out.println("  Poule dans limites : " + poule.verifierLimites(zoneElevage));

        // ── Production ────────────────────────────────────────────────────
        System.out.println("\nEnregistrement productions :");
        vache.enregistrerProduction(18.0, today);
        poule.enregistrerProduction(1, today);

        // ── Evenement sanitaire ───────────────────────────────────────────
        EvenementSanitaire vaccination = new EvenementSanitaire(
                1, vache, "Vaccination FMD", today, 452.0);
        vache.ajouterEvenement(vaccination);
        vache.setPoids(vaccination.getNouveauPoids());

        System.out.println("\nEvenement sanitaire : " + vaccination.getDescription()
                + " | Nouveau poids : " + vache.getPoids() + "kg");

        // ═══════════════════════════════════════════════════════════════════
        separateur("ZONE AQUACOLE : " + zoneAquacole.getNom());

        EspeceAquacole truite = new EspeceAquacole(1, "Truite arc-en-ciel", 500);
        zoneAquacole.ajouterEspece(truite);

        CapteurEau captEau = new CapteurEau(
                "CE-AQ-001", zoneAquacole, 10.0, 22.0, "°C",
                15.0, 8.5, 7.2, "Polyvalent");
        zoneAquacole.ajouterCapteurEau(captEau);

        System.out.println("Espece : " + truite.getEspece() + " | Nombre : " + truite.getNombre());
        System.out.println("Programme : " + zoneAquacole.getProgrammeAlimentation().getTypeAliment()
                + " | " + zoneAquacole.getProgrammeAlimentation().getQuantiteParRepas() + " kg/repas");

        ReleveNumerique relEau = captEau.envoyerReleve();
        System.out.println("Capteur eau -> temp=" + captEau.getTemperateur()
                + captEau.getUnite() + " | O2=" + captEau.getOxygene()
                + "mg/L | pH=" + captEau.getPh() + " | niveau=" + relEau.getNiveau());

        // ═══════════════════════════════════════════════════════════════════
        separateur("ALERTES");

        // Simuler une valeur hors seuil pour declencher une alerte
        captBio.setTemperatureCorporelle(40.5);   // fievre
        ReleveNumerique relFievre = captBio.envoyerReleve();

        if (relFievre.getNiveau() != NiveauReleve.NORMAL) {
            Alerte alerte = new Alerte(1, relFievre, NiveauGravite.CRITIQUE, new Date());
            System.out.println("ALERTE #" + alerte.getId()
                    + " | " + alerte.getNiveauGravite()
                    + " | valeur=" + relFievre.getValeur() + relFievre.getUnite()
                    + " | acquittee=" + alerte.isAcquittee());

            alerte.acquitter();
            System.out.println("Alerte acquittee : " + alerte.isAcquittee());
        } else {
            System.out.println("Aucune alerte critique.");
        }

        // ═══════════════════════════════════════════════════════════════════
        separateur("GESTION DES STATUTS");

        System.out.println("Suspension du capteur environnemental...");
        captEnv.suspendre();
        System.out.println("  [" + captEnv.getCode() + "] statut : " + captEnv.getStatut());

        System.out.println("Suspension de la zone culture...");
        zoneCulture.suspendre();
        System.out.println("  [" + zoneCulture.getCode() + "] statut : " + zoneCulture.getStatut());

        System.out.println("Reactivation de la zone culture...");
        zoneCulture.activer();
        System.out.println("  [" + zoneCulture.getCode() + "] statut : " + zoneCulture.getStatut());

        // ═══════════════════════════════════════════════════════════════════
        separateur("FIN DU SCENARIO");
        System.out.println("Toutes les fonctionnalites ont ete demonstrees avec succes.");
    }

    private static void separateur(String titre) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  " + titre);
        System.out.println("=".repeat(60));
    }
}
