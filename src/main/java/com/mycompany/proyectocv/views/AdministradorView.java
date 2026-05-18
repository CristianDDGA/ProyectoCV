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
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author Matius
 */
public class AdministradorView extends javax.swing.JFrame {

    private JPanel panelHeader;
    private JPanel panelSidebar;
    private JPanel panelContent;
    private JPanel panelCards;
    private JLabel lblTitulo;
    private JLabel lblSubtitulo;
    private JButton btnDashboard;
    private JButton btnProductos;
    private JButton btnVentas;
    private JButton btnReportes;
    private JButton btnUsuarios;
    private JButton btnConfiguracion;
    private JButton btnCerrarSesion;
    private JPanel cardVentas;
    private JPanel cardProductos;
    private JPanel cardUsuarios;
    private JPanel cardGanancias;
    private JLabel lblVentasCount;
    private JLabel lblProductosCount;
    private JLabel lblUsuariosCount;
    private JLabel lblGananciasCount;
    private JScrollPane scrollTabla;
    private JTable tablaActividad;
    private DefaultTableModel modeloActividad;

    private static final Color COLOR_TRANSPARENT = new Color(0, 0, 0, 0);

    public AdministradorView() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sistema TUTI - Admin");
        setResizable(false);
        setBackground(new Color(240, 242, 247));

        int frameWidth = 1100;
        int frameHeight = 700;

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

