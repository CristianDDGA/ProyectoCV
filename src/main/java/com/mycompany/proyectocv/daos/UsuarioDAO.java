package com.mycompany.proyectocv.daos;

import com.mycompany.proyectocv.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lenovo
 */
public class UsuarioDAO {

    private ConexionBD conexion = new ConexionBD();
    
    public List<Usuario> buscarUsuarios(String valor) {
        List<Usuario> lista = new ArrayList<>();
        // ILIKE es exclusivo de PostgreSQL y hace que no importe si escriben con mayúsculas o minúsculas
        String sql = "SELECT * FROM usuarios WHERE usuario ILIKE ?"; 
        try {
            Connection con = conexion.conectarBD();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + valor + "%"); // El % permite buscar coincidencias parciales
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario")); 
                u.setUsuario(rs.getString("username"));
                u.setRol(rs.getString("rol"));
                // No traemos la contraseña por seguridad
                lista.add(u);
            }
        } catch (Exception e) {
            System.err.println("Error al buscar usuario: " + e.getMessage());
        }
        return lista;
    }

    // 1. AUTENTICAR (Login)
    public Usuario autenticar(String user, String password) {
        Usuario usr = null;
        String sql = "SELECT * FROM usuarios WHERE usuario = ? AND contraseña = ? AND estado = true";

        // Usamos try-with-resources para cerrar automáticamente la conexión
        try (Connection conn = conexion.conectarBD(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setString(1, user);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usr = new Usuario();
                    usr.setIdUsuario(rs.getInt("id_usuario"));
                    usr.setUsuario(rs.getString("usuario"));
                    // Usamos el setter refactorizado (sin "ñ") pero leemos la columna real de la BD
                    usr.setContrasenia(rs.getString("contraseña")); 
                    usr.setRol(rs.getString("rol"));
                    usr.setEstado(rs.getBoolean("estado"));
                }
            }
        } catch (Exception e) {
            System.err.println("Error en la autenticación: " + e.getMessage());
        }
        return usr;
    }

    // 2. REGISTRAR USUARIO
    public boolean registrar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (usuario, contraseña, rol, estado) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = conexion.conectarBD(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuario.getUsuario());
            ps.setString(2, usuario.getContrasenia());
            ps.setString(3, usuario.getRol());
            ps.setBoolean(4, usuario.isEstado());
            
            ps.execute();
            return true;
        } catch (Exception e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }

    // 3. LISTAR USUARIOS
    public List<Usuario> listarUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY id_usuario ASC";

        try (Connection conn = conexion.conectarBD(); 
             PreparedStatement ps = conn.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setUsuario(rs.getString("usuario"));
                u.setContrasenia(rs.getString("contraseña"));
                u.setRol(rs.getString("rol"));
                u.setEstado(rs.getBoolean("estado"));
                lista.add(u);
            }
        } catch (Exception e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
        }
        return lista;
    }

    // 4. ACTUALIZAR USUARIO
    public boolean actualizar(Usuario usuario) {
        String sql = "UPDATE usuarios SET usuario=?, contraseña=?, rol=?, estado=? WHERE id_usuario=?";
        
        try (Connection conn = conexion.conectarBD(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuario.getUsuario());
            ps.setString(2, usuario.getContrasenia());
            ps.setString(3, usuario.getRol());
            ps.setBoolean(4, usuario.isEstado());
            ps.setInt(5, usuario.getIdUsuario());
            
            ps.execute();
            return true;
        } catch (Exception e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    // 5. ELIMINAR USUARIO
    public boolean eliminar(int idUsuario) {
        String sql = "DELETE FROM usuarios WHERE id_usuario=?";
        
        try (Connection conn = conexion.conectarBD(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.execute();
            return true;
        } catch (Exception e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }
}