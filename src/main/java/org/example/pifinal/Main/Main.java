package org.example.pifinal.Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.pifinal.Controller.DashDepartementController;
import org.example.pifinal.Utils.QRCodeGenerator;

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

    /*public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/org/example/pifinal/freelancers.fxml"));
        stage.setTitle("Freelancer Viewer");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }*/
    /*public static void main(String[] args) {
        QRCodeGenerator.generateQRCode("Hello, Abdurrahman! 🚀", "qrcode.png", 300, 300);
    }*/


}
