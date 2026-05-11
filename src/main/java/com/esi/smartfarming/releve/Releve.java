package com.esi.smartfarming.releve;

import com.esi.smartfarming.capteur.Capteur;
import com.esi.smartfarming.enums.NiveauReleve;

import java.util.Date;

@SuppressWarnings("unused")
public abstract class Releve {
    protected int id;
    protected Capteur capteur;
    protected Date dateHeure;
    protected NiveauReleve niveau;

    protected Releve(int id, Capteur capteur, Date dateHeure, NiveauReleve niveau) {
        this.id = id;
        this.capteur = capteur;
        this.dateHeure = dateHeure;
        this.niveau = niveau;
    }
}
