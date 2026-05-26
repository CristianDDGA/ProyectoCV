/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectocv.controller;

import com.mycompany.proyectocv.views.VistaAdmin;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Lenovo
 */
// --- IMPORTANTE: Importamos la UI específica de FlatLaf ---
import com.formdev.flatlaf.ui.FlatTabbedPaneUI;

public class AdminController implements ActionListener {

    private VistaAdmin vista;

    public AdminController(VistaAdmin vista) {
        this.vista = vista;

        // 1. Ocultar la barra superior usando la regla de FlatLaf
        this.vista.jTabbedPane1.setUI(new FlatTabbedPaneUI() {
            @Override
            protected int calculateTabAreaHeight(int tabPlacement, int horizRunCount, int maxTabHeight) {
                return 0; // Le decimos a FlatLaf que la altura de las pestañas es 0
            }
        });

        // 2. Escuchar clics en los botones del menú lateral
        this.vista.jBtnProductos.addActionListener(this);
        this.vista.jBtnInventario.addActionListener(this);
        this.vista.jBtnReport.addActionListener(this); 
        this.vista.jBtnUser.addActionListener(this);   
        this.vista.jBtnCerrarSesion.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        // --- NAVEGACIÓN DEL MENÚ LATERAL ---
        if (e.getSource() == vista.jBtnProductos) {
            vista.jTabbedPane1.setSelectedIndex(0); 
        }
        if (e.getSource() == vista.jBtnInventario) {
            vista.jTabbedPane1.setSelectedIndex(1); 
        }
        if (e.getSource() == vista.jBtnReport) {
            vista.jTabbedPane1.setSelectedIndex(2); 
        }
        if (e.getSource() == vista.jBtnUser) {
            vista.jTabbedPane1.setSelectedIndex(3); 
        }
        if (e.getSource() == vista.jBtnCerrarSesion) {
            vista.dispose();
            com.mycompany.proyectocv.views.VistaLogin vistaLogin = new com.mycompany.proyectocv.views.VistaLogin();
            com.mycompany.proyectocv.daos.UsuarioDAO daoUser = new com.mycompany.proyectocv.daos.UsuarioDAO();
            new com.mycompany.proyectocv.controller.LoginController(vistaLogin, daoUser);
            vistaLogin.setLocationRelativeTo(null);
            vistaLogin.setVisible(true);
        }
    }
}