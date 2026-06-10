# SmartFarming — Guide de Présentation

> Document de soutenance — TP POO ESI 2CP 2025/2026
> Objectif : couvrir tous les points du sujet + montrer la maîtrise des concepts POO

---

## 0. Pitch d'ouverture (30 secondes)

> « SmartFarming est une application de supervision d'une ferme intelligente.
> La ferme est divisée en **zones géographiques** (culture, élevage, aquaculture),
> chacune équipée de **capteurs IoT** qui envoient des relevés. Quand un relevé
> dépasse ses seuils, le système **génère automatiquement une alerte**.
> J'ai implémenté le modèle objet complet, une interface graphique JavaFX,
> et une **persistance** par sérialisation pour garder les données entre deux lancements. »

**Phrase clé à dire :** *« Le cœur du projet, c'est une hiérarchie de classes qui exploite l'héritage, le polymorphisme et les classes abstraites. »*

---

## 1. Analyse du domaine — les 3 zones

| Zone | Classe | Contient | Capteurs |
|------|--------|----------|----------|
| **Culture** | `ZoneCulture` | Cultures (céréales, légumes, fruits) | Environnemental + Sol |
| **Élevage** | `ZoneElevage` | Animaux (ruminants, volailles) | Biométrique + GPS |
| **Aquacole** | `ZoneAquacole` | Espèces aquacoles (poissons) | Eau |

**À dire :** *« Les capteurs environnementaux et de sol sont rattachés à la zone, pas à une culture précise — car ils mesurent les conditions globales du terrain. En revanche, le capteur biométrique et le GPS sont rattachés à un animal individuel. »*

> Point qui impressionne : tu montres que tu as compris la **différence de granularité** entre capteur-de-zone et capteur-d'individu. C'est exactement ce que demande le sujet.

---

## 2. Concepts POO mis en œuvre (LE cœur de la note)

### 2.1 Classes abstraites
On ne peut pas instancier une « Zone » ou un « Animal » générique — seulement leurs spécialisations.

- `Zone` (abstract) → `ZoneCulture`, `ZoneElevage`, `ZoneAquacole`
- `Animal` (abstract) → `Ruminant`, `Volaille`
- `Capteur` (abstract) → `CapteurNumerique` (abstract aussi) → 4 capteurs concrets
- `Releve` (abstract) → `ReleveNumerique`, `ReleveGPS`
- `HistoriqueProduction` (abstract) → 4 historiques concrets

**À dire :** *« `Zone` est abstraite avec une méthode abstraite `getHistorique()` : chaque zone produit un type d'historique différent, donc je laisse les sous-classes décider. »*

### 2.2 Héritage à deux niveaux (à montrer absolument)
```
Capteur (abstract)
  └── CapteurNumerique (abstract)   ← niveau intermédiaire
        ├── CapteurEnvironnemental
        ├── CapteurSol
        ├── CapteurBiometrique
        └── CapteurEau
  └── CapteurGPS                    ← n'est PAS numérique (génère ReleveGPS)
```

**Pourquoi ça impressionne :** *« J'ai introduit un niveau intermédiaire `CapteurNumerique` parce que 4 capteurs partagent la logique de relevé scalaire (une valeur + une unité), alors que le GPS produit des coordonnées. Le GPS hérite donc directement de `Capteur`, pas de `CapteurNumerique`. »*

### 2.3 Polymorphisme
- `envoyerReleve()` : **redéfinie** dans chaque capteur — chacun vérifie ses propres seuils et fixe le `NiveauReleve`.
- `verifierLimites(ZoneElevage)` : `Ruminant` → max 50 ; `Volaille` → max 200.
- `getHistorique()` : retourne le bon type d'historique selon la zone.

**À dire :** *« Quand je parcours `zone.getCapteurs()` et que j'appelle `envoyerReleve()`, c'est le **bon comportement** qui s'exécute selon le type réel du capteur, sans aucun `if`. C'est le polymorphisme. »*

### 2.4 Interface `Suspendable`
```java
interface Suspendable { void suspendre(); void activer(); }
```
Implémentée par **`Zone`** ET **`Capteur`** — deux hiérarchies sans lien de parenté partagent un contrat commun.

