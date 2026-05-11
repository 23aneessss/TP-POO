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
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public TypeFamille getFamille() { return famille; }
    public void setFamille(TypeFamille famille) { this.famille = famille; }

    public Date getDatePlantation() { return datePlantation; }
    public void setDatePlantation(Date datePlantation) { this.datePlantation = datePlantation; }

    public Date getDateRecoltePrevu() { return dateRecoltePrevu; }
    public void setDateRecoltePrevu(Date dateRecoltePrevu) { this.dateRecoltePrevu = dateRecoltePrevu; }

    public StageCroissance getStageCroissance() { return stageCroissance; }
    public void setStageCroissance(StageCroissance stageCroissance) { this.stageCroissance = stageCroissance; }

    public double getPHMin() { return pHMin; }
    public void setPHMin(double pHMin) { this.pHMin = pHMin; }

    public double getPHMax() { return pHMax; }
    public void setPHMax(double pHMax) { this.pHMax = pHMax; }

    public double getHumiditeMin() { return humiditeMin; }
    public void setHumiditeMin(double humiditeMin) { this.humiditeMin = humiditeMin; }

    public double getHumiditeMax() { return humiditeMax; }
    public void setHumiditeMax(double humiditeMax) { this.humiditeMax = humiditeMax; }
}
