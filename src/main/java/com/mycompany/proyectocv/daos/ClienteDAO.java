package com.mycompany.proyectocv.daos;

import com.mycompany.proyectocv.model.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    private ConexionBD conexion = new ConexionBD();

    public List<Cliente> listarClientes() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT id_cliente, cedula_ruc, nombre, apellido, telefono, email, direccion, fecha_creacion FROM clientes ORDER BY id_cliente ASC";
        
        try (Connection con = conexion.conectarBD();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cliente c = new Cliente();
                c.setIdCliente(rs.getInt("id_cliente"));
                c.setCedulaRuc(rs.getString("cedula_ruc"));
                c.setNombre(rs.getString("nombre"));
                c.setApellido(rs.getString("apellido"));
                c.setTelefono(rs.getString("telefono"));
                c.setEmail(rs.getString("email"));
                c.setDireccion(rs.getString("direccion"));
                c.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                lista.add(c);
            }
        } catch (Exception e) {
            System.err.println("Error al listar clientes: " + e.getMessage());
        }
        return lista;
    }
}
