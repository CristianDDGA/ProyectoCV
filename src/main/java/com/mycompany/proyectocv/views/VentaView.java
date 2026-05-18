/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.proyectocv.views;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author Lenovo
 */
public class VentaView extends javax.swing.JFrame {

    private JPanel panelHeader;
    private JPanel panelProductos;
    private JPanel panelCarrito;
    private JPanel panelResumen;
    private JLabel lblTitulo;
    private JLabel lblFecha;
    private JLabel lblUsuario;
    private JScrollPane scrollProductos;
    private JScrollPane scrollCarrito;
    private JTable tablaProductos;
    private JTable tablaCarrito;
    private DefaultTableModel modeloProductos;
    private DefaultTableModel modeloCarrito;
    private JTextField txtBuscar;
    private JComboBox<String> cmbCategoria;
    private JLabel lblSubtotal;
    private JLabel lblIVA;
    private JLabel lblTotal;
    private JTextField txtSubtotal;
    private JTextField txtIVA;
    private JTextField txtTotal;
    private JButton btnAgregar;
    private JButton btnQuitar;
    private JButton btnCobrar;
    private JButton btnNuevaVenta;
    private JButton btnCancelar;

    public VentaView() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sistema TUTI - Punto de Venta");
        setResizable(false);
        setBackground(new Color(240, 242, 247));

        int frameWidth = 1100;
        int frameHeight = 650;

        panelHeader = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(15, 32, 56),
                    getWidth(), 0, new Color(41, 98, 168)
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        panelHeader.setLayout(null);

        panelProductos = new JPanel();
        panelProductos.setOpaque(true);
        panelProductos.setBackground(Color.WHITE);
        panelProductos.setLayout(null);

        panelCarrito = new JPanel();
        panelCarrito.setOpaque(true);
        panelCarrito.setBackground(Color.WHITE);
        panelCarrito.setLayout(null);

