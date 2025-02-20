package Controller;

import entite.Certificat;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import service.CertificatService;

import java.time.LocalDate;

public class CertificatController {

    @FXML
    private ComboBox<Integer> cmbIdFormation;
    @FXML
    private DatePicker dateExamen;
    @FXML
    private ComboBox<String> cmbHeure;
    @FXML
    private Spinner<Integer> spnDuree;
    @FXML
    private TextField txtPrixExam;
    @FXML
    private ComboBox<String> cmbNiveau;
    @FXML
    private Button btnAjouter, btnModifier, btnSupprimer;

    private final CertificatService certificatService = new CertificatService();
    private Certificat certificatSelectionne; // Stocke le certificat sélectionné pour modification/suppression

    @FXML
    public void initialize() {
        cmbHeure.setItems(FXCollections.observableArrayList("08:00", "10:00", "13:00", "15:00", "18:00"));
        cmbNiveau.setItems(FXCollections.observableArrayList("Débutant", "Intermédiaire", "Avancé"));
        spnDuree.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(30, 240, 60, 30));

        ObservableList<Integer> formations = FXCollections.observableArrayList(certificatService.getAllFormationIds());
        cmbIdFormation.setItems(formations);

        // Permet de charger un certificat sélectionné si une méthode de sélection est ajoutée
        cmbIdFormation.setOnAction(event -> chargerCertificatSelectionne());
    }

    @FXML
    private void ajouterCertificat() {
        try {
            if (!validerChamps()) return;

            int idFormation = cmbIdFormation.getValue();
            LocalDate date = dateExamen.getValue();
            String heure = cmbHeure.getValue();
            int duree = spnDuree.getValue();
            String prixExam = txtPrixExam.getText();
            String niveau = cmbNiveau.getValue();

            Certificat certificat = new Certificat(idFormation, date.toString(), heure, duree, prixExam, niveau);
            certificatService.insert(certificat);

            afficherMessage("Succès", "Certificat ajouté avec succès !");
            viderChamps();
        } catch (Exception e) {
            afficherMessage("Erreur", "Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    @FXML
    private void modifierCertificat() {
        if (certificatSelectionne == null) {
            afficherMessage("Erreur", "Sélectionnez d'abord un certificat à modifier !");
            return;
        }

        if (!validerChamps()) return;

        certificatSelectionne.setIdFormation(cmbIdFormation.getValue());
        certificatSelectionne.setDateExamen(dateExamen.getValue().toString());
        certificatSelectionne.setHeure(cmbHeure.getValue());
        certificatSelectionne.setDuree(spnDuree.getValue());
        certificatSelectionne.setPrixExam(txtPrixExam.getText());
        certificatSelectionne.setNiveau(cmbNiveau.getValue());

        certificatService.update(certificatSelectionne);
        afficherMessage("Succès", "Certificat modifié avec succès !");
        viderChamps();
    }

    @FXML
    private void supprimerCertificat() {
        if (certificatSelectionne == null) {
            afficherMessage("Erreur", "Sélectionnez d'abord un certificat à supprimer !");
            return;
        }

        certificatService.delete(certificatSelectionne);
        afficherMessage("Succès", "Certificat supprimé avec succès !");
        viderChamps();
    }

    private void chargerCertificatSelectionne() {
        int idFormation = cmbIdFormation.getValue();
        if (idFormation == 0) return;

        certificatSelectionne = certificatService.getById(idFormation);
        if (certificatSelectionne != null) {
            dateExamen.setValue(LocalDate.parse(certificatSelectionne.getDateExamen()));
            cmbHeure.setValue(certificatSelectionne.getHeure());
            spnDuree.getValueFactory().setValue(certificatSelectionne.getDuree());
            txtPrixExam.setText(certificatSelectionne.getPrixExam());
            cmbNiveau.setValue(certificatSelectionne.getNiveau());
        }
    }

    private boolean validerChamps() {
        if (cmbIdFormation.getValue() == null || dateExamen.getValue() == null ||
                cmbHeure.getValue() == null || txtPrixExam.getText().isEmpty() || cmbNiveau.getValue() == null) {
            afficherMessage("Erreur", "Veuillez remplir tous les champs !");
            return false;
        }
        return true;
    }

    private void afficherMessage(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void viderChamps() {
        cmbIdFormation.getSelectionModel().clearSelection();
        dateExamen.setValue(null);
        cmbHeure.getSelectionModel().clearSelection();
        spnDuree.getValueFactory().setValue(60);
        txtPrixExam.clear();
        cmbNiveau.getSelectionModel().clearSelection();
        certificatSelectionne = null;
    }
}
