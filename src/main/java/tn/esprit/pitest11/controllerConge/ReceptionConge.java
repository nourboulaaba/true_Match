package tn.esprit.pitest11.controllerConge;

import tn.esprit.pitest11.Entities.Conges;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import tn.esprit.pitest11.utils.CongeRequest;

import java.sql.SQLException;
import java.util.List;

public class ReceptionConge {
    @FXML
    private ListView<String> congesList;
    @FXML
    private Button accepterButton;
    @FXML
    private Button rejeterButton;

    private final CongeRequest congeRequest = new CongeRequest();

    @FXML
    public void initialize() {
        // Créez un usine de cellules personnalisées pour la ListView
        congesList.setCellFactory(TextFieldListCell.forListView());

        // Charger les congés en attente
        loadCongesEnAttente();

        // Désactivez les boutons par défaut
        accepterButton.setDisable(true);
        rejeterButton.setDisable(true);

        // Ajoutez un écouteur de sélection sur la ListView pour activer/désactiver les boutons
        congesList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            boolean itemSelected = newValue != null;
            accepterButton.setDisable(!itemSelected);
            rejeterButton.setDisable(!itemSelected);
        });
    }

    private void loadCongesEnAttente() {
        try {
            List<Conges> congesListData = congeRequest.getCongesEnAttente();
            ObservableList<String> observableList = FXCollections.observableArrayList();

            // Convertir chaque objet Conges en une chaîne formatée et l'ajouter à la liste observable
            for (Conges conge : congesListData) {
                String congeString = "ID: " + conge.getIdConge() + " | Employé: " + conge.getIdEmploye() + " | Type: " + conge.getTypeConge() + " | Statut: " + conge.getStatut();
                observableList.add(congeString);
            }

            congesList.setItems(observableList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void accepterConge() {
        String selectedItem = congesList.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            try {
                // Assuming the selectedItem has the format "ID: <id> | ..."
                String[] parts = selectedItem.split("\\|");  // Split by pipe symbol
                String[] idParts = parts[0].split(":");     // Get the ID part
                int congeId = Integer.parseInt(idParts[1].trim()); // Extract the ID

                congeRequest.updateStatutConge(congeId, "Accepté");
                loadCongesEnAttente();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Error: The selected item format is incorrect.");
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void rejeterConge() {
        String selectedItem = congesList.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            try {
                // Split by pipe symbol
                String[] parts = selectedItem.split("\\|");
                String[] idParts = parts[0].split(":");
                int congeId = Integer.parseInt(idParts[1].trim());

                congeRequest.updateStatutConge(congeId, "Rejeté");
                loadCongesEnAttente();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Error: The selected item format is incorrect.");
                e.printStackTrace();
            }
        }
    }

}
