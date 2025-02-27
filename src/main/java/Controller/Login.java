package Controller;

import Entities.EmailSender;
import Entities.Role;
import Entities.User;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


import org.example.dao.DBConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import service.UserService;
import service.UserSession;
import utils.PreferenceManager;

public class Login implements Initializable {

    @FXML
    private Label errorLabel;
    @FXML
    private VBox SIGNUP;
    @FXML
    private VBox SIGNIN;
    @FXML
    private Pane ColorPane;
    private DropShadow shadow;
    @FXML
    private VBox SignUpInformation;
    @FXML
    private VBox SignInInformation;
    @FXML
    private Pane InformationPane;
    @FXML
    private Button minusBtn;
    @FXML
    private Button closeBtn;

    @FXML
    private TextField lastName;

    @FXML
    private TextField firstName;

    @FXML
    private TextField email;

    @FXML
    private TextField phoneNumber;

    @FXML
    private PasswordField password;


    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button signUpButton;
    @FXML
    private Connection connection;


    private UserService utilisateurService = new UserService();
    private int duration = 250;
    private double x, y;

    @FXML
    private void initialize() {
        loginButton.setOnAction(event -> login());
    }

    public Login() {
        connection = DBConnection.getInstance().getConnection();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.shadow = new DropShadow(BlurType.THREE_PASS_BOX, Color.web("#363636"), 0.0, 0.0, 8.0, 0.0);
        shadow.setHeight(0.0);
        shadow.setWidth(55.98);
        ColorPane.requestFocus();
    }


