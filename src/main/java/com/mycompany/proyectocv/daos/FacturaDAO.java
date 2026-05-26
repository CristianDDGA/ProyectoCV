package com.mycompany.proyectocv.daos;

import com.mycompany.proyectocv.model.Factura;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FacturaDAO {

    private ConexionBD conexion = new ConexionBD();

    public List<Factura> listarFacturas() {
        List<Factura> lista = new ArrayList<>();
        String sql = "SELECT id_factura, numero_factura, fecha, id_usuario, subtotal, iva, total, estado, id_cliente FROM facturas ORDER BY id_factura ASC";

        try (Connection con = conexion.conectarBD();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Factura f = new Factura();
                f.setIdFactura(rs.getInt("id_factura"));
                f.setNumeroFactura(rs.getString("numero_factura"));
                f.setFecha(rs.getTimestamp("fecha"));
                f.setIdUsuario(rs.getInt("id_usuario"));
                f.setSubtotal(rs.getDouble("subtotal"));
                f.setIva(rs.getDouble("iva"));
                f.setTotal(rs.getDouble("total"));
                f.setEstado(rs.getString("estado"));
                // id_cliente can be null
                int idCliente = rs.getInt("id_cliente");
                if (rs.wasNull()) {
                    f.setIdCliente(null);
                } else {
                    f.setIdCliente(idCliente);
                }
                lista.add(f);
            }
        } catch (Exception e) {
            System.err.println("Error al listar facturas: " + e.getMessage());
        }
        return lista;
    }
}
