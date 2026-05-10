# Plan de travail ameliore - TP POO Smart Farming (base UML)

## 1. Cible
Construire une base Java conforme au diagramme UML, puis implementer progressivement les regles metier.

## 2. Organisation par modules UML

1. `core` et `enums`
- `Suspendable`
- `StatutZone`, `TypeFamille`, `StageCroissance`, `StatutAnimal`, `StatutCapteur`, `NiveauGravite`, `NiveauReleve`

2. `zone`
- `Zone` (abstraite)
- `ZoneCulture`, `ZoneElevage`, `ZoneAquacole`

3. `culture` et `animal`
- `Culture`
- `Animal` (abstraite), `Ruminant`, `Volaille`, `EspeceAquacole`

4. `capteur` et `releve`
- `Capteur` (abstraite), `CapteurNumerique` (abstraite)
- `CapteurEnvironnemental`, `CapteurSol`, `CapteurBiometrique`, `CapteurEau`, `CapteurGPS`
- `Releve` (abstraite), `ReleveNumerique`, `ReleveGPS`

5. `alerte`, `historique`, `sanitaire`, `alimentation`
- `Alerte`
- `HistoriqueProduction` (abstraite), `HistoriqueCulture`, `HistoriqueLait`, `HistoriqueOeuf`, `HistoriqueAquacole`
- `EvenementSanitaire`
- `ProgrammeAlimentation`

## 3. Sequence de realisation conseillee

1. Phase A: socle
- Creer packages, enums, interfaces, classes abstraites.
- Verifier heritages UML.

2. Phase B: entites principales
- Implementer `Zone*`, `Culture`, `Animal*`, `EspeceAquacole`.
- Ajouter constructeurs + getters/setters UML.

3. Phase C: capteurs et releves
- Implementer `Capteur*` puis `Releve*`.
- Ajouter la logique minimale de collecte et historique.

4. Phase D: alertes, historiques, sanitaire
- Implementer `Alerte`, `Historique*`, `EvenementSanitaire`, `ProgrammeAlimentation`.
- Relier les dependances entre modules.

5. Phase E: validation
- Tests unitaires par module.
- Tests d'integration des relations UML.

## 4. Repartition binome (suggeree)

1. Membre A
- `zone`, `culture`, `historique`.

2. Membre B
- `animal`, `capteur`, `releve`, `alerte`, `sanitaire`, `alimentation`.

3. Travail commun
- Validation UML, conventions de nommage, integration finale, tests globaux.

## 5. Definition of Done

- Toutes les classes UML existent en Java.
- Signatures des methodes conformes au diagramme.
- Relations principales installees (heritage, composition, aggregation).
- Code compilable localement avec JDK configure.
- Jeux de tests minimaux executes sur les cas principaux.