        panelSidebar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(20, 38, 68));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        panelSidebar.setOpaque(false);
        panelSidebar.setLayout(null);

        panelContent = new JPanel();
        panelContent.setOpaque(true);
        panelContent.setBackground(new Color(240, 242, 247));
        panelContent.setLayout(null);

        panelCards = new JPanel();
        panelCards.setOpaque(false);
        panelCards.setLayout(null);

        lblTitulo = new JLabel("Administración");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);

        lblSubtitulo = new JLabel("Gestión central del sistema");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitulo.setForeground(new Color(200, 215, 235));

        btnDashboard = createSidebarButton("📊 Dashboard", true);
        btnProductos = createSidebarButton("📦 Productos", false);
        btnVentas = createSidebarButton("🛒 Ventas", false);
        btnReportes = createSidebarButton("📈 Reportes", false);
        btnUsuarios = createSidebarButton("👥 Usuarios", false);
        btnConfiguracion = createSidebarButton("⚙️ Configuración", false);
        btnCerrarSesion = createSidebarButton("🚪 Cerrar Sesión", false);

        cardVentas = createStatCard("Ventas", "124", new Color(46, 196, 160));
        cardProductos = createStatCard("Stock", "1,250", new Color(32, 140, 206));
        cardUsuarios = createStatCard("Usuarios", "8", new Color(255, 159, 28));
        cardGanancias = createStatCard("Ingresos", "$12.4k", new Color(155, 89, 218));

        modeloActividad = new DefaultTableModel(
            new Object [][] {
                {"FACT-001", "Juan Pérez", "$45.50", "OK", "15:30"},
                {"FACT-002", "María López", "$23.00", "OK", "15:42"},
                {"FACT-003", "Carlos García", "$67.25", "PEND", "15:55"},
                {"FACT-004", "Ana Martínez", "$12.75", "OK", "16:10"},
                {"FACT-005", "Roberto Sánchez", "$89.00", "ANUL", "16:25"}
            },
            new String [] {"ID", "Cliente", "Total", "Estado", "Hora"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaActividad = new JTable(modeloActividad);
        tablaActividad.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaActividad.setRowHeight(32);
        tablaActividad.setSelectionBackground(new Color(210, 235, 255));

        JTableHeader hAct = tablaActividad.getTableHeader();
        hAct.setFont(new Font("Segoe UI", Font.BOLD, 12));
        hAct.setPreferredSize(new java.awt.Dimension(100, 35));

        scrollTabla = new JScrollPane(tablaActividad);
        scrollTabla.setBorder(new LineBorder(new Color(220, 225, 235), 1, true));
        scrollTabla.getViewport().setBackground(Color.WHITE);

        int hH = 65;
        panelHeader.setBounds(0, 0, frameWidth, hH);
        lblTitulo.setBounds(20, 10, 300, 30);
        lblSubtitulo.setBounds(20, 35, 300, 20);

        int sideW = 200;
        panelSidebar.setBounds(0, hH, sideW, frameHeight - hH);
        int menuY = 20;
        int menuS = 45;
        btnDashboard.setBounds(10, menuY, 180, 40);
        btnProductos.setBounds(10, menuY + menuS, 180, 40);
        btnVentas.setBounds(10, menuY + menuS * 2, 180, 40);
        btnReportes.setBounds(10, menuY + menuS * 3, 180, 40);
        btnUsuarios.setBounds(10, menuY + menuS * 4, 180, 40);
        btnConfiguracion.setBounds(10, menuY + menuS * 5, 180, 40);
        btnCerrarSesion.setBounds(10, frameHeight - hH - 60, 180, 40);

        panelSidebar.add(btnDashboard);
        panelSidebar.add(btnProductos);
        panelSidebar.add(btnVentas);
        panelSidebar.add(btnReportes);
        panelSidebar.add(btnUsuarios);
        panelSidebar.add(btnConfiguracion);
        panelSidebar.add(btnCerrarSesion);

        panelContent.setBounds(sideW, hH, frameWidth - sideW, frameHeight - hH);
        
        panelCards.setBounds(20, 20, 860, 110);
        int cW = 200;
        int cS = 15;
        cardVentas.setBounds(0, 0, cW, 100);
        cardProductos.setBounds(cW + cS, 0, cW, 100);
        cardUsuarios.setBounds((cW + cS) * 2, 0, cW, 100);
        cardGanancias.setBounds((cW + cS) * 3, 0, cW, 100);

        panelCards.add(cardVentas);
        panelCards.add(cardProductos);
        panelCards.add(cardUsuarios);
        panelCards.add(cardGanancias);
        panelContent.add(panelCards);

        JLabel lblAct = new JLabel("📋 Actividad Reciente");
        lblAct.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblAct.setBounds(25, 145, 300, 30);
        panelContent.add(lblAct);

        scrollTabla.setBounds(20, 185, 860, 420);
        panelContent.add(scrollTabla);

        add(panelHeader);
        panelHeader.add(lblTitulo);
        panelHeader.add(lblSubtitulo);
        add(panelSidebar);
        add(panelContent);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelHeader, javax.swing.GroupLayout.PREFERRED_SIZE, frameWidth, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelSidebar, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(panelContent, javax.swing.GroupLayout.PREFERRED_SIZE, 900, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelSidebar, javax.swing.GroupLayout.PREFERRED_SIZE, 635, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelContent, javax.swing.GroupLayout.PREFERRED_SIZE, 635, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pack();
        setLocationRelativeTo(null);
    }

    private JButton createSidebarButton(String text, boolean selected) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setForeground(selected ? new Color(32, 140, 206) : new Color(200, 210, 225));
        button.setBackground(selected ? new Color(40, 70, 110, 50) : COLOR_TRANSPARENT);
        button.setBorder(new EmptyBorder(10, 15, 10, 15));
        button.setFocusPainted(false);
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        return button;
    }

    private JPanel createStatCard(String title, String value, Color accentColor) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(accentColor);
                g2.fillRoundRect(0, getHeight() - 4, getWidth(), 4, 4, 4);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(null);

        JLabel lblT = new JLabel(title);
        lblT.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblT.setForeground(new Color(130, 145, 165));
        lblT.setBounds(15, 15, 150, 20);

        JLabel lblV = new JLabel(value);
        lblV.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblV.setForeground(new Color(30, 40, 60));
        lblV.setBounds(15, 35, 150, 35);

        card.add(lblT);
        card.add(lblV);
        return card;
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

        java.awt.EventQueue.invokeLater(() -> new AdministradorView().setVisible(true));
    }

    public JButton getBtnDashboard() { return btnDashboard; }
    public JButton getBtnProductos() { return btnProductos; }
    public JButton getBtnVentas() { return btnVentas; }
    public JButton getBtnReportes() { return btnReportes; }
    public JButton getBtnUsuarios() { return btnUsuarios; }
    public JButton getBtnConfiguracion() { return btnConfiguracion; }
    public JButton getBtnCerrarSesion() { return btnCerrarSesion; }
    public JTable getTablaActividad() { return tablaActividad; }
    public DefaultTableModel getModeloActividad() { return modeloActividad; }
}
