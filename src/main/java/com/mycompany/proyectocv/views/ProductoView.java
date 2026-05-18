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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author Lenovo
 */
public class ProductoView extends javax.swing.JFrame {

    private JPanel panelPrincipal;
    private JPanel panelForm;
    private JPanel panelBotones;
    private JPanel panelTabla;
    
    private JLabel lblCodigo;
    private JLabel lblNombre;
    private JLabel lblPrecio;
    private JLabel lblStock;
    private JLabel lblID;
    
    public JTextField jTxtCodigo;
    public JTextField jTxtNombre;
    public JTextField jTxtPrecio;
    public JTextField jTxtStock;
    public JTextField jTxtID;
    
    public JButton jButton1;
    public JButton jButton2;
    public JButton jButton4;
    public JButton jButton3;
    
    private JScrollPane scrollTabla;
    public JTable jTable1;
    private DefaultTableModel modeloTabla;

    public ProductoView() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Gestión de Inventario - ProyectoCV");
        setResizable(false);
        
        int frameWidth = 1000;
        int frameHeight = 720;
        setSize(frameWidth, frameHeight);

        panelPrincipal = new JPanel();
        panelPrincipal.setBackground(new Color(242, 242, 242));
        panelPrincipal.setLayout(null);
        setContentPane(panelPrincipal);

        // --- SECCIÓN 1: DATOS DEL PRODUCTO ---
        panelForm = new JPanel();
        panelForm.setBackground(new Color(242, 242, 242));
        panelForm.setLayout(null);
        
