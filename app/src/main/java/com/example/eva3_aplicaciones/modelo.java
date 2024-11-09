package com.example.eva3_aplicaciones;

public class modelo {
    private int id; // id del producto
    private String nombre;
    private double precio;
    private String fechaCreacion;

    public modelo() {
    }

    public modelo(int id, String nombre, double precio, String fechaCreacion) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.fechaCreacion = fechaCreacion;
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
    public String getFechaCreacion() {

        return fechaCreacion;
    }
    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    @Override
    public String toString() {
        return nombre;
    }
}
