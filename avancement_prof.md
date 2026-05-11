# Rapport d'avancement - TP POO : ESI Smart Farming
## Binome : Anes & Ghano

---

## Avancement general

Le projet a bien avance depuis la premiere seance. Toutes les classes prevues dans le diagramme UML ont ete creees et organisees dans les packages correspondants. La structure du projet respecte les relations d'heritage, de composition et d'agregation definies dans la conception. Le code compile sans erreur.

Les corps de methodes sont en cours de completion. Chaque membre du binome travaille sur sa partie en autonomie avant de faire l'integration lors de la prochaine seance presentielle.

---

## Ce qui a ete realise

- Creation de tous les packages : `zone`, `culture`, `animal`, `capteur`, `releve`, `alerte`, `historique`, `alimentation`, `sanitaire`, `enums`, `core`.
- Creation de toutes les classes, classes abstraites, interface et enumerations de l'UML.
- Mise en place des attributs, constructeurs et getters/setters pour chaque classe.
- Implementation de l'interface `Suspendable` dans `Zone` et `Capteur`.
- Gestion des statuts via les enumerations (`StatutZone`, `StatutAnimal`, `StatutCapteur`).
- Liens entre classes : zones → historiques, animaux → evenements sanitaires, capteurs → releves.

---

## Ce qui reste a faire

- Completer les corps des methodes metier (logique d'enregistrement dans les historiques, production des animaux, verification des limites).
- Creer la classe `Main` avec un scenario complet de demonstration.
- Tester l'integration entre les deux parties du binome.
- Preparer la demonstration finale.

---

## Parole de Anes

"J'ai pris en charge la partie elevage et culture : les zones (`ZoneCulture`, `ZoneElevage`), les cultures, les animaux (`Ruminant`, `Volaille`), les evenements sanitaires et les historiques correspondants. Toutes mes classes sont creees et la structure est en place. Pour la seance de demain, je vais completer les methodes `enregistrerProduction` dans `Ruminant` et `Volaille`, ainsi que les methodes `enregistrer` dans les historiques de ma partie. Je vais aussi verifier que les liens entre animaux, zones et historiques fonctionnent correctement."

---

## Parole de Ghano

"J'ai pris en charge la partie capteurs, releves, alertes et aquaculture : toutes les classes de capteurs (`CapteurEnvironnemental`, `CapteurSol`, `CapteurBiometrique`, `CapteurEau`, `CapteurGPS`), les releves numeriques et GPS, les alertes, et l'historique aquacole. L'interface `Suspendable` est implementee dans `Capteur` et `Zone`. Pour demain, je vais finaliser le comportement de `CapteurGPS`, corriger la methode `supprimer` dans `Alerte`, et completer la methode `enregistrer` dans `HistoriqueAquacole`. Je vais aussi commencer a preparer le scenario de la classe `Main` avec Anes."
