package tn.esprit.pitest11.service;

import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class GeminiService {
    public static String analyserReclamation(String sujet, String description) {
        String prompt = "Classifie cette réclamation en 'Urgente', 'Moyenne' ou 'Simple' :\n" +
                "Sujet : " + sujet + "\n" +
                "Description : " + description;

        String classification = RequestHandler.sendRequest(prompt);
        return classification.length() > 100 ? classification.substring(0, 100) : classification;

    }
    private static String extraireClassification(String texte) {
        Pattern pattern = Pattern.compile("\\b(Urgente|Moyenne|Simple)\\b", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(texte);
        return matcher.find() ? matcher.group(1) : "Non classifiée";
    }
}
