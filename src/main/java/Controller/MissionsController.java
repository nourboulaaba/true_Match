package Controller;

import Entities.Mission;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.dao.DBConnection;
import service.missionService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class MissionsController implements Initializable {

    @FXML
    private TableView<Mission> missionsTable;

    private missionService missionService;

    @FXML
    private Button missionsButton;  // Le bouton "Missions"
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        missionService = new missionService();  // Initialisation manuelle si non injecté
        loadMissions();
    }

    public void handleMissionsButtonAction(javafx.event.ActionEvent actionEvent) {
        try {
            // Charger la vue Missions.fxml
            AnchorPane missionsView = FXMLLoader.load(getClass().getResource("/MissionsView.fxml"));

            // Créer une nouvelle scène avec la vue des missions
            Stage stage = (Stage) missionsButton.getScene().getWindow();
            Scene scene = new Scene(missionsView);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadMissions() {
        if (missionService != null) {
            List<Mission> missions = missionService.getAll();
            // Traitez et affichez les missions
        } else {
            System.err.println("MissionService is not initialized.");
        }
    }
}

