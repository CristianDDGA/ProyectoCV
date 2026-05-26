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

    public List<Cliente> buscarClientes(String valor) {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT id_cliente, cedula_ruc, nombre, apellido, telefono, email, direccion, fecha_creacion FROM clientes " +
                     "WHERE cedula_ruc LIKE ? OR nombre ILIKE ? OR apellido ILIKE ? ORDER BY id_cliente ASC";
        
        try (Connection con = conexion.conectarBD();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            String query = "%" + valor + "%";
            ps.setString(1, query);
            ps.setString(2, query);
            ps.setString(3, query);

            try (ResultSet rs = ps.executeQuery()) {
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
            }
        } catch (Exception e) {
            System.err.println("Error al buscar clientes: " + e.getMessage());
        }
        return lista;
    }

    public boolean registrar(Cliente cliente) {
        String sql = "INSERT INTO clientes (cedula_ruc, nombre, apellido, telefono, email, direccion, fecha_creacion) VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
        try (Connection con = conexion.conectarBD();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, cliente.getCedulaRuc());
            ps.setString(2, cliente.getNombre());
            ps.setString(3, cliente.getApellido());
            ps.setString(4, cliente.getTelefono());
            ps.setString(5, cliente.getEmail());
            ps.setString(6, cliente.getDireccion());
            
            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (Exception e) {
            System.err.println("Error al registrar cliente: " + e.getMessage());
            return false;
        }
    }

    public Cliente obtenerClientePorCedulaRuc(String cedulaRuc) {
        String sql = "SELECT id_cliente, cedula_ruc, nombre, apellido, telefono, email, direccion, fecha_creacion FROM clientes WHERE cedula_ruc = ?";
        try (Connection con = conexion.conectarBD();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cedulaRuc);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Cliente c = new Cliente();
                    c.setIdCliente(rs.getInt("id_cliente"));
                    c.setCedulaRuc(rs.getString("cedula_ruc"));
                    c.setNombre(rs.getString("nombre"));
                    c.setApellido(rs.getString("apellido"));
                    c.setTelefono(rs.getString("telefono"));
                    c.setEmail(rs.getString("email"));
                    c.setDireccion(rs.getString("direccion"));
                    c.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                    return c;
                }
            }
        } catch (Exception e) {
            System.err.println("Error al obtener cliente por cédula/RUC: " + e.getMessage());
        }
        return null;
    }
}
