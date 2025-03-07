package org.example.pifinal.Utils;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.asynchttpclient.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class FreelancerProxyServer {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(9090), 0);
        server.createContext("/freelancers", new FreelancerHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Server running on http://localhost:9090");
    }

    static class FreelancerHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                handleOptionsRequest(exchange);
                return;
            }

            try {
                String response = fetchFreelancersFromAPI(); // Call the real API!

                exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
                exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");

                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
                String errorResponse = "{\"error\": \"Failed to fetch freelancers\"}";
                exchange.sendResponseHeaders(500, errorResponse.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(errorResponse.getBytes());
                os.close();
            }
        }

        private void handleOptionsRequest(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
            exchange.sendResponseHeaders(204, -1); // No content for OPTIONS request
        }

        private String fetchFreelancersFromAPI() throws ExecutionException, InterruptedException {
            try (AsyncHttpClient client = new DefaultAsyncHttpClient()) {
                CompletableFuture<Response> futureResponse = client.prepare("GET", "https://freelancer-api.p.rapidapi.com/api/find-freelancers")
                        .setHeader("x-rapidapi-key", "0751871cebmsh7e5e6ff89395271p1d078fjsn6e25875a423b")
                        .setHeader("x-rapidapi-host", "freelancer-api.p.rapidapi.com")
                        .execute()
                        .toCompletableFuture();

                Response response = futureResponse.get();
                return response.getResponseBody();
            } catch (Exception e) {
                e.printStackTrace();
                return "{\"error\": \"Failed to fetch data from API\"}";
            }
        }
    }
}
