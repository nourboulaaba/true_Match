package controllerConge;

import entite.Conges;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import utils.CongeRequest;

import java.sql.SQLException;
import java.util.List;

public class TousLesCongesController {

    @FXML
    private ListView<Conges> congesListView;
    @FXML
    private Button refreshButton;

    private final CongeRequest congeRequest = new CongeRequest();

    @FXML
    public void initialize() {
        afficherTousLesConges();
        setupListView();
    }

    private void setupListView() {
        // Custom CellFactory to format the ListView items
        congesListView.setCellFactory(new Callback<ListView<Conges>, ListCell<Conges>>() {
            @Override
            public ListCell<Conges> call(ListView<Conges> param) {
                return new ListCell<Conges>() {
                    @Override
                    protected void updateItem(Conges item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null && !empty) {
                            setText(String.format("ID: %d | Employé: %d | Type: %s | Du: %s au: %s | Statut: %s",
                                    item.getIdConge(), item.getIdEmploye(), item.getTypeConge(),
                                    item.getDateDebut(), item.getDateFin(), item.getStatut()));
                        } else {
                            setText(null);
                        }
                    }
                };
            }
        });
    }

    @FXML
    public void afficherTousLesConges() {
        try {
            List<Conges> conges = congeRequest.getAllConges();
            ObservableList<Conges> observableConges = FXCollections.observableArrayList(conges);
            congesListView.setItems(observableConges);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
