package com.mycompany.proyectocv.daos;

import com.mycompany.proyectocv.model.Producto;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author Lenovo
 */
public class ProductoDAO {

    private ConexionBD conexion = new ConexionBD();

    
    
    public List<Producto> buscarProductos(String valor) {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE nombre ILIKE ?"; 
        try {
            Connection con = conexion.conectarBD();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + valor + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Producto p = new Producto();
                p.setIdProducto(rs.getInt("id_producto"));
                p.setCodigo(rs.getString("codigo"));
                p.setNombre(rs.getString("nombre"));
                p.setPrecio(rs.getDouble("precio"));
                lista.add(p);
            }
        } catch (Exception e) {
            System.err.println("Error al buscar producto: " + e.getMessage());
        }
        return lista;
    }
    // 1. REGISTRAR PRODUCTO E INVENTARIO (Transacción)
    public boolean registrar(Producto producto) {
        String sqlProducto = "INSERT INTO productos (codigo, nombre, precio) VALUES (?, ?, ?)";
        // Quemamos el número 0 en el SQL porque todo producto nuevo nace sin stock físico
        String sqlInventario = "INSERT INTO inventario (id_producto, stock_actual) VALUES (?, 0)";

        Connection conn = null;

        try {
            conn = conexion.conectarBD();
            // Apagamos el auto-commit para iniciar la transacción segura
            conn.setAutoCommit(false); 

            // 1er INSERT: Tabla productos (Pedimos que nos devuelva el ID generado)
            PreparedStatement psProducto = conn.prepareStatement(sqlProducto, PreparedStatement.RETURN_GENERATED_KEYS);
            psProducto.setString(1, producto.getCodigo());
            psProducto.setString(2, producto.getNombre());
            psProducto.setDouble(3, producto.getPrecio());
            psProducto.executeUpdate();

            // Obtenemos el ID que la base de datos le asignó al nuevo producto
            ResultSet rs = psProducto.getGeneratedKeys();
            int idNuevoProducto = 0;
            if (rs.next()) {
                idNuevoProducto = rs.getInt(1);
            }

            // 2do INSERT: Crear su espacio en inventario automáticamente con 0
            PreparedStatement psInventario = conn.prepareStatement(sqlInventario);
            psInventario.setInt(1, idNuevoProducto);
            psInventario.executeUpdate();

            // Si ambos Inserts fueron exitosos, confirmamos los cambios
            conn.commit(); 
            return true;

        } catch (Exception e) {
            // Si algo falla, deshacemos todo (Rollback) para no dejar datos a medias
            try {
                if (conn != null) conn.rollback(); 
            } catch (Exception ex) {
                System.err.println("Error en rollback: " + ex.getMessage());
            }
            System.err.println("Error al registrar producto: " + e.getMessage());
            return false;
        } finally {
            // Restauramos el comportamiento normal de la conexión
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (Exception e) {
                System.err.println("Error al restaurar autocommit: " + e.getMessage());
            }
        }
    }

    // 2. LISTAR PRODUCTOS (Solo Catálogo)
    public List<Producto> listarProductos() {
        List<Producto> lista = new ArrayList<>();
        // Consulta simplificada, ya no trae datos de inventario
        String sql = "SELECT * FROM productos ORDER BY id_producto ASC";

        try (Connection conn = conexion.conectarBD(); 
             PreparedStatement ps = conn.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Producto p = new Producto();
                p.setIdProducto(rs.getInt("id_producto"));
                p.setCodigo(rs.getString("codigo"));
                p.setNombre(rs.getString("nombre"));
                p.setPrecio(rs.getDouble("precio"));
                lista.add(p);
            }
        } catch (Exception e) {
            System.err.println("Error al listar productos: " + e.getMessage());
        }
        return lista;
    }

    // 3. EDITAR / ACTUALIZAR PRODUCTO (Solo Catálogo, NO toca el inventario)
    public boolean actualizar(Producto producto) {
        // Solo actualizamos la tabla productos
        String sql = "UPDATE productos SET codigo=?, nombre=?, precio=?, fecha_modificacion=CURRENT_TIMESTAMP WHERE id_producto=?";

        // Usamos try-with-resources que es más limpio y cierra la conexión automáticamente
        try (Connection conn = conexion.conectarBD(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, producto.getCodigo());
            ps.setString(2, producto.getNombre());
            ps.setDouble(3, producto.getPrecio());
            ps.setInt(4, producto.getIdProducto());
            
            ps.execute();
            return true;
            
        } catch (Exception e) {
            System.err.println("Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }

    // 4. ELIMINAR PRODUCTO
    public boolean eliminar(int idProducto) {
        // Gracias a la restricción "ON DELETE CASCADE" en PostgreSQL, 
        // solo necesitamos borrar el producto; la BD borrará su stock automáticamente.
        String sql = "DELETE FROM productos WHERE id_producto=?";
        
        try (Connection conn = conexion.conectarBD(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            ps.execute();
            return true;
        } catch (Exception e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }
}