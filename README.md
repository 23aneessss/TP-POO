# SmartFarming — TP Programmation Orientée Objet

**ESI — Année 2025/2026**

Système de gestion d'une ferme intelligente (IoT + JavaFX) illustrant les concepts
fondamentaux de la POO : héritage, polymorphisme, interfaces, classes abstraites,
sérialisation et architecture en couches.

---

## Présentation du projet

SmartFarming simule la supervision d'une ferme divisée en **3 zones** :

| Zone | Type | Contenu |
|------|------|---------|
| Zone Nord | `ZoneCulture` | Cultures (Blé, Tomates…), capteurs ENV et Sol |
| Zone Est | `ZoneElevage` | Animaux (Ruminants, Volailles), capteurs Bio et GPS |
| Zone Sud | `ZoneAquacole` | Espèces aquacoles (Tilapia…), capteurs Eau |

Chaque zone peut être **suspendue / réactivée** et est équipée de **capteurs IoT**
qui envoient des relevés. Si une valeur dépasse les seuils, une **alerte** est
automatiquement générée.

---

## Architecture du projet

```
src/main/java/com/esi/smartfarming/
├── Main.java                    ← Interface terminal (menu interactif)
├── alerte/                      ← Alerte (gravité, acquittement, suppression)
├── alimentation/                ← ProgrammeAlimentation
├── animal/                      ← Animal (abstract), Ruminant, Volaille, EspeceAquacole
├── capteur/                     ← Capteur (abstract), CapteurNumerique (abstract)
│   ├── CapteurEnvironnemental   │  température, humidité, pluviométrie
│   ├── CapteurSol               │  pH, humidité sol, azote
│   ├── CapteurBiometrique       │  température corporelle, activité
│   ├── CapteurEau               │  température eau, O₂, pH
│   └── CapteurGPS               │  latitude / longitude (ReleveGPS)
├── core/                        ← Interface Suspendable
├── culture/                     ← Culture (famille, stade de croissance, seuils)
├── data/                        ← DataStore (Singleton, sérialisation)
├── enums/                       ← NiveauGravite, NiveauReleve, StageCroissance,
│                                   StatutAnimal, StatutCapteur, StatutZone, TypeFamille
├── historique/                  ← HistoriqueProduction (abstract), HistoriqueLait,
│                                   HistoriqueOeuf, HistoriqueCulture, HistoriqueAquacole
├── releve/                      ← Releve (abstract), ReleveNumerique, ReleveGPS
├── sanitaire/                   ← EvenementSanitaire
├── zone/                        ← Zone (abstract), ZoneCulture, ZoneElevage, ZoneAquacole
└── ui/                          ← Interface graphique JavaFX
    ├── SmartFarmingApp.java      │  Point d'entrée JavaFX, navigation par onglets
    ├── DashboardView.java        │  Tableau de bord : stats, zones, alertes récentes
    ├── ZonesView.java            │  Gestion complète des 3 zones (CRUD + actions)
    ├── CapteursView.java         │  Liste capteurs + actions + graphique d'évolution
    └── AlertesView.java          │  Panneau des alertes (filtre, acquittement, suppression)
```

---

## Concepts POO illustrés

### 1. Héritage & Classes abstraites
```
Zone (abstract)
  ├── ZoneCulture
  ├── ZoneElevage
  └── ZoneAquacole

Animal (abstract)
  ├── Ruminant   → enregistrerProduction(double litres, Date)
  └── Volaille   → enregistrerProduction(int nbOeufs, Date)

Capteur (abstract) implements Suspendable, Serializable
  └── CapteurNumerique (abstract)
        ├── CapteurEnvironnemental
        ├── CapteurSol
        ├── CapteurBiometrique
        └── CapteurEau
  └── CapteurGPS

Releve (abstract)
  ├── ReleveNumerique
  └── ReleveGPS

HistoriqueProduction (abstract)
  ├── HistoriqueLait
  ├── HistoriqueOeuf
  ├── HistoriqueCulture
  └── HistoriqueAquacole
```

### 2. Interfaces
- **`Suspendable`** — implémentée par `Zone` et `Capteur` : `suspendre()` / `activer()`

### 3. Polymorphisme
- `envoyerReleve()` redéfini dans chaque sous-classe de `CapteurNumerique`
- `verifierLimites(ZoneElevage)` redéfini dans `Ruminant` (max 50) et `Volaille` (max 200)
- `getHistorique()` retourne le bon type selon la zone

### 4. Sérialisation Java (`java.io.Serializable`)
- Toutes les entités du domaine implémentent `Serializable`
- `DataStore` persiste l'état complet dans `data/ferme.dat`
- Les champs JavaFX (`ObservableList`) sont `transient` et reconstruits à la désérialisation

