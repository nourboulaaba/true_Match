package controllerConge;
import entite.Reclamations;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import utils.ReclamationRequest;

public class ListeReclamationController {

    @FXML
    private TableView<Reclamations> tableReclamations;
    @FXML
    private TableColumn<Reclamations, Integer> colId;
    @FXML
    private TableColumn<Reclamations, String> colIdentifier;
    @FXML
    private TableColumn<Reclamations, String> colSujet;
    @FXML
    private TableColumn<Reclamations, String> colDescription;
    @FXML
    private TableColumn<Reclamations, String> colImagePath;
    @FXML
    private TableColumn<Reclamations, String> colDate;

    @FXML
    public void initialize() {
        // Set up table columns
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colIdentifier.setCellValueFactory(new PropertyValueFactory<>("identifier"));
        colSujet.setCellValueFactory(new PropertyValueFactory<>("sujet"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colImagePath.setCellValueFactory(new PropertyValueFactory<>("imagePath"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateReclamation"));

        loadReclamations();
    }

    private void loadReclamations() {
        ObservableList<Reclamations> list = ReclamationRequest.getAllReclamations();
        tableReclamations.setItems(list);
    }
}
