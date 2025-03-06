package Controller;

import entite.Certificat;
import service.CertificatService;
import service.FormationService;
import service.CertificatPDF;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CertificatController {
    @FXML
    private ListView<Certificat> listCertificats; // ListView pour afficher les certificats

    @FXML
    private ComboBox<String> cmbNomFormation;

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


    @FXML private Label lblResultat;
    @FXML private Button btnTelecharger;
    @FXML private Button btnReprogrammer;

    private final CertificatService certificatService = new CertificatService();
    private final FormationService formationService = new FormationService();
    private Certificat certificatSelectionne;

    private final Map<String, Integer> formationMap = new HashMap<>(); // Associe un nom à un ID

    @FXML
    public void initialize() {
        cmbHeure.setItems(FXCollections.observableArrayList("08:00", "10:00", "13:00", "15:00", "18:00"));
        cmbNiveau.setItems(FXCollections.observableArrayList("Débutant", "Intermédiaire", "Avancé"));
        spnDuree.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(30, 240, 60, 30));

        // Charger les formations et stocker leurs noms avec leurs IDs
        List<String> formationNames = formationService.getAllFormationNames(); // Récupère les noms
        for (String nom : formationNames) {
            int idFormation = formationService.getFormationIdByName(nom); // Trouve l'ID
            formationMap.put(nom, idFormation);
        }

        // Ajouter les noms des formations dans le ComboBox
        cmbNomFormation.setItems(FXCollections.observableArrayList(formationMap.keySet()));

        // Gérer le changement de sélection
        cmbNomFormation.setOnAction(event -> chargerCertificatSelectionne());

        // Charger la liste des certificats au démarrage
        chargerListeCertificats();
        listCertificats.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                certificatSelectionne = newValue;
                // Affiche les données du certificat sélectionné dans les champs
                afficherCertificatSelectionne();
            }
        });
    }

    @FXML
    private void ajouterCertificat() {
        try {
            if (!validerChamps()) return;

            String nomFormation = cmbNomFormation.getValue();
            int idFormation = formationMap.get(nomFormation);

            LocalDate date = dateExamen.getValue();
            String heure = cmbHeure.getValue();
            int duree = spnDuree.getValue();
            String prixExam = txtPrixExam.getText();
            String niveau = cmbNiveau.getValue();

            Certificat certificat = new Certificat(idFormation, date.toString(), heure, duree, prixExam, niveau);
            certificatService.insert(certificat);

            afficherMessage("Succès", "Certificat ajouté avec succès !");
            viderChamps();

            // Mettre à jour la liste des certificats
            chargerListeCertificats();
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

        String nomFormation = cmbNomFormation.getValue();
        int idFormation = formationMap.get(nomFormation);

        certificatSelectionne.setIdFormation(idFormation);
        certificatSelectionne.setDateExamen(dateExamen.getValue().toString());
        certificatSelectionne.setHeure(cmbHeure.getValue());
        certificatSelectionne.setDuree(spnDuree.getValue());
        certificatSelectionne.setPrixExam(txtPrixExam.getText());
        certificatSelectionne.setNiveau(cmbNiveau.getValue());

        certificatService.update(certificatSelectionne);
        afficherMessage("Succès", "Certificat modifié avec succès !");
        viderChamps();

        // Mettre à jour la liste des certificats
        chargerListeCertificats();
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

        // Mettre à jour la liste des certificats
        chargerListeCertificats();
    }

    private void chargerListeCertificats() {
        // Récupérer la liste des certificats depuis le service
        List<Certificat> certificats = certificatService.getAllCertificats();

        // Convertir la liste des certificats en une ObservableList de Certificat
        ObservableList<Certificat> certificatList = FXCollections.observableArrayList(certificats);

        // Mettre à jour la ListView avec les objets Certificat
        listCertificats.setItems(certificatList);

        // Afficher un String personnalisé pour chaque Certificat dans la ListView
        listCertificats.setCellFactory(param -> new ListCell<Certificat>() {
            @Override
            protected void updateItem(Certificat item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText("Formation ID: " + item.getIdFormation() +
                            ", Date Examen: " + item.getDateExamen() +
                            ", Heure: " + item.getHeure() +
                            ", Niveau: " + item.getNiveau());
                } else {
                    setText(null);
                }
            }
        });
    }


    private boolean validerChamps() {
        if (cmbNomFormation.getValue() == null || dateExamen.getValue() == null ||
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
        cmbNomFormation.getSelectionModel().clearSelection();
        dateExamen.setValue(null);
        cmbHeure.getSelectionModel().clearSelection();
        spnDuree.getValueFactory().setValue(60);
        txtPrixExam.clear();
        cmbNiveau.getSelectionModel().clearSelection();
        certificatSelectionne = null;
    }
    private void afficherCertificatSelectionne() {
        if (certificatSelectionne != null) {
            dateExamen.setValue(LocalDate.parse(certificatSelectionne.getDateExamen()));
            cmbHeure.setValue(certificatSelectionne.getHeure());
            spnDuree.getValueFactory().setValue(certificatSelectionne.getDuree());
            txtPrixExam.setText(certificatSelectionne.getPrixExam());
            cmbNiveau.setValue(certificatSelectionne.getNiveau());
        }
    }
    private void chargerCertificatSelectionne() {
        if (cmbNomFormation.getValue() != null) {
            int idFormation = formationMap.get(cmbNomFormation.getValue());
            // Chercher le certificat lié à la formation sélectionnée
            CertificatService certificatService = new CertificatService();
            List<Certificat> certificats = certificatService.getCertificatByFormation(1); // Pass the formation ID


            if (certificats != null && !certificats.isEmpty()) {
                certificatSelectionne = certificats.get(0);  // Sélectionne le premier Certificat de la liste
                afficherCertificatSelectionne();
            } else {
                // Si aucun certificat n'est trouvé, vider les champs ou afficher un message
                viderChamps();
            }

        }
    }

    // Action lorsque les résultats sont publiés
    public void afficherResultatExamen(int idCertif, String resultat) {
        if ("réussi".equals(resultat)) {
            lblResultat.setText("🎉 Félicitations ! Vous avez réussi l'examen.");
            btnTelecharger.setVisible(true);  // Affiche le bouton de téléchargement
        } else {
            lblResultat.setText("❌ Vous n'avez pas réussi l'examen.");
            btnReprogrammer.setVisible(true); // Affiche le bouton de reprogrammation
        }
        certificatService.updateResultatExamen(idCertif, resultat);
    }

    // Action pour télécharger le certificat
    @FXML
    public void telechargerCertificat() {
        if (certificatSelectionne == null) {
            afficherMessage("Erreur", "Aucun certificat sélectionné pour le téléchargement.");
            return;
        }

        try {
            // Création du fichier PDF
            CertificatPDF certificatPDF = new CertificatPDF();
            String fileName = "C:/Users/rimaj/Downloads/Certificat_" + certificatSelectionne.getIdFormation() + ".pdf";
            certificatPDF.generateCertificat(certificatSelectionne, fileName);

            // Affichage du message
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Téléchargement");
            alert.setHeaderText(null);
            alert.setContentText("Votre certificat est maintenant prêt à être téléchargé.");
            alert.showAndWait();

        } catch (Exception e) {
            afficherMessage("Erreur", "Erreur lors de la génération du certificat PDF : " + e.getMessage());
        }
    }

    // Action pour reprogrammer l'examen
    @FXML
    public void reprogrammerExamen() {
        // Vous pouvez afficher un calendrier pour sélectionner une nouvelle date
        // Et mettre à jour la date de reprogrammation dans la base de données
        String nouvelleDate = "2025-04-10"; // Exemple, à remplacer par la date choisie par l'utilisateur
        certificatService.updateDateReprogrammation(1, nouvelleDate); // Par exemple, reprogrammer pour le certificat avec ID 1
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Reprogrammation");
        alert.setHeaderText(null);
        alert.setContentText("Votre examen a été reprogrammé.");
        alert.showAndWait();
    }
}