        panelResumen = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(32, 140, 206),
                    0, getHeight(), new Color(41, 98, 168)
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
            }
        };
        panelResumen.setOpaque(false);
        panelResumen.setLayout(null);

        lblTitulo = new JLabel("Punto de Venta");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);

        lblFecha = new JLabel("Fecha: 18/05/2026");
        lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblFecha.setForeground(new Color(200, 215, 235));

        lblUsuario = new JLabel("👤 Cajero: admin");
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblUsuario.setForeground(new Color(200, 215, 235));

        txtBuscar = new JTextField();
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtBuscar.setBackground(new Color(248, 250, 255));
        txtBuscar.setBorder(BorderFactory.createCompoundBorder(
            new RoundBorder(new Color(210, 220, 235), 8),
            new EmptyBorder(8, 12, 8, 12)
        ));

        String[] categorias = {"Todas las categorías", "Bebidas", "Lácteos", "Snacks", "Abarrotes"};
        cmbCategoria = new JComboBox<>(categorias);
        cmbCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbCategoria.setBackground(new Color(248, 250, 255));

        modeloProductos = new DefaultTableModel(
            new Object [][] {},
            new String [] {"Código", "Producto", "Precio", "Stock"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        modeloCarrito = new DefaultTableModel(
            new Object [][] {},
            new String [] {"Código", "Producto", "Cant", "P.Unit", "Subt"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaProductos = new JTable(modeloProductos);
        tablaProductos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaProductos.setRowHeight(32);
        tablaProductos.setSelectionBackground(new Color(210, 235, 255));

        JTableHeader hProd = tablaProductos.getTableHeader();
        hProd.setFont(new Font("Segoe UI", Font.BOLD, 12));
        hProd.setPreferredSize(new java.awt.Dimension(100, 35));

        tablaCarrito = new JTable(modeloCarrito);
        tablaCarrito.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaCarrito.setRowHeight(32);
        tablaCarrito.setSelectionBackground(new Color(210, 235, 255));

        JTableHeader hCarr = tablaCarrito.getTableHeader();
        hCarr.setFont(new Font("Segoe UI", Font.BOLD, 12));
        hCarr.setPreferredSize(new java.awt.Dimension(100, 35));

        scrollProductos = new JScrollPane(tablaProductos);
        scrollProductos.setBorder(new LineBorder(new Color(225, 230, 240), 1, true));
        scrollProductos.getViewport().setBackground(Color.WHITE);

        scrollCarrito = new JScrollPane(tablaCarrito);
        scrollCarrito.setBorder(new LineBorder(new Color(225, 230, 240), 1, true));
        scrollCarrito.getViewport().setBackground(Color.WHITE);

        lblSubtotal = new JLabel("Subtotal:");
        lblSubtotal.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblSubtotal.setForeground(Color.WHITE);

        lblIVA = new JLabel("IVA (12%):");
        lblIVA.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblIVA.setForeground(Color.WHITE);

        lblTotal = new JLabel("TOTAL:");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotal.setForeground(Color.WHITE);

        txtSubtotal = createResultField();
        txtIVA = createResultField();
        txtTotal = createResultField();
        txtTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));

        btnCobrar = createActionButton("COBRAR", new Color(46, 196, 160));
        btnNuevaVenta = createActionButton("NUEVA VENTA", new Color(32, 140, 206));
        btnCancelar = createActionButton("CANCELAR", new Color(150, 160, 180));

        int hH = 65;
        panelHeader.setBounds(0, 0, frameWidth, hH);
        lblTitulo.setBounds(20, 15, 300, 30);
        lblFecha.setBounds(frameWidth - 160, 12, 140, 20);
        lblUsuario.setBounds(frameWidth - 160, 35, 140, 20);

        int sideW = 530;
        int sideH = 550;
        int gap = 15;

        panelProductos.setBounds( gap, hH + 15, sideW, sideH );
        txtBuscar.setBounds(10, 10, 320, 35);
        cmbCategoria.setBounds(340, 10, 180, 35);
        scrollProductos.setBounds(10, 55, sideW - 20, sideH - 70);

        panelProductos.add(txtBuscar);
        panelProductos.add(cmbCategoria);
        panelProductos.add(scrollProductos);

        panelCarrito.setBounds(gap * 2 + sideW, hH + 15, sideW, 380);
        JLabel lblCarr = new JLabel("🛒 Carrito");
        lblCarr.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblCarr.setBounds(10, 5, 200, 30);
        scrollCarrito.setBounds(10, 40, sideW - 20, 330);
        panelCarrito.add(lblCarr);
        panelCarrito.add(scrollCarrito);

        panelResumen.setBounds(gap * 2 + sideW, hH + 15 + 395, sideW, 140);
        int resY = 15;
        lblSubtotal.setBounds(20, resY, 70, 25);
        txtSubtotal.setBounds(100, resY, 100, 25);

        lblIVA.setBounds(20, resY + 35, 70, 25);
        txtIVA.setBounds(100, resY + 35, 100, 25);

        lblTotal.setBounds(20, resY + 75, 70, 30);
        txtTotal.setBounds(100, resY + 75, 110, 35);

        btnCobrar.setBounds(360, resY + 10, 150, 45);
        btnNuevaVenta.setBounds(360, resY + 65, 150, 40);
        btnCancelar.setBounds(230, resY + 80, 110, 30);

        panelResumen.add(lblSubtotal);
        panelResumen.add(txtSubtotal);
        panelResumen.add(lblIVA);
        panelResumen.add(txtIVA);
        panelResumen.add(lblTotal);
        panelResumen.add(txtTotal);
        panelResumen.add(btnCobrar);
        panelResumen.add(btnNuevaVenta);
        panelResumen.add(btnCancelar);

        add(panelHeader);
        panelHeader.add(lblTitulo);
        panelHeader.add(lblFecha);
        panelHeader.add(lblUsuario);
        add(panelProductos);
        add(panelCarrito);
        add(panelResumen);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelHeader, javax.swing.GroupLayout.PREFERRED_SIZE, frameWidth, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(panelProductos, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelResumen, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelProductos, javax.swing.GroupLayout.PREFERRED_SIZE, 585, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panelCarrito, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(panelResumen, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        pack();
        setLocationRelativeTo(null);
    }

    private JTextField createResultField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.BOLD, 13));
        field.setBackground(new Color(255, 255, 255, 220));
        field.setBorder(BorderFactory.createCompoundBorder(
            new RoundBorder(new Color(255, 255, 255, 100), 5),
            new EmptyBorder(5, 10, 5, 10)
        ));
        field.setEditable(false);
        field.setHorizontalAlignment(SwingConstants.RIGHT);
        field.setText("0.00");
        return field;
    }

    private JButton createActionButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setBorder(new EmptyBorder(8, 15, 8, 15));
        return button;
    }

    private static class RoundBorder implements javax.swing.border.Border {
        private final Color color;
        private final int radius;

        RoundBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(4, 8, 4, 8);
        }
    }

    public static void main(String args[]) {
        try {
            com.formdev.flatlaf.FlatLightLaf.setup();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentaView().setVisible(true);
            }
        });
    }

    public JTable getTablaProductos() { return tablaProductos; }
    public JTable getTablaCarrito() { return tablaCarrito; }
    public DefaultTableModel getModeloProductos() { return modeloProductos; }
    public DefaultTableModel getModeloCarrito() { return modeloCarrito; }
    public JTextField getTxtBuscar() { return txtBuscar; }
    public JComboBox<String> getCmbCategoria() { return cmbCategoria; }
    public JTextField getTxtSubtotal() { return txtSubtotal; }
    public JTextField getTxtIVA() { return txtIVA; }
    public JTextField getTxtTotal() { return txtTotal; }
    public JButton getBtnAgregar() { return btnAgregar; }
    public JButton getBtnQuitar() { return btnQuitar; }
    public JButton getBtnCobrar() { return btnCobrar; }
    public JButton getBtnNuevaVenta() { return btnNuevaVenta; }
    public JButton getBtnCancelar() { return btnCancelar; }
}
