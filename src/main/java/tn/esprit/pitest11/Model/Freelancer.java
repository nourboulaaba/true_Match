package tn.esprit.pitest11.Model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class Freelancer {
    private final SimpleStringProperty name;
    private final SimpleStringProperty hourRating;
    private final SimpleStringProperty reviews;
    private final SimpleStringProperty earnings;
    private final SimpleStringProperty stars;
    private final SimpleStringProperty skills;
    private final SimpleStringProperty bio;
    private final SimpleObjectProperty<Hyperlink> profileLink;



    public SimpleStringProperty hourRatingProperty() {
        return hourRating;
    }

    public SimpleStringProperty reviewsProperty() {
        return reviews;
    }

    public SimpleStringProperty earningsProperty() {
        return earnings;
    }

    public SimpleStringProperty starsProperty() {
        return stars;
    }

    public SimpleStringProperty skillsProperty() {
        return skills;
    }

    public SimpleStringProperty bioProperty() {
        return bio;
    }

    public Freelancer(String name, String hourRating, String reviews, String earnings, String stars, String skills, String bio, String profileUrl) {
        this.name = new SimpleStringProperty(name);
        this.hourRating = new SimpleStringProperty(hourRating);
        this.reviews = new SimpleStringProperty(reviews);
        this.earnings = new SimpleStringProperty(earnings);
        this.stars = new SimpleStringProperty(stars);
        this.skills = new SimpleStringProperty(skills);
        this.bio = new SimpleStringProperty(bio);

        Hyperlink profileLink = new Hyperlink("Contact");
        profileLink.setOnAction(e -> openWebPage(profileUrl));
        this.profileLink = new SimpleObjectProperty<>(profileLink);
    }

    // Getters
    public String getName() { return name.get(); }
    public String getHourRating() { return hourRating.get(); }
    public String getReviews() { return reviews.get(); }
    public String getEarnings() { return earnings.get(); }
    public String getStars() { return stars.get(); }
    public String getSkills() { return skills.get(); }
    public String getBio() { return bio.get(); }
    public Hyperlink getProfileLink() { return profileLink.get(); }

    public SimpleObjectProperty<Hyperlink> profileLinkProperty() { return profileLink; }

    // Helper method to open link in default browser
    private void openWebPage(String url) {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.load(url);

        Stage webStage = new Stage();
        webStage.setTitle("Freelancer Profile");
        webStage.setScene(new Scene(webView, 800, 600));
        webStage.show();
    }
}

