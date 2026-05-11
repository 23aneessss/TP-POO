# Classes, attributs et methodes - ESI Smart Farming

---

## Interface

### `Suspendable` — `core/`
| Element | Type | Description |
|---|---|---|
| `suspendre()` | methode | Suspend l'entite |
| `activer()` | methode | Active l'entite |

---

## Enumerations — `enums/`

| Classe | Valeurs |
|---|---|
| `StatutZone` | ACTIVE, SUSPENDUE |
| `TypeFamille` | CEREALE, LEGUME, FRUIT |
| `StageCroissance` | SEMIS, GERMINATION, CROISSANCE, MATURITE, RECOLTE |
| `StatutAnimal` | SAIN, MALADE, QUARANTAINE |
| `StatutCapteur` | ACTIF, DEFAILLANT, SUSPENDU |
| `NiveauGravite` | AVERTISSEMENT, CRITIQUE |
| `NiveauReleve` | NORMAL, AVERTISSEMENT, CRITIQUE |

---

## Package `zone/`

### `Zone` — classe abstraite, implements Suspendable
| Element | Type |
|---|---|
| `code` | String |
| `nom` | String |
| `statut` | StatutZone |
| `capteurs` | List\<Capteur\> |
| `getCode()` | String |
| `getNom()` | String |
| `getStatut()` | StatutZone |
| `getCapteurs()` | List\<Capteur\> |
| `suspendre()` | void |
| `activer()` | void |
| `getHistorique()` | HistoriqueProduction (abstraite) |

### `ZoneCulture` extends Zone
| Element | Type |
|---|---|
| `cultures` | List\<Culture\> |
| `capteursEnv` | List\<CapteurEnvironnemental\> |
| `capteursSol` | List\<CapteurSol\> |
| `historique` | HistoriqueCulture |
| `ajouterCulture(c : Culture)` | void |
| `getCultures()` | List\<Culture\> |
| `genererRapportCultures()` | String |
| `enregistrerRendement(valeur : double, date : Date)` | void |
| `getHistorique()` | HistoriqueProduction |

### `ZoneElevage` extends Zone
| Element | Type |
|---|---|
| `animaux` | List\<Animal\> |
| `programmeAlimentation` | ProgrammeAlimentation |
| `historiqueLait` | HistoriqueLait |
| `historiqueOeuf` | HistoriqueOeuf |
| `ajouterAnimal(a : Animal)` | void |
| `getAnimaux()` | List\<Animal\> |
| `setProgrammeAlimentation(p)` | void |
| `getProgrammeAlimentation()` | ProgrammeAlimentation |
| `getHistorique()` | HistoriqueProduction |

### `ZoneAquacole` extends Zone
| Element | Type |
|---|---|
| `especes` | List\<EspeceAquacole\> |
| `capteursEau` | List\<CapteurEau\> |
| `programmeAlimentation` | ProgrammeAlimentation |
| `historique` | HistoriqueAquacole |
| `ajouterEspece(e : EspeceAquacole)` | void |
| `getEspeces()` | List\<EspeceAquacole\> |
| `setProgrammeAlimentation(p)` | void |
| `getProgrammeAlimentation()` | ProgrammeAlimentation |
| `getHistorique()` | HistoriqueProduction |

---

## Package `culture/`

### `Culture`
| Element | Type |
|---|---|
| `id` | int |
| `nom` | String |
| `famille` | TypeFamille |
| `datePlantation` | Date |
| `dateRecoltePrevu` | Date |
| `stageCroissance` | StageCroissance |
| `pHMin` | double |
| `pHMax` | double |
| `humiditeMin` | double |
| `humiditeMax` | double |
| `getId()` | int |
| `getNom()` | String |
| `getFamille()` | TypeFamille |
| `getStageCroissance()` | StageCroissance |
| `setStageCroissance(s)` | void |
| `getExigencesPedologiques()` | String |

---

## Package `animal/`

### `Animal` — classe abstraite
| Element | Type |
|---|---|
| `numero` | int |
| `espece` | String |
| `age` | int |
| `poids` | double |
| `etatSante` | StatutAnimal |
| `capteurBiometrique` | CapteurBiometrique |
| `capteurGPS` | CapteurGPS |
| `evenements` | List\<EvenementSanitaire\> |
| `getNumero()` | int |
| `getEspece()` | String |
| `getAge()` | int |
| `getPoids()` | double |
| `setPoids(p)` | void |
| `getEtatSante()` | StatutAnimal |
| `setEtatSante(s)` | void |
| `ajouterEvenement(e)` | void |
| `verifierLimites(z : ZoneElevage)` | boolean (abstraite) |

### `Ruminant` extends Animal
| Element | Type |
|---|---|
| `enregistrerProduction(lait : double, date : Date)` | void |
| `verifierLimites(z : ZoneElevage)` | boolean |

### `Volaille` extends Animal
| Element | Type |
|---|---|
| `enregistrerProduction(nbOeufs : int, date : Date)` | void |
| `verifierLimites(z : ZoneElevage)` | boolean |

