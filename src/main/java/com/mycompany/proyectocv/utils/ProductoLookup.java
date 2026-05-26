package com.mycompany.proyectocv.utils;

public class ProductoLookup {

    private final String nombre;
    private final boolean encontrado;

    public ProductoLookup(String nombre, boolean encontrado) {
        this.nombre = nombre;
        this.encontrado = encontrado;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isEncontrado() {
        return encontrado;
    }
}
