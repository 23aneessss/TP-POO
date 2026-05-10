# Plan de travail - TP POO : ESI Smart Farming

## 1. Informations generales

- **Projet :** Application de bureau pour la gestion intelligente d'une ferme.
- **Membres du binome :** Anes et Ghano.
- **Mode de travail :** Travail en binome avec une repartition des taches par modules.
- **Organisation :** Seances de travail sur site les lundis, avec un travail a domicile entre les seances.

## 2. Objectif du travail

L'objectif du travail est de concevoir puis d'implementer progressivement le systeme demande dans le sujet du TP **ESI - Smart Farming**, selon la conception UML modifiee du dossier `tpContext`.

Le travail sera realise par etapes :

1. Etudier le sujet et valider la conception UML modifiee.
2. Creer la structure complete des packages et des classes du modele objet.
3. Implementer les modules fonctionnels (zones, cultures, animaux, capteurs, releves, alertes, historiques).
4. Integrer les parties realisees par chaque membre du binome.
5. Tester, corriger et preparer la remise finale.

La conception UML modifiee servira de reference avant de commencer l'implementation principale.

## 3. Repartition generale des taches

### 3.1. Taches de Anes

Anes prendra en charge les parties suivantes :

- Classes attribuees a Anes :
  - `Zone` ;
  - `ZoneCulture` ;
  - `ZoneElevage` ;
  - `Culture` ;
  - `Animal` ;
  - `Ruminant` ;
  - `Volaille` ;
  - `ProgrammeAlimentation` ;
  - `EvenementSanitaire` ;
  - `HistoriqueProduction` ;
  - `HistoriqueCulture` ;
  - `HistoriqueLait` ;
  - `HistoriqueOeuf` ;
  - `StatutZone` ;
  - `StatutAnimal` ;
  - `TypeFamille` ;
  - `StageCroissance`.
- Responsabilites fonctionnelles de Anes :
  - gestion des zones de culture ;
  - gestion des zones d'elevage ;
  - gestion complete des cultures ;
  - gestion des animaux d'elevage et du suivi sanitaire ;
  - generation de rapport des cultures ;
  - enregistrement des rendements de culture ;
  - integration des historiques culture, lait et oeufs ;
  - verification des relations entre zones et capteurs.

### 3.2. Taches de Ghano

Ghano prendra en charge les parties suivantes :

- Classes attribuees a Ghano :
  - `ZoneAquacole` ;
  - `EspeceAquacole` ;
  - `Suspendable` ;
  - `Capteur` ;
  - `CapteurNumerique` ;
  - `CapteurEnvironnemental` ;
  - `CapteurSol` ;
  - `CapteurBiometrique` ;
  - `CapteurEau` ;
  - `CapteurGPS` ;
  - `StatutCapteur` ;
  - `Releve` ;
  - `ReleveNumerique` ;
  - `ReleveGPS` ;
  - `NiveauReleve` ;
  - `Alerte` ;
  - `NiveauGravite` ;
  - `HistoriqueAquacole`.
- Responsabilites fonctionnelles de Ghano :
  - gestion des zones aquacoles ;
  - gestion des especes aquacoles ;
  - gestion complete des capteurs et des releves ;
  - gestion des alertes ;
  - integration de l'historique aquacole ;
  - verification de l'interface `Suspendable` et des transitions de statut.

### 3.3. Travail commun

Les parties suivantes seront realisees en commun :

- validation de l'UML modifie ;
- verification de la coherence des packages `com.esi.smartfarming.*` ;
- creation des classes abstraites et interfaces de base ;
- verification des relations entre les classes (heritage, composition, agregation) ;
- integration finale des modules ;
- tests globaux ;
- correction des erreurs ;
- preparation de la demonstration finale.

## 4. Planning par seances

### 4.1. Avant la premiere seance sur site

Travail prepare par Anes et Ghano :

- relire attentivement le sujet du TP ;
- verifier la conception UML modifiee ;
- verifier la liste complete des classes, interface, classes abstraites et enumerations ;
- preparer la repartition des modules ;
- identifier les classes principales a creer pendant la premiere seance.

Objectif de cette etape : arriver a la premiere seance avec une conception claire, une liste de classes complete et une repartition validee.

### 4.2. Seance 1 sur site - Lundi

Travail commun :

- creer la structure du projet et des packages ;
- creer toutes les classes de base selon l'UML modifie ;
- mettre en place les attributs, constructeurs et methodes ;
- verifier les classes abstraites et interfaces (`Zone`, `Capteur`, `Animal`, `Releve`, `HistoriqueProduction`, `Suspendable`) ;
- verifier que les classes respectent les relations prevues dans la conception.

Repartition pendant la seance :

- **Anes :**
  - initialise `zone`, `culture`, `animal`, `sanitaire`, `historique` (culture/lait/oeufs) ;
  - prepare les methodes de base liees aux zones, cultures et animaux d'elevage.
- **Ghano :**
  - initialise `zone aquacole`, `capteur`, `releve`, `alerte` ;
  - prepare les methodes de base liees a l'aquaculture, aux capteurs et aux alertes.

