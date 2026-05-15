package com.esi.smartfarming.capteur;

import com.esi.smartfarming.animal.Animal;
import com.esi.smartfarming.enums.NiveauReleve;
import com.esi.smartfarming.releve.ReleveNumerique;
import com.esi.smartfarming.zone.Zone;

import java.util.Date;

public class CapteurBiometrique extends CapteurNumerique {
    private Animal animal;
    private float temperatureCorporelle;
    private float niveauActivite;

    public CapteurBiometrique(String code, Zone zone, double seuilMin, double seuilMax, String unite,
                               Animal animal, float temperatureCorporelle, float niveauActivite) {
        super(code, zone, seuilMin, seuilMax, unite);
        this.animal = animal;
        this.temperatureCorporelle = temperatureCorporelle;
        this.niveauActivite = niveauActivite;
    }

    public Animal getAnimal() { return animal; }
    public void setAnimal(Animal animal) { this.animal = animal; }

    public float getTemperatureCorporelle() { return temperatureCorporelle; }
    public void setTemperatureCorporelle(float temperatureCorporelle) { this.temperatureCorporelle = temperatureCorporelle; }

    public float getNiveauActivite() { return niveauActivite; }
    public void setNiveauActivite(float niveauActivite) { this.niveauActivite = niveauActivite; }

    @Override
    public ReleveNumerique envoyerReleve() {
        NiveauReleve niveau = verifierSeuil(temperatureCorporelle) ? NiveauReleve.NORMAL : NiveauReleve.AVERTISSEMENT;
        ReleveNumerique releve = new ReleveNumerique(historique.size() + 1, this, new Date(), niveau, temperatureCorporelle, unite, this);
        historique.add(releve);
        return releve;
    }
}
