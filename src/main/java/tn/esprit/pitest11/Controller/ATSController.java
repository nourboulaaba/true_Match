package tn.esprit.pitest11.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import tn.esprit.pitest11.Model.CV;
import tn.esprit.pitest11.Model.Offre;
import tn.esprit.pitest11.Utils.GeminiAPI;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ATSController {

    private Offre offre;
    private List<CV> cvs = new ArrayList<>();

    @FXML
    private ListView<String> rankingList;

    @FXML
    private Button upload;

    public void setOffre(Offre offre) {
        this.offre = offre;
        updateRankingList(); // Refresh ranking based on current data
    }

    @FXML
    void openUpload(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {
            CV cv = new CV();
            cv.setFilepath(file.getAbsolutePath());
            cv.setName(file.getName());
            cv.setDescription(extractText(file)); // Extract text from the image using OCR

            // Score CV using Gemini API
            scoreCVWithGemini(cv);

            // Score CV based on word matching
            int matchScore = matchWordsScore(cv);

            // Set the higher score
            int finalScore = Math.max(cv.getScore(), matchScore);
            cv.setScore(finalScore);

            cvs.add(cv); // Add the new CV to the list
            updateRankingList(); // Immediately update the UI


            updateRankingList(); // Immediately update the UI
        }
    }

    private String cleanExtractedText(String rawText) {
        // Normalize spaces (replace multiple spaces with a single space)
        rawText = rawText.replaceAll("\\s+", " ").trim();

        // Optionally replace some common OCR misreads (e.g., "1" -> "l", "O" -> "0")
        rawText = rawText.replaceAll("1", "l").replaceAll("O", "0");

        // Remove any unwanted special characters (optional)
        rawText = rawText.replaceAll("[^a-zA-Z0-9\\s,.!?]", "");

        return rawText;
    }


    private String extractText(File file) {
        Tesseract tesseract = getTesseract();
        try {
            String text = tesseract.doOCR(file); // Perform OCR on the image file
            text = cleanExtractedText(text); // Clean up the extracted text
            System.out.println("Extracted and cleaned text: " + text); // Debug log for cleaned text
            return text;
        } catch (TesseractException e) {
            e.printStackTrace();
            return "Error reading CV";
        }
    }


    private Tesseract getTesseract() {
        Tesseract instance = new Tesseract();
        instance.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata"); // Ensure tessdata is set correctly
        instance.setLanguage("eng");
        return instance;
    }

    private void scoreCVWithGemini(CV cv) {
        if (offre != null && cv.getDescription() != null) {
            String prompt = "Analyze this CV description:\n" + cv.getDescription() +
                    "\n\nCompare it with the job offer description:\n" + offre.getDescription() +
                    "\n\nGive a relevance score from 0 to 100.";

            try {
                String response = GeminiAPI.getGeminiResponse(prompt).toString();
                int score = extractScoreFromResponse(response);
                cv.setScore(score);
            } catch (IOException e) {
                e.printStackTrace();
                cv.setScore(0); // Default score if API fails
            }
        }
    }



    private int matchWordsScore(CV cv) {
        if (offre != null && cv.getDescription() != null) {
            String[] cvWords = cv.getDescription().toLowerCase().split("\\s+");
            String[] offerWords = offre.getDescription().toLowerCase().split("\\s+");

            // Count the number of matching words
            int matchCount = 0;
            for (String cvWord : cvWords) {
                for (String offerWord : offerWords) {
                    if (cvWord.equals(offerWord)) {
                        matchCount++;
                    }
                }
            }

            // Return a score based on the number of matches
            return matchCount * 2; // Multiply by 2 to scale the score
        }
        return 0; // Return 0 if no description is available
    }

    private int extractScoreFromResponse(String response) {
        try {
            String scoreStr = response.replaceAll("\\D+", ""); // Extract digits
            return Integer.parseInt(scoreStr);
        } catch (NumberFormatException e) {
            return 0; // Default score if parsing fails
        }
    }

    private void updateRankingList() {
        cvs.sort(Comparator.comparingInt(CV::getScore).reversed()); // Sort CVs by score (best first)

        rankingList.getItems().clear(); // Clear existing items
        for (CV cv : cvs) {
            rankingList.getItems().add(cv.getName() + " - Score: " + cv.getScore());
            System.out.println(cv.getName() + " - Score: " + cv.getDescription());
        }

        rankingList.refresh(); // Ensure ListView updates immediately
    }

    @FXML
    public void initialize() {
        updateRankingList(); // Ensure the UI reflects the stored data
    }
}
