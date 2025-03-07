package tn.esprit.pitest11.Controller;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tn.esprit.pitest11.Model.Offre;
import tn.esprit.pitest11.Services.OffreService;
import tn.esprit.pitest11.Utils.GeminiAPI;
import tn.esprit.pitest11.Utils.QRCodeGenerator;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

public class OffreCellController {
    public ImageView qrCodeImageView;
    public Button contrat;
    public Button mail;
    public Label scoreAI;
    private Offre offre;
    private OffreService offreService = new OffreService();
    private DashOffreController parentController;

    @FXML
    private Button delete;

    @FXML
    private Text descOffre;

    @FXML
    private Text salaireMax;

    @FXML
    private Text salaireMin;

    @FXML
    private Text titreOffre;

    @FXML
    private Button update;



    public void setOffre(Offre offre, DashOffreController parentController) {
        this.offre = offre;
        this.parentController = parentController;
        updateView();
        generateAndDisplayQRCode();
        fetchAndDisplayScore();


    }

    private void updateView() {
        titreOffre.setText(offre.getTitre());
        descOffre.setText(offre.getDescription());
        salaireMin.setText(String.valueOf(offre.getSalaireMin()));
        salaireMax.setText(String.valueOf(offre.getSalaireMax()));
    }

    @FXML
    void delete(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Suppression");
        alert.setHeaderText("Voulez-vous supprimer cette offre ?");
        alert.setContentText("Cette action est irréversible.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            offreService.delete(offre.getId());
            parentController.populateListView();
            parentController.populateCharts();;
        }
    }

    @FXML
    void update(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/pifinal/UpdateOffre.fxml"));
            AnchorPane pane = loader.load();

            UpdateOffreController controller = loader.getController();
            controller.setOffre(offre, parentController);

            Stage stage = new Stage();
            stage.setTitle("Modifier Offre");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(pane));
            stage.showAndWait();

            parentController.populateListView();
            parentController.populateCharts();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateAndDisplayQRCode() {
        if (offre != null) {
            try {
                String qrText = offre.getTitre() + "\n" + offre.getDescription();
                String qrFilePath = "Static/Images" + offre.getId() + "_QRCode.png"; // Save in a dedicated folder

                boolean success = QRCodeGenerator.generateJobQRCode(offre.getId(), offre.getTitre(),offre.getDescription(), offre.getSalaireMin(),offre.getSalaireMax(),offre.getDepartement().getNom(),150,150 , qrFilePath);
                if (success) {
                    File qrFile = new File(qrFilePath);
                    if (qrFile.exists()) {
                        qrCodeImageView.setImage(new Image(qrFile.toURI().toString()));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }


    public void contrat(ActionEvent actionEvent) {
        try {
            String filePath = "Contrat.pdf";
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            document.add(new Paragraph("Contrat d'Embauche").setBold().setFontSize(20));
            document.add(new Paragraph("Date: .........."));
            document.add(new Paragraph("Nom de l'employé: .........."));
            document.add(new Paragraph("Titre du Poste: " + (offre != null ? offre.getTitre() : "..........")));
            document.add(new Paragraph("Description du Poste: "+offre.getDescription()));
            document.add(new Paragraph("Salaire Minimum: "+offre.getSalaireMin()));
            document.add(new Paragraph("Salaire Maximum: "+offre.getSalaireMax()));
            document.add(new Paragraph("Département: "+offre.getDepartement().getNom()));

            document.add(new Paragraph("\nConditions générales:").setBold());
            document.add(new Paragraph("1. Période d'essai: 3 mois renouvelable.\n2. Congés annuels selon l'article 234 du code du travail.\n3. Respect du règlement intérieur et des normes de sécurité en vigueur."));

            document.add(new Paragraph("\nSignature de l'Employé: ..........................................."));
            document.add(new Paragraph("Signature de l'Employeur: ..........................................."));

            document.close();
            showAlertSuccess("PDF Généré", "Le contrat a été généré avec succès.");

            File pdfFile = new File(filePath);
            if (pdfFile.exists()) {
                String os = System.getProperty("os.name").toLowerCase();
                if (os.contains("win")) {
                    new ProcessBuilder("cmd", "/c", "start", filePath).start();
                } else if (os.contains("mac")) {
                    new ProcessBuilder("open", filePath).start();
                } else {
                    new ProcessBuilder("xdg-open", filePath).start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la génération du PDF.");
        }
    }

    @FXML
    public void fetchAndDisplayScore() {
        if (offre != null) {
            String offerDetails = offre.getTitre() + " " + offre.getDescription() + offre.getSalaireMin()+offre.getSalaireMax();
            try {
                String score = GeminiAPI.getOfferScore(offerDetails);
                scoreAI.setText("Score AI : " + score );
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible de récupérer le score AI.");
            }
        }
    }


    public void mail(ActionEvent actionEvent) {
        sendEmail("sfaxiabderahmene5@gmail.com");

    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showAlertSuccess(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void sendEmail(String recipient) {
        final String senderEmail = "sfaxiabderahmene5@gmail.com";
        final String senderPassword = "qlhl ylab ulqq uowv"; // Use App Password if using Gmail

        // Check if the PDF file exists before proceeding
        String filePath = "Contrat.pdf";
        File file = new File(filePath);

        if (!file.exists()) {
            showAlert("PDF Not Found", "Please generate the PDF report before sending the email.");
            return;
        }

        // SMTP Server Properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        // Create Session
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Create Email Message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject("Contrat");

            // Create the email body
            MimeBodyPart textPart = new MimeBodyPart();
            String emailContent = "Hello,\n\nPlease find the contract report .\n\nBest Regards,\n";
            textPart.setText(emailContent);

            // Attach the PDF file
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(file);

            // Create multipart email
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(attachmentPart);

            // Set the content of the message
            message.setContent(multipart);

            // Send Email
            Transport.send(message);
            System.out.println("Email with PDF sent successfully to " + recipient);

            // Show success alert
            showAlertSuccess("Success", "Email sent successfully to " + recipient);

        } catch (MessagingException | IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to send email. Please try again.");
        }
    }


}
