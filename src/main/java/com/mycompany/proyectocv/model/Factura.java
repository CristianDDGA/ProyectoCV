package com.mycompany.proyectocv.model;

import java.util.Date;

public class Factura {

    private int idFactura;
    private String numeroFactura;
    private Date fecha;
    private int idUsuario;
    private double subtotal;
    private double iva;
    private double total;
    private String estado;
    private Integer idCliente;

    public Factura() {}

    public Factura(int idFactura, String numeroFactura, Date fecha, int idUsuario, double subtotal, double iva, double total, String estado, Integer idCliente) {
        this.idFactura = idFactura;
        this.numeroFactura = numeroFactura;
        this.fecha = fecha;
        this.idUsuario = idUsuario;
        this.subtotal = subtotal;
        this.iva = iva;
        this.total = total;
        this.estado = estado;
        this.idCliente = idCliente;
    }

    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }
}