        TitledBorder borderForm = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1), 
            "Datos del Producto", 
            TitledBorder.LEFT, 
            TitledBorder.TOP, 
            new Font("Segoe UI", Font.BOLD, 18), 
            new Color(30, 30, 30)
        );
        panelForm.setBorder(borderForm);
        panelForm.setBounds(15, 20, 960, 280);

        int labelX = 30;
        int fieldX = 150;
        int fieldWidth = 350;
        int startY = 40;
        int rowGap = 45;
        int fieldHeight = 35;

        lblCodigo = createLabel("Código:");
        lblCodigo.setBounds(labelX, startY, 100, 30);
        jTxtCodigo = createTextField();
        jTxtCodigo.setBounds(fieldX, startY, fieldWidth, fieldHeight);

        lblNombre = createLabel("Nombre:");
        lblNombre.setBounds(labelX, startY + rowGap, 100, 30);
        jTxtNombre = createTextField();
        jTxtNombre.setBounds(fieldX, startY + rowGap, fieldWidth, fieldHeight);

        lblPrecio = createLabel("Precio:");
        lblPrecio.setBounds(labelX, startY + rowGap * 2, 100, 30);
        jTxtPrecio = createTextField();
        jTxtPrecio.setBounds(fieldX, startY + rowGap * 2, fieldWidth, fieldHeight);

        lblStock = createLabel("Stock:");
        lblStock.setBounds(labelX, startY + rowGap * 3, 100, 30);
        jTxtStock = createTextField();
        jTxtStock.setBounds(fieldX, startY + rowGap * 3, fieldWidth, fieldHeight);

        lblID = createLabel("ID:");
        lblID.setBounds(labelX, startY + rowGap * 4, 100, 30);
        jTxtID = createTextField();
        jTxtID.setEditable(false);
        jTxtID.setBackground(new Color(225, 225, 225));
        jTxtID.setBounds(fieldX, startY + rowGap * 4, fieldWidth, fieldHeight);

        panelForm.add(lblCodigo);
        panelForm.add(jTxtCodigo);
        panelForm.add(lblNombre);
        panelForm.add(jTxtNombre);
        panelForm.add(lblPrecio);
        panelForm.add(jTxtPrecio);
        panelForm.add(lblStock);
        panelForm.add(jTxtStock);
        panelForm.add(lblID);
        panelForm.add(jTxtID);
        panelPrincipal.add(panelForm);

        // --- SECCIÓN 2: BOTONES ---
        panelBotones = new JPanel();
        panelBotones.setBackground(new Color(242, 242, 242));
        panelBotones.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        panelBotones.setLayout(null);
        panelBotones.setBounds(15, 315, 960, 70);

        int btnWidth = 160;
        int btnHeight = 45;
        int btnGap = 20;
        int totalBtnsWidth = (btnWidth * 4) + (btnGap * 3);
        int startBtnX = (960 - totalBtnsWidth) / 2;

        jButton1 = createButton("💾 Guardar", new Color(46, 196, 160)); // Green
        jButton1.setBounds(startBtnX, 12, btnWidth, btnHeight);

        jButton2 = createButton("📝 Actualizar", new Color(32, 140, 206)); // Blue
        jButton2.setBounds(startBtnX + btnWidth + btnGap, 12, btnWidth, btnHeight);

        jButton4 = createButton("🗑️ Eliminar", new Color(232, 62, 62)); // Red
        jButton4.setBounds(startBtnX + (btnWidth + btnGap) * 2, 12, btnWidth, btnHeight);

        jButton3 = createButton("🧹 Limpiar", new Color(150, 160, 180)); // Gray
        jButton3.setBounds(startBtnX + (btnWidth + btnGap) * 3, 12, btnWidth, btnHeight);

        panelBotones.add(jButton1);
        panelBotones.add(jButton2);
        panelBotones.add(jButton4);
        panelBotones.add(jButton3);
        panelPrincipal.add(panelBotones);

        // --- SECCIÓN 3: TABLA ---
        panelTabla = new JPanel();
        panelTabla.setBackground(new Color(242, 242, 242));
        panelTabla.setLayout(null);
        
        TitledBorder borderTabla = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1), 
            "Lista de Inventario", 
            TitledBorder.LEFT, 
            TitledBorder.TOP, 
            new Font("Segoe UI", Font.BOLD, 18), 
            new Color(30, 30, 30)
        );
        panelTabla.setBorder(borderTabla);
        panelTabla.setBounds(15, 400, 960, 260);

        modeloTabla = new DefaultTableModel(
            new Object [][] {},
            new String [] {"ID", "Código", "Nombre", "Precio", "Stock"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        jTable1 = new JTable(modeloTabla);
        jTable1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jTable1.setRowHeight(32);
        jTable1.setSelectionBackground(new Color(210, 235, 255));
        jTable1.setSelectionForeground(Color.BLACK);
        jTable1.setGridColor(new Color(230, 230, 230));

        JTableHeader header = jTable1.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setBackground(new Color(245, 245, 245));
        header.setPreferredSize(new java.awt.Dimension(100, 40));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < jTable1.getColumnCount(); i++) {
            jTable1.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        scrollTabla = new JScrollPane(jTable1);
        scrollTabla.setBounds(15, 35, 930, 210);
        scrollTabla.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollTabla.getViewport().setBackground(Color.WHITE);

        panelTabla.add(scrollTabla);
        panelPrincipal.add(panelTabla);

        setLocationRelativeTo(null);

        jTable1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = jTable1.getSelectedRow();
                if (row >= 0) {
                    jTxtID.setText(jTable1.getValueAt(row, 0).toString());
                    jTxtCodigo.setText(jTable1.getValueAt(row, 1).toString());
                    jTxtNombre.setText(jTable1.getValueAt(row, 2).toString());
                    jTxtPrecio.setText(jTable1.getValueAt(row, 3).toString());
                    jTxtStock.setText(jTable1.getValueAt(row, 4).toString());
                }
            }
        });
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 20));
        label.setForeground(new Color(40, 40, 40));
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            new RoundBorder(new Color(200, 200, 200), 10),
            new EmptyBorder(5, 12, 5, 12)
        ));
        return field;
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setForeground(new Color(20, 20, 20));
        button.setBackground(bgColor);
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            new RoundBorder(new Color(150, 150, 150), 12),
            new EmptyBorder(5, 15, 5, 15)
        ));
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
                new ProductoView().setVisible(true);
            }
        });
    }

    // Getters para compatibilidad con controladores
    public javax.swing.JButton getjButton1() { return jButton1; }
    public javax.swing.JButton getjButton2() { return jButton2; }
    public javax.swing.JButton getjButton3() { return jButton3; }
    public javax.swing.JButton getjButton4() { return jButton4; }
    public javax.swing.JTable getjTable1() { return jTable1; }
    public javax.swing.JTextField getjTxtCodigo() { return jTxtCodigo; }
    public javax.swing.JTextField getjTxtID() { return jTxtID; }
    public javax.swing.JTextField getjTxtNombre() { return jTxtNombre; }
    public javax.swing.JTextField getjTxtPrecio() { return jTxtPrecio; }
    public javax.swing.JTextField getjTxtStock() { return jTxtStock; }

    public JButton getBtnGuardar() { return jButton1; }
    public JButton getBtnActualizar() { return jButton2; }
    public JButton getBtnEliminar() { return jButton4; }
    public JButton getBtnLimpiar() { return jButton3; }
    public JTable getTablaProductos() { return jTable1; }
    public JTextField getTxtCodigo() { return jTxtCodigo; }
    public JTextField getTxtID() { return jTxtID; }
    public JTextField getTxtNombre() { return jTxtNombre; }
    public JTextField getTxtPrecio() { return jTxtPrecio; }
    public JTextField getTxtStock() { return jTxtStock; }
    public DefaultTableModel getModeloTabla() { return modeloTabla; }
}
