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
    public void setId(int id) { this.id = id; }

    public String getEspece() { return espece; }
    public void setEspece(String espece) { this.espece = espece; }

    public int getNombre() { return nombre; }
    public void setNombre(int nombre) { this.nombre = nombre; }
}
