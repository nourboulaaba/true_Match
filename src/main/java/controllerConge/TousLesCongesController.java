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

public class TousLesCongesController {

    @FXML
    private TableView<Conges> congesTable;
    @FXML
    private TableColumn<Conges, Integer> idCol;
    @FXML
    private TableColumn<Conges, Integer> employeCol;
    @FXML
    private TableColumn<Conges, String> dateDebutCol;
    @FXML
    private TableColumn<Conges, String> dateFinCol;
    @FXML
    private TableColumn<Conges, String> typeCol;
    @FXML
    private TableColumn<Conges, String> statutCol;
    @FXML
    private Button refreshButton;

    private final CongeRequest congeRequest = new CongeRequest();

    @FXML
    public void initialize() {
        setupTable();
        afficherTousLesConges();
    }

    private void setupTable() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("idConge"));
        employeCol.setCellValueFactory(new PropertyValueFactory<>("idEmploye"));
        dateDebutCol.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        dateFinCol.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("typeConge"));
        statutCol.setCellValueFactory(new PropertyValueFactory<>("statut"));
    }

    @FXML
    public void afficherTousLesConges() {
        try {
            List<Conges> conges = congeRequest.getAllConges();
            ObservableList<Conges> observableConges = FXCollections.observableArrayList(conges);
            congesTable.setItems(observableConges);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
