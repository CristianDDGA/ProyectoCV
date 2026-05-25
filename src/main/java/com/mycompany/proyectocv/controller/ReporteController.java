package com.mycompany.proyectocv.controller;

import com.mycompany.proyectocv.daos.ConexionBD;
import com.mycompany.proyectocv.views.VistaAdmin;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.jBtnReporteInventario) {
            generarReporteInventario();
        }
        if (e.getSource() == vista.jBtnReporteVentas) {
            generarReporteVentas();
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
            String rutaReporte = "src/main/resources/ReporteInventario.jrxml";
            
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

    private void generarReporteVentas() {
        try {
            // Capturar los rangos de fechas de los JTextFields 
            String fechaInicio = vista.jTxtFechaInicio.getText().trim();
            String fechaFin = vista.jTxtFechaFin.getText().trim();

            if (fechaInicio.isEmpty() || fechaFin.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Por favor, ingrese la Fecha de Inicio y la Fecha de Fin.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Pasar las fechas convertidas a SQL Date para la consulta de Jasper
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("Param_FechaInicio", java.sql.Date.valueOf(fechaInicio));
            parametros.put("Param_FechaFin", java.sql.Date.valueOf(fechaFin));

            // Ajusta la ruta exacta según dónde guardes el archivo .jrxml
            String rutaReporte = "src/main/resources/ReporteVentas.jrxml";

            JasperReport reporte = JasperCompileManager.compileReport(rutaReporte);
            Connection con = conexion.conectarBD();
            JasperPrint print = JasperFillManager.fillReport(reporte, parametros, con);

            JasperViewer.viewReport(print, false);

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(vista, "El formato de fecha es incorrecto. Use YYYY-MM-DD.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error al generar el reporte de ventas: \n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}