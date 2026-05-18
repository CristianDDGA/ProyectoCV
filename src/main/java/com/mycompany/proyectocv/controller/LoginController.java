/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectocv.controller;

import com.mycompany.proyectocv.daos.UsuarioDAO;
import com.mycompany.proyectocv.model.Usuario;
import com.mycompany.proyectocv.views.LoginView;
import com.mycompany.proyectocv.views.VentaView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author Lenovo
 */
public class LoginController implements ActionListener {

    private LoginView vista;
    private UsuarioDAO dao;

    public LoginController(LoginView vista, UsuarioDAO dao) {
        this.vista = vista;
        this.dao = dao;
        // Asignamos el evento de clic al botón de la vista
        this.vista.jBtnLogin.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Verificamos si el clic vino del botón ingresar
        if (e.getSource() == vista.jBtnLogin) {

            // Accedemos a los textos directamente
            String user = vista.jTxtUsuario.getText();
            String pass = new String(vista.jPswContrasenia.getPassword());

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Por favor, llene todos los campos.");
            } else {
                // Usamos el DAO para consultar la base de datos
                Usuario u = dao.autenticar(user, pass);

                if (u != null) {
                    JOptionPane.showMessageDialog(vista, "Bienvenido al sistema. Rol: " + u.getRol());

                    // Abrimos la pantalla de ventas
                    VentaView ventaView = new VentaView();
                    ventaView.setLocationRelativeTo(null);
                    ventaView.setVisible(true);

                    // Cerramos la ventana de Login
                    vista.dispose();
                } else {
                    JOptionPane.showMessageDialog(vista, "Credenciales incorrectas o el usuario está inactivo.");
                }
            }
        }
    }
}
