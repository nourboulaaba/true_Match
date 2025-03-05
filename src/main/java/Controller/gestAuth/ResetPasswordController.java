package Controller.gestAuth;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import service.OTPService;

public class ResetPasswordController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Text emailError;

    @FXML
    private TextField ftCode;

    @FXML
    private Text ftEmail;

    @FXML
    void confirmeOTP(ActionEvent event) {
        if (OTPService.validateOTP(ftCode.getText())){

            //implement change password code here ....
            try {
                Stage stage = (Stage) ftEmail.getScene().getWindow(); // Get reference to the login window's stage
                stage.setTitle("Change Password");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Auth/ChangeForgotPassword.fxml"));
                Parent p = loader.load();

                Scene scene = new Scene(p);

                stage.setScene(scene);

                stage.show();
            } catch (Exception e) {

                e.printStackTrace();
                // Handle navigation failure
            }
        }
        else {
            emailError.setText("this code is incorrect");
        }
    }

    @FXML
    void goToLogin(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

    }

    @FXML
    void initialize() {
        assert emailError != null : "fx:id=\"emailError\" was not injected: check your FXML file 'ResetPassword.fxml'.";
        assert ftCode != null : "fx:id=\"ftCode\" was not injected: check your FXML file 'ResetPassword.fxml'.";
        assert ftEmail != null : "fx:id=\"ftEmail\" was not injected: check your FXML file 'ResetPassword.fxml'.";

    }

}