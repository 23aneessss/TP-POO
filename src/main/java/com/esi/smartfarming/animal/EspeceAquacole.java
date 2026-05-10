package com.esi.smartfarming.animal;

public class EspeceAquacole {
    private int id;
    private String espece;
    private int nombre;

    public EspeceAquacole(int id, String espece, int nombre) {
        this.id = id;
        this.espece = espece;
        this.nombre = nombre;
    }

    public int getId() { return id; }
    public String getEspece() { return espece; }
    public int getNombre() { return nombre; }
}