### `EspeceAquacole`
| Element | Type |
|---|---|
| `id` | int |
| `espece` | String |
| `nombre` | int |
| `getId()` | int |
| `getEspece()` | String |
| `getNombre()` | int |

---

## Package `capteur/`

### `Capteur` — classe abstraite, implements Suspendable
| Element | Type |
|---|---|
| `code` | String |
| `zone` | Zone |
| `statut` | StatutCapteur |
| `seuilMin` | double |
| `seuilMax` | double |
| `getCode()` | String |
| `getZone()` | Zone |
| `getStatut()` | StatutCapteur |
| `getSeuilMin()` | double |
| `getSeuilMax()` | double |
| `suspendre()` | void |
| `activer()` | void |

### `CapteurNumerique` — classe abstraite, extends Capteur
| Element | Type |
|---|---|
| `unite` | String |
| `historique` | List\<ReleveNumerique\> |
| `getUnite()` | String |
| `getHistorique()` | List\<ReleveNumerique\> |
| `envoyerReleve()` | ReleveNumerique (abstraite) |
| `verifierSeuil(valeur : double)` | boolean |

### `CapteurEnvironnemental` extends CapteurNumerique
| Element | Type |
|---|---|
| `typeCapture` | String |
| `envoyerReleve()` | ReleveNumerique |

### `CapteurSol` extends CapteurNumerique
| Element | Type |
|---|---|
| `typeCapture` | String |
| `envoyerReleve()` | ReleveNumerique |

### `CapteurBiometrique` extends CapteurNumerique
| Element | Type |
|---|---|
| `animal` | Animal |
| `typeCapture` | String |
| `envoyerReleve()` | ReleveNumerique |

### `CapteurEau` extends CapteurNumerique
| Element | Type |
|---|---|
| `typeCapture` | String |
| `envoyerReleve()` | ReleveNumerique |

### `CapteurGPS` extends Capteur
| Element | Type |
|---|---|
| `animal` | Animal |
| `historiqueGPS` | List\<ReleveGPS\> |
| `getHistoriqueGPS()` | List\<ReleveGPS\> |
| `envoyerReleve()` | ReleveGPS |

---

## Package `releve/`

### `Releve` — classe abstraite
| Element | Type |
|---|---|
| `id` | int |
| `capteur` | Capteur |
| `dateHeure` | Date |
| `niveau` | NiveauReleve |
| `getId()` | int |
| `getCapteur()` | Capteur |
| `getDateHeure()` | Date |
| `getNiveau()` | NiveauReleve |

### `ReleveNumerique` extends Releve
| Element | Type |
|---|---|
| `valeur` | double |
| `unite` | String |
| `capteurNumerique` | CapteurNumerique |
| `getValeur()` | double |
| `getUnite()` | String |
| `getCapteurNumerique()` | CapteurNumerique |

### `ReleveGPS` extends Releve
| Element | Type |
|---|---|
| `latitude` | double |
| `longitude` | double |
| `capteurGPS` | CapteurGPS |
| `getLatitude()` | double |
| `getLongitude()` | double |
| `getCapteurGPS()` | CapteurGPS |

---

## Package `alerte/`

### `Alerte`
| Element | Type |
|---|---|
| `id` | int |
| `releve` | Releve |
| `niveauGravite` | NiveauGravite |
| `dateHeure` | Date |
| `acquittee` | boolean |
| `getId()` | int |
| `getReleve()` | Releve |
| `getNiveauGravite()` | NiveauGravite |
| `getDateHeure()` | Date |
| `acquitter()` | void |
| `supprimer()` | void |
| `isAcquittee()` | boolean |

---

## Package `historique/`

### `HistoriqueProduction` — classe abstraite
| Element | Type |
|---|---|
| `zone` | Zone |
| `date` | Date |
| `getZone()` | Zone |
| `getDate()` | Date |
| `enregistrer()` | void (abstraite) |

### `HistoriqueCulture` extends HistoriqueProduction
| Element | Type |
|---|---|
| `rendement` | double |
| `enregistrer()` | void |

### `HistoriqueLait` extends HistoriqueProduction
| Element | Type |
|---|---|
| `quantiteLait` | double |
| `enregistrer()` | void |

### `HistoriqueOeuf` extends HistoriqueProduction
| Element | Type |
|---|---|
| `nombreOeufs` | int |
| `enregistrer()` | void |

### `HistoriqueAquacole` extends HistoriqueProduction
| Element | Type |
|---|---|
| `poidsRecolte` | double |
| `enregistrer()` | void |

---

## Package `alimentation/`

### `ProgrammeAlimentation`
| Element | Type |
|---|---|
| `typeAliment` | String |
| `quantiteParRepas` | double |
| `getTypeAliment()` | String |
| `getQuantiteParRepas()` | double |

---

## Package `sanitaire/`

### `EvenementSanitaire`
| Element | Type |
|---|---|
| `id` | int |
| `animal` | Animal |
| `description` | String |
| `date` | Date |
| `nouveauPoids` | double |
| `getId()` | int |
| `getAnimal()` | Animal |
| `getDescription()` | String |
| `getDate()` | Date |
| `getNouveauPoids()` | double |
