/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectocv.controller;

import com.mycompany.proyectocv.daos.InventarioDAO;
import com.mycompany.proyectocv.model.Inventario;
import com.mycompany.proyectocv.views.VistaAdmin;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Lenovo
 */
public class InventarioController implements ActionListener {

    private VistaAdmin vista;
    private InventarioDAO dao;
    private DefaultTableModel modeloTabla;

    public InventarioController(VistaAdmin vista, InventarioDAO dao) {
        this.vista = vista;
        this.dao = dao;

        // NOTA: La navegación (jBtnInventario) ya la maneja el AdminController

        // Escuchar clics en los botones exclusivos de la pestaña inventario
        this.vista.jBtnSumarStock.addActionListener(this);
        this.vista.jBtnLimpiarInv.addActionListener(this);
        this.vista.jBtnBuscarInventario.addActionListener(this);
        // Bloquear edición manual de ID y Nombre por seguridad
        this.vista.jTxtIdProductoInv.setEditable(false);
        this.vista.jTxtNombreProductoInv.setEditable(false);
        

        // Escuchar clics en la tabla
        this.vista.jTableInventario.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                llenarCampos();
            }
        });

        listar(); // Llenar la tabla al iniciar
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        // NOTA: Ya no hay código de navegación aquí
        
                // --- BOTÓN BUSCAR ---
        if (e.getSource() == vista.jBtnBuscarInventario) {
            String textoBusqueda = vista.jTxtBuscarInventario.getText().trim();
            if (textoBusqueda.isEmpty()) {
                listar(); // Si está vacío, carga todo normal
            } else {
                buscar(textoBusqueda); // Si hay texto, filtra
            }
        }

        // --- BOTÓN SUMAR STOCK ---
        if (e.getSource() == vista.jBtnSumarStock) {
            if (validarDatos()) {
                int idProducto = Integer.parseInt(vista.jTxtIdProductoInv.getText().trim());
                int cantidadASumar = Integer.parseInt(vista.jTxtCantidadSumar.getText().trim());

                if (dao.sumarStock(idProducto, cantidadASumar)) {
                    JOptionPane.showMessageDialog(vista, "Mercadería ingresada correctamente al inventario.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    limpiarTabla();
                    listar();
                    limpiarCampos();
                } else {
                    JOptionPane.showMessageDialog(vista, "Error al intentar sumar el stock.", "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        // --- BOTÓN LIMPIAR ---
        if (e.getSource() == vista.jBtnLimpiarInv) {
            limpiarCampos();
        }
    }

    
    
    private void buscar(String texto) {
        // 1. Llamamos al DAO y le pasamos el texto
        List<Inventario> lista = dao.buscarInventario(texto);
        
        // 2. Preparamos la tabla
        modeloTabla = (DefaultTableModel) vista.jTableInventario.getModel();
        modeloTabla.setColumnIdentifiers(new Object[]{"ID Inv", "ID Prod", "Código", "Nombre", "Stock Actual", "Ubicación"});
        
        limpiarTabla(); // Borramos lo que haya actualmente en la tabla

        // 3. Llenamos la tabla con los resultados filtrados
        Object[] ob = new Object[6];
        for (int i = 0; i < lista.size(); i++) {
            ob[0] = lista.get(i).getIdInventario();
            ob[1] = lista.get(i).getIdProducto();
            ob[2] = lista.get(i).getCodigoProducto();
            ob[3] = lista.get(i).getNombreProducto();
            ob[4] = lista.get(i).getStockActual();
            ob[5] = lista.get(i).getUbicacion();
            modeloTabla.addRow(ob);
        }
        vista.jTableInventario.setModel(modeloTabla);
    }
    // =======================================================
    // MÉTODO DE VALIDACIÓN
    // =======================================================
    
    private boolean validarDatos() {
        if (vista.jTxtIdProductoInv.getText().trim().isEmpty() || vista.jTxtNombreProductoInv.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Por favor, seleccione un producto de la tabla primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (vista.jTxtCantidadSumar.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Ingrese la cantidad de mercadería a sumar.", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            int cantidad = Integer.parseInt(vista.jTxtCantidadSumar.getText().trim());
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(vista, "La cantidad a ingresar debe ser mayor a 0.", "Valor Inválido", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "La cantidad debe ser un número entero.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    // --- MÉTODOS AUXILIARES ---
    private void listar() {
        List<Inventario> lista = dao.listarInventario();
        modeloTabla = (DefaultTableModel) vista.jTableInventario.getModel();
        modeloTabla.setColumnIdentifiers(new Object[]{"ID Inv", "ID Prod", "Código", "Nombre", "Stock Actual", "Ubicación"});

        limpiarTabla();

        Object[] ob = new Object[6];
        for (int i = 0; i < lista.size(); i++) {
            ob[0] = lista.get(i).getIdInventario();
            ob[1] = lista.get(i).getIdProducto();
            ob[2] = lista.get(i).getCodigoProducto();
            ob[3] = lista.get(i).getNombreProducto();
            ob[4] = lista.get(i).getStockActual();
            ob[5] = lista.get(i).getUbicacion();
            modeloTabla.addRow(ob);
        }
        vista.jTableInventario.setModel(modeloTabla);
        
        // Ajuste de tamaño de columnas para que se vea bien
        if (vista.jTableInventario.getColumnModel().getColumnCount() > 0) {
            vista.jTableInventario.getColumnModel().getColumn(0).setPreferredWidth(50);
            vista.jTableInventario.getColumnModel().getColumn(1).setPreferredWidth(50);
            vista.jTableInventario.getColumnModel().getColumn(2).setPreferredWidth(80);
            vista.jTableInventario.getColumnModel().getColumn(3).setPreferredWidth(220);
            vista.jTableInventario.getColumnModel().getColumn(4).setPreferredWidth(80);
            vista.jTableInventario.getColumnModel().getColumn(5).setPreferredWidth(120);
        }
    }

    private void llenarCampos() {
        int fila = vista.jTableInventario.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Seleccione una fila");
        } else {
            try {
                if (vista.jTableInventario.getValueAt(fila, 1) == null) {
                    return; 
                }

                // Carga ID del Producto (Columna 1) y Nombre (Columna 3)
                vista.jTxtIdProductoInv.setText(vista.jTableInventario.getValueAt(fila, 1).toString());
                vista.jTxtNombreProductoInv.setText(vista.jTableInventario.getValueAt(fila, 3).toString());
                
                vista.jTxtCantidadSumar.setText("");
                vista.jTxtCantidadSumar.requestFocus();
                
            } catch (Exception e) {
                System.err.println("Error al leer la fila seleccionada: " + e.getMessage());
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
        vista.jTxtIdProductoInv.setText("");
        vista.jTxtNombreProductoInv.setText("");
        vista.jTxtCantidadSumar.setText("");
    }
}