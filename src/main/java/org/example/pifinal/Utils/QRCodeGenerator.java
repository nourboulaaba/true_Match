package org.example.pifinal.Utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class QRCodeGenerator {

    private static final String BASE_URL = "https://glrpfdmtqqifsqupgzxr9a.on.drv.tw/userData.html";

    public static boolean generateJobQRCode(int id, String titre, String description, int salaireMin, int salaireMax, String departement, int width, int height, String filePath) {
        try {
            // Construct the URL with parameters
            String url = BASE_URL + "?id=" + id +
                    "&titre=" + URLEncoder.encode(titre, StandardCharsets.UTF_8) +
                    "&description=" + URLEncoder.encode(description, StandardCharsets.UTF_8) +
                    "&salaireMin=" + salaireMin +
                    "&salaireMax=" + salaireMax +
                    "&departement=" + URLEncoder.encode(departement, StandardCharsets.UTF_8);

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, width, height);

            Path path = FileSystems.getDefault().getPath(filePath);
            File qrFile = new File(filePath);
            qrFile.getParentFile().mkdirs(); // Ensure directory exists

            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
            return true;
        } catch (WriterException | IOException e) {
            System.out.println("Erreur lors de la génération du QR Code : " + e.getMessage());
            return false;
        }
    }
}
