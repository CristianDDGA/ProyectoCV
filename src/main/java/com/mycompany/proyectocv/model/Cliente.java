package com.mycompany.proyectocv.model;

import java.util.Date;

public class Cliente {
    private int idCliente;
    private String cedulaRuc;
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private String direccion;
    private Date fechaCreacion;

    public Cliente() {}

    public Cliente(int idCliente, String cedulaRuc, String nombre, String apellido, String telefono, String email, String direccion, Date fechaCreacion) {
        this.idCliente = idCliente;
        this.cedulaRuc = cedulaRuc;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.fechaCreacion = fechaCreacion;
    }

    public Cliente(String cedulaRuc, String nombre, String apellido, String telefono, String email, String direccion) {
        this.cedulaRuc = cedulaRuc;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getCedulaRuc() {
        return cedulaRuc;
    }

    public void setCedulaRuc(String cedulaRuc) {
        this.cedulaRuc = cedulaRuc;
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
