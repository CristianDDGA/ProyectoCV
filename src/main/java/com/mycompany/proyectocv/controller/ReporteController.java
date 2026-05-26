package com.mycompany.proyectocv.controller;

import com.mycompany.proyectocv.daos.ConexionBD;
import com.mycompany.proyectocv.views.VistaAdmin;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public class ReporteController implements ActionListener {

    private VistaAdmin vista;
    private ConexionBD conexion;

    public ReporteController(VistaAdmin vista) {
        this.vista = vista;
        this.conexion = new ConexionBD();

        // Escuchar los clics de los botones de la pestaña de Reportes
        this.vista.jBtnReporteInventario.addActionListener(this);
        this.vista.jBtnReporteVentas.addActionListener(this);
        this.vista.jBtnBuscarCajero.addActionListener(this); // <-- FALTA ESTA LÍNEA
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.jBtnReporteInventario) {
            generarReporteInventario();
        }
        if (e.getSource() == vista.jBtnReporteVentas) {
            generarReporteVentas();
        }
        if (e.getSource() == vista.jBtnBuscarCajero) {
            generarReporteVentasCajero();
        }
    }

    private void generarReporteInventario() {
        try {
            // Capturar el filtro de stock desde el JTextField
            int limiteStock = 999999;
            String textoFiltro = vista.jTxtFiltroStock.getText().trim();
            if (!textoFiltro.isEmpty()) {
                limiteStock = Integer.parseInt(textoFiltro);
            }

            // Configurar el parámetro para el mapa de JasperReports
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("Param_StockLimite", limiteStock);

            // Ajusta la ruta exacta según dónde guardes el archivo .jrxml
            String rutaReporte = "src/main/java/com/mycompany/proyectocv/reportes/ReporteInventario.jrxml";
            JasperReport reporte = JasperCompileManager.compileReport(rutaReporte);
            Connection con = conexion.conectarBD();
            JasperPrint print = JasperFillManager.fillReport(reporte, parametros, con);

            // Desplegar el visor sin cerrar toda la aplicación al salir
            JasperViewer.viewReport(print, false);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "El límite de stock debe ser un número entero.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error al generar el reporte de inventario: \n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generarReporteVentasCajero() {
        try {
            String fechaInicio = vista.jTxtFechaInicio.getText().trim();
            String fechaFin = vista.jTxtFechaFin.getText().trim();
            String cajeroBuscado = vista.jTxtBuscarCajero.getText().trim(); // Leemos el buscador

            if (fechaInicio.isEmpty() || fechaFin.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Por favor, ingrese la Fecha de Inicio y la Fecha de Fin.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Map<String, Object> parametros = new HashMap<>();
            parametros.put("Param_FechaInicio", java.sql.Date.valueOf(fechaInicio));
            parametros.put("Param_FechaFin", java.sql.Date.valueOf(fechaFin));

            // LA MAGIA DEL BUSCADOR:
            if (cajeroBuscado.isEmpty()) {
                // Si está vacío, enviamos el comodín "%" de SQL para que traiga TODOS los cajeros
                parametros.put("Param_Cajero", "%");
            } else {
                // Si escribió algo, enviamos el nombre exacto
                parametros.put("Param_Cajero", cajeroBuscado);
            }

            InputStream logoStream = getClass().getResourceAsStream("/UTA-STORE.png");
            if (logoStream != null) {
                parametros.put("Param_Logo", logoStream);
            }

            String rutaReporte = "src/main/java/com/mycompany/proyectocv/reportes/ReporteVentas.jrxml";

            JasperReport reporte = JasperCompileManager.compileReport(rutaReporte);
            Connection con = conexion.conectarBD();
            JasperPrint print = JasperFillManager.fillReport(reporte, parametros, con);

            JasperViewer.viewReport(print, false);

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(vista, "El formato de fecha es incorrecto. Use YYYY-MM-DD.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error al generar el reporte: \n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generarReporteVentas() {
        try {
            String fechaI = vista.jTxtFechaInicio.getText().trim();
            String fechaF = vista.jTxtFechaFin.getText().trim();

            Map<String, Object> parametros = new HashMap<>();
            // Enviamos el String directo, sin convertir a Date en Java
            parametros.put("Param_FechaInicio", fechaI);
            parametros.put("Param_FechaFin", fechaF);

            InputStream logoStream = getClass().getResourceAsStream("/UTA-STORE.png");
            if (logoStream != null) {
                parametros.put("Param_Logo", logoStream);
            }

            String ruta = "src/main/java/com/mycompany/proyectocv/reportes/ReporteVentasGeneral.jrxml";
            JasperReport reporte = JasperCompileManager.compileReport(ruta);
            Connection con = conexion.conectarBD();
            JasperPrint print = JasperFillManager.fillReport(reporte, parametros, con);

            JasperViewer.viewReport(print, false);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }
}
