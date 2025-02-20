package controllerConge;
import entite.Conges;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import utils.CongeRequest;
import java.sql.SQLException;
import java.util.List;

public class ReceptionConge {
    @FXML
    private TableView<Conges> congesTable;
    @FXML
    private TableColumn<Conges, Integer> idCol;
    @FXML
    private TableColumn<Conges, Integer> employeCol;
    @FXML
    private TableColumn<Conges, String> typeCol;
    @FXML
    private TableColumn<Conges, String> statutCol;
    @FXML
    private Button accepterButton;
    @FXML
    private Button rejeterButton;

    private final CongeRequest congeRequest = new CongeRequest();

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("idConge"));
        employeCol.setCellValueFactory(new PropertyValueFactory<>("idEmploye"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("typeConge"));
        statutCol.setCellValueFactory(new PropertyValueFactory<>("statut"));
        loadCongesEnAttente();
    }

    private void loadCongesEnAttente() {
        try {
            List<Conges> congesList = congeRequest.getCongesEnAttente();
            ObservableList<Conges> observableList = FXCollections.observableArrayList(congesList);
            congesTable.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void accepterConge() {
        Conges selectedConge = congesTable.getSelectionModel().getSelectedItem();
        if (selectedConge != null) {
            try {
                congeRequest.updateStatutConge(selectedConge.getIdConge(), "Accepté");
                loadCongesEnAttente();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void rejeterConge() {
        Conges selectedConge = congesTable.getSelectionModel().getSelectedItem();
        if (selectedConge != null) {
            try {
                congeRequest.updateStatutConge(selectedConge.getIdConge(), "Rejeté");
                loadCongesEnAttente();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void afficherTousLesConges() {
        CongeRequest congeRequest = new CongeRequest();
        try {
            List<Conges> conges = congeRequest.getAllConges();
            ObservableList<Conges> observableConges = FXCollections.observableArrayList(conges);
            congesTable.setItems(observableConges);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
