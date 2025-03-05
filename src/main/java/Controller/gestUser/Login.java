package Controller.gestUser;

import Controller.gestAuth.GoogleAuthController;
import Entities.EmailSender;
import Entities.Role;
import Entities.User;
import javafx.fxml.FXMLLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import org.example.dao.DBConnection;
import org.mindrot.jbcrypt.BCrypt;
import service.UserService;
import service.UserSession;
import utils.PreferenceManager;

import java.awt.event.KeyEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Login implements Initializable {

    public Button loginButtonFace;
    @FXML private Label errorLabel;
    @FXML private VBox SIGNUP;
    @FXML private VBox SIGNIN;
    @FXML private Pane ColorPane;
    @FXML private VBox SignUpInformation;
    @FXML private VBox SignInInformation;
    @FXML private Pane InformationPane;
    @FXML private Button minusBtn;
    @FXML private Button closeBtn;
    @FXML private TextField lastName;
    @FXML private TextField firstName;
    @FXML private TextField email;
    @FXML private TextField phoneNumber;
    @FXML private PasswordField password;
    @FXML private PasswordField confirmPassword;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button signUpButton;
    @FXML private Label errorLastName;
    @FXML private Label errorFirstName;
    @FXML private Label errorEmail;
    @FXML private Label errorPhone;
    @FXML private Label errorPassword;
    @FXML private Label errorConfirmPassword;
    @FXML private ProgressBar passwordStrengthBar;
    private final String CLIENT_SECRET_FILE = "/client_secret_59297607140-h0dk215jiob0b5anfmjlbu8vemnutv86.apps.googleusercontent.com.json";
    private final Connection connection = DBConnection.getInstance().getConnection();
    private final UserService userService = new UserService();
    private int duration = 250;
    private double x, y;
    private DropShadow shadow;





    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.shadow = new DropShadow(BlurType.THREE_PASS_BOX, Color.web("#363636"), 0.0, 0.0, 8.0, 0.0);
        shadow.setHeight(0.0);
        shadow.setWidth(55.98);
        ColorPane.requestFocus();
    }
    @FXML
    private void handleFaceIdLogin(ActionEvent event) {
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "\"C:\\Users\\hp\\Desktop\\Nouveau dossier\\myenv\\Scripts\\python.exe\"",
                    "\"C:\\Users\\hp\\Desktop\\Nouveau dossier\\main.py\""

            );

            pb.redirectErrorStream(true); // Merge error and output streams
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            process.waitFor(); // Wait for Python script to finish

            String scriptOutput = output.toString().trim();
            System.out.println("Python Output: " + scriptOutput);

            if (!scriptOutput.isEmpty()) {
                if (scriptOutput.contains("ad")) {
                //    loadFXML("/DashboardFront/DashboardFront.fxml", event);

                }
                else if(scriptOutput.contains("@")) {

                    //collect the user data
                    User user=this.userService.getUserByEmail(scriptOutput);

                    //set the U ser session
                    UserSession.setCurrentUser(user);
                    PreferenceManager.save("role", user.getRole().name());
                    PreferenceManager.save("id", user.getId());
                    PreferenceManager.save("lastName", user.getLastName());
                    PreferenceManager.save("firstName", user.getFirstName());
                    PreferenceManager.save("email", user.getEmail());
                    PreferenceManager.save("identifier", user.getIdentifier());
                    PreferenceManager.save("password", user.getPassword());
                    PreferenceManager.save("CIN", user.getCin());
                    PreferenceManager.save("faceId", user.getFaceId());
                    PreferenceManager.save("salary", user.getSalary());
                    PreferenceManager.save("hireDate", user.getHireDate());
                    PreferenceManager.save("phoneNumber", user.getPhoneNumber());
                    PreferenceManager.save("cv", user.getCv());
                    PreferenceManager.save("profilePhoto", user.getProfilePhoto());
                    PreferenceManager.save("isLoggedIn", true);
                    System.out.println("________________________________________________>");
                    //aftyer craeting the session try to navigatye to the dashboard



                    //si oui naviger le linterafce dashboard en faire une session
                    System.out.println("Redirection vers l'interface client...");

                    openDashboard(user.getRole().name());
                    closeWindow();
                }
                else if  (scriptOutput.contains("unknown_person")) {
                    showAlert("Erreur", "Utilisateur non trouvé.");
                }
                else
                {
                    showAlert("Erreur", "Utilisateur non trouvé.");

                }
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors de l'exécution du script Python.");
        }
    }
    @FXML
    public void SignUP(ActionEvent actionEvent) {
        SIGNIN.setVisible(true);
        SIGNIN.setOpacity(0.0);
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(this.duration), new KeyValue(ColorPane.translateXProperty(), 250)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(this.duration), new KeyValue(InformationPane.translateXProperty(), -250)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(this.duration), new KeyValue(ColorPane.styleProperty(), "-fx-background-color:  linear-gradient(to right, #fc5c65 , #fc5c65); -fx-background-radius : 20 ")));

        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(100), new KeyValue(SIGNUP.opacityProperty(), 0)));
        timeline.setOnFinished(e -> {
            SignUpInformation.setVisible(true);
            SignInInformation.setVisible(false);
            Timeline timeline2 = new Timeline();
            timeline2.getKeyFrames().add(new KeyFrame(Duration.millis(this.duration), new KeyValue(ColorPane.translateXProperty(), 0)));
            timeline2.getKeyFrames().add(new KeyFrame(Duration.millis(this.duration), new KeyValue(InformationPane.translateXProperty(), 0)));
            timeline2.getKeyFrames().add(new KeyFrame(Duration.millis(this.duration), new KeyValue(ColorPane.styleProperty(), "-fx-background-color:  linear-gradient(to right, #fc5c65 , #fc5c65); -fx-background-radius : 20 0 0 20")));
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
    @FXML
    void handleFaceId(ActionEvent event) {
        try {
            String email = this.email.getText().trim();
            if (email.isEmpty()) {
                showAlert("Erreur", "Veuillez entrer un email avant d'enregistrer le visage.");
                return;
            }
            String role ="employee";


            ProcessBuilder pb = new ProcessBuilder(
                    "\"C:\\Users\\hp\\Desktop\\Nouveau dossier\\myenv\\Scripts\\python.exe\"",
                    "\"C:\\Users\\hp\\Desktop\\Nouveau dossier\\savePicture.py\"",
                    email
            );
            pb.redirectErrorStream(true); // Capture error messages

            Process process = pb.start();

            // Read output from the script (optional, for debugging)
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            // Wait for the process to complete
            int exitCode = process.waitFor();
            System.out.println("Python script exited with code: " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Veuillez saisir  l'email et le mot de passe");
            return;
        }
        User user = userService.getUserByEmail(username);
        if (user == null) {
            showAlert("Error", "Email introuvable.");
            return;
        }

        if (BCrypt.checkpw(password, user.getPassword())) {
            UserSession.setCurrentUser(user);
            PreferenceManager.save("isLoggedIn", true);
            PreferenceManager.save("role", user.getRole().name());
            PreferenceManager.save("id", user.getId());
            PreferenceManager.save("lastName", user.getLastName());
            PreferenceManager.save("firstName", user.getFirstName());
            PreferenceManager.save("email", user.getEmail());
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

    public void openDashboard(String role) {
        String fxmlFile;

        if ("RH".equalsIgnoreCase(role)) {
            fxmlFile = "/Dashboard.fxml";
        } else if ("EMPLOYE".equalsIgnoreCase(role)) {
            fxmlFile = "/DashEmployee.fxml";
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

    public void closeWindow() {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();
    }


    @FXML
    private void registerUser() {

        String lastNameText = lastName.getText().trim();
        String firstNameText = firstName.getText().trim();
        String emailText = email.getText().trim();
        String phoneNumberText = phoneNumber.getText().trim();
        String passwordText = password.getText();
        String confirmPasswordText = confirmPassword.getText();

        boolean isValid = true;

        if (!isValidName(lastNameText)) {
            errorLastName.setText("Nom invalide (lettres uniquement)");
            errorLastName.setVisible(true);
            isValid = false;
        } else {
            errorLastName.setVisible(false);
        }

        if (!isValidName(firstNameText)) {
            errorFirstName.setText("Prénom invalide (lettres uniquement)");
            errorFirstName.setVisible(true);
            isValid = false;
        } else {
            errorFirstName.setVisible(false);
        }

        if (!isValidEmail(emailText)) {
            errorEmail.setText("Adresse email invalide");
            errorEmail.setVisible(true);
            isValid = false;
        } else {
            errorEmail.setVisible(false);
        }
        if (passwordText.length() < 9) {
            errorPassword.setText("Le mot de passe doit contenir au moins 9 caractères");
            errorPassword.setVisible(true);
            isValid = false;
        } else {
            errorPassword.setVisible(false);
        }
        if (!phoneNumberText.matches("\\d{8}")) {
            errorPhone.setText("Le numéro doit contenir 8 chiffres");
            errorPhone.setVisible(true);
            isValid = false;
        } else {
            errorPhone.setVisible(false);
        }
        if (!isValidEmail(emailText)) {
            errorEmail.setText("Adresse email invalide");
            errorEmail.setVisible(true);
            isValid = false;
        } else {
            // Vérifier si l'email existe déjà dans la base de données
            String checkEmailQuery = "SELECT COUNT(*) AS email_count FROM user WHERE email = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkEmailQuery)) {
                checkStmt.setString(1, emailText);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    int emailCount = rs.getInt("email_count"); // Nombre d'emails correspondants
                    if (emailCount > 0) {
                        errorEmail.setText("Cet email est déjà utilisé");
                        errorEmail.setVisible(true);
                        isValid = false;
                    } else {
                        errorEmail.setVisible(false);
                    }
                }
            } catch (SQLException e) {
                showAlert("Erreur", "Une erreur est survenue lors de la vérification de l'email : " + e.getMessage());
                isValid = false;
            }
        }

        if (!passwordText.equals(confirmPasswordText)) {
            errorConfirmPassword.setText("Les mots de passe ne correspondent pas !");
            errorConfirmPassword.setVisible(true);
            isValid = false;
        } else {
            errorConfirmPassword.setText("Mot de passe confirmé !");
            errorConfirmPassword.setTextFill(Color.GREEN);
            errorConfirmPassword.setVisible(true);
        }

        if (!isValid) return;

        String hashedPassword = BCrypt.hashpw(passwordText, BCrypt.gensalt());

        String insertQuery = "INSERT INTO user (last_name, first_name, email, password, phone_number, role, salary) VALUES (?, ?, ?, ?, ?, ?, 50)";
        try (PreparedStatement stmt = connection.prepareStatement(insertQuery)) {
            stmt.setString(1, lastNameText);
            stmt.setString(2, firstNameText);
            stmt.setString(3, emailText);
            stmt.setString(4, hashedPassword);
            stmt.setString(5, phoneNumberText);
            stmt.setString(6, Role.EMPLOYE.toString());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try {
                    EmailSender.sendWelcomeEmail(emailText);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("Erreur lors de l'envoi de l'email : " + e.getMessage());
                }

                showAlert("Succès", "Utilisateur inscrit avec succès !");
            } else {
                showAlert("Erreur", "L'inscription a échoué.");
            }
        } catch (SQLException e) {
            showAlert("Erreur", "Une erreur est survenue lors de l'inscription : " + e.getMessage());
        }
    }

    private boolean isValidName(String name) {
        return name.matches("^[a-zA-ZÀ-ÿ\\s]+$");
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
    }

    private int checkPasswordStrength(String password) {
        if (password.length() < 6) return 0;
        if (password.matches("[a-zA-Z]+")) return 1;
        if (password.matches("[a-zA-Z0-9]+")) return 2;
        if (password.matches(".*[!@#$%^&*()_+{}:;<>?].*")) return 3;
        return 3;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void SignIn(ActionEvent actionEvent) {
        SIGNUP.setVisible(true);
        SIGNUP.setOpacity(0.0);
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(this.duration), new KeyValue(ColorPane.translateXProperty(), 250)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(this.duration), new KeyValue(InformationPane.translateXProperty(), -250)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(this.duration), new KeyValue(ColorPane.styleProperty(), "-fx-background-color:  linear-gradient(to right, #699BF7 , #FFFFFF); -fx-background-radius : 20 ")));

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

    @FXML
    void forgotPassword() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Auth/ForgetPassword.fxml"));
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


    @FXML
    void handleFaceId(KeyEvent event) {
        System.out.println("this button is clicked !!!!");
    }
    @FXML
    public void GoogleLoginButton() {
        GoogleAuthController authController = new GoogleAuthController();
        authController.performGoogleLogin();
        closeWindow();

    }


}