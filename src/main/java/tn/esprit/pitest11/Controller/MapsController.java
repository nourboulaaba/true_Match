package tn.esprit.pitest11.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import tn.esprit.pitest11.Entities.Entretien;

import java.net.URL;
import java.util.ResourceBundle;

public class MapsController implements Initializable {

    // The Entretien object is set via the setter method
    private Entretien entretien;
    private double longitude;
    private double latitude;

    @FXML
    private WebView webviewMaps;

    // No-argument constructor required for FXML
    public MapsController() { }

    // Setter to pass the Entretien and extract its coordinates
    public void setEntretien(Entretien entretien) {
        this.entretien = entretien;
        this.longitude = entretien.getLongitude();
        this.latitude = entretien.getLatitude();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        WebEngine webEngine = webviewMaps.getEngine();
        // Load the local HTML file from resources and append query parameters
        URL url = getClass().getResource("/tn/esprit/pitest11/maps.html");
        if (url != null) {
            String urlWithParams = url.toExternalForm() + "?lat=" + latitude + "&lng=" + longitude;
            webEngine.load(urlWithParams);
        } else {
            System.err.println("maps.html file not found!");
        }
    }
}
