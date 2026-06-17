package com.esi.smartfarming.animal;

import com.esi.smartfarming.capteur.CapteurBiometrique;
import com.esi.smartfarming.capteur.CapteurGPS;
import com.esi.smartfarming.enums.StatutAnimal;
import com.esi.smartfarming.sanitaire.EvenementSanitaire;
import com.esi.smartfarming.zone.ZoneElevage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Animal implements Serializable {
    private static final long serialVersionUID = 1L;
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
    public void setPoids(double poids) { this.poids = poids; }
    public StatutAnimal getEtatSante() { return etatSante; }
    public void setEtatSante(StatutAnimal etatSante) { this.etatSante = etatSante; }
    public CapteurBiometrique getCapteurBiometrique() { return capteurBiometrique; }
    public void setCapteurBiometrique(CapteurBiometrique capteurBiometrique) { this.capteurBiometrique = capteurBiometrique; }
    public CapteurGPS getCapteurGPS() { return capteurGPS; }
    public void setCapteurGPS(CapteurGPS capteurGPS) { this.capteurGPS = capteurGPS; }
    public List<EvenementSanitaire> getEvenements() { return evenements; }

    public void ajouterEvenement(EvenementSanitaire e) {
        this.evenements.add(e);
    }

    public abstract boolean verifierLimites(ZoneElevage z);
}
