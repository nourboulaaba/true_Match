package tn.esprit.pitest11.Controller;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.net.URL;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXPasswordField;

import tn.esprit.pitest11.example.dao.DBConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Login implements Initializable{
    @FXML
    private VBox SIGNUP;
    @FXML
    private VBox SIGNIN;
    @FXML
    private Pane ColorPane;
    private DropShadow shadow ;
    @FXML
    private VBox SignUpInformation;
    @FXML
    private VBox SignInInformation;
    @FXML
    private Pane InformationPane;
    @FXML
    private JFXButton minusBtn;
    @FXML
    private JFXButton closeBtn;

    @FXML
    private JFXTextField lastName;

    @FXML
    private JFXTextField firstName;

    @FXML
    private JFXTextField email;

    @FXML
    private JFXTextField phoneNumber;

    @FXML
    private JFXPasswordField password;

    @FXML
    private JFXButton signUpButton;
    @FXML
    private Connection connection;

    private int duration = 250;
    private double x, y;


    public Login(){
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
            Timeline timeline2 =  new Timeline();
            timeline2.getKeyFrames().add(new KeyFrame(Duration.millis(this.duration), new KeyValue(ColorPane.translateXProperty(), 0)));
            timeline2.getKeyFrames().add(new KeyFrame(Duration.millis(this.duration), new KeyValue(InformationPane.translateXProperty(), 0)));
            timeline2.getKeyFrames().add(new KeyFrame(Duration.millis(this.duration), new KeyValue(ColorPane.styleProperty(), "-fx-background-color:  linear-gradient(to right, #eb3b5a , #fc5c65); -fx-background-radius : 20 0 0 20")));
            this.shadow.setOffsetX(8.0);
            timeline2.getKeyFrames().add(new KeyFrame(Duration.millis(this.duration), new KeyValue(ColorPane.effectProperty(),  this.shadow )));
            timeline2.setOnFinished(e2 -> {
                SIGNUP.setVisible(false);
                Timeline timeline3 =  new Timeline();
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
    private void registerUser() {
        String lastNameText = lastName.getText();
        String firstNameText = firstName.getText();
        String emailText = email.getText();
        String phoneNumberText = phoneNumber.getText();
        String passwordText = password.getText();

        String hashedPassword = BCrypt.hashpw(passwordText, BCrypt.gensalt());
       // String insertQuery = "INSERT INTO utilisateur (lastName, firstName, email,  password , phoneNumber) VALUES (?, ?, ?, ?, ?)";
        String insertQuery = "INSERT INTO utilisateur (lastName, firstName, email,  password , phoneNumber) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertQuery)) {
            stmt.setString(1, lastNameText);
            stmt.setString(2, firstNameText);  // Insérer la valeur JSON pour roles
            stmt.setString(3, emailText);
            stmt.setString(4, hashedPassword);
            stmt.setString(5, phoneNumberText);

            int rowsAffected = stmt.executeUpdate();



            //int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User registered successfully!");
            } else {
                System.out.println("User registration failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
            Timeline timeline2 =  new Timeline();
            timeline2.getKeyFrames().add(new KeyFrame(Duration.millis(this.duration), new KeyValue(ColorPane.translateXProperty(), 500)));
            timeline2.getKeyFrames().add(new KeyFrame(Duration.millis(this.duration), new KeyValue(InformationPane.translateXProperty(), -500)));
            timeline2.getKeyFrames().add(new KeyFrame(Duration.millis(this.duration), new KeyValue(ColorPane.styleProperty(), "-fx-background-color:  linear-gradient(to right, #699BF7 , #FFFFFF); -fx-background-radius :  0 20 20 0 ")));
            this.shadow.setOffsetX(-8.0);
            timeline2.getKeyFrames().add(new KeyFrame(Duration.millis(this.duration), new KeyValue(ColorPane.effectProperty(),  this.shadow )));
            timeline2.setOnFinished(e2 -> {
                minusBtn.getStyleClass().remove("topButton");
                closeBtn.getStyleClass().remove("topButton");
                minusBtn.getStyleClass().add("topButton2");
                closeBtn.getStyleClass().add("topButton2");
                SIGNIN.setVisible(false);
                Timeline timeline3 =  new Timeline();
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


}