**À dire :** *« Suspendre une zone suspend automatiquement ses capteurs ; un capteur défaillant peut être suspendu pour maintenance puis réactivé. L'interface garantit que les deux exposent le même contrat. »*

### 2.5 Énumérations (7)
`StatutZone`, `StatutAnimal`, `StatutCapteur`, `TypeFamille`, `StageCroissance`, `NiveauGravite`, `NiveauReleve`.

**À dire :** *« J'utilise des enums pour tous les états finis — ça évite les "magic strings" et le compilateur vérifie l'exhaustivité dans mes `switch`. »*

### 2.6 Composition vs Agrégation (vocabulaire qui fait pro)
- **Composition** (`*--`) : `ZoneElevage` **possède** son `ProgrammeAlimentation` et ses historiques — ils n'existent pas sans elle.
- **Agrégation** (`o--`) : `ZoneCulture` **référence** des `Culture` — qui pourraient exister indépendamment.

---

## 3. Le mécanisme central : Relevés → Alertes

**C'est LE scénario à démontrer en live.**

1. L'utilisateur clique **« Envoyer relevés »** sur une zone.
2. Chaque capteur **ACTIF** appelle `envoyerReleve()` → enregistre la valeur dans son **historique**.
3. Le capteur compare la valeur à ses **seuils min/max** → fixe `NiveauReleve` (NORMAL / AVERTISSEMENT / CRITIQUE).
4. Si ce n'est pas NORMAL → une **`Alerte` est créée automatiquement** avec le bon `NiveauGravite`.
5. L'alerte **apparaît instantanément** dans le panneau Alertes (grâce à l'`ObservableList` JavaFX).

**À dire :** *« Le relevé est l'objet qui fait le lien entre le capteur et l'alerte : une `Alerte` référence le `Releve` qui l'a déclenchée, donc je peux toujours remonter à la source — quel capteur, quelle zone, quelle valeur. »*

---

## 4. Les fonctions demandées par le sujet (checklist complète)

> Montre cette liste pour prouver que **rien ne manque**.

### Gérer les zones
- [x] Ajouter / modifier / **suspendre / activer** une zone
- [x] Affecter cultures et animaux à une zone
- [x] Vue d'ensemble de toutes les zones (statut + nb d'entités)
- [x] Enregistrer la production de chaque zone

### Gérer les cultures
- [x] Enregistrer une culture (type, dates, exigences pédologiques)
- [x] Mettre à jour / afficher le **stade de croissance**
- [x] Générer un rapport de l'état des cultures

### Gérer les animaux
- [x] Enregistrer un animal (espèce, âge, poids, état de santé)
- [x] Consigner les **événements sanitaires** (maladies, évolution du poids)
- [x] Définir / afficher les **programmes d'alimentation** par zone
- [x] **Vérifier les limites** de capacité

### Gérer les capteurs
- [x] Ajouter / configurer un capteur (type, zone, seuils min/max)
- [x] Tableau de bord des relevés par zone avec **indicateurs colorés**
- [x] Consulter l'**historique des relevés** d'un capteur (graphique)
- [x] Changer le statut (actif / défaillant / suspendu)
- [x] **Graphiques d'évolution** des relevés par capteur

### Gérer les alertes
- [x] **Déclenchement automatique** quand un relevé dépasse les seuils
- [x] Panneau des alertes actives, **triées par gravité** (critique en premier)
- [x] **Acquitter / supprimer** une alerte
- [x] Filtrage des alertes (par gravité, acquittées)

---

## 5. Architecture de l'application (à montrer si on demande le « comment »)

```
┌─────────────────────────────────────────────┐
│  Couche UI (JavaFX)                          │
│  Dashboard · Zones · Capteurs · Alertes      │
└───────────────────┬─────────────────────────┘
                    │
┌───────────────────▼─────────────────────────┐
│  DataStore (Singleton)                       │
│  • point d'accès unique aux données          │
│  • toutes les mutations passent ici          │
│  • sauvegarde automatique après chaque action│
└───────────────────┬─────────────────────────┘
                    │
┌───────────────────▼─────────────────────────┐
│  Modèle métier (Zone, Animal, Capteur…)      │
│  + Persistance : data/ferme.dat              │
└─────────────────────────────────────────────┘
```

