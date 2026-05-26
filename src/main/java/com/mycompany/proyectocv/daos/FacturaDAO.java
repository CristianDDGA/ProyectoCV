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

    public String obtenerUltimoNumeroFactura() {
        String sql = "SELECT numero_factura FROM facturas ORDER BY id_factura DESC LIMIT 1";
        try (Connection con = conexion.conectarBD();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getString("numero_factura");
            }
        } catch (Exception e) {
            System.err.println("Error al obtener último número de factura: " + e.getMessage());
        }
        return null;
    }

    public boolean registrarFactura(Factura factura, List<Object[]> detalles) {
        String sqlFactura = "INSERT INTO facturas (numero_factura, fecha, id_usuario, subtotal, iva, total, estado, id_cliente) VALUES (?, CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalles_factura (id_factura, id_producto, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
        String sqlUpdateStock = "UPDATE inventario SET stock_actual = stock_actual - ? WHERE id_producto = ?";
        
        Connection con = null;
        try {
            con = conexion.conectarBD();
            con.setAutoCommit(false);
            
            // 1. Insert Factura
            PreparedStatement psFactura = con.prepareStatement(sqlFactura, PreparedStatement.RETURN_GENERATED_KEYS);
            psFactura.setString(1, factura.getNumeroFactura());
            psFactura.setInt(2, factura.getIdUsuario());
            psFactura.setDouble(3, factura.getSubtotal());
            psFactura.setDouble(4, factura.getIva());
            psFactura.setDouble(5, factura.getTotal());
            psFactura.setString(6, factura.getEstado());
            if (factura.getIdCliente() == null) {
                psFactura.setNull(7, java.sql.Types.INTEGER);
            } else {
                psFactura.setInt(7, factura.getIdCliente());
            }
            
            psFactura.executeUpdate();
            
            ResultSet rs = psFactura.getGeneratedKeys();
            int idFacturaGenerada = 0;
            if (rs.next()) {
                idFacturaGenerada = rs.getInt(1);
            }
            
            // 2. Insert Detalle & Update Stock
            PreparedStatement psDetalle = con.prepareStatement(sqlDetalle);
            PreparedStatement psUpdateStock = con.prepareStatement(sqlUpdateStock);
            
            for (Object[] row : detalles) {
                String codigo = row[0].toString();
                int cantidad = Integer.parseInt(row[2].toString());
                double precioUnit = Double.parseDouble(row[3].toString());
                double subtotalDetalle = Double.parseDouble(row[4].toString());
                
                // Resolve the product ID from code
                int idProducto = 0;
                String sqlProd = "SELECT id_producto FROM productos WHERE codigo = ?";
                try (PreparedStatement psProd = con.prepareStatement(sqlProd)) {
                    psProd.setString(1, codigo);
                    try (ResultSet rsProd = psProd.executeQuery()) {
                        if (rsProd.next()) {
                            idProducto = rsProd.getInt("id_producto");
                        } else {
                            throw new Exception("Producto no encontrado con el código: " + codigo);
                        }
                    }
                }
                
                // Add to batch for detalles_factura
                psDetalle.setInt(1, idFacturaGenerada);
                psDetalle.setInt(2, idProducto);
                psDetalle.setInt(3, cantidad);
                psDetalle.setDouble(4, precioUnit);
                psDetalle.setDouble(5, subtotalDetalle);
                psDetalle.addBatch();
                
                // Add to batch for stock deduction in inventario
                psUpdateStock.setInt(1, cantidad);
                psUpdateStock.setInt(2, idProducto);
                psUpdateStock.addBatch();
            }
            
            psDetalle.executeBatch();
            psUpdateStock.executeBatch();
            
            con.commit();
            return true;
        } catch (Exception e) {
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (Exception ex) {
                System.err.println("Error en rollback de factura: " + ex.getMessage());
            }
            System.err.println("Error al registrar factura: " + e.getMessage());
            return false;
        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (Exception e) {
                System.err.println("Error al cerrar conexión de factura: " + e.getMessage());
            }
        }
    }

    public List<Factura> buscarFacturas(String filtro) {
        List<Factura> lista = new ArrayList<>();
        String sql = "SELECT f.id_factura, f.numero_factura, f.fecha, f.id_usuario, f.subtotal, f.iva, f.total, f.estado, f.id_cliente " +
                     "FROM facturas f " +
                     "LEFT JOIN clientes c ON f.id_cliente = c.id_cliente " +
                     "WHERE f.numero_factura ILIKE ? OR c.cedula_ruc ILIKE ? OR c.nombre ILIKE ? OR c.apellido ILIKE ? " +
                     "ORDER BY f.id_factura ASC";

        try (Connection con = conexion.conectarBD();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            String query = "%" + filtro + "%";
            ps.setString(1, query);
            ps.setString(2, query);
            ps.setString(3, query);
            ps.setString(4, query);
            
            try (ResultSet rs = ps.executeQuery()) {
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
                    int idCliente = rs.getInt("id_cliente");
                    if (rs.wasNull()) {
                        f.setIdCliente(null);
                    } else {
                        f.setIdCliente(idCliente);
                    }
                    lista.add(f);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al buscar facturas: " + e.getMessage());
        }
        return lista;
    }
}
