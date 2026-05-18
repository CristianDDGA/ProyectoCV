/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectocv.daos;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**
 *
 * @author Lenovo
 */
public class ConexionBD {
    
    private Connection conexion;
    
    public Connection conectarBD(){
    
        try {
            Class.forName("org.postgresql.Driver");
            conexion=DriverManager.getConnection("jdbc:postgresql://localhost:5432/ventas_cv","postgres","root");
           // JOptionPane.showMessageDialog(null, "Conectado");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return conexion;
    }
}
