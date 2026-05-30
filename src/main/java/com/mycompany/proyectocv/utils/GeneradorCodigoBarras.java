package com.mycompany.proyectocv.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GeneradorCodigoBarras {

    public static String generarCodigoUnico() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 13).toUpperCase();
    }

    public static String generarCodigoSecuencial(int correlativo) {
        return String.format("PROD%08d", correlativo);
    }

    public static BufferedImage generarImagenCode128(String codigo, int ancho, int alto) throws WriterException {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.MARGIN, 2);
        
        Code128Writer writer = new Code128Writer();
        BitMatrix bitMatrix = writer.encode(codigo, BarcodeFormat.CODE_128, ancho, alto, hints);
        
        return toBufferedImage(bitMatrix);
    }

    private static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        
        return image;
    }
}
