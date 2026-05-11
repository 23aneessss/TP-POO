package com.esi.smartfarming.animal;

import com.esi.smartfarming.capteur.CapteurBiometrique;
import com.esi.smartfarming.capteur.CapteurGPS;
import com.esi.smartfarming.enums.StatutAnimal;
import com.esi.smartfarming.sanitaire.EvenementSanitaire;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
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
}
