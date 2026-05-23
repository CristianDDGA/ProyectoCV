/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectocv.daos;

import com.mycompany.proyectocv.model.Inventario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lenovo
 */
public class InventarioDAO {

    private ConexionBD conexion = new ConexionBD();
    
    public List<Inventario> buscarInventario(String valor) {
        List<Inventario> lista = new ArrayList<>();
        // Asumiendo que haces un JOIN o que la vista de inventario tiene el nombre del producto
        String sql = "SELECT * FROM inventario i INNER JOIN productos p ON i.id_producto = p.id_producto WHERE p.nombre ILIKE ?"; 
        try {
            Connection con = conexion.conectarBD();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + valor + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Inventario inv = new Inventario();
                inv.setIdInventario(rs.getInt("id_inventario"));
                inv.setIdProducto(rs.getInt("id_producto"));
                inv.setCodigoProducto(rs.getString("codigo"));
                inv.setNombreProducto(rs.getString("nombre"));
                inv.setStockActual(rs.getInt("stock_actual"));
                inv.setUbicacion(rs.getString("ubicacion"));
                lista.add(inv);
            }
        } catch (Exception e) {
            System.err.println("Error al buscar inventario: " + e.getMessage());
        }
        return lista;
    }

    // 1. LISTAR INVENTARIO (Une inventario con productos para mostrar el código y nombre)
    public List<Inventario> listarInventario() {
        List<Inventario> lista = new ArrayList<>();
        String sql = "SELECT i.id_inventario, i.id_producto, p.codigo, p.nombre, i.stock_actual, i.ubicacion " +
                     "FROM inventario i " +
                     "INNER JOIN productos p ON i.id_producto = p.id_producto " +
                     "ORDER BY i.id_inventario ASC";

        try (Connection conn = conexion.conectarBD(); 
             PreparedStatement ps = conn.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Inventario inv = new Inventario();
                inv.setIdInventario(rs.getInt("id_inventario"));
                inv.setIdProducto(rs.getInt("id_producto"));
                inv.setCodigoProducto(rs.getString("codigo")); // Viene de la tabla productos
                inv.setNombreProducto(rs.getString("nombre")); // Viene de la tabla productos
                inv.setStockActual(rs.getInt("stock_actual"));
                inv.setUbicacion(rs.getString("ubicacion"));
                lista.add(inv);
            }
        } catch (Exception e) {
            System.err.println("Error al listar inventario: " + e.getMessage());
        }
        return lista;
    }

    // 2. SUMAR STOCK (Ingreso de nueva mercadería)
    public boolean sumarStock(int idProducto, int cantidadIngresada) {
        // Hacemos que la BD sume automáticamente: stock_actual = stock_actual + nueva_cantidad
        String sql = "UPDATE inventario SET stock_actual = stock_actual + ? WHERE id_producto = ?";
        
        try (Connection conn = conexion.conectarBD(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cantidadIngresada);
            ps.setInt(2, idProducto);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.err.println("Error al sumar stock: " + e.getMessage());
            return false;
        }
    }
}
