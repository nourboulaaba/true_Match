package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;



public class mainFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage)throws IOException {

<<<<<<< Updated upstream
        FXMLLoader fxmlLoader = new FXMLLoader(mainFX.class.getResource("/LOGIN.fxml"));
=======
        FXMLLoader fxmlLoader = new FXMLLoader(mainFX.class.getResource("/Dashboard.fxml"));
>>>>>>> Stashed changes
        Scene scene = new Scene(fxmlLoader.load(), 980, 700);
        stage.setTitle("Login!");
        stage.setScene(scene);
        stage.show();


    }
}
