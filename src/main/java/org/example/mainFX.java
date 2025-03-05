package org.example;

import Controller.gestAuth.GoogleAuthController;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import service.UserSession;
import utils.PreferenceManager;

import java.io.IOException;


public class mainFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {

        try {

            FXMLLoader loader;
            if (PreferenceManager.getBoolean("isLoggedIn", false)) {
                if (PreferenceManager.getString("role", "RH").equals("RH")) {
                    System.out.println(UserSession.getConnectedUser());
                    loader = new FXMLLoader(mainFX.class.getResource("/Dashboard.fxml"));
                    stage.setTitle("Dashboard - " + PreferenceManager.getString("role", "non role").toUpperCase());
                } else {
                    loader = new FXMLLoader(mainFX.class.getResource("/DashEmployee.fxml"));
                    stage.setTitle("Dashboard - " + PreferenceManager.getString("role", "non role").toUpperCase());
                }
            } else {
                System.out.println("--------------------------------");

                loader = new FXMLLoader(mainFX.class.getResource("/Auth/Login.fxml"));
                stage.setTitle("Login!");
            }

            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
