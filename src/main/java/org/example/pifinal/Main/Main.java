package org.example.pifinal.Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.pifinal.Controller.DashDepartementController;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/pifinal/DashDepartement.fxml"));
            AnchorPane root = loader.load();

            DashDepartementController controller = loader.getController();
            controller.populateListView(); // Ensuring that the list is populated at start

            primaryStage.setTitle("Gestion des Départements");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
