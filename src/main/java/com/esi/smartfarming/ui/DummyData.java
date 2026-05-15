package com.esi.smartfarming.ui;

public class DummyData {

    // [code, nom, type, statut, entites, nb_capteurs]
    public static final String[][] ZONES = {
        {"ZC-01", "Zone Nord", "ZoneCulture",  "ACTIVE",    "2 cultures", "3"},
        {"ZE-01", "Zone Est",  "ZoneElevage",  "ACTIVE",    "5 animaux",  "4"},
        {"ZA-01", "Zone Sud",  "ZoneAquacole", "SUSPENDUE", "1 espece",   "1"},
    };

    // [id, nom, famille, stade, pHMin, pHMax, humMin, humMax]
    public static final String[][] CULTURES = {
        {"1", "Ble",     "CEREALE", "CROISSANCE", "6.0", "7.0", "40%", "60%"},
        {"2", "Tomates", "LEGUME",  "MATURITE",   "5.5", "6.8", "60%", "80%"},
    };

    // [numero, espece, type, age, poids, sante]
    public static final String[][] ANIMAUX = {
        {"1", "Vache Holstein",     "Ruminant", "4 ans", "450 kg", "SAIN"},
        {"2", "Vache Charolaise",   "Ruminant", "3 ans", "420 kg", "SAIN"},
        {"3", "Vache Montbeliarde", "Ruminant", "5 ans", "480 kg", "MALADE"},
        {"4", "Poule Leghorn",      "Volaille", "1 an",  "2.5 kg", "SAIN"},
        {"5", "Poule Sussex",       "Volaille", "2 ans", "2.8 kg", "QUARANTAINE"},
    };

    // [code, type, zone, statut, seuilMin, seuilMax, derniere_valeur, niveau_releve]
    public static final String[][] CAPTEURS = {
        {"ENV-01", "Environnemental", "Zone Nord", "ACTIF",      "15.0", "35.0", "22.5 C",        "NORMAL"},
        {"SOL-01", "Sol",             "Zone Nord", "ACTIF",      "6.0",  "7.5",  "pH 6.8",        "NORMAL"},
        {"SOL-02", "Sol",             "Zone Nord", "DEFAILLANT", "6.0",  "7.5",  "pH 7.8",        "CRITIQUE"},
        {"BIO-01", "Biometrique",     "Zone Est",  "ACTIF",      "37.5", "39.5", "38.5 C",        "NORMAL"},
        {"BIO-02", "Biometrique",     "Zone Est",  "ACTIF",      "37.5", "39.5", "39.8 C",        "AVERTISSEMENT"},
        {"GPS-01", "GPS",             "Zone Est",  "ACTIF",      "—",    "—",    "36.73N  3.07E", "NORMAL"},
        {"GPS-02", "GPS",             "Zone Est",  "ACTIF",      "—",    "—",    "36.72N  3.08E", "NORMAL"},
        {"EAU-01", "Eau",             "Zone Sud",  "SUSPENDU",   "18.0", "28.0", "20.0 C",        "NORMAL"},
    };

    // [id, niveau, capteur, zone, description, date, acquittee]
    public static final String[][] ALERTES = {
        {"ALT-001", "CRITIQUE",      "SOL-02", "Zone Nord", "pH hors seuil (7.8 > 7.5)",        "15/05 09:23", "Non"},
        {"ALT-002", "AVERTISSEMENT", "BIO-02", "Zone Est",  "Temperature elevee (39.8 C)",       "15/05 11:15", "Non"},
        {"ALT-003", "CRITIQUE",      "EAU-01", "Zone Sud",  "Capteur suspendu — zone inactive",  "13/05 16:30", "Non"},
        {"ALT-004", "AVERTISSEMENT", "ENV-01", "Zone Nord", "Humidite proche limite haute",      "15/05 08:45", "Oui"},
    };

    // Historique de releves pour le graphique — relevés sur 12 heures
    public static final String[] HEURES = {
        "08h", "09h", "10h", "11h", "12h", "13h", "14h", "15h", "16h", "17h", "18h", "19h"
    };

    // Temperature ENV-01 (seuil 15–35)
    public static final double[] TEMP_ENV01 = {
        18.2, 19.5, 21.0, 22.5, 24.1, 25.8, 26.3, 25.1, 23.7, 22.0, 20.5, 19.8
    };

    // pH SOL-02 (seuil 6.0–7.5 — depasse a partir de 14h)
    public static final double[] PH_SOL02 = {
        6.8, 6.9, 6.8, 7.0, 7.2, 7.4, 7.6, 7.8, 7.5, 7.3, 7.1, 7.0
    };

    // Temperature BIO-02 (seuil 37.5–39.5 — depasse apres 15h)
    public static final double[] TEMP_BIO02 = {
        38.0, 38.2, 38.5, 38.8, 39.0, 39.2, 39.4, 39.8, 39.6, 39.3, 39.1, 38.9
    };
}
