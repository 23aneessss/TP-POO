package com.esi.smartfarming.alerte;

import com.esi.smartfarming.enums.NiveauGravite;
import com.esi.smartfarming.releve.Releve;

import java.util.Date;


public class Alerte {
    private int id;
    private Releve releve;
    private NiveauGravite niveauGravite;
    private Date dateHeure;
    private boolean acquittee;

    public Alerte(int id, Releve releve, NiveauGravite niveauGravite, Date dateHeure) {
        this.id = id;
        this.releve = releve;
        this.niveauGravite = niveauGravite;
        this.dateHeure = dateHeure;
        this.acquittee = false;
    }

    public int getId() { return id; }
    public Releve getReleve() { return releve; }
    public NiveauGravite getNiveauGravite() { return niveauGravite; }
    public Date getDateHeure() { return dateHeure; }
    public void acquitter() { this.acquittee = true; }
    public void supprimer() { this.releve = null; }
    public boolean isAcquittee() { return acquittee; }
}
