package com.mycompany.proyectocv.controller;

import com.mycompany.proyectocv.daos.UsuarioDAO;
import com.mycompany.proyectocv.model.Usuario;
import com.mycompany.proyectocv.daos.ProductoDAO;
import com.mycompany.proyectocv.daos.InventarioDAO;
import com.mycompany.proyectocv.daos.ClienteDAO;
import com.mycompany.proyectocv.daos.FacturaDAO;
import com.mycompany.proyectocv.views.VistaLogin;
import com.mycompany.proyectocv.views.VistaAdmin;
import com.mycompany.proyectocv.views.VistaCajero;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class LoginController implements ActionListener {

    private VistaLogin vista;
    private UsuarioDAO dao;

    public LoginController(VistaLogin vista, UsuarioDAO dao) {
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

                        // 1. Instanciamos la vista única del Administrador
                        VistaAdmin vistaAdmin = new VistaAdmin();

                        // 2. Instanciamos TODOS los DAOs que usa esa vista
                        ProductoDAO productoDao = new ProductoDAO();
                        InventarioDAO inventarioDao = new InventarioDAO();
                        UsuarioDAO usuarioDaoAdmin = new UsuarioDAO();

                        // 3. ENLAZAMOS LA VISTA CON TODOS SUS CONTROLADORES
                        // *** AQUÍ ESTÁ LA CLAVE QUE FALTABA: Despertar al jefe de navegación ***
                        AdminController adminController = new AdminController(vistaAdmin);
                        ProductoController productoController = new ProductoController(vistaAdmin, productoDao);
                        InventarioController inventarioController = new InventarioController(vistaAdmin, inventarioDao);
                        UsuarioController usuarioController = new UsuarioController(vistaAdmin, usuarioDaoAdmin);
                        
                        ReporteController reporteController = new ReporteController(vistaAdmin);

                        // Ajustamos el tamaño para que quepa bien el menú lateral y la tabla
                        vistaAdmin.setSize(1200,700);
                        vistaAdmin.setLocationRelativeTo(null);
                        vistaAdmin.setExtendedState(Frame.MAXIMIZED_BOTH);
                        
                        vistaAdmin.setVisible(true);

                    } else if (u.getRol().equals("Cajero")) {

                        // Si es Cajero, abrimos el Punto de Venta
                        VistaCajero vistaCajero = new VistaCajero();
                        
                        // Instanciamos los DAOs y el controlador para el Cajero
                        ClienteDAO clienteDao = new ClienteDAO();
                        FacturaDAO facturaDao = new FacturaDAO();
                        ProductoDAO productoDao = new ProductoDAO();
                        VentaController ventaController = new VentaController(vistaCajero, clienteDao, facturaDao, productoDao, u);

                        vistaCajero.setLocationRelativeTo(null);
                        vistaCajero.setVisible(true);
                    }

                    vista.dispose(); // Cerramos el login

                } else {
                    JOptionPane.showMessageDialog(vista, "Credenciales incorrectas o el usuario está inactivo.");
                }
            }
        }
    }
}
