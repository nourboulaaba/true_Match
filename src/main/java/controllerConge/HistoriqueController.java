package controllerConge;
import entite.Conges;
import utils.CongeRequest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;

public class HistoriqueController {
    @FXML
    private TextField idEmployeField;
    @FXML
    private Button rechercherButton;
    @FXML
    private TableView<Conges> tableView;
    @FXML
    private TableColumn<Conges, Integer> colId;
    @FXML
    private TableColumn<Conges, String> colType;
    @FXML
    private TableColumn<Conges, String> colDebut;
    @FXML
    private TableColumn<Conges, String> colFin;
    @FXML
    private TableColumn<Conges, String> colStatut;
    @FXML
    private Label errorLabel;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idConge"));
        colType.setCellValueFactory(new PropertyValueFactory<>("typeConge"));
        colDebut.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        colFin.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
    }

    @FXML
    private void rechercherHistorique() {
        errorLabel.setText("");
        try {
            int idEmploye = Integer.parseInt(idEmployeField.getText());
            List<Conges> conges = CongeRequest.getCongesByEmploye(idEmploye);
            if (conges.isEmpty()) {
                errorLabel.setText("Aucun congé trouvé pour cet ID employé.");
            } else {
                ObservableList<Conges> observableList = FXCollections.observableArrayList(conges);
                tableView.setItems(observableList);
            }
        } catch (NumberFormatException e) {
            errorLabel.setText("Veuillez entrer un ID employé valide (entier).");
        }
    }
}
