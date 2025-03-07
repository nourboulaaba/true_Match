package tn.esprit.pitest11.Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tn.esprit.pitest11.Controller.DashAdminRecrutementController;
import tn.esprit.pitest11.Controller.DashUserController;
import tn.esprit.pitest11.Model.*;
import tn.esprit.pitest11.Services.ConvoqueService;
import tn.esprit.pitest11.Services.EntretienService;
import tn.esprit.pitest11.Services.OffreService;
import tn.esprit.pitest11.Utils.DataSource;

import java.time.LocalDate;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            OffreService os = new OffreService();
            System.out.println(os.readById(1));
            System.out.println(os.readAll());
            //FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/pitest11/DashUser.fxml"));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/pitest11/DashAdminRecrutement.fxml"));
            AnchorPane root = loader.load();

            DashAdminRecrutementController controller = loader.getController();
            //DashUserController controller = loader.getController();

            primaryStage.setTitle("Admin Recrutement Dashboard");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}