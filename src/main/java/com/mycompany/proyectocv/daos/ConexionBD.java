/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectocv.daos;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 *
 * @author Lenovo
 */
public class ConexionBD {

    private Connection conexion;
    public Connection conectarBD() {
        try {
            // Registramos el driver de PostgreSQL
            Class.forName("org.postgresql.Driver");

            // Conectamos directamente usando tus datos nativos
            conexion = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ventas_cv", "postgres", "root");

            // Si llega aquí, es que todo está perfecto
            return conexion;

        } catch (Exception e) {
            // Si hay un error de contraseña o puerto, saltará este aviso con el motivo exacto
            JOptionPane.showMessageDialog(null, "Error de conexión físico: " + e.getMessage());
            return null;
        }

    }
}
