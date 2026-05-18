/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectocv.daos;

import com.mycompany.proyectocv.model.Usuario;
import java.sql.*;

/**
 *
 * @author Lenovo
 */
public class UsuarioDAO {

    private ConexionBD conexion = new ConexionBD();

    public Usuario autenticar(String user, String password) {
        Usuario usr = null;
        try {

            String sql = "SELECT * FROM usuarios WHERE usuario = ? AND contraseña = ? AND estado = true";
            Connection conn = conexion.conectarBD();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usr = new Usuario();
                    usr.setIdUsuario(rs.getInt("id_usuario"));
                    usr.setUsuario(rs.getString("usuario"));
                    usr.setContraseña(rs.getString("contraseña"));
                    usr.setRol(rs.getString("rol"));
                    usr.setEstado(rs.getBoolean("estado"));
                }
            }
        } catch (Exception e) {
            System.err.println("Error en la autenticación: " + e.getMessage());
        }
        return usr;
    }

}
