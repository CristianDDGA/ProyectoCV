package com.mycompany.proyectocv.controller;

import com.mycompany.proyectocv.daos.UsuarioDAO;
import com.mycompany.proyectocv.model.Usuario;
import com.mycompany.proyectocv.views.LoginView;
import com.mycompany.proyectocv.views.VentaView;
import com.mycompany.proyectocv.views.ProductoView;
import com.mycompany.proyectocv.daos.ProductoDAO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class LoginController implements ActionListener {

    private LoginView vista;
    private UsuarioDAO dao;

    public LoginController(LoginView vista, UsuarioDAO dao) {
        this.vista = vista;
        this.dao = dao;
        this.vista.jBtnLogin.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == vista.jBtnLogin) {

            String user = vista.jTxtUsuario.getText();
            String pass = new String(vista.jPswContrasenia.getPassword());

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Por favor, llene todos los campos.");
            } else {
                Usuario u = dao.autenticar(user, pass);

                if (u != null) {
                    JOptionPane.showMessageDialog(vista, "Bienvenido al sistema. Rol: " + u.getRol());

                    // --- AQUÍ ESTÁ LA MAGIA DEL REDIRECCIONAMIENTO ---
                    if (u.getRol().equals("Administrador")) {
                        
                        // Si es Admin, abrimos el Gestor de Productos
                        ProductoView productoView = new ProductoView();
                        ProductoDAO productoDao = new ProductoDAO();
                        ProductoController productoController = new ProductoController(productoView, productoDao);
                        
                        productoView.setSize(850, 600); // Forzamos tamaño por si acaso
                        productoView.setLocationRelativeTo(null);
                        productoView.setVisible(true);
                        
                    } else if (u.getRol().equals("Cajero")) {
                        
                        // Si es Cajero, abrimos el Punto de Venta
                        VentaView ventaView = new VentaView();
                        ventaView.setLocationRelativeTo(null);
                        ventaView.setVisible(true);
                    }

                    vista.dispose(); // Cerramos el login
                    
                } else {
                    JOptionPane.showMessageDialog(vista, "Credenciales incorrectas o el usuario está inactivo.");
                }
            }
        }
    }
}