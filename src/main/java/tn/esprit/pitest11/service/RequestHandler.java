package tn.esprit.pitest11.service;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class RequestHandler {
    public static String sendRequest(String prompt) {
        try {
            System.out.println("Starting API request...");

            URL url = new URL(Config.GEMINI_API_URL + "?key=" + Config.API_KEY);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Constructing JSON request body
            JsonObject requestBody = new JsonObject();
            JsonArray contentsArray = new JsonArray();
            JsonObject contentObject = new JsonObject();
            JsonArray partsArray = new JsonArray();
            JsonObject part = new JsonObject();

            part.addProperty("text", prompt);
            partsArray.add(part);
            contentObject.add("parts", partsArray);
            contentsArray.add(contentObject);
            requestBody.add("contents", contentsArray);

            System.out.println("Sending request: " + requestBody);

            // Sending request
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = requestBody.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Reading response
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode != 200) {
                System.out.println("Error response from API: " + responseCode);
                Scanner errorScanner = new Scanner(connection.getErrorStream());
                String errorResponse = errorScanner.useDelimiter("\\A").next();
                errorScanner.close();
                System.out.println("Error Response: " + errorResponse);
                return "Erreur API: " + errorResponse;
            }

            Scanner scanner = new Scanner(connection.getInputStream());
            String response = scanner.useDelimiter("\\A").next();
            scanner.close();

            System.out.println("API Response: " + response);

            // Parsing JSON response
            JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
            JsonArray candidates = jsonResponse.getAsJsonArray("candidates");

            if (candidates != null && candidates.size() > 0) {
                JsonObject firstCandidate = candidates.get(0).getAsJsonObject();

                if (firstCandidate.has("content")) {
                    JsonObject content = firstCandidate.getAsJsonObject("content");

                    if (content.has("parts")) {
                        JsonArray parts = content.getAsJsonArray("parts");

                        if (parts.size() > 0) {
                            return parts.get(0).getAsJsonObject().get("text").getAsString();
                        }
                    }
                }
            }

            return "Aucune réponse reçue de l'API.";
        } catch (Exception e) {
            return "Erreur API : " + e.getMessage();
        }
    }
}
