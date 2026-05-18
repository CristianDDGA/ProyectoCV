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
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Lenovo
 */
public class LoginView extends javax.swing.JFrame {

    private JPanel panelFondo;
    private JPanel panelLogin;
    private JLabel lblTitulo;
    private JLabel lblSubtitulo;
    private JLabel lblUsuario;
    private JLabel lblContrasenia;
    public JTextField jTxtUsuario;
    public JPasswordField jPswContrasenia;
    public JButton jBtnLogin;
    private JLabel lblIconoUsuario;

    public LoginView() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sistema TUTI - Login");
        setResizable(false);

        // Frame Size
        int frameWidth = 450;
        int frameHeight = 580;

        panelFondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(15, 32, 56),
                    getWidth(), getHeight(), new Color(41, 98, 168)
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        panelFondo.setLayout(null);

        panelLogin = new JPanel();
        panelLogin.setOpaque(true);
        panelLogin.setBackground(new Color(255, 255, 255, 250));
        panelLogin.setLayout(null);

        lblIconoUsuario = new JLabel("🔐");
        lblIconoUsuario.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        lblIconoUsuario.setHorizontalAlignment(SwingConstants.CENTER);

        lblTitulo = new JLabel("Sistema TUTI");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(15, 32, 56));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        lblSubtitulo = new JLabel("Gestión de Punto de Venta");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitulo.setForeground(new Color(100, 120, 140));
        lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);

        lblUsuario = new JLabel("USUARIO");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUsuario.setForeground(new Color(50, 70, 95));

        jTxtUsuario = new JTextField();
        jTxtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jTxtUsuario.setForeground(new Color(30, 40, 55));
        jTxtUsuario.setBackground(new Color(248, 250, 255));
        jTxtUsuario.setBorder(BorderFactory.createCompoundBorder(
            new RoundBorder(new Color(200, 210, 225), 8),
            new EmptyBorder(10, 15, 10, 15)
        ));
        jTxtUsuario.setCaretColor(new Color(32, 140, 206));

        lblContrasenia = new JLabel("CONTRASEÑA");
        lblContrasenia.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblContrasenia.setForeground(new Color(50, 70, 95));

        jPswContrasenia = new JPasswordField();
        jPswContrasenia.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jPswContrasenia.setForeground(new Color(30, 40, 55));
        jPswContrasenia.setBackground(new Color(248, 250, 255));
        jPswContrasenia.setBorder(BorderFactory.createCompoundBorder(
            new RoundBorder(new Color(200, 210, 225), 8),
            new EmptyBorder(10, 15, 10, 15)
        ));
        jPswContrasenia.setCaretColor(new Color(32, 140, 206));

        jBtnLogin = new JButton("INICIAR SESIÓN");
        jBtnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jBtnLogin.setForeground(Color.WHITE);
        jBtnLogin.setBackground(new Color(32, 140, 206));
        jBtnLogin.setBorder(new EmptyBorder(12, 20, 12, 20));
        jBtnLogin.setFocusPainted(false);
        jBtnLogin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jBtnLogin.setContentAreaFilled(true);
        jBtnLogin.setOpaque(true);

        int panelWidth = 350;
        int panelHeight = 460;
        int x = (frameWidth - panelWidth) / 2;
        int y = (frameHeight - panelHeight) / 2;

        panelLogin.setBounds(x, y, panelWidth, panelHeight);
        panelLogin.setBorder(new EmptyBorder(25, 30, 25, 30));

        // Compact positioning within panelLogin
        lblIconoUsuario.setBounds(0, 20, panelWidth, 50);
        lblTitulo.setBounds(0, 70, panelWidth, 40);
        lblSubtitulo.setBounds(0, 105, panelWidth, 20);

        int fieldX = 35;
        int fieldWidth = panelWidth - 70;

        lblUsuario.setBounds(fieldX, 150, fieldWidth, 20);
        jTxtUsuario.setBounds(fieldX, 175, fieldWidth, 42);

        lblContrasenia.setBounds(fieldX, 235, fieldWidth, 20);
        jPswContrasenia.setBounds(fieldX, 260, fieldWidth, 42);

        jBtnLogin.setBounds(fieldX, 350, fieldWidth, 48);

        panelLogin.add(lblIconoUsuario);
        panelLogin.add(lblTitulo);
        panelLogin.add(lblSubtitulo);
        panelLogin.add(lblUsuario);
        panelLogin.add(jTxtUsuario);
        panelLogin.add(lblContrasenia);
        panelLogin.add(jPswContrasenia);
        panelLogin.add(jBtnLogin);

        panelFondo.add(panelLogin);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelFondo, javax.swing.GroupLayout.PREFERRED_SIZE, frameWidth, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelFondo, javax.swing.GroupLayout.PREFERRED_SIZE, frameHeight, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
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
                new LoginView().setVisible(true);
            }
        });
    }

    public javax.swing.JButton getjBtnLogin() {
        return jBtnLogin;
    }

    public javax.swing.JPasswordField getjPswContrasenia() {
        return jPswContrasenia;
    }

    public javax.swing.JTextField getjTxtUsuario() {
        return jTxtUsuario;
    }
}
