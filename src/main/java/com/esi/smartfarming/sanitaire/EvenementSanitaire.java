package com.esi.smartfarming.sanitaire;

import com.esi.smartfarming.animal.Animal;

import java.util.Date;

public class EvenementSanitaire {
    private int id;
    private Animal animal;
    private String description;
    private Date date;
    private double nouveauPoids;

    public EvenementSanitaire(int id, Animal animal, String description, Date date, double nouveauPoids) {
        this.id = id;
        this.animal = animal;
        this.description = description;
        this.date = date;
        this.nouveauPoids = nouveauPoids;
    }
}
