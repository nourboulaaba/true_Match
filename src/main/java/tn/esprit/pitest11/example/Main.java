package tn.esprit.pitest11.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane; // Use AnchorPane if that's the type of root node in your FXML
import javafx.stage.Stage;
import tn.esprit.pitest11.service.contratService;
import tn.esprit.pitest11.service.missionService;

public class Main extends Application {

    public static void main(String[] args) {
        // Launch the JavaFX Application
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load your FXML file here
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dashboard.fxml"));
        // If the root element in FXML is AnchorPane, cast it to AnchorPane
        AnchorPane root = loader.load();  // Load the FXML and cast to AnchorPane
        Scene scene = new Scene(root);

        // Set the scene to the primary stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("True Match");
        primaryStage.show();

        // Call your existing business logic if needed
        contratService contratService = new contratService();

        // Example business logic:
      //  Contrat contrat1 = new Contrat(1, 200, "CDI", new Date(), new Date(), 6000.0);
       // contratService.insert(contrat1);

        // Test CRUD for Mission
        missionService missionService = new missionService();
        // Example mission addition:
      //  Mission mission1 = new Mission("Mission Yasmine", new Date(), "Moknine", 2);
      //  missionService.insert(mission1);
/*formation2
import tn.esprit.pitest11.Entities.formations;
import tn.esprit.pitest11.Entities.Certificat;
import tn.esprit.pitest11.service.FormationService;
import tn.esprit.pitest11.service.CertificatService;

public class Main {
    public static void main(String[] args) {
        FormationService formationService = new FormationService();
        CertificatService certificatService = new CertificatService();

        // --------- Gestion des Formations ---------
        System.out.println("===== Gestion des Formations =====");

        formations f1 = new formations(1, "Java Basics", "Introduction à Java", "100€");
        formations f2 = new formations(2, "Spring Boot", "Développement Web avec Spring Boot", "200€");

        formationService.insert(f1);
        formationService.insert(f2);

        System.out.println("Toutes les formations en base : " + formationService.getAll());

        formations f1Updated = new formations(1, "Java Avancé", "Concepts avancés de Java", "150€");
        formationService.update(f1Updated);

        System.out.println("Formation avec ID 1 : " + formationService.getById(1));

        formationService.delete(f2);

        System.out.println("Toutes les formations après suppression : " + formationService.getAll());


        // --------- Gestion des Certificats ---------
        System.out.println("\n===== Gestion des Certificats =====");

        Certificat c1 = new Certificat(1, 1, "2025-05-10", "10:00", 120, "100€", "Débutant");
        Certificat c2 = new Certificat(2, 2, "2025-06-15", "14:00", 90, "150€", "Intermédiaire");

        certificatService.insert(c1);
        certificatService.insert(c2);


        System.out.println("Tous les certificats en base : " + certificatService.getAll());

        Certificat c1Updated = new Certificat(1, 1, "2025-05-12", "11:00", 130, "120€", "Avancé");
        certificatService.update(c1Updated);

        System.out.println("Certificat avec ID 1 : " + certificatService.getById(1));

        certificatService.delete(c2);

        System.out.println("Tous les certificats après suppression : " + certificatService.getAll());*/
    }
}
