package tn.esprit.pitest11.controllerConge;

import tn.esprit.pitest11.Entities.Conges;
import tn.esprit.pitest11.utils.CongeRequest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class HistoriqueController {
    @FXML
    private TextField idEmployeField;
    @FXML
    private Button rechercherButton;
    @FXML
    private ListView<Conges> listView;
    @FXML
    private Label errorLabel;

    @FXML
    public void initialize() {
        // Personnalisation de l'affichage des congés dans la ListView
        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Conges conge, boolean empty) {
                super.updateItem(conge, empty);
                if (empty || conge == null) {
                    setText(null);
                } else {
                    setText(String.format("ID: %d | Type: %s | Début: %s | Fin: %s | Statut: %s",
                            conge.getIdConge(), conge.getTypeConge(), conge.getDateDebut(), conge.getDateFin(), conge.getStatut()));
                }
            }
        });
    }

    @FXML
    private void rechercherHistorique() {
        errorLabel.setText("");
        listView.getItems().clear(); // Efface la liste avant de charger les résultats
        try {
            int idEmploye = Integer.parseInt(idEmployeField.getText().trim());

            // Vérifier si l'employé existe dans la base de données
            CongeRequest congeRequest = new CongeRequest();
            if (!congeRequest.doesEmployeExist(idEmploye)) {
                errorLabel.setText("L'ID employé n'existe pas.");
                return;
            }

            // Si l'employé existe, récupérez ses congés
            List<Conges> conges = CongeRequest.getCongesByEmploye(idEmploye);

            if (conges.isEmpty()) {
                errorLabel.setText("Aucun congé trouvé pour cet ID employé.");
            } else {
                ObservableList<Conges> observableList = FXCollections.observableArrayList(conges);
                listView.setItems(observableList);
            }
        } catch (NumberFormatException e) {
            errorLabel.setText("Veuillez entrer un ID employé valide (nombre entier).");
        }
    }
}
