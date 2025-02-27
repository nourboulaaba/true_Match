package Controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import Entities.User;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import service.OTPService;
import service.UserService;

public class ChangeForgotPasswordController {

    @FXML
    private Text confirmError;

    @FXML
    private Text errtest;

    @FXML
    private PasswordField fpConfirm;

    @FXML
    private PasswordField fpNew;

    @FXML
    private Text newError;



    private final UserService userService = new UserService();

    @FXML
    void changePassword(ActionEvent event) {


        String email = OTPService.EMAIL;
        if (fpNew.getText().isEmpty()) {

            newError.setText("New Password is required");
            fpNew.getStyleClass().add("error");

        } else if (fpNew.getText().length()<8) {

            fpNew.getStyleClass().add("error");
            newError.setText("New Password must contain at least 8 characters.");
        }

        // Confirm password validation
        else if (!fpConfirm.getText().equals(fpNew.getText())) {
            fpConfirm.getStyleClass().add("error");

            confirmError.setText("Passwords do not match");
        }else {
            try {
                User user = new User();
                User user1 = userService.getUserByEmail(email);
                user.setId(user1.getId());
                user.setPassword(fpConfirm.getText());
                userService.changePassword(user);
                goToLogin(event);

            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    @FXML
    void goToLogin(Event event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

    }

    @FXML
    void initialize() {
        assert newError != null : "fx:id=\"newError\" was not injected: check your FXML file 'ChangeForgotPassword.fxml'.";
        assert confirmError != null : "fx:id=\"confirmError\" was not injected: check your FXML file 'ChangeForgotPassword.fxml'.";
        assert fpConfirm != null : "fx:id=\"fpConfirm\" was not injected: check your FXML file 'ChangeForgotPassword.fxml'.";
        assert fpNew != null : "fx:id=\"fpNew\" was not injected: check your FXML file 'ChangeForgotPassword.fxml'.";

    }

}