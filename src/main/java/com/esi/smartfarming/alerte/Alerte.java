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
    public void setId(int id) { this.id = id; }

    public Releve getReleve() { return releve; }
    public void setReleve(Releve releve) { this.releve = releve; }

    public NiveauGravite getNiveauGravite() { return niveauGravite; }
    public void setNiveauGravite(NiveauGravite niveauGravite) { this.niveauGravite = niveauGravite; }

    public Date getDateHeure() { return dateHeure; }
    public void setDateHeure(Date dateHeure) { this.dateHeure = dateHeure; }

    public boolean isAcquittee() { return acquittee; }
    public void setAcquittee(boolean acquittee) { this.acquittee = acquittee; }
}
