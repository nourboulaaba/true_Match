package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.PreferenceManager;

import java.io.IOException;



public class mainFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage)throws IOException {
try {
    FXMLLoader loader;
    if (PreferenceManager.getBoolean("isLoggedI", false)) {
        System.out.println("((((((((((((((((((((((((((");
        loader = new FXMLLoader(mainFX.class.getResource("/Dashboard.fxml"));
        stage.setTitle("Dashboard - " + PreferenceManager.getString("role", "non role").toUpperCase());
    } else {
        System.out.println("--------------------------------");

        loader = new FXMLLoader(mainFX.class.getResource("/Login.fxml"));
        stage.setTitle("Login!");
    }

    Scene scene = new Scene(loader.load());
    stage.setScene(scene);
    stage.show();
}catch(Exception e) {
    e.printStackTrace();
}

    }
}
