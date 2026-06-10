package com.esi.smartfarming.historique;

import java.io.Serializable;
import java.util.Date;

public class ProductionRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private Date date;
    private String zone;
    private String type;
    private String detail;
    private double valeur;
    private String unite;

    public ProductionRecord(Date date, String zone, String type, String detail, double valeur, String unite) {
        this.date = date;
        this.zone = zone;
        this.type = type;
        this.detail = detail;
        this.valeur = valeur;
        this.unite = unite;
    }

    public Date getDate() { return date; }
    public String getZone() { return zone; }
    public String getType() { return type; }
    public String getDetail() { return detail; }
    public double getValeur() { return valeur; }
    public String getUnite() { return unite; }
}
