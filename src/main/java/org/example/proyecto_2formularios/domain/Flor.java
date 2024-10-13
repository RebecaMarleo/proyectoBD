package org.example.proyecto_2formularios.domain;

public class Flor {

    private int id;
    private String nombre;
    private double precio;
    private String color;

    public Flor() {

    }

    public Flor(int id, String nombre, double precio, String color) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.color = color;
    }

    public Flor(String nombre, double precio, String color) {
        this.nombre = nombre;
        this.precio = precio;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
