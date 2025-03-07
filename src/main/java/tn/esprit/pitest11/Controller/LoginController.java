package tn.esprit.pitest11.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.paint.Color;

public class LoginController {

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField telephoneField;

    @FXML
    private PasswordField motDePasseField;

    @FXML
    private PasswordField confirmerMotDePasseField;

    @FXML
    private Label errorLabel;

    @FXML
    private void handleSignUp() {
        // Réinitialiser les messages d'erreur
        errorLabel.setText("");
        errorLabel.setTextFill(Color.RED);

        // Validation des champs
        if (!validateNomPrenom() || !validateEmail() || !validateTelephone() || !validateMotDePasse()) {
            return; // Arrêter si une validation échoue
        }

        // Si tout est valide, procéder à l'inscription
        // (par exemple, enregistrer les données dans la base de données)
        errorLabel.setText("Inscription réussie !");
        errorLabel.setTextFill(Color.GREEN);
    }

    private boolean validateNomPrenom() {
        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();

        if (nom.isEmpty() || prenom.isEmpty()) {
            errorLabel.setText("Le nom et le prénom sont obligatoires.");
            return false;
        }

        if (!nom.matches("[a-zA-Z]+") || !prenom.matches("[a-zA-Z]+")) {
            errorLabel.setText("Le nom et le prénom ne doivent contenir que des lettres.");
            return false;
        }

        return true;
    }

    private boolean validateEmail() {
        String email = emailField.getText().trim();
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        if (email.isEmpty()) {
            errorLabel.setText("L'email est obligatoire.");
            return false;
        }

        if (!email.matches(emailRegex)) {
            errorLabel.setText("Veuillez entrer une adresse email valide.");
            return false;
        }

        return true;
    }

    private boolean validateTelephone() {
        String telephone = telephoneField.getText().trim();

        if (telephone.isEmpty()) {
            errorLabel.setText("Le numéro de téléphone est obligatoire.");
            return false;
        }

        if (!telephone.matches("\\d{10}")) {
            errorLabel.setText("Le numéro de téléphone doit contenir 10 chiffres.");
            return false;
        }

        return true;
    }

    private boolean validateMotDePasse() {
        String motDePasse = motDePasseField.getText().trim();
        String confirmerMotDePasse = confirmerMotDePasseField.getText().trim();

        if (motDePasse.isEmpty() || confirmerMotDePasse.isEmpty()) {
            errorLabel.setText("Le mot de passe et la confirmation sont obligatoires.");
            return false;
        }

        if (!motDePasse.equals(confirmerMotDePasse)) {
            errorLabel.setText("Les mots de passe ne correspondent pas.");
            return false;
        }

        if (motDePasse.length() < 8) {
            errorLabel.setText("Le mot de passe doit contenir au moins 8 caractères.");
            return false;
        }

        if (!motDePasse.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
            errorLabel.setText("Le mot de passe doit contenir au moins une majuscule, une minuscule, un chiffre et un caractère spécial.");
            return false;
        }

        return true;
    }
}