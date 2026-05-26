package com.mycompany.proyectocv.controller;

import com.mycompany.proyectocv.daos.ProductoDAO;
import com.mycompany.proyectocv.model.Producto;
import com.mycompany.proyectocv.utils.GeneradorCodigoBarras;
import com.mycompany.proyectocv.utils.VisorCodigoBarras;
import com.mycompany.proyectocv.utils.ProductoLookup;
import com.mycompany.proyectocv.utils.BarcodeLookupService;
import com.mycompany.proyectocv.views.VistaAdmin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
    
    // Bandera para saber si el código actual viene de un escaneo
    private boolean codigoEscaneado = false;

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
        
        // TxtCodigo empieza vacío y no editable para que solo se llene vía escáner o generación
        vista.TxtCodigo.setText("");
        vista.TxtCodigo.setEditable(false);

        // --- ESCÁNER DE CÓDIGO DE BARRAS SIEMPRE ACTIVO ---
        // El campo de escaneo está siempre visible y escuchando
        this.vista.jTxtEscanerAdmin.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    procesarEscaneo();
                }
            }
        });
        // El campo de escaneo siempre visible — el botón de alternar ya no se necesita
        vista.jTxtEscanerAdmin.setVisible(true);
        vista.jBtnEscanearCodigo.setVisible(false); // Ocultar el botón, el escaneo es siempre activo
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
            // Primero aseguramos que nombre y precio sean válidos
            if (vista.jTxtNombre.getText().trim().isEmpty()
                    || vista.jTxtPrecio.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Por favor, llene el nombre y el precio del producto.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Validar precio
            double precio;
            try {
                precio = Double.parseDouble(vista.jTxtPrecio.getText().trim());
                if (precio <= 0) {
                    JOptionPane.showMessageDialog(vista, "El precio debe ser mayor a 0.", "Valor Inválido", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(vista, "El precio debe ser un número válido.\nUse punto (.) para decimales, ejemplo: 2.50", "Error de Formato", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Obtener/Generar código de barras
            String codigoIngresado = vista.TxtCodigo.getText().trim();
            
            if (codigoIngresado.isEmpty()) {
                // Campo vacío → generamos uno automático
                codigoIngresado = GeneradorCodigoBarras.generarCodigoUnico();
                vista.TxtCodigo.setText(codigoIngresado); // Mostrar el generado
            } else if (dao.codigoExiste(codigoIngresado)) {
                JOptionPane.showMessageDialog(vista, "El código de barras ya existe. Use otro o deje el campo vacío para generar uno automático.", "Código Duplicado", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Producto p = new Producto(
                    codigoIngresado,
                    vista.jTxtNombre.getText().trim(),
                    precio
            );

            if (dao.registrar(p)) {
                JOptionPane.showMessageDialog(vista, "Producto Registrado con Código: " + codigoIngresado, "Éxito", JOptionPane.INFORMATION_MESSAGE);
                VisorCodigoBarras.mostrar(vista, codigoIngresado, vista.jTxtNombre.getText().trim());
                limpiarTabla();
                listar();
                limpiarCampos(); // Limpiar todo para el siguiente producto
            } else {
                JOptionPane.showMessageDialog(vista, "Error al registrar el producto. Verifique que el código no esté duplicado.", "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
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
                codigoEscaneado = false; // Ya no es un código escaneado, es de la BD

                vista.TxtCodigo.setEditable(false);
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
        codigoEscaneado = false;
        vista.TxtCodigo.setText("");      // Vacío → se generará automático al guardar si no se escanea
        vista.TxtCodigo.setEditable(false);
        vista.jTxtNombre.setText("");
        vista.jTxtPrecio.setText("");
        vista.jTxtEscanerAdmin.setText(""); // Limpiar también el campo de escaneo
    }

    // =======================================================
    // ESCÁNER DE CÓDIGO DE BARRAS (Open Food Facts)
    // Siempre activo — no requiere clickear un botón
    // =======================================================

    private void procesarEscaneo() {
        String codigo = vista.jTxtEscanerAdmin.getText().trim();
        if (codigo.isEmpty()) {
            return;
        }

        // Guardamos que esto viene de un escaneo
        codigoEscaneado = true;

        // Poner el código escaneado en TxtCodigo INMEDIATAMENTE
        vista.TxtCodigo.setText(codigo);
        vista.jTxtEscanerAdmin.setText(""); // Limpiar para próximo escaneo

        // 1. Verificar si el producto YA EXISTE en la base de datos local
        Producto existente = dao.buscarPorCodigo(codigo);
        if (existente != null) {
            // Ya existe — cargar sus datos para editar
            vista.TxtCodigo.setText(existente.getCodigo());
            vista.jTxtNombre.setText(existente.getNombre());
            vista.jTxtPrecio.setText(String.valueOf(existente.getPrecio()));
            vista.jTxtPrecio.requestFocusInWindow();
            JOptionPane.showMessageDialog(vista,
                    "El producto ya está registrado:\n" + existente.getNombre() + "\n\nSe cargaron sus datos para editar.",
                    "Producto Existente", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // 2. No existe en BD local — buscar en Open Food Facts
        ProductoLookup lookup = BarcodeLookupService.buscar(codigo);

        if (lookup.isEncontrado()) {
            vista.jTxtNombre.setText(lookup.getNombre());
            vista.jTxtPrecio.requestFocusInWindow();
            JOptionPane.showMessageDialog(vista,
                    "Producto encontrado en Open Food Facts: " + lookup.getNombre(),
                    "Escaneo Exitoso", JOptionPane.INFORMATION_MESSAGE);
        } else {
            vista.jTxtNombre.setText("");
            vista.jTxtNombre.requestFocusInWindow();
            JOptionPane.showMessageDialog(vista,
                    "Código " + codigo + " no encontrado.\n"
                    + "Es un producto nuevo — complete los datos y presione Guardar.",
                    "Producto Nuevo", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
