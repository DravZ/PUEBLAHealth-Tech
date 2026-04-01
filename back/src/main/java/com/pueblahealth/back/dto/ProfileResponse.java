package com.pueblahealth.back.dto;

public class ProfileResponse {
    private String nombre;
    private String email;
    private String curp;

    public ProfileResponse(String nombre, String email, String curp) {
        this.nombre = nombre;
        this.email = email;
        this.curp = curp;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getCurp() {
        return curp;
    }
}