package com.mycompany.proyectocv.utils;

import com.google.zxing.WriterException;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class VisorCodigoBarras extends JDialog {

    private JLabel lblImagen;
    private JLabel lblCodigo;
    private JButton btnGuardar;
    private JButton btnAbrirCarpeta;
    private JButton btnCerrar;
    private String codigo;
    private String nombreProducto;

    public VisorCodigoBarras(JFrame parent, String codigo, String nombreProducto) {
        super(parent, "Código de Barras - " + nombreProducto, true);
        this.codigo = codigo;
        this.nombreProducto = nombreProducto;
        initComponents(codigo);
        setLocationRelativeTo(parent);
    }

    private void initComponents(String codigo) {
        setSize(420, 320);
        setResizable(false);
        setLayout(new BorderLayout(10, 10));

        ((JPanel) getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15));

        lblCodigo = new JLabel("Código: " + codigo);
        lblCodigo.setFont(new java.awt.Font("Segoe UI", 1, 18));
        lblCodigo.setHorizontalAlignment(JLabel.CENTER);

        try {
            BufferedImage img = GeneradorCodigoBarras.generarImagenCode128(codigo, 350, 120);
            Image scaled = img.getScaledInstance(350, 120, Image.SCALE_DEFAULT);
            lblImagen = new JLabel(new ImageIcon(scaled));
            lblImagen.setHorizontalAlignment(JLabel.CENTER);
        } catch (WriterException | NullPointerException e) {
            lblImagen = new JLabel("Error al generar código de barras");
            lblImagen.setHorizontalAlignment(JLabel.CENTER);
            System.err.println("Error generando código: " + e.getMessage());
        }

        btnGuardar = new JButton("💾 Guardar Imagen");
        btnAbrirCarpeta = new JButton("📂 Abrir Carpeta");
        btnCerrar = new JButton("Cerrar");

        btnGuardar.addActionListener(e -> guardarImagen());
        btnAbrirCarpeta.addActionListener(e -> abrirCarpeta());
        btnCerrar.addActionListener(e -> dispose());

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panelBotones.add(btnGuardar);
        panelBotones.add(btnAbrirCarpeta);
        panelBotones.add(btnCerrar);

        add(lblCodigo, BorderLayout.NORTH);
        add(lblImagen, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    dispose();
                }
            }
        });
    }

    private void guardarImagen() {
        try {
            File archivo = GestorCodigosBarras.guardarCodigo(codigo, nombreProducto);
            JOptionPane.showMessageDialog(this,
                    "Código guardado en:\n" + archivo.getAbsolutePath(),
                    "Guardado Exitoso",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (WriterException | IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar imagen: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirCarpeta() {
        try {
            String ruta = GestorCodigosBarras.getRutaCarpeta();
            java.awt.Desktop.getDesktop().open(new File(ruta));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo abrir la carpeta:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void mostrar(JFrame parent, String codigo, String nombreProducto) {
        SwingUtilities.invokeLater(() -> {
            VisorCodigoBarras dialog = new VisorCodigoBarras(parent, codigo, nombreProducto);
            dialog.setVisible(true);
        });
    }
}