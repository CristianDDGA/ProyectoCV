/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectocv.controller;

import com.mycompany.proyectocv.daos.ProductoDAO;
import com.mycompany.proyectocv.model.Producto;
import com.mycompany.proyectocv.views.ProductoView;
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
public class ProductoController implements ActionListener{

    
    
    private ProductoView vista;
    private ProductoDAO dao;
    private DefaultTableModel modeloTabla;

    public ProductoController(ProductoView vista, ProductoDAO dao) {
        this.vista = vista;
        this.dao = dao;

        // Escuchar clics en los botones según tus nombres de variable
        this.vista.jButton1.addActionListener(this); // Guardar
        this.vista.jButton2.addActionListener(this); // Actualizar
        this.vista.jButton4.addActionListener(this); // Eliminar
        this.vista.jButton3.addActionListener(this); // Limpiar

        // Escuchar clics en la tabla para pasar los datos a las cajas de texto
        this.vista.jTable1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                llenarCampos();
            }
        });

        listar(); // Llenar la tabla al iniciar la pantalla
    }
@Override
    public void actionPerformed(ActionEvent e) {
        // --- BOTÓN GUARDAR (jButton1) ---
        if (e.getSource() == vista.jButton1) {
            if (camposVacios()) {
                JOptionPane.showMessageDialog(vista, "Llene todos los campos");
            } else {
                Producto p = new Producto(
                        vista.jTxtCodigo.getText(),
                        vista.jTxtNombre.getText(),
                        Double.parseDouble(vista.jTxtPrecio.getText()),
                        Integer.parseInt(vista.jTxtStock.getText())
                );
                if (dao.registrar(p)) {
                    JOptionPane.showMessageDialog(vista, "Producto Registrado");
                    limpiarTabla();
                    listar();
                    limpiarCampos();
                } else {
                    JOptionPane.showMessageDialog(vista, "Error al registrar");
                }
            }
        }

        // --- BOTÓN ACTUALIZAR (jButton2) ---
        if (e.getSource() == vista.jButton2) {
            if ("".equals(vista.jTxtID.getText())) {
                JOptionPane.showMessageDialog(vista, "Seleccione un producto de la tabla");
            } else {
                Producto p = new Producto();
                p.setIdProducto(Integer.parseInt(vista.jTxtID.getText()));
                p.setCodigo(vista.jTxtCodigo.getText());
                p.setNombre(vista.jTxtNombre.getText());
                p.setPrecio(Double.parseDouble(vista.jTxtPrecio.getText()));
                p.setStock(Integer.parseInt(vista.jTxtStock.getText()));

                if (dao.actualizar(p)) {
                    JOptionPane.showMessageDialog(vista, "Producto Actualizado");
                    limpiarTabla();
                    listar();
                    limpiarCampos();
                } else {
                    JOptionPane.showMessageDialog(vista, "Error al actualizar");
                }
            }
        }

        // --- BOTÓN ELIMINAR (jButton4) ---
        if (e.getSource() == vista.jButton4) {
            if (!"".equals(vista.jTxtID.getText())) {
                int id = Integer.parseInt(vista.jTxtID.getText());
                if (dao.eliminar(id)) {
                    JOptionPane.showMessageDialog(vista, "Producto Eliminado");
                    limpiarTabla();
                    listar();
                    limpiarCampos();
                }
            } else {
                JOptionPane.showMessageDialog(vista, "Seleccione un producto de la tabla");
            }
        }

        // --- BOTÓN LIMPIAR (jButton3) ---
        if (e.getSource() == vista.jButton3) {
            limpiarCampos();
        }
    }

    // --- MÉTODOS AUXILIARES ---

    private void listar() {
        List<Producto> lista = dao.listarProductos();
        modeloTabla = (DefaultTableModel) vista.jTable1.getModel();
        
        // Configuramos las columnas por si NetBeans te dejó "Title 1, Title 2..."
        modeloTabla.setColumnIdentifiers(new Object[]{"ID", "Código", "Nombre", "Precio", "Stock"});
        
        Object[] ob = new Object[5];
        for (int i = 0; i < lista.size(); i++) {
            ob[0] = lista.get(i).getIdProducto();
            ob[1] = lista.get(i).getCodigo();
            ob[2] = lista.get(i).getNombre();
            ob[3] = lista.get(i).getPrecio();
            ob[4] = lista.get(i).getStock();
            modeloTabla.addRow(ob);
        }
        vista.jTable1.setModel(modeloTabla);
    }

    private void llenarCampos() {
        int fila = vista.jTable1.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Seleccione una fila");
        } else {
            vista.jTxtID.setText(vista.jTable1.getValueAt(fila, 0).toString());
            vista.jTxtCodigo.setText(vista.jTable1.getValueAt(fila, 1).toString());
            vista.jTxtNombre.setText(vista.jTable1.getValueAt(fila, 2).toString());
            vista.jTxtPrecio.setText(vista.jTable1.getValueAt(fila, 3).toString());
            vista.jTxtStock.setText(vista.jTable1.getValueAt(fila, 4).toString());
        }
    }

    private void limpiarTabla() {
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            modeloTabla.removeRow(i);
            i = i - 1;
        }
    }

    private void limpiarCampos() {
        vista.jTxtID.setText("");
        vista.jTxtCodigo.setText("");
        vista.jTxtNombre.setText("");
        vista.jTxtPrecio.setText("");
        vista.jTxtStock.setText("");
    }

    private boolean camposVacios() {
        return vista.jTxtCodigo.getText().isEmpty() || vista.jTxtNombre.getText().isEmpty()
                || vista.jTxtPrecio.getText().isEmpty() || vista.jTxtStock.getText().isEmpty();
    }
    
}
