package org.example.pifinal.Utils;

import javafx.scene.control.TableView;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;
import org.example.pifinal.Model.Freelancer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class FreelancerParser {

    public static void loadFreelancers(Consumer<Freelancer> onFreelancerLoaded) {
        try (AsyncHttpClient client = new DefaultAsyncHttpClient()) {
            client.prepare("GET", "http://localhost:9090/freelancers")
                    .execute()
                    .toCompletableFuture()
                    .thenApply(Response::getResponseBody)
                    .thenAccept(jsonResponse -> parseFreelancers(jsonResponse, onFreelancerLoaded))
                    .join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseFreelancers(String jsonResponse, Consumer<Freelancer> onFreelancerLoaded) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray freelancers = jsonObject.getJSONArray("freelancers");

            for (int i = 0; i < freelancers.length(); i++) {
                JSONObject obj = freelancers.getJSONObject(i);

                Freelancer freelancer = new Freelancer(
                        obj.getString("name"),
                        obj.getString("hourRating"),
                        obj.getString("reviews"),
                        obj.getString("earnings"),
                        obj.getString("stars"),
                        obj.getString("skills"),
                        obj.getString("bio"),
                        obj.getString("freelancerProfile")
                );

                // Pass each freelancer to the callback
                onFreelancerLoaded.accept(freelancer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
