package Controller.gestUser;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import service.OTPService;
import service.UserService;
import javafx.scene.Node;


public class ForgotPasswordController {





    @FXML
    private Text emailError;

    @FXML
    private TextField ftEmail;


    private final UserService serviceUser = new UserService();

    @FXML
    void findAccount(ActionEvent event) {
        if (serviceUser.emailExists(ftEmail.getText())) {
            String otp = OTPService.generateOTP(ftEmail.getText(), 6);
            OTPService.sendOTP(ftEmail.getText(), otp);
            try {
                Stage stage = (Stage) ftEmail.getScene().getWindow(); // Get reference to the login window's stage
                stage.setTitle("ForgotPassword");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Auth/ResetPassword.fxml"));
                Parent p = loader.load();

                Scene scene = new Scene(p);

                stage.setScene(scene);

                stage.show();
            } catch (Exception e) {

                e.printStackTrace();
                // Handle navigation failure
            }

        } else
            emailError.setText("The email address does not exist.");


    }

    @FXML
    void goToLogin(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

    }

    @FXML
    void initialize() {

        assert emailError != null : "fx:id=\"emailError\" was not injected: check your FXML file 'ForgotPassword.fxml'.";
        assert ftEmail != null : "fx:id=\"ftEmail\" was not injected: check your FXML file 'ForgotPassword.fxml'.";

    }

}