package mihailop13.plugins.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public final class AIService {

    private final HttpClient httpClient;
    private final String apiKey = System.getenv("GROQ_API_KEY");
    private final String apiURL = System.getenv("GROQ_API_URL");

    public AIService() {
        this.httpClient = HttpClient.newBuilder().build();
    }

    public CompletableFuture<String> sendMessage(String fullPrompt) {
        String safePrompt = fullPrompt
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");

        String jsonPayload = "{\n" +
                "  \"model\": \"llama-3.3-70b-versatile\",\n" +
                "  \"messages\": [{\"role\": \"user\", \"content\": \"" + safePrompt + "\"}]\n" +
                "}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiURL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload, java.nio.charset.StandardCharsets.UTF_8))
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() == 200) {
                        String body = response.body();
                        int start = body.indexOf("\"content\":\"") + 11;
                        int end = body.indexOf("\"}", start);
                        String content = body.substring(start, end);

                        return content.replace("\\n", "\n").replace("\\\"", "\"");
                    } else {
                        return "Error: " + response.statusCode() + " - " + response.body();
                    }
                });
    }
}
