package tn.esprit.pitest11.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import tn.esprit.pitest11.Entities.Certificat;
import java.io.FileOutputStream;
import java.io.IOException;

public class CertificatPDF {

    public void generateCertificat(Certificat certificat, String fileName) throws DocumentException, IOException {
        Document document = new Document();

        // Créer le fichier PDF
        PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();

        // Ajouter un titre
        document.add(new Paragraph("Certificat d'Examen"));
        document.add(new Paragraph("\n"));

        // Ajouter les informations du certificat
        document.add(new Paragraph("Formation ID: " + certificat.getIdFormation()));
        document.add(new Paragraph("Date de l'examen: " + certificat.getDateExamen()));
        document.add(new Paragraph("Heure: " + certificat.getHeure()));
        document.add(new Paragraph("Durée: " + certificat.getDuree() + " minutes"));
        document.add(new Paragraph("Prix de l'examen: " + certificat.getPrixExam()));
        document.add(new Paragraph("Niveau: " + certificat.getNiveau()));

        // Clôturer le document
        document.close();
    }
}
