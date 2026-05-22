package com.esi.smartfarming.releve;

import com.esi.smartfarming.capteur.Capteur;
import com.esi.smartfarming.enums.NiveauReleve;

import java.io.Serializable;
import java.util.Date;

public abstract class Releve implements Serializable {
    private static final long serialVersionUID = 1L;
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

    public int getId() { return id; }
    public Capteur getCapteur() { return capteur; }
    public Date getDateHeure() { return dateHeure; }
    public NiveauReleve getNiveau() { return niveau; }
}
