package com.mycompany.proyectocv.utils;

import com.google.zxing.WriterException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GestorCodigosBarras {

    private static final String CARPETA_BASE = "codigos-barras";

    public static String getRutaCarpeta() {
        File carpeta = new File(CARPETA_BASE);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
        return carpeta.getAbsolutePath();
    }

    public static String getNombreArchivo(String codigo, String nombreProducto) {
        String nombreLimpio = nombreProducto
                .replaceAll("[^a-zA-Z0-9áéíóúñÑ\\s]", "")
                .trim()
                .replaceAll("\\s+", "_");
        if (nombreLimpio.length() > 30) {
            nombreLimpio = nombreLimpio.substring(0, 30);
        }
        return codigo + "_" + nombreLimpio + ".png";
    }

    public static File guardarCodigo(String codigo, String nombreProducto) throws WriterException, IOException {
        BufferedImage img = GeneradorCodigoBarras.generarImagenCode128(codigo, 350, 120);
        String rutaCarpeta = getRutaCarpeta();
        String nombreArchivo = getNombreArchivo(codigo, nombreProducto);

        // Evitar sobrescribir: si existe, agregar sufijo
        File archivo = new File(rutaCarpeta, nombreArchivo);
        int contador = 1;
        while (archivo.exists()) {
            String base = nombreArchivo.replace(".png", "");
            archivo = new File(rutaCarpeta, base + "_v" + contador + ".png");
            contador++;
        }

        ImageIO.write(img, "png", archivo);
        return archivo;
    }

    public static File[] listarCodigosGuardados() {
        File carpeta = new File(CARPETA_BASE);
        if (!carpeta.exists()) {
            return new File[0];
        }
        return carpeta.listFiles((dir, name) -> name.endsWith(".png"));
    }

    public static int contarCodigosGuardados() {
        File[] archivos = listarCodigosGuardados();
        return archivos != null ? archivos.length : 0;
    }
}
