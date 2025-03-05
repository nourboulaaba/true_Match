package Controller.gestUser;

import Entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;
import service.UserService;
import service.UserSession;
import utils.PreferenceManager;

import java.sql.SQLException;

public class ChangePasswordController {

    @FXML
    private Text confirmError;

    @FXML
    private Text currError;

    @FXML
    private PasswordField fpConfirm;

    @FXML
    private PasswordField fpCurrent;

    @FXML
    private PasswordField fpNew;

    @FXML
    private Text newError;


    private final UserService userService = new UserService();
    void clearError(){
        confirmError.setText("");
        currError.setText("");
        newError.setText("");
        fpConfirm.getStyleClass().remove("error");
        fpCurrent.getStyleClass().remove("error");
        fpNew.getStyleClass().remove("error");

    }
    @FXML
    void saveChanges(ActionEvent event) {
        clearError();
        boolean isValid = true;
        if (fpCurrent.getText().isEmpty()) {
            isValid = false;
            currError.setText("Current Password is required");
            fpCurrent.getStyleClass().add("error");
        } else if (fpCurrent.getText().length()<8) {
            currError.setText("Current Password must contain at least 8 characters.");
            fpCurrent.getStyleClass().add("error");
            isValid = false;
        }
// Password validation
        if (fpNew.getText().isEmpty()) {
            isValid = false;
            newError.setText("New Password is required");
            fpNew.getStyleClass().add("error");

        } else if (fpNew.getText().length()<8) {
            isValid = false;
            fpNew.getStyleClass().add("error");
            newError.setText("New Password must contain at least 8 characters.");
        }

        // Confirm password validation
        else if (!fpConfirm.getText().equals(fpNew.getText())) {
            fpConfirm.getStyleClass().add("error");
            isValid = false;
            confirmError.setText("Passwords do not match");
        }
        if(isValid) {
            String password = fpCurrent.getText();
            if ( BCrypt.checkpw(password, UserSession.getConnectedUser().getPassword())){
                User user = new User();
                user.setPassword(fpNew.getText());
                user.setId(UserSession.getConnectedUser().getId());
                try {
                    String hashedPass=userService.changePassword(user);
                    UserSession.getConnectedUser().setPassword(hashedPass);
                    PreferenceManager.save("password", hashedPass);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Password changed");
                    alert.setContentText("You are updates your password.");
                    alert.show();
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }else {
                currError.setText("Current Password is incorrect!");
                fpCurrent.getStyleClass().add("error");
            }
        }

    }

}