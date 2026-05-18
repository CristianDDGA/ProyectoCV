/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectocv.daos;

import java.io.FileInputStream;
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
        Properties props = new Properties();

        try {
            // Buscamos el archivo directamente en la raíz física del proyecto
            FileInputStream in = new FileInputStream("db.properties");
            props.load(in);
            in.close();

            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String pass = props.getProperty("db.password");

            Class.forName("org.postgresql.Driver");
            conexion = DriverManager.getConnection(url, user, pass);

            return conexion;

        } catch (java.io.FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error: Falta el archivo db.properties en la raíz del proyecto.");
            return null;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error de conexión: " + e.getMessage());
            return null;
        }
    }
}
