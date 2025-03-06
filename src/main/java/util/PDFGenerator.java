package util;

import Entities.Contrat;
import Entities.Mission;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class PDFGenerator {
    
    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.DARK_GRAY);
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
    private static final Font CELL_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
    private static final Font FOOTER_FONT = new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC, BaseColor.GRAY);
    
    /**
     * Génère un PDF contenant la liste des contrats
     * @param contrats Liste des contrats à inclure dans le PDF
     * @param stage Stage parent pour afficher le sélecteur de dossier
     * @return Chemin du fichier PDF généré ou null en cas d'erreur
     */
    public static String generateContratsPDF(List<Contrat> contrats, Stage stage) {
        try {
            // Demander à l'utilisateur où sauvegarder le fichier
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Choisir l'emplacement pour enregistrer le PDF");
            File selectedDirectory = directoryChooser.showDialog(stage);
            
            if (selectedDirectory == null) {
                return null; // L'utilisateur a annulé
            }
            
            // Créer le nom du fichier avec la date actuelle
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String fileName = "Liste_Contrats_" + dateFormat.format(new Date()) + ".pdf";
            String filePath = selectedDirectory.getAbsolutePath() + File.separator + fileName;
            
            // Créer le document PDF
            Document document = new Document(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
            
            // Ajouter des métadonnées
            document.addTitle("Liste des Contrats");
            document.addAuthor("True Match");
            document.addCreator("True Match Application");
            
            // Ajouter un en-tête et un pied de page
            HeaderFooter event = new HeaderFooter("Liste des Contrats");
            writer.setPageEvent(event);
            
            document.open();
            
            // Ajouter le titre
            Paragraph title = new Paragraph("Liste des Contrats", TITLE_FONT);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);
            
            // Ajouter la date de génération
            SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Paragraph dateGeneration = new Paragraph("Généré le : " + displayDateFormat.format(new Date()), CELL_FONT);
            dateGeneration.setAlignment(Element.ALIGN_RIGHT);
            dateGeneration.setSpacingAfter(20);
            document.add(dateGeneration);
            
            // Créer le tableau
            PdfPTable table = new PdfPTable(6); // 6 colonnes
            table.setWidthPercentage(100);
            
            // Définir les largeurs relatives des colonnes
            float[] columnWidths = {1f, 1.5f, 2f, 2f, 2f, 1.5f};
            table.setWidths(columnWidths);
            
            // Ajouter les en-têtes de colonnes
            addTableHeader(table, new String[]{"ID", "ID Employé", "Type", "Date Début", "Date Fin", "Salaire (DT)"});
            
            // Ajouter les données
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            for (Contrat contrat : contrats) {
                table.addCell(createCell(String.valueOf(contrat.getIdContrat()), CELL_FONT, Element.ALIGN_CENTER));
                table.addCell(createCell(String.valueOf(contrat.getIdEmploye()), CELL_FONT, Element.ALIGN_CENTER));
                table.addCell(createCell(contrat.getType(), CELL_FONT, Element.ALIGN_LEFT));
                table.addCell(createCell(df.format(contrat.getDateDebut()), CELL_FONT, Element.ALIGN_CENTER));
                table.addCell(createCell(df.format(contrat.getDateFin()), CELL_FONT, Element.ALIGN_CENTER));
                table.addCell(createCell(String.format("%.2f", contrat.getSalaire()), CELL_FONT, Element.ALIGN_RIGHT));
            }
            
            document.add(table);
            
            // Ajouter un résumé
            Paragraph summary = new Paragraph("Nombre total de contrats : " + contrats.size(), CELL_FONT);
            summary.setSpacingBefore(20);
            document.add(summary);
            
            document.close();
            
            return filePath;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Génère un PDF contenant la liste des missions
     * @param missions Liste des missions à inclure dans le PDF
     * @param stage Stage parent pour afficher le sélecteur de dossier
     * @return Chemin du fichier PDF généré ou null en cas d'erreur
     */
    public static String generateMissionsPDF(List<Mission> missions, Stage stage) {
        try {
            // Demander à l'utilisateur où sauvegarder le fichier
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Choisir l'emplacement pour enregistrer le PDF");
            File selectedDirectory = directoryChooser.showDialog(stage);
            
            if (selectedDirectory == null) {
                return null; // L'utilisateur a annulé
            }
            
            // Créer le nom du fichier avec la date actuelle
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String fileName = "Liste_Missions_" + dateFormat.format(new Date()) + ".pdf";
            String filePath = selectedDirectory.getAbsolutePath() + File.separator + fileName;
            
            // Créer le document PDF
            Document document = new Document(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
            
            // Ajouter des métadonnées
            document.addTitle("Liste des Missions");
            document.addAuthor("True Match");
            document.addCreator("True Match Application");
            
            // Ajouter un en-tête et un pied de page
            HeaderFooter event = new HeaderFooter("Liste des Missions");
            writer.setPageEvent(event);
            
            document.open();
            
            // Ajouter le titre
            Paragraph title = new Paragraph("Liste des Missions", TITLE_FONT);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);
            
            // Ajouter la date de génération
            SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Paragraph dateGeneration = new Paragraph("Généré le : " + displayDateFormat.format(new Date()), CELL_FONT);
            dateGeneration.setAlignment(Element.ALIGN_RIGHT);
            dateGeneration.setSpacingAfter(20);
            document.add(dateGeneration);
            
            // Créer le tableau
            PdfPTable table = new PdfPTable(5); // 5 colonnes
            table.setWidthPercentage(100);
            
            // Définir les largeurs relatives des colonnes
            float[] columnWidths = {1f, 3f, 2f, 3f, 1.5f};
            table.setWidths(columnWidths);
            
            // Ajouter les en-têtes de colonnes
            addTableHeader(table, new String[]{"ID", "Titre", "Date", "Destination", "ID Employé"});
            
            // Ajouter les données
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            for (Mission mission : missions) {
                table.addCell(createCell(String.valueOf(mission.getIdMission()), CELL_FONT, Element.ALIGN_CENTER));
                table.addCell(createCell(mission.getTitre(), CELL_FONT, Element.ALIGN_LEFT));
                table.addCell(createCell(df.format(mission.getDate()), CELL_FONT, Element.ALIGN_CENTER));
                table.addCell(createCell(mission.getDestination(), CELL_FONT, Element.ALIGN_LEFT));
                table.addCell(createCell(String.valueOf(mission.getIdEmploye()), CELL_FONT, Element.ALIGN_CENTER));
            }
            
            document.add(table);
            
            // Ajouter un résumé
            Paragraph summary = new Paragraph("Nombre total de missions : " + missions.size(), CELL_FONT);
            summary.setSpacingBefore(20);
            document.add(summary);
            
            document.close();
            
            return filePath;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Ajoute les en-têtes au tableau
     * @param table Le tableau PDF
     * @param headers Les en-têtes à ajouter
     */
    private static void addTableHeader(PdfPTable table, String[] headers) {
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, HEADER_FONT));
            cell.setBackgroundColor(new BaseColor(41, 128, 185)); // Bleu
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(5);
            table.addCell(cell);
        }
    }
    
    /**
     * Crée une cellule de tableau avec le texte et le style spécifiés
     * @param text Le texte à afficher
     * @param font La police à utiliser
     * @param alignment L'alignement du texte
     * @return La cellule créée
     */
    private static PdfPCell createCell(String text, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(alignment);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5);
        return cell;
    }
    
    /**
     * Classe interne pour gérer les en-têtes et pieds de page
     */
    static class HeaderFooter extends PdfPageEventHelper {
        private String title;
        
        public HeaderFooter(String title) {
            this.title = title;
        }
        
        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();
            
            // En-tête
            ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
                    new Phrase("True Match", HEADER_FONT),
                    document.leftMargin(), document.top() + 10, 0);
            
            ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT,
                    new Phrase(title, HEADER_FONT),
                    document.right(), document.top() + 10, 0);
            
            // Pied de page
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                    new Phrase("Page " + writer.getPageNumber(), FOOTER_FONT),
                    (document.right() - document.left()) / 2 + document.leftMargin(),
                    document.bottom() - 10, 0);
            
            // Ligne de séparation en haut
            cb.setLineWidth(1);
            cb.moveTo(document.leftMargin(), document.top());
            cb.lineTo(document.right(), document.top());
            cb.stroke();
            
            // Ligne de séparation en bas
            cb.moveTo(document.leftMargin(), document.bottom() - 20);
            cb.lineTo(document.right(), document.bottom() - 20);
            cb.stroke();
        }
    }
}
