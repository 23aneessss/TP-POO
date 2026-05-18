package com.esi.smartfarming.capteur;

import com.esi.smartfarming.animal.Animal;
import com.esi.smartfarming.enums.NiveauReleve;
import com.esi.smartfarming.releve.ReleveNumerique;
import com.esi.smartfarming.zone.Zone;

import java.util.Date;

public class CapteurBiometrique extends CapteurNumerique {
    private Animal animal;
    private double temperatureCorporelle;
    private double niveauActivite;

    private double tempCorpMin, tempCorpMax;
    private double activiteMin, activiteMax;

    public CapteurBiometrique(String code, Zone zone, String unite,
                               Animal animal, double temperatureCorporelle, double niveauActivite,
                               double tempCorpMin, double tempCorpMax,
                               double activiteMin, double activiteMax) {
        super(code, zone, unite);
        this.animal                = animal;
        this.temperatureCorporelle = temperatureCorporelle;
        this.niveauActivite        = niveauActivite;
        this.tempCorpMin = tempCorpMin; this.tempCorpMax = tempCorpMax;
        this.activiteMin = activiteMin; this.activiteMax = activiteMax;
    }

    public Animal getAnimal() { return animal; }
    public void setAnimal(Animal animal) { this.animal = animal; }

    public double getTemperatureCorporelle() { return temperatureCorporelle; }
    public void setTemperatureCorporelle(double temperatureCorporelle) { this.temperatureCorporelle = temperatureCorporelle; }

    public double getNiveauActivite() { return niveauActivite; }
    public void setNiveauActivite(double niveauActivite) { this.niveauActivite = niveauActivite; }

    public double getTempCorpMin() { return tempCorpMin; } public double getTempCorpMax() { return tempCorpMax; }
    public double getActiviteMin() { return activiteMin; } public double getActiviteMax() { return activiteMax; }

    public boolean verifierTemperatureCorporelle() { return temperatureCorporelle >= tempCorpMin && temperatureCorporelle <= tempCorpMax; }
    public boolean verifierNiveauActivite()        { return niveauActivite        >= activiteMin && niveauActivite        <= activiteMax; }

    @Override
    public ReleveNumerique envoyerReleve() {
        boolean ok = verifierTemperatureCorporelle() && verifierNiveauActivite();
        NiveauReleve niveau = ok ? NiveauReleve.NORMAL : NiveauReleve.AVERTISSEMENT;
        ReleveNumerique releve = new ReleveNumerique(historique.size() + 1, this, new Date(), niveau, temperatureCorporelle, unite, this);
        historique.add(releve);
        return releve;
    }
}
