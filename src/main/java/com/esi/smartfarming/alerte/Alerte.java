package com.esi.smartfarming.alerte;

import com.esi.smartfarming.enums.NiveauGravite;
import com.esi.smartfarming.releve.Releve;

import java.io.Serializable;
import java.util.Date;

/**
 * Représente une alerte générée automatiquement lorsqu'un relevé de capteur
 * dépasse ses seuils définis.
 * <p>
 * Une alerte est liée à un {@link com.esi.smartfarming.releve.Releve} et porte
 * un niveau de gravité ({@link com.esi.smartfarming.enums.NiveauGravite}) :
 * <ul>
 *   <li>{@code AVERTISSEMENT} — valeur proche du seuil, surveillance requise</li>
 *   <li>{@code CRITIQUE}      — valeur hors seuil, action immédiate requise</li>
 * </ul>
 * Une alerte peut être <em>acquittée</em> (prise en charge confirmée) ou
 * <em>supprimée</em> (retirée de la liste active).
 */
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
