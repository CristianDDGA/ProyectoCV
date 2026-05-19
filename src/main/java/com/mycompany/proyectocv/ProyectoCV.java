/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.proyectocv;       

import com.mycompany.proyectocv.controller.LoginController;
import com.mycompany.proyectocv.daos.ConexionBD;
import com.mycompany.proyectocv.daos.UsuarioDAO;

import com.mycompany.proyectocv.views.VistaLogin;
import java.sql.Connection;

/**
 *
 * @author Lenovo
 */
public class ProyectoCV {

public static void main(String[] args) {
        
// 1. Probar y levantar la conexión a PostgreSQL
        ConexionBD cn = new ConexionBD();
        Connection conn = cn.conectarBD();
        System.out.println("Conexión establecida: " + conn);
        
        // 2. Levantar la interfaz de Login con su respectivo Controlador
        VistaLogin vistaLogin = new VistaLogin();
        UsuarioDAO daoUser = new UsuarioDAO();
        
        // Aquí se enlaza la vista con el controlador para que los botones tengan vida
        LoginController controladorLogin = new LoginController(vistaLogin, daoUser);
        
        // Configuración de la ventana
        vistaLogin.setLocationRelativeTo(null); // Centrar en pantalla
        vistaLogin.setVisible(true);
    }
}
