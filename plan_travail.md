# Plan de travail - TP POO : ESI Smart Farming

## 1. Informations generales

- **Projet :** Application de bureau pour la gestion intelligente d'une ferme.
- **Membres du binome :** Anes et Ghano.
- **Mode de travail :** Travail en binome avec une repartition des taches par modules.
- **Organisation :** Seances de travail sur site les lundis, avec un travail a domicile entre les seances.

## 2. Objectif du travail

L'objectif du travail est de concevoir puis d'implementer progressivement le systeme demande dans le sujet du TP **ESI - Smart Farming**.

Le travail sera realise par etapes :

1. Etudier le sujet et valider la conception UML.
2. Creer les classes principales du modele objet.
3. Implementer les modules fonctionnels.
4. Integrer les parties realisees par chaque membre du binome.
5. Tester, corriger et preparer la remise finale.

La conception UML validee servira de reference avant de commencer l'implementation principale.

## 3. Repartition generale des taches

### 3.1. Taches de Anes

Anes prendra en charge les parties suivantes :

- Gestion des zones :
  - ajouter une zone ;
  - modifier une zone ;
  - desactiver ou suspendre une zone ;
  - gerer le statut actif ou suspendu d'une zone.
- Gestion des cultures :
  - enregistrer les cultures ;
  - gerer les familles de cultures ;
  - gerer les dates de plantation et de recolte ;
  - mettre a jour le stade de croissance ;
  - gerer les exigences pedologiques.
- Production des zones :
  - enregistrer le rendement des cultures ;
  - afficher une vue d'ensemble des zones ;
  - afficher le nombre d'entites hebergees dans chaque zone.
- Participation a l'integration generale du projet.

### 3.2. Taches de Ghano

Ghano prendra en charge les parties suivantes :

- Gestion des animaux :
  - enregistrer les animaux ;
  - gerer l'espece, l'age, le poids et l'etat de sante ;
  - consigner les evenements sanitaires ;
  - suivre les evolutions de poids.
- Programmes d'alimentation :
  - definir les programmes d'alimentation pour les zones d'elevage ;
  - definir les programmes d'alimentation pour les zones aquacoles.
- Gestion des capteurs :
  - ajouter et configurer les capteurs ;
  - gerer le type, la zone et les seuils ;
  - changer le statut d'un capteur ;
  - gerer l'historique des releves.
- Gestion des alertes :
  - declencher automatiquement une alerte lorsqu'un releve depasse les seuils ;
  - afficher les alertes actives ;
  - acquitter ou supprimer une alerte ;
  - consulter l'historique des alertes.

### 3.3. Travail commun

Les parties suivantes seront realisees en commun :

- validation de l'UML ;
- creation des classes de base ;
- verification des relations entre les classes ;
- integration finale des modules ;
- tests globaux ;
- correction des erreurs ;
- preparation de la demonstration finale.

## 4. Planning par seances

### 4.1. Avant la premiere seance sur site

Travail prepare par Anes et Ghano :

- relire attentivement le sujet du TP ;
- verifier la conception UML ;
- verifier la liste des classes, interfaces, classes abstraites et enumerations ;
- preparer la repartition des modules ;
- identifier les classes principales a creer pendant la premiere seance.

Objectif de cette etape : arriver a la premiere seance avec une conception claire et une repartition validee.

### 4.2. Seance 1 sur site - Lundi

Travail commun :

- creer la structure du projet ;
- creer les classes principales selon l'UML ;
- mettre en place les attributs principaux ;
- ajouter les constructeurs ;
- ajouter les methodes de base ;
- verifier que les classes respectent les relations prevues dans la conception.

Repartition pendant la seance :

- **Anes :**
  - commence les classes liees aux zones ;
  - commence les classes liees aux cultures ;
  - prepare les methodes de base pour la gestion des zones et des cultures.
- **Ghano :**
  - commence les classes liees aux animaux ;
  - commence les classes liees aux capteurs ;
  - commence les classes liees aux alertes.

Objectif de la seance : obtenir une premiere base de code contenant les classes principales du projet.

### 4.3. Travail a domicile entre les deux lundis

Travail de Anes :

- completer les fonctions de gestion des zones ;
- completer les fonctions de gestion des cultures ;
- ajouter la gestion de la production des zones ;
- tester separement les classes dont il est responsable.

Travail de Ghano :

- completer les fonctions de gestion des animaux ;
- completer les programmes d'alimentation ;
- completer la gestion des capteurs ;
- completer la gestion des alertes ;
- tester separement les classes dont il est responsable.

Travail commun a distance :

- comparer l'avancement de chaque partie ;
- verifier que les noms des classes et methodes restent coherents avec l'UML ;
- preparer les points a integrer pendant la deuxieme seance sur site.

Objectif de cette etape : avancer les modules separement avant l'integration.

### 4.4. Seance 2 sur site - Lundi suivant

Travail commun :

- integrer les modules developpes par Anes et Ghano ;
- verifier les relations entre les zones, les entites, les capteurs et les alertes ;
- corriger les incoherences entre les parties ;
- commencer les tests globaux ;
- verifier que les fonctionnalites principales demandees dans le sujet sont presentes.

Repartition pendant la seance :

- **Anes :**
  - verifie l'integration des zones, cultures et productions ;
  - corrige les problemes lies a ces modules.
- **Ghano :**
  - verifie l'integration des animaux, capteurs et alertes ;
  - corrige les problemes lies a ces modules.

Objectif de la seance : obtenir une version integree du projet.

### 4.5. Travail a domicile apres la deuxieme seance

Travail de Anes :

- finaliser les fonctionnalites liees aux zones ;
- finaliser les fonctionnalites liees aux cultures ;
- verifier les rapports et les affichages associes aux zones.

Travail de Ghano :

- finaliser les fonctionnalites liees aux animaux ;
- finaliser les fonctionnalites liees aux capteurs ;
- finaliser les fonctionnalites liees aux alertes et aux historiques.

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
- aucune fonctionnalite exterieure au sujet n'est ajoutee ;
- la troisieme seance est indiquee comme optionnelle.

## 6. Hypotheses

- Le document est un plan de travail et ne contient pas encore le code du projet.
- La conception UML fournie dans le dossier `tpContext` sert de reference.
- Les deux seances du lundi sont obligatoires.
- La troisieme seance reste optionnelle selon la disponibilite ou la demande du professeur.
- Le travail est realise progressivement par parties, et non en une seule fois.
