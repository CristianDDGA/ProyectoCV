package com.mycompany.proyectocv;       

import com.formdev.flatlaf.FlatLightLaf; // IMPORTACIÓN DEL TEMA
import com.mycompany.proyectocv.controller.LoginController;
import com.mycompany.proyectocv.daos.ConexionBD;
import com.mycompany.proyectocv.daos.UsuarioDAO;
import com.mycompany.proyectocv.views.VistaLogin;
import java.sql.Connection;
import javax.swing.UIManager; // IMPORTACIÓN PARA APLICAR EL TEMA

/**
 *
 * @author Lenovo
 */
public class ProyectoCV {

    public static void main(String[] args) {
        
        // --- 0. Inicializar el diseño moderno (FlatLaf) ---
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("No se pudo inicializar FlatLaf");
        }
        
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