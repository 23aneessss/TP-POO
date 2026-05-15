package com.esi.smartfarming.culture;

import com.esi.smartfarming.enums.StageCroissance;
import com.esi.smartfarming.enums.TypeFamille;

import java.util.Date;

public class Culture {
    private int id;
    private String nom;
    private TypeFamille famille;
    private Date datePlantation;
    private Date dateRecoltePrevu;
    private StageCroissance stageCroissance;
    private double pHMin;
    private double pHMax;
    private double humiditeMin;
    private double humiditeMax;

    public Culture(int id, String nom, TypeFamille famille, Date datePlantation, Date dateRecoltePrevu,
                   double pHMin, double pHMax, double humiditeMin, double humiditeMax) {
        this.id = id;
        this.nom = nom;
        this.famille = famille;
        this.datePlantation = datePlantation;
        this.dateRecoltePrevu = dateRecoltePrevu;
        this.stageCroissance = StageCroissance.SEMIS;
        this.pHMin = pHMin;
        this.pHMax = pHMax;
        this.humiditeMin = humiditeMin;
        this.humiditeMax = humiditeMax;
    }

    public int getId() { return id; }
    public String getNom() { return nom; }
    public TypeFamille getFamille() { return famille; }
    public Date getDatePlantation() { return datePlantation; }
    public Date getDateRecoltePrevu() { return dateRecoltePrevu; }
    public StageCroissance getStageCroissance() { return stageCroissance; }
    public void setStageCroissance(StageCroissance stageCroissance) { this.stageCroissance = stageCroissance; }
    public double getPHMin() { return pHMin; }
    public double getPHMax() { return pHMax; }
    public double getHumiditeMin() { return humiditeMin; }
    public double getHumiditeMax() { return humiditeMax; }

    public String getExigencesPedologiques() {
        return "pH: [" + pHMin + " - " + pHMax + "] | Humidite: [" + humiditeMin + "% - " + humiditeMax + "%]";
    }
}