Objectif de la seance : obtenir une base de code complete contenant toutes les classes prevues.

### 4.3. Travail a domicile entre les deux lundis

Travail de Anes :

- completer les fonctions de `ZoneCulture`, `ZoneElevage`, `Culture`, `Animal`, `Ruminant`, `Volaille` ;
- completer `HistoriqueCulture`, `HistoriqueLait`, `HistoriqueOeuf` ;
- verifier l'integration avec les capteurs environnementaux et sol ;
- tester separement les classes dont il est responsable.

Travail de Ghano :

- completer les fonctions de `ZoneAquacole` et `EspeceAquacole` ;
- completer la gestion complete des capteurs et releves ;
- completer `Alerte` et `HistoriqueAquacole` ;
- tester separement les classes dont il est responsable.

Travail commun a distance :

- comparer l'avancement de chaque partie ;
- verifier que les noms des classes et methodes restent coherents avec l'UML ;
- preparer les points a integrer pendant la deuxieme seance sur site.

Objectif de cette etape : avancer les modules separement avant l'integration.

### 4.4. Seance 2 sur site - Lundi suivant

Travail commun :

- integrer les modules developpes par Anes et Ghano ;
- verifier les relations entre zones, cultures, animaux, capteurs, releves et alertes ;
- corriger les incoherences entre les parties ;
- commencer les tests globaux ;
- verifier que les fonctionnalites principales demandees dans le sujet sont presentes.

Repartition pendant la seance :

- **Anes :**
  - verifie l'integration des zones, cultures, animaux d'elevage et historiques associes ;
  - corrige les problemes lies a ces modules.
- **Ghano :**
  - verifie l'integration aquacole, capteurs, releves, alertes et historiques associes ;
  - corrige les problemes lies a ces modules.

Objectif de la seance : obtenir une version integree du projet conforme a la conception modifiee.

### 4.5. Travail a domicile apres la deuxieme seance

Travail de Anes :

- finaliser les fonctionnalites liees a `Zone` / `ZoneCulture` / `ZoneElevage` / `Culture` ;
- finaliser les fonctionnalites de `Animal` / `Ruminant` / `Volaille` / `EvenementSanitaire` ;
- finaliser les methodes de rapport et rendement ;
- verifier les affichages associes aux zones et cultures.

Travail de Ghano :

- finaliser les fonctionnalites liees a `ZoneAquacole` et `EspeceAquacole` ;
- finaliser les fonctionnalites liees aux capteurs et releves ;
- finaliser les alertes et les historiques d'elevage/aquaculture.

Travail commun :

- corriger les bugs detectes pendant les tests ;
- completer les affichages necessaires ;
- verifier les historiques ;
- preparer la demonstration finale ;
- relire le code et nettoyer les parties inutiles.

Objectif de cette etape : finaliser le projet avant la remise.

### 4.6. Seance 3 optionnelle

Cette seance sera utilisee uniquement si elle est programmee ou demandee pour la validation finale.

Travail prevu :

- effectuer une validation finale ;
- lancer les tests complets ;
- verifier le comportement global de l'application ;
- nettoyer le code ;
- preparer la remise finale.

Objectif de la seance : confirmer que le projet est pret a etre presente.

## 5. Verification du document

Avant la remise du plan, les points suivants doivent etre verifies :

- le document est redige en francais ;
- Anes et Ghano ont chacun des responsabilites claires ;
- le travail est organise par etapes ;
- le plan suit l'ordre suivant : conception UML, creation des classes, developpement principal, integration, validation ;
- toutes les classes de la nouvelle structure sont prises en compte ;
- aucune fonctionnalite exterieure au sujet n'est ajoutee ;
- la troisieme seance est indiquee comme optionnelle.

## 6. Hypotheses

- Le document est un plan de travail et ne contient pas encore le code du projet.
- La conception UML modifiee fournie dans le dossier `tpContext` sert de reference principale.
- Les deux seances du lundi sont obligatoires.
- La troisieme seance reste optionnelle selon la disponibilite ou la demande du professeur.
- Le travail est realise progressivement par parties, et non en une seule fois.
- Les classes suivantes doivent exister dans le projet final :
  - `Suspendable` ;
  - `Zone`, `ZoneCulture`, `ZoneElevage`, `ZoneAquacole` ;
  - `Culture` ;
  - `Animal`, `Ruminant`, `Volaille`, `EspeceAquacole` ;
  - `Capteur`, `CapteurNumerique`, `CapteurEnvironnemental`, `CapteurSol`, `CapteurBiometrique`, `CapteurEau`, `CapteurGPS` ;
  - `Releve`, `ReleveNumerique`, `ReleveGPS` ;
  - `Alerte` ;
  - `ProgrammeAlimentation` ;
  - `EvenementSanitaire` ;
  - `HistoriqueProduction`, `HistoriqueCulture`, `HistoriqueLait`, `HistoriqueOeuf`, `HistoriqueAquacole` ;
  - `StatutZone`, `TypeFamille`, `StageCroissance`, `StatutAnimal`, `StatutCapteur`, `NiveauGravite`, `NiveauReleve`.
