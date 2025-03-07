package tn.esprit.pitest11.Model;

import java.util.Objects;

public class CV {
    private int id;
    private String name;
    private String description;
    private String filepath;
    private int score;
    private String offreDescription;
    private int offreID;

    public CV(int id, String name, String description, String filepath, int score, String offreDescription) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.filepath = filepath;
        this.score = score;
        this.offreDescription = offreDescription;
    }

    public CV(int id, String name, String description, String filepath, int score, String offreDescription, int offreID) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.filepath = filepath;
        this.score = score;
        this.offreDescription = offreDescription;
        this.offreID = offreID;
    }

    public int getOffreID() {
        return offreID;
    }

    public void setOffreID(int offreID) {
        this.offreID = offreID;
    }

    public CV() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getOffreDescription() {
        return offreDescription;
    }

    public void setOffreDescription(String offreDescription) {
        this.offreDescription = offreDescription;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CV cv = (CV) o;
        return id == cv.id && score == cv.score && Objects.equals(name, cv.name) && Objects.equals(description, cv.description) && Objects.equals(filepath, cv.filepath) && Objects.equals(offreDescription, cv.offreDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, filepath, score, offreDescription);
    }

    @Override
    public String toString() {
        return "CV{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", filepath='" + filepath + '\'' +
                ", score=" + score +
                ", offreDescription='" + offreDescription + '\'' +
                '}';
    }
}
