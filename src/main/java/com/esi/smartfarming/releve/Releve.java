package com.esi.smartfarming.releve;

import com.esi.smartfarming.capteur.Capteur;
import com.esi.smartfarming.enums.NiveauReleve;

import java.util.Date;


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

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Capteur getCapteur() { return capteur; }
    public void setCapteur(Capteur capteur) { this.capteur = capteur; }

    public Date getDateHeure() { return dateHeure; }
    public void setDateHeure(Date dateHeure) { this.dateHeure = dateHeure; }

    public NiveauReleve getNiveau() { return niveau; }
    public void setNiveau(NiveauReleve niveau) { this.niveau = niveau; }
}
