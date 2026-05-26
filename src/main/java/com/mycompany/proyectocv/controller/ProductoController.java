package com.mycompany.proyectocv.controller;

import com.mycompany.proyectocv.daos.ProductoDAO;
import com.mycompany.proyectocv.model.Producto;
import com.mycompany.proyectocv.utils.GeneradorCodigoBarras;
import com.mycompany.proyectocv.utils.VisorCodigoBarras;
import com.mycompany.proyectocv.views.VistaAdmin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ProductoController implements ActionListener {

    private VistaAdmin vista;
    private ProductoDAO dao;
    private DefaultTableModel modeloTabla;

    private int idProductoSeleccionado = -1;

    public ProductoController(VistaAdmin vista, ProductoDAO dao) {
        this.vista = vista;
        this.dao = dao;

        // NOTA: Ya no ocultamos las pestañas aquí, eso lo hace el AdminController
        // Escuchar clics SOLO en los botones del CRUD de Productos
        this.vista.jBtnGuardar.addActionListener(this);
        this.vista.jBtnActualizar.addActionListener(this);
        this.vista.jBtnEliminar.addActionListener(this);
        this.vista.jBtnLimpiar.addActionListener(this);
        this.vista.jBtnVerCodigo.addActionListener(this);
        this.vista.jBtnBuscarProducto.addActionListener(this);
        this.vista.jBtnRefrescarProducto.addActionListener(this);

        // Escuchar clics en la tabla
        this.vista.jTable1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                llenarCampos();
            }
        });

        listar(); // Llenar la tabla al iniciar
        generarYCargarCodigo(); // Generar código automáticamente al abrir
    }

    private void generarYCargarCodigo() {
        String nuevoCodigo = generarCodigoBarras();
        vista.TxtCodigo.setText(nuevoCodigo);
        vista.TxtCodigo.setEditable(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // NOTA: Ya no hay navegación aquí, de eso se encarga el AdminController
        //BOTON BUSACAR --
        if (e.getSource() == vista.jBtnBuscarProducto) {
            String textoBusqueda = vista.jTxtBuscarProducto.getText().trim();
            if (textoBusqueda.isEmpty()) {

                listar();
            }else{
            buscar(textoBusqueda);
            }
        }
        
        // --- BOTÓN REFRESCAR / MOSTRAR TODOS ---
        if (e.getSource() == vista.jBtnRefrescarProducto) {
            vista.jTxtBuscarProducto.setText(""); // Limpiamos la cajita de búsqueda
            listar(); // Traemos toda la tabla de nuevo
            limpiarCampos(); // Deseleccionamos cualquier cosa que estuviera marcada
        }

        // --- BOTÓN GUARDAR ---
        if (e.getSource() == vista.jBtnGuardar) {
            if (validarDatos()) {
                String codigoIngresado = vista.TxtCodigo.getText().trim();
                
                if (codigoIngresado.isEmpty()) {
                    codigoIngresado = generarCodigoBarras();
                } else if (dao.codigoExiste(codigoIngresado)) {
                    JOptionPane.showMessageDialog(vista, "El código de barras ya existe. Use otro o deje el campo vacío para generar uno automático.", "Código Duplicado", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Producto p = new Producto(
                        codigoIngresado,
                        vista.jTxtNombre.getText().trim(),
                        Double.parseDouble(vista.jTxtPrecio.getText().trim())
                );

                if (dao.registrar(p)) {
                    JOptionPane.showMessageDialog(vista, "Producto Registrado con Código: " + codigoIngresado, "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    VisorCodigoBarras.mostrar(vista, codigoIngresado, vista.jTxtNombre.getText().trim());
                    limpiarTabla();
                    listar();
                    generarYCargarCodigo(); // Generar nuevo código automáticamente para el siguiente
                } else {
                    JOptionPane.showMessageDialog(vista, "Error al registrar el producto.\nVerifique que el código no esté duplicado.", "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        // --- BOTÓN ACTUALIZAR ---
        if (e.getSource() == vista.jBtnActualizar) {
            if (idProductoSeleccionado == -1) {
                JOptionPane.showMessageDialog(vista, "Seleccione un producto de la tabla primero", "Aviso", JOptionPane.WARNING_MESSAGE);
            } else {
                if (validarDatos()) {
                    Producto p = new Producto();
                    p.setIdProducto(idProductoSeleccionado);
                    p.setCodigo(vista.TxtCodigo.getText().trim());
                    p.setNombre(vista.jTxtNombre.getText().trim());
                    p.setPrecio(Double.parseDouble(vista.jTxtPrecio.getText().trim()));

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
                        JOptionPane.showMessageDialog(vista, "No se puede eliminar el producto porque ya está en uso.", "Acción Denegada", JOptionPane.ERROR_MESSAGE);
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

        // --- BOTÓN VER CÓDIGO ---
        if (e.getSource() == vista.jBtnVerCodigo) {
            int fila = vista.jTable1.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(vista, "Seleccione un producto de la tabla primero", "Aviso", JOptionPane.WARNING_MESSAGE);
            } else {
                try {
                    String codigo = vista.jTable1.getValueAt(fila, 1).toString();
                    String nombre = vista.jTable1.getValueAt(fila, 2).toString();
                    VisorCodigoBarras.mostrar(vista, codigo, nombre);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(vista, "Error al mostrar código: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    // =======================================================
    // MÉTODO DE VALIDACIÓN (Sin Stock)
    // =======================================================
    private void buscar(String texto) {
        // 1. Llamamos al DAO y le pasamos el texto
        List<Producto> lista = dao.buscarProductos(texto);

        // 2. Preparamos la tabla
        modeloTabla = (DefaultTableModel) vista.jTable1.getModel();
        modeloTabla.setColumnIdentifiers(new Object[]{"ID", "Código", "Nombre", "Precio"});

        limpiarTabla(); // Borramos lo que haya actualmente en la tabla

        // 3. Llenamos la tabla con los resultados filtrados
        
        Object[] ob = new Object[4];
        for (int i = 0; i < lista.size(); i++) {
            ob[0] = lista.get(i).getIdProducto();
            ob[1] = lista.get(i).getCodigo();
            ob[2] = lista.get(i).getNombre();
            ob[3] = lista.get(i).getPrecio();
            modeloTabla.addRow(ob);
        }
        vista.jTable1.setModel(modeloTabla);
    }

    private boolean validarDatos() {
        if (vista.TxtCodigo.getText().trim().isEmpty()
                || vista.jTxtNombre.getText().trim().isEmpty()
                || vista.jTxtPrecio.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(vista, "Por favor, llene todos los campos del formulario.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
            return false;
        }

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

        return true;
    }

    // --- MÉTODOS AUXILIARES ---
    private void listar() {
        List<Producto> lista = dao.listarProductos();
        modeloTabla = (DefaultTableModel) vista.jTable1.getModel();
        // Solo 4 columnas
        modeloTabla.setColumnIdentifiers(new Object[]{"ID", "Código", "Nombre", "Precio"});
        limpiarTabla();
        Object[] ob = new Object[4];
        for (int i = 0; i < lista.size(); i++) {
            ob[0] = lista.get(i).getIdProducto();
            ob[1] = lista.get(i).getCodigo();
            ob[2] = lista.get(i).getNombre();
            ob[3] = lista.get(i).getPrecio();
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
        generarYCargarCodigo();
        vista.jTxtNombre.setText("");
        vista.jTxtPrecio.setText("");
    }

    public String generarCodigoBarras() {
        String nuevoCodigo = GeneradorCodigoBarras.generarCodigoUnico();
        return nuevoCodigo;
    }
}
