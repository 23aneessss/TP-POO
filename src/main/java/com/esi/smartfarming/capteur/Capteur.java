package com.esi.smartfarming.capteur;

import com.esi.smartfarming.core.Suspendable;
import com.esi.smartfarming.enums.StatutCapteur;
import com.esi.smartfarming.zone.Zone;

import java.io.Serializable;

/**
 * Classe abstraite représentant un capteur IoT de la ferme.
 * <p>
 * Un capteur est rattaché à une {@link com.esi.smartfarming.zone.Zone} et possède
 * un statut : ACTIF, SUSPENDU ou DEFAILLANT.
 * <p>
 * Hiérarchie des sous-classes :
 * <ul>
 *   <li>{@link CapteurNumerique} — capteur à mesure scalaire (température, pH…)
 *     <ul>
 *       <li>{@link CapteurEnvironnemental} — température, humidité, pluviométrie</li>
 *       <li>{@link CapteurSol}            — pH, humidité sol, teneur en azote</li>
 *       <li>{@link CapteurBiometrique}    — température corporelle, activité animale</li>
 *       <li>{@link CapteurEau}            — température eau, oxygène, pH aquacole</li>
 *     </ul>
 *   </li>
 *   <li>{@link CapteurGPS} — position géographique de l'animal (lat/lon)</li>
 * </ul>
 *
 * @see com.esi.smartfarming.core.Suspendable
 */
public abstract class Capteur implements Suspendable, Serializable {
    private static final long serialVersionUID = 1L;

    protected String code;
    protected Zone zone;
    protected StatutCapteur statut;

    protected Capteur(String code, Zone zone) {
        this.code = code;
        this.zone = zone;
        this.statut = StatutCapteur.ACTIF;
    }

    public String getCode() { return code; }
    public Zone getZone() { return zone; }
    public StatutCapteur getStatut() { return statut; }

    @Override
    public void suspendre() { this.statut = StatutCapteur.SUSPENDU; }

    @Override
    public void activer() { this.statut = StatutCapteur.ACTIF; }

    public void marquerDefaillant() { this.statut = StatutCapteur.DEFAILLANT; }
}