### 5. Singleton
- `DataStore.getInstance()` garantit un seul état cohérent partagé entre toutes les vues

---

## Fonctionnalités de l'interface graphique

### Dashboard
- Statistiques en temps réel (zones actives, capteurs actifs, alertes non acquittées, total animaux)
- Cartes résumées des 3 zones
- 3 dernières alertes

### Zone Nord — Culture
- Suspendre / Activer la zone
- Ajouter une culture (nom, famille, stade, pH min/max, humidité min/max)
- Changer le stade de croissance d'une culture
- Enregistrer le rendement (t/ha)
- Ajouter un capteur Environnemental (9 paramètres)
- Ajouter un capteur Sol (9 paramètres)
- Envoyer les relevés → génère des alertes si hors seuils

### Zone Est — Élevage
- Suspendre / Activer la zone
- Modifier le programme d'alimentation
- Ajouter un animal (Ruminant ou Volaille)
- Enregistrer la production (litres de lait / nombre d'œufs par animal)
- Enregistrer un événement sanitaire (description + nouveau poids)
- Vérifier les limites de capacité
- Ajouter un capteur Biométrique à un animal (7 paramètres)
- Ajouter un capteur GPS à un animal

### Zone Sud — Aquacole
- Suspendre / Activer la zone
- Modifier le programme d'alimentation
- Ajouter une espèce aquacole
- Ajouter un capteur Eau (10 paramètres)
- Envoyer les relevés → génère des alertes si hors seuils

### Capteurs
- Liste complète de tous les capteurs (toutes zones)
- Valeur actuelle, niveau de conformité, seuils
- **Suspendre / Activer** chaque capteur
- **Marquer défaillant** un capteur
- **Envoyer un relevé manuel** (CapteurNumerique → valeur + niveau ; CapteurGPS → lat/lon)
- Graphique d'évolution des relevés

### Alertes
- Tableau de toutes les alertes avec niveau de gravité (CRITIQUE / AVERTISSEMENT)
- Filtres : Toutes | CRITIQUE | AVERTISSEMENT | Acquittées
- Acquitter une alerte (confirme la prise en charge)
- Supprimer une alerte
- Badge du nombre d'alertes actives
- Mises à jour automatiques quand de nouvelles alertes sont créées (via `ObservableList`)

---

## Lancer le projet

### Prérequis
- Java 17 (OpenJDK 17 Homebrew)
- JavaFX SDK 17.0.13 (macOS ARM64)

### Variables d'environnement
```bash
export JAVA=/opt/homebrew/opt/openjdk@17/bin
export FX=/Users/mac/Downloads/javafx-sdk-17.0.13/lib
```

### Compiler
```bash
find src -name "*.java" | xargs $JAVA/javac \
  --module-path $FX \
  --add-modules javafx.controls,javafx.fxml \
  -d out
```

### Lancer l'interface graphique
```bash
$JAVA/java \
  --module-path $FX \
  --add-modules javafx.controls,javafx.fxml \
  -cp out com.esi.smartfarming.ui.SmartFarmingApp
```

### Lancer le menu terminal
```bash
$JAVA/java -cp out com.esi.smartfarming.Main
```

### Réinitialiser les données
```bash
rm -f data/ferme.dat
```
Au prochain démarrage, `DataStore.buildDefault()` recrée les données initiales.

---

## Données par défaut (`buildDefault`)

| Entité | Données initiales |
|--------|-------------------|
| Zone Nord | 2 cultures (Blé CROISSANCE, Tomates MATURITE), 3 capteurs (ENV-01 actif, SOL-01 actif, SOL-02 **défaillant**) |
| Zone Est | 3 Ruminants (dont 1 malade), 2 Volailles (dont 1 en quarantaine), 4 capteurs (BIO-01, BIO-02, GPS-01, GPS-02) |
| Zone Sud | **Suspendue**, 1 espèce (Tilapia 250), 1 capteur EAU-01 suspendu |
| Alertes | 4 alertes initiales (1 CRITIQUE sol, 1 AVERT biométrique, 1 CRITIQUE eau, 1 AVERT env. acquittée) |

---

## Persistance

L'état complet de la ferme est sérialisé dans `data/ferme.dat` à chaque mutation.
Au démarrage, `DataStore.loadOrCreate()` tente de charger ce fichier ; en cas d'échec
(fichier corrompu ou absent), il appelle `buildDefault()` et sauvegarde immédiatement.

---

*Projet réalisé dans le cadre du TP Programmation Orientée Objet — ESI 2025/2026*
