package tn.esprit.pitest11.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import tn.esprit.pitest11.Model.Freelancer;
import tn.esprit.pitest11.Utils.FreelancerParser;

public class FreelancersController {

    @FXML
    private TableView<Freelancer> freelancerTable;
    @FXML private TableColumn<Freelancer, String> nameColumn;
    @FXML private TableColumn<Freelancer, String> skillsColumn;
    @FXML private TableColumn<Freelancer, String> hourRateColumn;
    @FXML private TableColumn<Freelancer, Hyperlink> profileLinkColumn;

    public void initialize() {
        // Bind columns to Freelancer properties
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        skillsColumn.setCellValueFactory(new PropertyValueFactory<>("skills"));
        hourRateColumn.setCellValueFactory(new PropertyValueFactory<>("hourRating"));
        profileLinkColumn.setCellValueFactory(new PropertyValueFactory<>("profileLink"));

        loadFreelancers();
    }

    public void loadFreelancers() {
        try {
            ObservableList<Freelancer> data = FXCollections.observableArrayList();

            // Load freelancers using the parser
            FreelancerParser.loadFreelancers(freelancer -> data.add(freelancer));

            // Populate the TableView
            freelancerTable.setItems(data);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