    @FXML
    public void SignUP(ActionEvent actionEvent) {
        SIGNIN.setVisible(true);
        SIGNIN.setOpacity(0.0);
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(this.duration), new KeyValue(ColorPane.translateXProperty(), 250)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(this.duration), new KeyValue(InformationPane.translateXProperty(), -250)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(this.duration), new KeyValue(ColorPane.styleProperty(), "-fx-background-color:  linear-gradient(to right, #eb3b5a , #fc5c65); -fx-background-radius : 20 ")));

        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(100), new KeyValue(SIGNUP.opacityProperty(), 0)));
        timeline.setOnFinished(e -> {
            SignUpInformation.setVisible(true);
            SignInInformation.setVisible(false);
            Timeline timeline2 = new Timeline();
            timeline2.getKeyFrames().add(new KeyFrame(Duration.millis(this.duration), new KeyValue(ColorPane.translateXProperty(), 0)));
            timeline2.getKeyFrames().add(new KeyFrame(Duration.millis(this.duration), new KeyValue(InformationPane.translateXProperty(), 0)));
            timeline2.getKeyFrames().add(new KeyFrame(Duration.millis(this.duration), new KeyValue(ColorPane.styleProperty(), "-fx-background-color:  linear-gradient(to right, #eb3b5a , #fc5c65); -fx-background-radius : 20 0 0 20")));
            this.shadow.setOffsetX(8.0);
            timeline2.getKeyFrames().add(new KeyFrame(Duration.millis(this.duration), new KeyValue(ColorPane.effectProperty(), this.shadow)));
            timeline2.setOnFinished(e2 -> {
                SIGNUP.setVisible(false);
                Timeline timeline3 = new Timeline();
                timeline3.getKeyFrames().add(new KeyFrame(Duration.millis(this.duration), new KeyValue(SIGNIN.opacityProperty(), 1)));
                timeline3.play();
            });
            timeline2.play();
        });
        minusBtn.getStyleClass().add("topButton");
        closeBtn.getStyleClass().add("topButton");
        minusBtn.getStyleClass().remove("topButton2");
        closeBtn.getStyleClass().remove("topButton2");
        timeline.play();


    }

    /*public void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter both username and password.");
            return;
        }

        String sql = "SELECT * FROM utilisateur WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                String role = rs.getString("role");

                if (org.mindrot.jbcrypt.BCrypt.checkpw(password, hashedPassword)) {
                    String lastName = rs.getString("lastName");
                    String firstName = rs.getString("firstName");
                    String identifier = rs.getString("identifier");
                    String email = rs.getString("email");
                    String cin = rs.getString("cin");
                    String faceId = rs.getString("faceId");
                    String salary = rs.getString("salary");
                    String hireDate = rs.getString("hireDate");
                    String phoneNumber = rs.getString("phoneNumber");
                    String cv = rs.getString("cv");
                    String profilePhoto = rs.getString("profilePhoto");
                    String id = rs.getString("id");
                    utilisateur user = new utilisateur();

                    utilisateurService.setCurrentUser(user);
                    showAlert("Success", "Login successful!");

                    openDashboard(role); // Pass role to open the corresponding dashboard
                    closeWindow();
                } else {
                    showAlert("Error", "Invalid password.");
                }
            } else {
                showAlert("Error", "Username not found.");
            }
        } catch (SQLException e) {
            showAlert("Error", "An error occurred during login: " + e.getMessage());
        }
    }
*/

    @FXML
    public void login() {

        String username = usernameField.getText();
        String password = passwordField.getText();
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter both username and password.");
            return;
        }
        User user = utilisateurService.getUserByEmail(username);
        if (user == null) {
            showAlert("Error", "Username not found.");
            return;
        }

        if (org.mindrot.jbcrypt.BCrypt.checkpw(password, user.getPassword())) {
            UserSession.setCurrentUser(user);
            PreferenceManager.save("isLoggedIn", true);
            PreferenceManager.save("role", user.getRole().name());
            PreferenceManager.save("id", user.getId());
            PreferenceManager.save("lastName", user.getLastName());
            PreferenceManager.save("firstName", user.getIdentifier());
            PreferenceManager.save("identifier", user.getIdentifier());
            PreferenceManager.save("password", user.getPassword());
            PreferenceManager.save("CIN", user.getCin());
            PreferenceManager.save("faceId", user.getFaceId());
            PreferenceManager.save("salary", user.getSalary());
            PreferenceManager.save("hireDate", user.getHireDate());
            PreferenceManager.save("phoneNumber", user.getPhoneNumber());
            PreferenceManager.save("cv", user.getCv());
            PreferenceManager.save("profilePhoto", user.getProfilePhoto());

            showAlert("Success", "Login successful!");
            openDashboard(user.getRole().name());
            closeWindow();
        } else {
            showAlert("Error", "Invalid password.");
        }
    }


    private void openDashboard(String role) {
        String fxmlFile;

        if ("RH".equalsIgnoreCase(role)) {
            fxmlFile = "/Dashboard.fxml"; // Dashboard pour les candidats
        } else if ("EMPLOYE".equalsIgnoreCase(role)) {
            fxmlFile = "/DashEmployee.fxml"; // Dashboard pour les employés
        } else {
            showAlert("Error", "Unknown role: " + role);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Dashboard - " + role.toUpperCase());

            stage.show();
        } catch (Exception e) {
            showAlert("Error", "Failed to open the dashboard: " + e.getMessage());
        }
    }


    private void closeWindow() {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void registerUser() {
        String lastNameText = lastName.getText();
        String firstNameText = firstName.getText();
        String emailText = email.getText();
        String phoneNumberText = phoneNumber.getText();
        String passwordText = password.getText();

        // Vérification des champs vides
        if (lastNameText.isEmpty() || firstNameText.isEmpty() || emailText.isEmpty() || phoneNumberText.isEmpty() || passwordText.isEmpty()) {
            showAlert("Error", "All fields are required.");
            return;
        }

        // Validation de l'email
        if (!isValidEmail(emailText)) {
            showAlert("Error", "Please enter a valid email address.");
            return;
        }

        // Validation du numéro de téléphone (format simple)
        if (!isValidPhoneNumber(phoneNumberText)) {
            showAlert("Error", "Please enter a valid phone number.");
            return;
        }

        // Validation du mot de passe
        if (!isValidPassword(passwordText)) {
            showAlert("Error", "Password must be at least 6 characters long.");
            return;
        }

        String hashedPassword = BCrypt.hashpw(passwordText, BCrypt.gensalt());
        String insertQuery = "INSERT INTO user (last_name, first_name, email, password, phone_number, role, salary) VALUES (?, ?, ?, ?, ?, ?, 50)";
        try (PreparedStatement stmt = connection.prepareStatement(insertQuery)) {
            stmt.setString(1, lastNameText);
            stmt.setString(2, firstNameText);
            stmt.setString(3, emailText);
            stmt.setString(4, hashedPassword);
            stmt.setString(5, phoneNumberText);
            stmt.setString(6, Role.EMPLOYE.toString() );


            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try {
                    EmailSender.sendWelcomeEmail(emailText);
                } catch (Exception e) {
                    e.printStackTrace(); // Affichez l'erreur dans les logs
                    System.err.println("Erreur lors de l'envoi de l'email : " + e.getMessage());
                }

                showAlert("Success", "User registered successfully!");

            } else {
                showAlert("Error", "User registration failed.");
            }
        } catch (SQLException e) {
            showAlert("Error", "An error occurred during registration: " + e.getMessage());
        }
    }


    @FXML
    public void SignIn(ActionEvent actionEvent) {
        SIGNUP.setVisible(true);
        SIGNUP.setOpacity(0.0);
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(this.duration), new KeyValue(ColorPane.translateXProperty(), 250)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(this.duration), new KeyValue(InformationPane.translateXProperty(), -250)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(this.duration), new KeyValue(ColorPane.styleProperty(), "-fx-background-color:  linear-gradient(to right, ##699BF7 , ##FFFFFF); -fx-background-radius : 20 ")));

        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(100), new KeyValue(SIGNIN.opacityProperty(), 0)));
        timeline.setOnFinished(e -> {
            SignUpInformation.setVisible(false);
            SignInInformation.setVisible(true);
            Timeline timeline2 = new Timeline();
            timeline2.getKeyFrames().add(new KeyFrame(Duration.millis(this.duration), new KeyValue(ColorPane.translateXProperty(), 500)));
            timeline2.getKeyFrames().add(new KeyFrame(Duration.millis(this.duration), new KeyValue(InformationPane.translateXProperty(), -500)));
            timeline2.getKeyFrames().add(new KeyFrame(Duration.millis(this.duration), new KeyValue(ColorPane.styleProperty(), "-fx-background-color:  linear-gradient(to right, #699BF7 , #FFFFFF); -fx-background-radius :  0 20 20 0 ")));
            this.shadow.setOffsetX(-8.0);
            timeline2.getKeyFrames().add(new KeyFrame(Duration.millis(this.duration), new KeyValue(ColorPane.effectProperty(), this.shadow)));
            timeline2.setOnFinished(e2 -> {
                minusBtn.getStyleClass().remove("topButton");
                closeBtn.getStyleClass().remove("topButton");
                minusBtn.getStyleClass().add("topButton2");
                closeBtn.getStyleClass().add("topButton2");
                SIGNIN.setVisible(false);
                Timeline timeline3 = new Timeline();
                timeline3.getKeyFrames().add(new KeyFrame(Duration.millis(this.duration), new KeyValue(SIGNUP.opacityProperty(), 1)));
                timeline3.play();
            });
            timeline2.play();
        });
        timeline.play();
    }

    @FXML
    private void close(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void minus(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    public void screenPressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }


    @FXML
    public void screenDragged(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 6;  // Minimum 6 caractères pour le mot de passe
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        String phoneRegex = "^[0-9]{8}$";  // Simple validation pour un numéro à 10 chiffres
        return phoneNumber.matches(phoneRegex);
    }


    @FXML
    void forgotPassword(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ForgetPassword.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur de chargement de la scène.", "Essayer ultérieurement.");
        }
    }

}