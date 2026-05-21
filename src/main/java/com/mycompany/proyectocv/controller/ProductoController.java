package com.mycompany.proyectocv.controller;

import com.mycompany.proyectocv.daos.ProductoDAO;
import com.mycompany.proyectocv.model.Producto;
import com.mycompany.proyectocv.views.VistaProductos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ProductoController implements ActionListener {

    private VistaProductos vista;
    private ProductoDAO dao;
    private DefaultTableModel modeloTabla;
    
    private int idProductoSeleccionado = -1; 

    public ProductoController(VistaProductos vista, ProductoDAO dao) {
        this.vista = vista;
        this.dao = dao;

        // Ocultar la barra superior del JTabbedPane
        this.vista.jTabbedPane1.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override
            protected int calculateTabAreaHeight(int tabPlacement, int horizRunCount, int maxTabHeight) {
                return 0; 
            }
        });

        // Escuchar clics en los botones laterales
        this.vista.jBtnProductos.addActionListener(this);
        this.vista.jBtnReport.addActionListener(this);
        this.vista.jBtnUser.addActionListener(this);

        // Escuchar clics en los botones del CRUD
        this.vista.jBtnGuardar.addActionListener(this); 
        this.vista.jBtnActualizar.addActionListener(this); 
        this.vista.jBtnEliminar.addActionListener(this); 
        this.vista.jBtnLimpiar.addActionListener(this); 

        // Escuchar clics en la tabla
        this.vista.jTable1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                llenarCampos();
            }
        });

        listar(); // Llenar la tabla al iniciar
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        // --- NAVEGACIÓN DEL MENÚ LATERAL ---
        if (e.getSource() == vista.jBtnProductos) {
            vista.jTabbedPane1.setSelectedIndex(0);
        }
        if (e.getSource() == vista.jBtnReport) {
            vista.jTabbedPane1.setSelectedIndex(1);
        }
        if (e.getSource() == vista.jBtnUser) {
            vista.jTabbedPane1.setSelectedIndex(2);
        }

        // --- BOTÓN GUARDAR ---
        if (e.getSource() == vista.jBtnGuardar) {
            if (validarDatos()) { // <-- AQUÍ USAMOS LA NUEVA VALIDACIÓN
                Producto p = new Producto(
                        vista.TxtCodigo.getText().trim(), 
                        vista.jTxtNombre.getText().trim(),
                        Double.parseDouble(vista.jTxtPrecio.getText().trim()),
                        Integer.parseInt(vista.jTxtStock.getText().trim())
                );
                
                if (dao.registrar(p)) {
                    JOptionPane.showMessageDialog(vista, "Producto Registrado Exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    limpiarTabla();
                    listar();
                    limpiarCampos();
                } else {
                    // Si falla, suele ser porque el código ya existe (Restricción UNIQUE en BD)
                    JOptionPane.showMessageDialog(vista, "Error al registrar el producto.\nVerifique que el código no esté duplicado.", "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        // --- BOTÓN ACTUALIZAR ---
        if (e.getSource() == vista.jBtnActualizar) {
            if (idProductoSeleccionado == -1) {
                JOptionPane.showMessageDialog(vista, "Seleccione un producto de la tabla primero", "Aviso", JOptionPane.WARNING_MESSAGE);
            } else {
                if (validarDatos()) { // <-- AQUÍ USAMOS LA NUEVA VALIDACIÓN
                    Producto p = new Producto();
                    p.setIdProducto(idProductoSeleccionado); 
                    p.setCodigo(vista.TxtCodigo.getText().trim());
                    p.setNombre(vista.jTxtNombre.getText().trim());
                    p.setPrecio(Double.parseDouble(vista.jTxtPrecio.getText().trim()));
                    p.setStock(Integer.parseInt(vista.jTxtStock.getText().trim()));

                    if (dao.actualizar(p)) {
                        JOptionPane.showMessageDialog(vista, "Producto Actualizado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
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
        if (e.getSource() == vista.jBtnEliminar) {
            if (idProductoSeleccionado != -1) {
                int confirmacion = JOptionPane.showConfirmDialog(vista, "¿Está seguro de eliminar este producto?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (confirmacion == JOptionPane.YES_OPTION) {
                    if (dao.eliminar(idProductoSeleccionado)) {
                        JOptionPane.showMessageDialog(vista, "Producto Eliminado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        limpiarTabla();
                        listar();
                        limpiarCampos();
                    } else {
                        JOptionPane.showMessageDialog(vista, "No se puede eliminar el producto porque ya está en una factura.", "Acción Denegada", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(vista, "Seleccione un producto de la tabla para eliminar", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        }

        // --- BOTÓN LIMPIAR ---
        if (e.getSource() == vista.jBtnLimpiar) {
            limpiarCampos();
        }
    }

    // =======================================================
    // MÉTODO DE VALIDACIÓN BLINDADA
    // =======================================================
    private boolean validarDatos() {
        // 1. Validar que los campos no estén vacíos
        if (vista.TxtCodigo.getText().trim().isEmpty() || 
            vista.jTxtNombre.getText().trim().isEmpty() || 
            vista.jTxtPrecio.getText().trim().isEmpty() || 
            vista.jTxtStock.getText().trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(vista, "Por favor, llene todos los campos del formulario.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // 2. Validar formato numérico y regla de negocio del Precio
        try {
            double precio = Double.parseDouble(vista.jTxtPrecio.getText().trim());
            if (precio <= 0) {
                JOptionPane.showMessageDialog(vista, "El precio debe ser mayor a 0.", "Valor Inválido", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "El precio debe ser un número válido.\nUse punto (.) para decimales, ejemplo: 2.50", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // 3. Validar formato numérico y regla de negocio del Stock
        try {
            int stock = Integer.parseInt(vista.jTxtStock.getText().trim());
            if (stock <= 0) {
                JOptionPane.showMessageDialog(vista, "El stock no puede ser negativo  y debe ser mayor a 0.", "Valor Inválido", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "La cantidad (stock) debe ser un número entero.\nEjemplo: 10, 50, 100", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Si pasa todas las pruebas, retorna verdadero
        return true;
    }

    // --- MÉTODOS AUXILIARES ---
    private void listar() {
        List<Producto> lista = dao.listarProductos();
        modeloTabla = (DefaultTableModel) vista.jTable1.getModel();
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
            try {
                if (vista.jTable1.getValueAt(fila, 0) == null) {
                    return; 
                }

                idProductoSeleccionado = Integer.parseInt(vista.jTable1.getValueAt(fila, 0).toString());
                
                vista.TxtCodigo.setText(vista.jTable1.getValueAt(fila, 1) != null ? vista.jTable1.getValueAt(fila, 1).toString() : "");
                vista.jTxtNombre.setText(vista.jTable1.getValueAt(fila, 2) != null ? vista.jTable1.getValueAt(fila, 2).toString() : "");
                vista.jTxtPrecio.setText(vista.jTable1.getValueAt(fila, 3) != null ? vista.jTable1.getValueAt(fila, 3).toString() : "");
                vista.jTxtStock.setText(vista.jTable1.getValueAt(fila, 4) != null ? vista.jTable1.getValueAt(fila, 4).toString() : "");
                
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
        idProductoSeleccionado = -1;
        vista.TxtCodigo.setText("");
        vista.jTxtNombre.setText("");
        vista.jTxtPrecio.setText("");
        vista.jTxtStock.setText("");
    }
}