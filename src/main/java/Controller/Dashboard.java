package Controller;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class Dashboard {

    @FXML
    private Label label;

    @FXML
    private VBox pnl_scroll;

    @FXML
    private Button missionsButton;

    @FXML
    private Button contratsButton;

    @FXML
    private void handleButtonAction(MouseEvent event) {
        refreshNodes();
    }

   // @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        refreshNodes();
    }

    private void refreshNodes()
    {
        pnl_scroll.getChildren().clear();

        Node [] nodes = new  Node[15];

        for(int i = 0; i<10; i++)
        {
            try {
                nodes[i] = (Node)FXMLLoader.load(getClass().getResource("Item.fxml"));
                pnl_scroll.getChildren().add(nodes[i]);

            } catch (IOException ex) {
                Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    @FXML
    public void handleMissionsButtonAction(javafx.event.ActionEvent actionEvent) {
        try {
            // Charger la vue GestionMissions.fxml
            javafx.scene.Parent missionsView = FXMLLoader.load(getClass().getResource("/GestionMissions.fxml"));

            // Créer une nouvelle scène avec la vue des missions
            Stage stage = (Stage) missionsButton.getScene().getWindow();
            Scene scene = new Scene(missionsView);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleContratsButtonAction(javafx.event.ActionEvent actionEvent) {
        try {
            // Charger la vue GestionContrats.fxml
            javafx.scene.Parent contratsView = FXMLLoader.load(getClass().getResource("/GestionContrats.fxml"));

            // Créer une nouvelle scène avec la vue des contrats
            Stage stage = (Stage) contratsButton.getScene().getWindow();
            Scene scene = new Scene(contratsView);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
