package com.esi.smartfarming.alerte;

import com.esi.smartfarming.enums.NiveauGravite;
import com.esi.smartfarming.releve.Releve;

import java.io.Serializable;
import java.util.Date;

public class Alerte implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private Releve releve;
    private NiveauGravite niveauGravite;
    private Date dateHeure;
    private boolean acquittee;
    private boolean supprimee;

    public Alerte(int id, Releve releve, NiveauGravite niveauGravite, Date dateHeure) {
        this.id = id;
        this.releve = releve;
        this.niveauGravite = niveauGravite;
        this.dateHeure = dateHeure;
        this.acquittee = false;
        this.supprimee = false;
    }

    public int getId() { return id; }
    public Releve getReleve() { return releve; }
    public NiveauGravite getNiveauGravite() { return niveauGravite; }
    public Date getDateHeure() { return dateHeure; }
    public boolean isAcquittee() { return acquittee; }
    public boolean isSupprimee() { return supprimee; }

    public void acquitter() {
        this.acquittee = true;
    }

    public void supprimer() {
        this.supprimee = true;
        this.releve = null;
    }
}
