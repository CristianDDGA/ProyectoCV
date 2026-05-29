package com.mycompany.proyectocv.controller;

import com.mycompany.proyectocv.daos.ClienteDAO;
import com.mycompany.proyectocv.daos.FacturaDAO;
import com.mycompany.proyectocv.daos.ProductoDAO;
import com.mycompany.proyectocv.model.Cliente;
import com.mycompany.proyectocv.model.Factura;
import com.mycompany.proyectocv.model.Producto;
import com.mycompany.proyectocv.model.Usuario;
import com.mycompany.proyectocv.views.VistaCajero;
import com.formdev.flatlaf.ui.FlatTabbedPaneUI;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class VentaController implements ActionListener {

    private VistaCajero vista;
    private ClienteDAO clienteDao;
    private FacturaDAO facturaDao;
    private ProductoDAO productoDao;
    private DefaultTableModel modeloClientesBuscar;
    private DefaultTableModel modeloFacturas;
    private DefaultTableModel modeloProductosBuscar;
    private Usuario usuarioLogueado;
    private Cliente clienteSeleccionado;
    private com.mycompany.proyectocv.daos.InventarioDAO inventarioDao = new com.mycompany.proyectocv.daos.InventarioDAO();
    private boolean isUpdatingTable = false;

    public VentaController(VistaCajero vista, ClienteDAO clienteDao, FacturaDAO facturaDao, ProductoDAO productoDao, Usuario usuarioLogueado) {
        this.vista = vista;
        this.clienteDao = clienteDao;
        this.facturaDao = facturaDao;
        this.productoDao = productoDao;
        this.usuarioLogueado = usuarioLogueado;

        // Ocultar cabeceras del tabbed pane
        this.vista.jTabbedPane1.setUI(new FlatTabbedPaneUI() {
            @Override
            protected int calculateTabAreaHeight(int tabPlacement, int horizRunCount, int maxTabHeight) {
                return 0;
            }
        });

        // Asegurar que jPanel2 (Nueva Venta) tenga scroll vertical suave
        this.vista.jPanel2.setAutoscrolls(true);
        javax.swing.JScrollPane scrollNuevaVenta = new javax.swing.JScrollPane(this.vista.jPanel2);
        scrollNuevaVenta.setBorder(null);
        scrollNuevaVenta.getVerticalScrollBar().setUnitIncrement(16);

        // Reemplazar jPanel2 en jTabbedPane1 con el JScrollPane
        this.vista.jTabbedPane1.remove(this.vista.jPanel2);
        this.vista.jTabbedPane1.insertTab("Nueva Venta", null, scrollNuevaVenta, null, 0);
        this.vista.jTabbedPane1.setSelectedIndex(0);

        // Configurar tabla de detalles programáticamente para que solo la cantidad (columna 2) sea editable
        this.vista.jTblDetalleProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "CODIGO", "DESCRIPCIÓN", "CANTIDAD", "P. UNIT", "SUBTOTAL"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, false, false
            };

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });

        this.vista.jTblDetalleProductos.getModel().addTableModelListener(new javax.swing.event.TableModelListener() {
            @Override
            public void tableChanged(javax.swing.event.TableModelEvent e) {
                if (isUpdatingTable) return;
                if (e.getType() == javax.swing.event.TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    int col = e.getColumn();
                    if (col == 2) {
                        handleCantidadModificada(row);
                    }
                }
            }
        });

        // Enlazar botones laterales de navegación
        this.vista.jBtnNuevaVenta.addActionListener(this);
        this.vista.jBtnClientes.addActionListener(this);
        this.vista.jBtnHistorial.addActionListener(this);

        // Enlazar botones del flujo de clientes en facturación
        this.vista.jBtnBuscarCliente.addActionListener(this);
        this.vista.jBtnSeleccionarBuscar.addActionListener(this);
        this.vista.jBtnCrearCliente.addActionListener(this);
        this.vista.jButton3.addActionListener(this); // Consumidor Final

        // Enlazar botón Generar Factura
        this.vista.jBtnGenerarFactura.addActionListener(this);

        // Enlazar botones de registro de cliente
        this.vista.jBtnSeleccionarCrear.addActionListener(this);
        this.vista.jCbxTipoId.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cédula", "RUC" }));
        this.vista.jCbxTipoId.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                configurarCamposRegistroPorTipoId();
            }
        });

        // Enlazar botones de productos en facturación
        this.vista.jButton1.addActionListener(this); // Agregar Producto
        this.vista.jButton2.addActionListener(this); // Quitar Producto
        this.vista.jBtnSeleccionarProducto.addActionListener(this);

        // Enlazar botones agregados
        this.vista.jBtnVerClientes.addActionListener(this);
        this.vista.jBtnMas.addActionListener(this);
        this.vista.jBtnMenos.addActionListener(this);

        // Configurar buscadores dinámicos
        this.vista.jTxtBuscarCliente.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                listarClientesBuscar(vista.jTxtBuscarCliente.getText());
            }
        });

        this.vista.jTxtBuscarProducto.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                listarProductosBuscar(vista.jTxtBuscarProducto.getText());
            }
        });

        this.vista.jTxtBuscarFactura.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                listarFacturasFiltradas(vista.jTxtBuscarFactura.getText());
            }
        });

        // Garantizar que los campos del cliente en Nueva Venta no sean editables
        this.vista.jTxtTipoId.setEditable(false);
        this.vista.jTxtNumeroId.setEditable(false);
        this.vista.jTxtNombreCliente.setEditable(false);
        this.vista.jtxtDireccion.setEditable(false);
        this.vista.jTxtCorreo.setEditable(false);

        // Cargar vistas y configuraciones iniciales
        configurarCamposRegistroPorTipoId();
        listarClientesBuscar("");
        listarProductosBuscar("");
        listarFacturas();
        recalcularTotales();

        // Ocultar el campo de escáner (se maneja desde el botón)
        this.vista.jTxtEscaner.setVisible(false);
        this.vista.jTxtEscaner.setEnabled(true);

        // Botón grande de escaneo — activa modo escáner
        this.vista.jBtnEscanear.addActionListener(e -> activarModoEscaner());

        // Configurar listeners para el escáner de códigos de barras
        this.vista.jTxtEscaner.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();
                    SwingUtilities.invokeLater(() -> {
                        String entrada = vista.jTxtEscaner.getText().trim();
                        if (!entrada.isEmpty()) {
                            procesarEscaneo(entrada);
                        }
                    });
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    salirModoEscaner();
                }
            }
        });

        // Salir del modo escáner cuando el usuario hace clic en otro componente
        this.vista.jTxtEscaner.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                Component opposite = e.getOppositeComponent();
                // No salir si el foco va al botón (lo clickeó para activar modo)
                // No salir si opposite es null (cambio de ventana, ej. JOptionPane)
                if (opposite != null && opposite != vista.jBtnEscanear) {
                    salirModoEscaner();
                }
            }
        });

        // Dar foco al botón de escáner al iniciar
        this.vista.jBtnEscanear.requestFocus();

        // Robar foco cada vez que la ventana se activa
        this.vista.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                if (vista.jTxtEscaner.isVisible()) {
                    vista.jTxtEscaner.requestFocusInWindow();
                } else {
                    vista.jBtnEscanear.requestFocusInWindow();
                }
            }
        });
    }

    private void procesarEscaneo(String codigo) {
        Producto producto = productoDao.buscarPorCodigo(codigo);

        if (producto == null) {
            JOptionPane.showMessageDialog(vista,
                    "Código " + codigo + " no está registrado en el sistema.\n\n"
                    + "Debe agregar el producto desde el panel de Administración\n"
                    + "antes de poder venderlo en el cajero.",
                    "Producto No Registrado", JOptionPane.WARNING_MESSAGE);
            resetearModoEscaner();
            return;
        }

        int stockDisponible = inventarioDao.obtenerStockPorCodigo(codigo);
        if (stockDisponible <= 0) {
            JOptionPane.showMessageDialog(vista, "Producto sin stock disponible.\nStock actual: " + stockDisponible, "Stock Agotado", JOptionPane.WARNING_MESSAGE);
            resetearModoEscaner();
            return;
        }

        int cantEnTabla = 0;
        DefaultTableModel model = (DefaultTableModel) vista.jTblDetalleProductos.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).toString().equals(codigo)) {
                cantEnTabla = Integer.parseInt(model.getValueAt(i, 2).toString());
                break;
            }
        }

        if (cantEnTabla + 1 > stockDisponible) {
            JOptionPane.showMessageDialog(vista, "No hay suficiente stock.\nStock disponible: " + stockDisponible + "\nYa en carrito: " + cantEnTabla, "Stock Insuficiente", JOptionPane.WARNING_MESSAGE);
            resetearModoEscaner();
            return;
        }

        agregarProductoALaTabla(codigo, producto.getNombre(), 1, producto.getPrecio());
        // Escaneo exitoso: limpia y queda listo para el próximo
        vista.jTxtEscaner.setText("");
        vista.jTxtEscaner.requestFocusInWindow();
    }

    /** Activa el modo escáner: muestra el campo y le da foco */
    private void activarModoEscaner() {
        vista.jTxtEscaner.setText("");
        vista.jTxtEscaner.setVisible(true);
        vista.jBtnEscanear.setText("📷 ESCANEE EL PRODUCTO...");
        SwingUtilities.invokeLater(() -> vista.jTxtEscaner.requestFocusInWindow());
    }

    /** Sale del modo escáner: oculta campo, restaura el botón */
    private void salirModoEscaner() {
        vista.jTxtEscaner.setText("");
        vista.jTxtEscaner.setVisible(false);
        vista.jBtnEscanear.setText("📷 ESCANEAR PRODUCTO");
        vista.jBtnEscanear.requestFocusInWindow();
    }

    /** Resetea tras un error de escaneo */
    private void resetearModoEscaner() {
        salirModoEscaner();
    }

    private void configurarCamposRegistroPorTipoId() {
        if (vista.jCbxTipoId.getSelectedItem() == null) return;
        String tipoId = vista.jCbxTipoId.getSelectedItem().toString();

        if (tipoId.equals("Cédula")) {
            // Ocultar Razón Social
            vista.jLabel26.setVisible(false);
            vista.jTxtRazonSocialRegistro.setVisible(false);

            // Mostrar campos de Cédula
            vista.jLabel13.setVisible(true);
            vista.jLabel25.setVisible(true);
            vista.jTxtNombreRegistro.setVisible(true);
            vista.jTxtApellidoRegistro.setVisible(true);
            vista.jLabel14.setVisible(true);
            vista.jTxtFechaNacimientoRegistro.setVisible(true);
        } else if (tipoId.equals("RUC")) {
            // Mostrar Razón Social
            vista.jLabel26.setVisible(true);
            vista.jTxtRazonSocialRegistro.setVisible(true);

            // Ocultar campos de Cédula
            vista.jLabel13.setVisible(false);
            vista.jLabel25.setVisible(false);
            vista.jTxtNombreRegistro.setVisible(false);
            vista.jTxtApellidoRegistro.setVisible(false);
            vista.jLabel14.setVisible(false);
            vista.jTxtFechaNacimientoRegistro.setVisible(false);
        }
    }

    private void limpiarCamposRegistro() {
        vista.jTxtNumeroIdRegistro.setText("");
        vista.jTxtNombreRegistro.setText("");
        vista.jTxtApellidoRegistro.setText("");
        vista.jTxtFechaNacimientoRegistro.setText("");
        vista.jTxtRazonSocialRegistro.setText("");
        vista.jTxtDireccion.setText("");
        vista.jTxtCorreoElectronicoRegistro.setText("");
    }

    public void listarClientesBuscar(String filtro) {
        List<Cliente> lista;
        if (filtro == null || filtro.trim().isEmpty()) {
            lista = clienteDao.listarClientes();
        } else {
            lista = clienteDao.buscarClientes(filtro.trim());
        }

        modeloClientesBuscar = (DefaultTableModel) vista.jTblClientesBuscar.getModel();
        modeloClientesBuscar.setColumnIdentifiers(new Object[]{"ID", "Cédula/RUC", "Nombre", "Apellido", "Teléfono", "Email", "Dirección", "Fecha Registro"});
        modeloClientesBuscar.setRowCount(0);

        Object[] ob = new Object[8];
        for (Cliente c : lista) {
            ob[0] = c.getIdCliente();
            ob[1] = c.getCedulaRuc();
            ob[2] = c.getNombre();
            ob[3] = c.getApellido();
            ob[4] = c.getTelefono();
            ob[5] = c.getEmail();
            ob[6] = c.getDireccion();
            ob[7] = c.getFechaCreacion();
            modeloClientesBuscar.addRow(ob);
        }
        vista.jTblClientesBuscar.setModel(modeloClientesBuscar);
    }

    public void listarProductosBuscar(String filtro) {
        List<Producto> lista;
        if (filtro == null || filtro.trim().isEmpty()) {
            lista = productoDao.listarProductos();
        } else {
            lista = productoDao.buscarProductos(filtro.trim());
        }

        modeloProductosBuscar = (DefaultTableModel) vista.jTblBuscarProductos.getModel();
        modeloProductosBuscar.setColumnIdentifiers(new Object[]{"ID", "Código", "Nombre", "Precio"});
        modeloProductosBuscar.setRowCount(0);

        Object[] ob = new Object[4];
        for (Producto p : lista) {
            ob[0] = p.getIdProducto();
            ob[1] = p.getCodigo();
            ob[2] = p.getNombre();
            ob[3] = p.getPrecio();
            modeloProductosBuscar.addRow(ob);
        }
        vista.jTblBuscarProductos.setModel(modeloProductosBuscar);
    }

    public void listarFacturas() {
        listarFacturasFiltradas("");
    }

    public void listarFacturasFiltradas(String filtro) {
        List<Factura> lista;
        if (filtro == null || filtro.trim().isEmpty()) {
            lista = facturaDao.listarFacturas();
        } else {
            lista = facturaDao.buscarFacturas(filtro.trim());
        }
        modeloFacturas = (DefaultTableModel) vista.jTblFacturas.getModel();
        modeloFacturas.setColumnIdentifiers(new Object[]{"ID", "Nro. Factura", "Fecha", "Usuario ID", "Subtotal", "IVA", "Total", "Estado", "Cliente ID"});
        modeloFacturas.setRowCount(0);

        Object[] ob = new Object[9];
        for (Factura f : lista) {
            ob[0] = f.getIdFactura();
            ob[1] = f.getNumeroFactura();
            ob[2] = f.getFecha();
            ob[3] = f.getIdUsuario();
            ob[4] = f.getSubtotal();
            ob[5] = f.getIva();
            ob[6] = f.getTotal();
            ob[7] = f.getEstado();
            ob[8] = f.getIdCliente();
            modeloFacturas.addRow(ob);
        }
        vista.jTblFacturas.setModel(modeloFacturas);
    }

    private void agregarProductoALaTabla(String codigo, String descripcion, int cantidad, double precio) {
        DefaultTableModel model = (DefaultTableModel) vista.jTblDetalleProductos.getModel();
        boolean existe = false;

        isUpdatingTable = true;
        try {
            for (int i = 0; i < model.getRowCount(); i++) {
                String codTabla = model.getValueAt(i, 0).toString();
                if (codTabla.equals(codigo)) {
                    int cantActual = Integer.parseInt(model.getValueAt(i, 2).toString());
                    int nuevaCant = cantActual + cantidad;
                    double nuevoSubtotal = nuevaCant * precio;
                    model.setValueAt(nuevaCant, i, 2);
                    model.setValueAt(nuevoSubtotal, i, 4);
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                double subtotal = cantidad * precio;
                model.addRow(new Object[]{codigo, descripcion, cantidad, precio, subtotal});
            }
        } finally {
            isUpdatingTable = false;
        }

        recalcularTotales();
    }

    private void recalcularTotales() {
        DefaultTableModel model = (DefaultTableModel) vista.jTblDetalleProductos.getModel();
        double subtotalAcumulado = 0;

        for (int i = 0; i < model.getRowCount(); i++) {
            double sub = Double.parseDouble(model.getValueAt(i, 4).toString());
            subtotalAcumulado += sub;
        }

        double iva = subtotalAcumulado * 0.15; // 15% IVA
        double total = subtotalAcumulado + iva;

        vista.jLblSubtotales.setText(String.format(java.util.Locale.US, "$%.2f", subtotalAcumulado));
        vista.jLblIva.setText(String.format(java.util.Locale.US, "$%.2f", iva));
        vista.jLblTotal.setText(String.format(java.util.Locale.US, "$%.2f", total));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.jBtnNuevaVenta) {
            vista.jTabbedPane1.setSelectedIndex(0);
        } else if (e.getSource() == vista.jBtnClientes) {
            vista.jTabbedPane1.setSelectedIndex(1);
        } else if (e.getSource() == vista.jBtnHistorial) {
            vista.jTabbedPane1.setSelectedIndex(2);
            listarFacturas();
        } else if (e.getSource() == vista.jBtnBuscarCliente) {
            // Abrir pestaña de buscar clientes (índice 3)
            vista.jTabbedPane1.setSelectedIndex(3);
            vista.jTxtBuscarCliente.setText("");
            listarClientesBuscar("");
        } else if (e.getSource() == vista.jBtnCrearCliente) {
            // Redirigir a Crear Clientes (índice 1)
            vista.jTabbedPane1.setSelectedIndex(1);
        } else if (e.getSource() == vista.jBtnSeleccionarCrear) {
            // Guardar cliente
            String tipoId = vista.jCbxTipoId.getSelectedItem().toString();
            String numeroId = vista.jTxtNumeroIdRegistro.getText().trim();
            String direccion = vista.jTxtDireccion.getText().trim();
            String email = vista.jTxtCorreoElectronicoRegistro.getText().trim();

            if (numeroId.isEmpty() || direccion.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Por favor complete todos los campos obligatorios del cliente.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!numeroId.matches("\\d+")) {
                JOptionPane.showMessageDialog(vista, "La identificación debe contener únicamente números.", "Identificación Inválida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (tipoId.equals("Cédula") && numeroId.length() != 10) {
                JOptionPane.showMessageDialog(vista, "La cédula debe tener exactamente 10 dígitos.", "Cédula Inválida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (tipoId.equals("RUC") && numeroId.length() != 13) {
                JOptionPane.showMessageDialog(vista, "El RUC debe tener exactamente 13 dígitos.", "RUC Inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                JOptionPane.showMessageDialog(vista, "El formato del correo electrónico es inválido.", "Correo Inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Cliente c = new Cliente();
            c.setCedulaRuc(numeroId);
            c.setDireccion(direccion);
            c.setEmail(email);

            if (tipoId.equals("Cédula")) {
                String nombre = vista.jTxtNombreRegistro.getText().trim();
                String apellido = vista.jTxtApellidoRegistro.getText().trim();
                String fechaNac = vista.jTxtFechaNacimientoRegistro.getText().trim();

                if (nombre.isEmpty() || apellido.isEmpty() || fechaNac.isEmpty()) {
                    JOptionPane.showMessageDialog(vista, "Por favor complete todos los campos de la persona natural.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$") || !apellido.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) {
                    JOptionPane.showMessageDialog(vista, "El nombre y el apellido deben contener únicamente letras.", "Formato Inválido", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                c.setNombre(nombre);
                c.setApellido(apellido);
            } else {
                String razonSocial = vista.jTxtRazonSocialRegistro.getText().trim();
                if (razonSocial.isEmpty()) {
                    JOptionPane.showMessageDialog(vista, "Por favor complete la razón social de la empresa.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                c.setNombre(razonSocial);
                c.setApellido(""); // RUC no requiere apellido
            }

            if (clienteDao.registrar(c)) {
                JOptionPane.showMessageDialog(vista, "Cliente registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCamposRegistro();
                listarClientesBuscar("");
                
                // Redirigir automáticamente a la pestaña de Buscar Clientes (índice 3)
                vista.jTabbedPane1.setSelectedIndex(3);
            } else {
                JOptionPane.showMessageDialog(vista, "Error al registrar el cliente. Verifique que la identificación no esté duplicada.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else if (e.getSource() == vista.jBtnSeleccionarBuscar) {
            int fila = vista.jTblClientesBuscar.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(vista, "Seleccione un cliente de la tabla primero", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int idCliente = Integer.parseInt(vista.jTblClientesBuscar.getValueAt(fila, 0).toString());
            String cedulaRuc = vista.jTblClientesBuscar.getValueAt(fila, 1).toString();
            String nombre = vista.jTblClientesBuscar.getValueAt(fila, 2).toString();
            String apellido = vista.jTblClientesBuscar.getValueAt(fila, 3) != null ? vista.jTblClientesBuscar.getValueAt(fila, 3).toString() : "";
            String email = vista.jTblClientesBuscar.getValueAt(fila, 5) != null ? vista.jTblClientesBuscar.getValueAt(fila, 5).toString() : "";
            String direccion = vista.jTblClientesBuscar.getValueAt(fila, 6) != null ? vista.jTblClientesBuscar.getValueAt(fila, 6).toString() : "";

            clienteSeleccionado = new Cliente();
            clienteSeleccionado.setIdCliente(idCliente);
            clienteSeleccionado.setCedulaRuc(cedulaRuc);
            clienteSeleccionado.setNombre(nombre);
            clienteSeleccionado.setApellido(apellido);
            clienteSeleccionado.setEmail(email);
            clienteSeleccionado.setDireccion(direccion);

            if (cedulaRuc.length() == 10) {
                vista.jTxtTipoId.setText("Cédula");
                String nombreCompleto = nombre;
                if (!apellido.trim().isEmpty()) {
                    nombreCompleto += " " + apellido;
                }
                vista.jTxtNombreCliente.setText(nombreCompleto);
            } else if (cedulaRuc.length() == 13) {
                vista.jTxtTipoId.setText("RUC");
                vista.jTxtNombreCliente.setText(nombre);
            } else {
                vista.jTxtTipoId.setText("Otro");
                String nombreCompleto = nombre;
                if (!apellido.trim().isEmpty()) {
                    nombreCompleto += " " + apellido;
                }
                vista.jTxtNombreCliente.setText(nombreCompleto);
            }

            vista.jTxtNumeroId.setText(cedulaRuc);
            vista.jtxtDireccion.setText(direccion);
            vista.jTxtCorreo.setText(email);

            vista.jTabbedPane1.setSelectedIndex(0);

        } else if (e.getSource() == vista.jButton1) {
            // Botón "Agregar Producto" en Nueva Venta
            vista.jTabbedPane1.setSelectedIndex(4); // Pestaña de Productos (índice 4)
            vista.jTxtBuscarProducto.setText("");
            listarProductosBuscar("");

        } else if (e.getSource() == vista.jButton2) {
            // Botón "Quitar Producto" en Nueva Venta
            int fila = vista.jTblDetalleProductos.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(vista, "Seleccione un producto de la tabla detalle para quitar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            DefaultTableModel model = (DefaultTableModel) vista.jTblDetalleProductos.getModel();
            model.removeRow(fila);
            recalcularTotales();

        } else if (e.getSource() == vista.jBtnSeleccionarProducto) {
            // Botón "Seleccionar Producto" en la pestaña de Productos (índice 4)
            int fila = vista.jTblBuscarProductos.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(vista, "Seleccione un producto de la tabla primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String codigo = vista.jTblBuscarProductos.getValueAt(fila, 1).toString();
            String nombre = vista.jTblBuscarProductos.getValueAt(fila, 2).toString();
            double precio = Double.parseDouble(vista.jTblBuscarProductos.getValueAt(fila, 3).toString());

            String cantStr = JOptionPane.showInputDialog(vista, "Ingrese la cantidad del producto a vender:", "Cantidad de Venta", JOptionPane.QUESTION_MESSAGE);
            if (cantStr == null) return; // Cancelado por el usuario

            try {
                int cantidad = Integer.parseInt(cantStr.trim());
                if (cantidad <= 0) {
                    JOptionPane.showMessageDialog(vista, "La cantidad debe ser mayor a 0.", "Valor Inválido", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Validar stock disponible
                int stockDisponible = inventarioDao.obtenerStockPorCodigo(codigo);
                int cantEnTabla = 0;
                DefaultTableModel model = (DefaultTableModel) vista.jTblDetalleProductos.getModel();
                for (int i = 0; i < model.getRowCount(); i++) {
                    if (model.getValueAt(i, 0).toString().equals(codigo)) {
                        cantEnTabla = Integer.parseInt(model.getValueAt(i, 2).toString());
                        break;
                    }
                }

                if (cantidad + cantEnTabla > stockDisponible) {
                    JOptionPane.showMessageDialog(vista, "No hay suficiente stock en inventario.\nStock disponible: " + stockDisponible + "\nCantidad ya agregada en carrito: " + cantEnTabla, "Stock Insuficiente", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Agregar el producto y recalcular
                agregarProductoALaTabla(codigo, nombre, cantidad, precio);

                // Redirigir a Nueva Venta (índice 0)
                vista.jTabbedPane1.setSelectedIndex(0);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vista, "Por favor ingrese un número entero válido para la cantidad.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == vista.jButton3) {
            // Consumidor Final
            clienteSeleccionado = null;
            vista.jTxtTipoId.setText("Consumidor Final");
            vista.jTxtNumeroId.setText("9999999999");
            vista.jTxtNombreCliente.setText("Consumidor Final");
            vista.jtxtDireccion.setText("S/N");
            vista.jTxtCorreo.setText("");

        } else if (e.getSource() == vista.jBtnGenerarFactura) {
            // Generar Factura
            String idStr = vista.jTxtNumeroId.getText().trim();
            if (idStr.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Debe seleccionar un cliente o elegir Consumidor Final.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            DefaultTableModel modelDetalle = (DefaultTableModel) vista.jTblDetalleProductos.getModel();
            if (modelDetalle.getRowCount() == 0) {
                JOptionPane.showMessageDialog(vista, "Debe agregar al menos un producto a la factura.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double subtotal = 0;
            List<Object[]> detalles = new ArrayList<>();
            for (int i = 0; i < modelDetalle.getRowCount(); i++) {
                Object[] row = new Object[5];
                row[0] = modelDetalle.getValueAt(i, 0); // CODIGO
                row[1] = modelDetalle.getValueAt(i, 1); // DESCRIPCIÓN
                row[2] = modelDetalle.getValueAt(i, 2); // CANTIDAD
                row[3] = modelDetalle.getValueAt(i, 3); // P. UNIT
                row[4] = modelDetalle.getValueAt(i, 4); // SUBTOTAL
                detalles.add(row);
                subtotal += Double.parseDouble(row[4].toString());
            }

            double iva = subtotal * 0.15;
            double total = subtotal + iva;

            Factura f = new Factura();
            f.setNumeroFactura(generarSiguienteNumeroFactura());
            f.setIdUsuario(usuarioLogueado != null ? usuarioLogueado.getIdUsuario() : 1);
            f.setSubtotal(subtotal);
            f.setIva(iva);
            f.setTotal(total);
            f.setEstado("Completada");
            f.setIdCliente(clienteSeleccionado != null ? clienteSeleccionado.getIdCliente() : null);

            if (facturaDao.registrarFactura(f, detalles)) {
                JOptionPane.showMessageDialog(vista, "Factura " + f.getNumeroFactura() + " generada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                
                // Limpiar campos
                modelDetalle.setRowCount(0);
                vista.jTxtTipoId.setText("");
                vista.jTxtNumeroId.setText("");
                vista.jTxtNombreCliente.setText("");
                vista.jtxtDireccion.setText("");
                vista.jTxtCorreo.setText("");
                clienteSeleccionado = null;
                recalcularTotales();
                listarFacturas();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al generar la factura. Verifique si hay suficiente stock disponible.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == vista.jBtnVerClientes) {
            vista.jTabbedPane1.setSelectedIndex(3);
            vista.jTxtBuscarCliente.setText("");
            listarClientesBuscar("");
        } else if (e.getSource() == vista.jBtnMas) {
            int fila = vista.jTblDetalleProductos.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(vista, "Seleccione un producto de la tabla detalle para aumentar la cantidad.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            DefaultTableModel model = (DefaultTableModel) vista.jTblDetalleProductos.getModel();
            int cantActual = Integer.parseInt(model.getValueAt(fila, 2).toString());
            int nuevaCant = cantActual + 1;

            // Validar stock disponible
            String codigo = model.getValueAt(fila, 0).toString();
            int stockDisponible = inventarioDao.obtenerStockPorCodigo(codigo);
            if (nuevaCant > stockDisponible) {
                JOptionPane.showMessageDialog(vista, "No hay suficiente stock en inventario.\nStock disponible: " + stockDisponible, "Stock Insuficiente", JOptionPane.WARNING_MESSAGE);
                return;
            }

            isUpdatingTable = true;
            try {
                model.setValueAt(nuevaCant, fila, 2);
                double precio = Double.parseDouble(model.getValueAt(fila, 3).toString());
                model.setValueAt(nuevaCant * precio, fila, 4);
            } finally {
                isUpdatingTable = false;
            }
            recalcularTotales();

        } else if (e.getSource() == vista.jBtnMenos) {
            int fila = vista.jTblDetalleProductos.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(vista, "Seleccione un producto de la tabla detalle para disminuir la cantidad.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            DefaultTableModel model = (DefaultTableModel) vista.jTblDetalleProductos.getModel();
            int cantActual = Integer.parseInt(model.getValueAt(fila, 2).toString());
            if (cantActual <= 1) {
                JOptionPane.showMessageDialog(vista, "La cantidad mínima debe ser 1.", "Cantidad Mínima", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int nuevaCant = cantActual - 1;

            isUpdatingTable = true;
            try {
                model.setValueAt(nuevaCant, fila, 2);
                double precio = Double.parseDouble(model.getValueAt(fila, 3).toString());
                model.setValueAt(nuevaCant * precio, fila, 4);
            } finally {
                isUpdatingTable = false;
            }
            recalcularTotales();
        }
    }

    private void handleCantidadModificada(int row) {
        isUpdatingTable = true;
        try {
            DefaultTableModel model = (DefaultTableModel) vista.jTblDetalleProductos.getModel();
            Object value = model.getValueAt(row, 2);
            if (value == null) return;

            String cantStr = value.toString().trim();
            int nuevaCant;
            try {
                nuevaCant = Integer.parseInt(cantStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vista, "La cantidad debe ser un número entero válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
                model.setValueAt(1, row, 2);
                nuevaCant = 1;
            }

            if (nuevaCant < 1) {
                JOptionPane.showMessageDialog(vista, "La cantidad debe ser al menos 1.", "Cantidad Inválida", JOptionPane.WARNING_MESSAGE);
                model.setValueAt(1, row, 2);
                nuevaCant = 1;
            }

            // Validar stock disponible en inventario
            String codigo = model.getValueAt(row, 0).toString();
            int stockDisponible = inventarioDao.obtenerStockPorCodigo(codigo);
            if (nuevaCant > stockDisponible) {
                JOptionPane.showMessageDialog(vista, "No hay suficiente stock en inventario.\nStock disponible: " + stockDisponible, "Stock Insuficiente", JOptionPane.WARNING_MESSAGE);
                model.setValueAt(stockDisponible > 0 ? stockDisponible : 1, row, 2);
                nuevaCant = stockDisponible > 0 ? stockDisponible : 1;
            }

            // Actualizar subtotal de la fila
            double precio = Double.parseDouble(model.getValueAt(row, 3).toString());
            double nuevoSubtotal = nuevaCant * precio;
            model.setValueAt(nuevoSubtotal, row, 4);

            recalcularTotales();
        } catch (Exception e) {
            System.err.println("Error al modificar cantidad: " + e.getMessage());
        } finally {
            isUpdatingTable = false;
        }
    }

    private String generarSiguienteNumeroFactura() {
        String ultimoNum = facturaDao.obtenerUltimoNumeroFactura();
        if (ultimoNum == null) {
            return "FACT-001";
        }
        try {
            int lastDash = ultimoNum.lastIndexOf("-");
            if (lastDash == -1) {
                return ultimoNum + "-001";
            }
            String prefix = ultimoNum.substring(0, lastDash + 1);
            String numStr = ultimoNum.substring(lastDash + 1);
            long sec = Long.parseLong(numStr);
            sec++;
            String formatStr = "%s%0" + numStr.length() + "d";
            return String.format(formatStr, prefix, sec);
        } catch (Exception e) {
            return "FACT-001";
        }
    }
}
