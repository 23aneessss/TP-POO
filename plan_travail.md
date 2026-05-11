# Plan de travail - TP POO : ESI Smart Farming
## Binome : Anes & Ghano

---

## Etat actuel du projet (11/05/2026)

Toutes les classes ont ete creees et compilent correctement. La structure complete du projet est en place : packages, heritage, interface, enumerations, attributs, constructeurs et getters. Les corps de certaines methodes restent a completer (logique metier, liens entre classes, scenario de demonstration).

---

## Ce qu'il reste a faire (version reduite et realiste)

### Partie Anes

- Completer `HistoriqueCulture.enregistrer()` : afficher ou stocker le rendement avec la date et la zone.
- Completer `HistoriqueLait.enregistrer()` : afficher ou stocker la quantite de lait avec la date.
- Completer `HistoriqueOeuf.enregistrer()` : afficher ou stocker le nombre d'oeufs avec la date.
- Completer `Ruminant.enregistrerProduction()` : creer un `HistoriqueLait` et appeler `enregistrer()`.
- Completer `Volaille.enregistrerProduction()` : creer un `HistoriqueOeuf` et appeler `enregistrer()`.
- Completer `Ruminant.verifierLimites()` et `Volaille.verifierLimites()` : verifier que le nombre d'animaux dans la zone ne depasse pas une limite raisonnable.
- Ajouter `ajouterCapteurEnv()` et `ajouterCapteurSol()` dans `ZoneCulture` si manquant.

### Partie Ghano

- Completer `HistoriqueAquacole.enregistrer()` : afficher ou stocker le poids de recolte avec la date.
- Corriger `Alerte.supprimer()` : la logique actuelle met le releve a null, il faut adapter (marquer comme supprimee ou vider les references).
- Ajouter `getProgrammeAlimentation()` dans `ZoneAquacole` (methode manquante).
- Corriger `CapteurGPS.envoyerReleve()` : les coordonnees sont codees en dur a 0.0, ajouter des parametres reels ou simuler des valeurs.
- Ajouter `ajouterCapteurEau()` dans `ZoneAquacole` si manquant.

### Partie commune

- Creer une classe `Main` avec un scenario complet qui instancie et relie les objets.
- Verifier la compilation globale du projet.
- Preparer une demonstration coherente pour le prof.

---

## Planning des seances restantes

### Seance 1 sur site - Demain (12/05/2026)

**Objectif : completer les corps de methodes et commencer le scenario de demo.**

| Tache | Responsable |
|---|---|
| Completer `enregistrer()` dans HistoriqueCulture, HistoriqueLait, HistoriqueOeuf | Anes |
| Completer `enregistrerProduction()` dans Ruminant et Volaille | Anes |
| Completer `verifierLimites()` dans Ruminant et Volaille | Anes |
| Completer `enregistrer()` dans HistoriqueAquacole | Ghano |
| Corriger `Alerte.supprimer()` et `CapteurGPS.envoyerReleve()` | Ghano |
| Ajouter `getProgrammeAlimentation()` dans ZoneAquacole | Ghano |
| Commencer la classe `Main` avec un premier scenario | Commun |

---

### Travail a domicile entre la seance 1 et la seance 2

**Objectif : finir chaque module et le tester independamment.**

| Tache | Responsable |
|---|---|
| Verifier que la ZoneCulture genere un rapport coherent | Anes |
| Tester le lien Ruminant → HistoriqueLait et Volaille → HistoriqueOeuf | Anes |
| Tester le lien ZoneAquacole → EspeceAquacole → HistoriqueAquacole | Ghano |
| Tester le flux Capteur → Releve → Alerte | Ghano |
| Finaliser et enrichir la classe Main | Commun |

---

### Seance 2 sur site

**Objectif : integration des deux parties et validation du scenario complet.**

| Tache | Responsable |
|---|---|
| Verifier que les zones, animaux et historiques s'enchainent correctement | Anes |
| Verifier que capteurs, releves et alertes s'enchainent correctement | Ghano |
| Executer la classe Main et corriger les erreurs | Commun |
| Verifier la coherence avec le diagramme UML | Commun |

---

### Travail a domicile entre la seance 2 et la seance 3

**Objectif : corrections finales et preparation de la demonstration.**

| Tache | Responsable |
|---|---|
| Nettoyer le code de la partie elevage/culture | Anes |
| Nettoyer le code de la partie capteurs/alertes/aquacole | Ghano |
| Finaliser la classe Main pour la demonstration | Commun |
| Verifier qu'aucune classe ne manque par rapport a l'UML | Commun |

---

### Seance 3 sur site (validation finale)

**Objectif : demonstration au professeur.**

| Tache | Responsable |
|---|---|
| Presenter le scenario de la classe Main | Commun |
| Repondre aux questions du prof sur les classes dont chacun est responsable | Anes / Ghano |
| Corriger les remarques sur place si necessaire | Commun |

---

## Repartition des responsabilites par module

| Module | Responsable |
|---|---|
| Zone, ZoneCulture, ZoneElevage | Anes |
| Culture, Animal, Ruminant, Volaille | Anes |
| ProgrammeAlimentation, EvenementSanitaire | Anes |
| HistoriqueCulture, HistoriqueLait, HistoriqueOeuf | Anes |
| Enumerations : StatutZone, StatutAnimal, TypeFamille, StageCroissance | Anes |
| ZoneAquacole, EspeceAquacole | Ghano |
| Capteur, CapteurNumerique, CapteurEnvironnemental, CapteurSol | Ghano |
| CapteurBiometrique, CapteurEau, CapteurGPS | Ghano |
| Releve, ReleveNumerique, ReleveGPS | Ghano |
| Alerte, HistoriqueAquacole | Ghano |
| Interface Suspendable | Ghano |
| Enumerations : StatutCapteur, NiveauGravite, NiveauReleve | Ghano |
| Classe Main (scenario de demo) | Commun |