### Patterns / techniques à citer
- **Singleton** (`DataStore.getInstance()`) : un seul état cohérent partagé par toutes les vues.
- **Sérialisation Java** : toutes les entités `implements Serializable` → l'état complet est écrit dans `data/ferme.dat`. *« Je relance l'appli, mes données sont toujours là. »*
- **`transient` + `ObservableList`** : les composants JavaFX ne sont pas sérialisables, donc je les marque `transient` et je les reconstruis au chargement.
- **Séparation modèle / vue** : la logique métier ne dépend pas de JavaFX (preuve : il existe aussi une version terminal `Main.java`).

---

## 6. Démo live — déroulé conseillé (3 minutes)

1. **Dashboard** → « Voici les stats temps réel : zones actives, capteurs actifs, alertes. »
2. **Zone Culture** → ajouter une culture, changer son stade. Ajouter un capteur Sol.
3. **Zone Culture → Envoyer relevés** → « Regardez : une alerte CRITIQUE vient d'apparaître. »
4. **Onglet Alertes** → « Elle est là, je l'acquitte. »
5. **Onglet Capteurs** → sélectionner un capteur dans le menu → **le graphique affiche son historique** avec les lignes de seuil.
6. **Fermer / relancer l'appli** → « Tout est conservé : c'est la persistance. »

---

## 7. Questions pièges du prof — réponses prêtes

**« Pourquoi une classe abstraite et pas une interface pour Zone ? »**
> Parce que `Zone` partage du **code** et des **attributs** communs (code, nom, statut, liste de capteurs), pas seulement un contrat. Une interface ne porte pas d'état. J'utilise les deux : héritage abstrait pour le partage d'implémentation, interface `Suspendable` pour le contrat transverse.

**« Pourquoi CapteurGPS n'hérite pas de CapteurNumerique ? »**
> Parce qu'il ne produit pas une valeur scalaire mais des **coordonnées** (latitude/longitude → `ReleveGPS`). Le forcer dans `CapteurNumerique` casserait le modèle.

**« Comment les alertes sont-elles triées par gravité ? »**
> Le panneau filtre et affiche les CRITIQUE en premier ; chaque alerte garde une référence vers son relevé déclencheur, donc je connais sa gravité et sa source.

**« Que se passe-t-il si on suspend une zone ? »**
> Ses capteurs ne peuvent plus envoyer de relevés — `envoyerReleve()` est ignoré pour les capteurs non-ACTIF. C'est exactement le comportement « maintenance » décrit dans le sujet.

**« Comment garantissez-vous l'unicité du DataStore ? »**
> Constructeur privé + méthode statique `getInstance()` qui charge depuis le fichier ou construit les données par défaut. C'est le pattern Singleton.

**« Et si le fichier de données est corrompu ? »**
> `loadOrCreate()` capture l'exception et régénère les données par défaut via `buildDefault()` — l'appli ne plante jamais au démarrage.

---

## 8. Points forts à mettre en avant en conclusion

- ✅ **Hiérarchie complète** : 5 arbres d'héritage, dont 2 à deux niveaux.
- ✅ **Polymorphisme réel** : `envoyerReleve()` / `verifierLimites()` / `getHistorique()`.
- ✅ **Génération automatique d'alertes** : le scénario central du sujet, fonctionnel.
- ✅ **Persistance** : sérialisation → les données survivent aux relances.
- ✅ **Double interface** : terminal (`Main`) ET graphique (JavaFX), même modèle métier.
- ✅ **Toutes les fonctions du sujet** sont couvertes (voir checklist §4).

**Phrase de clôture :** *« Le projet n'est pas qu'une maquette : c'est un modèle objet rigoureux où chaque concept POO répond à un vrai besoin métier de la ferme. »*

---

*Bon courage pour la soutenance.*
