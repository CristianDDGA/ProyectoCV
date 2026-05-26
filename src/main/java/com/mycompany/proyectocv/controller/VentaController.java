package com.mycompany.proyectocv.controller;

import com.mycompany.proyectocv.daos.ClienteDAO;
import com.mycompany.proyectocv.daos.FacturaDAO;
import com.mycompany.proyectocv.model.Cliente;
import com.mycompany.proyectocv.model.Factura;
import com.mycompany.proyectocv.views.VistaCajero;
import com.formdev.flatlaf.ui.FlatTabbedPaneUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class VentaController implements ActionListener {

    private VistaCajero vista;
    private ClienteDAO clienteDao;
    private FacturaDAO facturaDao;
    private DefaultTableModel modeloClientes;
    private DefaultTableModel modeloFacturas;

    public VentaController(VistaCajero vista, ClienteDAO clienteDao, FacturaDAO facturaDao) {
        this.vista = vista;
        this.clienteDao = clienteDao;
        this.facturaDao = facturaDao;

        // Ocultar las cabeceras de las pestañas para usar FlatLaf y controlarlas con los botones laterales
        this.vista.jTabbedPane1.setUI(new FlatTabbedPaneUI() {
            @Override
            protected int calculateTabAreaHeight(int tabPlacement, int horizRunCount, int maxTabHeight) {
                return 0; // Altura de pestaña 0 para ocultar el header
            }
        });

        // Registrar los action listeners para los botones laterales
        this.vista.jBtnNuevaVenta.addActionListener(this);
        this.vista.jBtnClientes.addActionListener(this);
        this.vista.jBtnHistorial.addActionListener(this);

        // Inicializar y cargar los datos de las tablas
        listarClientes();
        listarFacturas();
    }

    public void listarClientes() {
        List<Cliente> lista = clienteDao.listarClientes();
        modeloClientes = (DefaultTableModel) vista.jTblClientes.getModel();
        modeloClientes.setColumnIdentifiers(new Object[]{"ID", "Cédula/RUC", "Nombre", "Apellido", "Teléfono", "Email", "Dirección", "Fecha Registro"});
        
        modeloClientes.setRowCount(0); // Limpiar filas anteriores

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
            modeloClientes.addRow(ob);
        }
        vista.jTblClientes.setModel(modeloClientes);
    }

    public void listarFacturas() {
        List<Factura> lista = facturaDao.listarFacturas();
        modeloFacturas = (DefaultTableModel) vista.jTblFacturas.getModel();
        modeloFacturas.setColumnIdentifiers(new Object[]{"ID", "Nro. Factura", "Fecha", "Usuario ID", "Subtotal", "IVA", "Total", "Estado", "Cliente ID"});
        
        modeloFacturas.setRowCount(0); // Limpiar filas anteriores

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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.jBtnNuevaVenta) {
            vista.jTabbedPane1.setSelectedIndex(0);
        } else if (e.getSource() == vista.jBtnClientes) {
            vista.jTabbedPane1.setSelectedIndex(1);
            listarClientes(); // Refrescar los datos al seleccionar la pestaña
        } else if (e.getSource() == vista.jBtnHistorial) {
            vista.jTabbedPane1.setSelectedIndex(2);
            listarFacturas(); // Refrescar los datos al seleccionar la pestaña
        }
    }
}
