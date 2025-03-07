package tn.esprit.pitest11.Controller.gestAuth;

import tn.esprit.pitest11.Controller.gestUser.Login;
import tn.esprit.pitest11.Entities.Role;
import tn.esprit.pitest11.Entities.User;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.java6.auth.oauth2.VerificationCodeReceiver;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfoplus;
import javafx.scene.control.Alert;
import tn.esprit.pitest11.service.UserService;
import tn.esprit.pitest11.service.UserSession;
import tn.esprit.pitest11.utils.PreferenceManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class GoogleAuthController {
    private final UserService userService = new UserService();
    private final String CLIENT_SECRET_FILE = "/client_secret_59297607140-h0dk215jiob0b5anfmjlbu8vemnutv86.apps.googleusercontent.com.json";

    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Arrays.asList(
            "https://www.googleapis.com/auth/userinfo.profile",
            "https://www.googleapis.com/auth/userinfo.email"
    );
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();


    private Credential credential;


    public Credential authorize() throws Exception {
        // Load client secrets
        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                JSON_FACTORY,
                new InputStreamReader(new FileInputStream(getClass().getResource(CLIENT_SECRET_FILE).getFile()))
        );

        // Build flow and trigger user authorization request
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport,
                JSON_FACTORY,
                clientSecrets,
                SCOPES
        )
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .setApprovalPrompt("force")  // This will show consent screen every time

                .build();

        // Use LocalServerReceiver for web-based authentication
        VerificationCodeReceiver receiver = new LocalServerReceiver.Builder()
                .setPort(8888)
                .build();

        // Authorize and return credentials
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }


    public Userinfoplus getUserInfo() throws Exception {
        if (credential == null) {
            throw new IllegalStateException("User must be authorized first. Call authorize() method first.");
        }

        // Create Oauth2 tn.esprit.pitest11.service
        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Oauth2 oauth2 = new Oauth2.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName("Your Application Name")
                .build();

        // Get user info
        return oauth2.userinfo().get().execute();
    }

    public void performGoogleLogin() {


        try {
            Credential credential = authorize();

            this.credential = credential;
            if (credential == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Login Cancelled");
                alert.setHeaderText("Google Authentication");
                alert.setContentText("Google sign-in was cancelled. Please try again.");
                alert.showAndWait();
                return; // Exit the method
            }

            Userinfoplus userInfo = getUserInfo();
            User user;
            if (!userService.emailExists(userInfo.getEmail())) {
                user = new User(userInfo.getFamilyName(), userInfo.getGivenName(), userInfo.getEmail(), null, null);
                user.setRole(Role.EMPLOYE);
                // Define the destination folder
                String destinationFolder = "src/upload/";

                String fileName = userInfo.getName() +userInfo.getId()+ ".png";

                String destinationPath = destinationFolder + fileName;

                // Now call your saveProfileImage method
                saveProfileImage(userInfo.getPicture(), destinationPath);
                File savedFile = new File(destinationPath);
                String fileUrl = savedFile.toURI().toString();
                user.setProfilePhoto(fileUrl);
                user = userService.insertGoogleSignup(user);

            } else {
                user = userService.getUserByEmail(userInfo.getEmail());
            }

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
            System.out.println("User Info: " + userInfo); // Print the entire Userinfoplus object
            PreferenceManager.save("isLoggedIn", true);
            System.out.println(user);
            // Authentication successful
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Login Successful");
            alert.setHeaderText("Google Authentication");
            alert.setContentText("You are now logged in with Google!");
            alert.showAndWait();
            Login logincontroller = new Login();
            logincontroller.openDashboard(user.getRole().name());


        } catch (Exception e) {
            // Handle authentication errors
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Authentication Error");
            alert.setHeaderText("Google Login Failed");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void logout() {
        try {
            // Remove the stored tokens directory
            File tokensDirectory = new File(TOKENS_DIRECTORY_PATH);
            if (tokensDirectory.exists()) {
                deleteDirectory(tokensDirectory);
            }

            // Show logout success alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Logout Successful");
            alert.setHeaderText("Google Authentication");
            alert.setContentText("You have been logged out!");
            alert.showAndWait();

        } catch (Exception e) {
            // Handle logout errors
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Logout Error");
            alert.setHeaderText("Google Logout Failed");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    // Helper method to recursively delete directory and its contents
    private void deleteDirectory(File directoryToBeDeleted) throws IOException {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        if (!directoryToBeDeleted.delete()) {
            throw new IOException("Failed to delete " + directoryToBeDeleted);
        }
    }

    public void saveProfileImage(String imageUrl, String destinationPath) {
        try (InputStream in = new URL(imageUrl).openStream()) {
            // Create directories if they don't exist
            Files.createDirectories(Paths.get(destinationPath).getParent());

            // Save the image to the destination path, replacing if it exists
            Files.copy(in, Paths.get(destinationPath), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Image saved successfully to " + destinationPath);


        } catch (IOException e) {
            System.err.println("Error saving profile image: " + e.getMessage());
            e.printStackTrace();
        }
    }

}