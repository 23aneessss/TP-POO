package com.esi.smartfarming.animal;

import com.esi.smartfarming.capteur.CapteurBiometrique;
import com.esi.smartfarming.capteur.CapteurGPS;
import com.esi.smartfarming.enums.StatutAnimal;
import com.esi.smartfarming.sanitaire.EvenementSanitaire;
import com.esi.smartfarming.zone.ZoneElevage;

import java.util.ArrayList;
import java.util.List;

public abstract class Animal {
    private int numero;
    private String espece;
    private int age;
    private double poids;
    private StatutAnimal etatSante;
    private CapteurBiometrique capteurBiometrique;
    private CapteurGPS capteurGPS;
    private List<EvenementSanitaire> evenements;

    protected Animal(int numero, String espece, int age, double poids) {
        this.numero = numero;
        this.espece = espece;
        this.age = age;
        this.poids = poids;
        this.etatSante = StatutAnimal.SAIN;
        this.evenements = new ArrayList<>();
    }

    public int getNumero() { return numero; }
    public String getEspece() { return espece; }
    public int getAge() { return age; }
    public double getPoids() { return poids; }
    public void setPoids(double p) { this.poids = p; }
    public StatutAnimal getEtatSante() { return etatSante; }
    public void setEtatSante(StatutAnimal s) { this.etatSante = s; }
    public void ajouterEvenement(EvenementSanitaire e) { this.evenements.add(e); }

    protected abstract boolean verifierLimites(ZoneElevage z);
}
