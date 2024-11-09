package com.example.eva3_aplicaciones;

public class user {
    private String uid;
    private String email;
    private String nombre;
    private String apellido;

    public user() {
    }

    public user(String uid, String email, String nombre, String apellido) {
        this.uid = uid;
        this.email = email;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
}
