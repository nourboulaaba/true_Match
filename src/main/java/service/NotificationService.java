package service;
import entite.Certificat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class NotificationService {

    private CertificatService certificatService;

    public NotificationService() {
        this.certificatService = new CertificatService();  // Exemple d'initialisation
    }

    public void startNotificationTimer() {
        // Timer vérifiant tous les jours
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkForUpcomingExams();
            }
        }, 0, 1000 * 60 * 60 * 24); // Vérifie tous les jours
    }

    private void checkForUpcomingExams() {
        List<Certificat> certificats = certificatService.getAllCertificats();
        LocalDate today = LocalDate.now();

        for (Certificat certificat : certificats) {
            // Conversion de la dateExamen de String à LocalDate
            LocalDate examDate = convertStringToDate(certificat.getDateExamen());

            if (examDate != null) {
                long daysUntilExam = ChronoUnit.DAYS.between(today, examDate);

                // Si l'examen est prévu dans 2 jours
                if (daysUntilExam == 2) {
                    Platform.runLater(() -> {
                        showNotification(certificat);
                    });
                }
            }
        }
    }

    // Fonction de conversion de String en LocalDate
    private LocalDate convertStringToDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");  // Format attendu pour la date
        try {
            return LocalDate.parse(dateStr, formatter);  // Conversion de String à LocalDate
        } catch (Exception e) {
            e.printStackTrace();
            return null;  // Retourne null en cas d'erreur de format
        }
    }

    private void showNotification(Certificat certificat) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Rappel d'Examen");
        alert.setHeaderText("Examen à venir !");
        alert.setContentText("Votre examen pour le certificat " + certificat.getNiveau() +
                " est prévu pour le " + certificat.getDateExamen() + ". Ne l'oubliez pas !");
        alert.showAndWait();
    }
}
