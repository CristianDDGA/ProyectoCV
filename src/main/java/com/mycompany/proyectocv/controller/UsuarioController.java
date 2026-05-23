/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectocv.controller;

/**
 *
 * @author Lenovo
 */
import com.mycompany.proyectocv.daos.UsuarioDAO;
import com.mycompany.proyectocv.model.Usuario;
import com.mycompany.proyectocv.views.VistaAdmin;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class UsuarioController implements ActionListener {

    private VistaAdmin vista;
    private UsuarioDAO dao;
    private DefaultTableModel modeloTabla;
    private int idUsuarioSeleccionado = -1;

    public UsuarioController(VistaAdmin vista, UsuarioDAO dao) {
        this.vista = vista;
        this.dao = dao;
        this.vista.jCboRol.removeAllItems(); // Limpiamos por si acaso
        this.vista.jCboRol.addItem("Administrador");
        this.vista.jCboRol.addItem("Cajero");
        // Escuchar clics SOLO en los botones del CRUD de Usuarios
        this.vista.jBtnGuardarUser.addActionListener(this);
        this.vista.jBtnActualizarUser.addActionListener(this);
        this.vista.jBtnEliminarUser.addActionListener(this);
        this.vista.jBtnLimpiarUser.addActionListener(this);
        this.vista.jBtnBuscarUsuario.addActionListener(this);

        // Escuchar clics en la tabla de usuarios
        this.vista.jTblUsuario.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                llenarCampos();
            }
        });

        listar(); // Llenar la tabla al iniciar
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // --- BOTÓN BUSCAR ---
        if (e.getSource() == vista.jBtnBuscarUsuario) {
            String textoBusqueda = vista.jTxtBuscarUsuario.getText().trim();
            if (textoBusqueda.isEmpty()) {
                listar(); // Si está vacío, carga todo normal
            } else {
                buscar(textoBusqueda); // Si hay texto, filtra
            }
        }

        // --- BOTÓN GUARDAR ---
        if (e.getSource() == vista.jBtnGuardarUser) {
            if (validarDatos()) {
                Usuario u = new Usuario();
                u.setUsuario(vista.jTxtUsuario.getText().trim());
                u.setContrasenia(new String(vista.jPswClave.getPassword()));
                u.setRol(vista.jCboRol.getSelectedItem().toString());

                if (dao.registrar(u)) {
                    JOptionPane.showMessageDialog(vista, "Usuario Registrado Exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    limpiarTabla();
                    listar();
                    limpiarCampos();
                } else {
                    JOptionPane.showMessageDialog(vista, "Error al registrar. Tal vez el nombre de usuario ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        // --- BOTÓN ACTUALIZAR ---
        if (e.getSource() == vista.jBtnActualizarUser) {
            if (idUsuarioSeleccionado == -1) {
                JOptionPane.showMessageDialog(vista, "Seleccione un usuario de la tabla primero", "Aviso", JOptionPane.WARNING_MESSAGE);
            } else {
                if (validarDatos()) {
                    Usuario u = new Usuario();
                    u.setIdUsuario(idUsuarioSeleccionado);
                    u.setUsuario(vista.jTxtUsuario.getText().trim());

                    // Si la contraseña está vacía, podríamos asumir que no quiere cambiarla, 
                    // pero para mantenerlo simple, pediremos que siempre la escriba.
                    u.setContrasenia(new String(vista.jPswClave.getPassword()));
                    u.setRol(vista.jCboRol.getSelectedItem().toString());

                    if (dao.actualizar(u)) {
                        JOptionPane.showMessageDialog(vista, "Usuario Actualizado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        limpiarTabla();
                        listar();
                        limpiarCampos();
                    } else {
                        JOptionPane.showMessageDialog(vista, "Error al actualizar", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }

        // --- BOTÓN ELIMINAR ---
        if (e.getSource() == vista.jBtnEliminarUser) {
            if (idUsuarioSeleccionado != -1) {
                // Validación de seguridad para no eliminarse a sí mismo (opcional, pero buena práctica)
                int confirmacion = JOptionPane.showConfirmDialog(vista, "¿Está seguro de eliminar este usuario?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (confirmacion == JOptionPane.YES_OPTION) {
                    if (dao.eliminar(idUsuarioSeleccionado)) {
                        JOptionPane.showMessageDialog(vista, "Usuario Eliminado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        limpiarTabla();
                        listar();
                        limpiarCampos();
                    } else {
                        JOptionPane.showMessageDialog(vista, "Error al eliminar el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(vista, "Seleccione un usuario de la tabla para eliminar", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        }

        // --- BOTÓN LIMPIAR ---
        if (e.getSource() == vista.jBtnLimpiarUser) {
            limpiarCampos();
        }
    }

    // =======================================================
    // MÉTODO DE VALIDACIÓN
    // =======================================================
    private boolean validarDatos() {
        String pass = new String(vista.jPswClave.getPassword());

        if (vista.jTxtUsuario.getText().trim().isEmpty() || pass.trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Por favor, llene el usuario y la contraseña.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    // --- MÉTODOS AUXILIARES ---
    private void listar() {
        List<Usuario> lista = dao.listarUsuarios();
        modeloTabla = (DefaultTableModel) vista.jTblUsuario.getModel();
        modeloTabla.setColumnIdentifiers(new Object[]{"ID", "Usuario", "Rol"}); // Ocultamos la contraseña por seguridad

        limpiarTabla();

        Object[] ob = new Object[3];
        for (int i = 0; i < lista.size(); i++) {
            ob[0] = lista.get(i).getIdUsuario();
            ob[1] = lista.get(i).getUsuario(); // O getUsuario() dependiendo de tu modelo
            ob[2] = lista.get(i).getRol();
            modeloTabla.addRow(ob);
        }
        vista.jTblUsuario.setModel(modeloTabla);
    }

    private void buscar(String texto) {
        // 1. Llamamos al DAO y le pasamos el texto
        List<Usuario> lista = dao.buscarUsuarios(texto);

        // 2. Preparamos la tabla
        modeloTabla = (DefaultTableModel) vista.jTblUsuario.getModel();
        modeloTabla.setColumnIdentifiers(new Object[]{"ID", "Usuario", "Rol"});

        limpiarTabla(); // Borramos lo que haya actualmente en la tabla

        // 3. Llenamos la tabla con los resultados filtrados
        Object[] ob = new Object[3];
        for (int i = 0; i < lista.size(); i++) {
            ob[0] = lista.get(i).getIdUsuario();
            ob[1] = lista.get(i).getUsuario();
            ob[2] = lista.get(i).getRol();
            modeloTabla.addRow(ob);
        }
        vista.jTblUsuario.setModel(modeloTabla);
    }

    private void llenarCampos() {
        int fila = vista.jTblUsuario.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Seleccione una fila");
        } else {
            try {
                if (vista.jTblUsuario.getValueAt(fila, 0) == null) {
                    return;
                }

                idUsuarioSeleccionado = Integer.parseInt(vista.jTblUsuario.getValueAt(fila, 0).toString());
                vista.jTxtUsuario.setText(vista.jTblUsuario.getValueAt(fila, 1).toString());
                vista.jCboRol.setSelectedItem(vista.jTblUsuario.getValueAt(fila, 2).toString());
                vista.jPswClave.setText(""); // Dejamos la clave en blanco por seguridad al seleccionar

            } catch (Exception e) {
                System.err.println("Error al leer la fila: " + e.getMessage());
            }
        }
    }

    private void limpiarTabla() {
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            modeloTabla.removeRow(i);
            i = i - 1;
        }
    }

    private void limpiarCampos() {
        idUsuarioSeleccionado = -1;
        vista.jTxtUsuario.setText("");
        vista.jPswClave.setText("");
        vista.jCboRol.setSelectedIndex(0);
    }
}
