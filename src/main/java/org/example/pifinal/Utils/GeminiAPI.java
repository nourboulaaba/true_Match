package org.example.pifinal.Utils;



import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class GeminiAPI {
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=AIzaSyCWYXrEGlHeFdgJ-HEgeP-d-53vxgsxCso";

    public static String getOfferScore(String offerDetails) throws IOException {
        JsonObject response = getGeminiResponse("Give a score out of 10 for the following offer: " + offerDetails+"Make sure that the score is not superior to 10 and respond only with the score number");
        return extractScore(response);
    }

    public static JsonObject getGeminiResponse(String prompt) throws IOException {
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String requestBody = "{\"contents\": [{\"parts\":[{\"text\": \"" + prompt + "\"}]}]}";

        try (OutputStream os = conn.getOutputStream()) {
            os.write(requestBody.getBytes(StandardCharsets.UTF_8));
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            return JsonParser.parseString(response.toString()).getAsJsonObject();
        }
    }

    private static String extractScore(JsonObject response) {
        try {
            String content = response.getAsJsonArray("candidates")
                    .get(0).getAsJsonObject()
                    .get("content").getAsJsonObject()
                    .getAsJsonArray("parts")
                    .get(0).getAsJsonObject()
                    .get("text").getAsString();

            return content.replaceAll("[^0-9]", "").trim();
        } catch (Exception e) {
            e.printStackTrace();
            return "N/A";
        }
    }
}
