/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.proyectocv;

import com.mycompany.proyectocv.controller.LoginController;
import com.mycompany.proyectocv.daos.ConexionBD;
import com.mycompany.proyectocv.daos.UsuarioDAO;
import com.mycompany.proyectocv.views.LoginView;

/**
 *
 * @author Lenovo
 */
public class ProyectoCV {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        
        ConexionBD cc= new ConexionBD();
        cc.conectarBD();
        System.out.println(cc.conectarBD());
        
        LoginView vistaLogin=new LoginView();
        UsuarioDAO daoUser= new UsuarioDAO();
        LoginController controladorLogin=new LoginController(vistaLogin, daoUser);
        vistaLogin.setLocationRelativeTo(null);
        vistaLogin.setVisible(true);
    }
}
