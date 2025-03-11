import http.server.HttpTaskServer;
import manager.FileBackedTaskManager;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpTaskManagerTests {
    @Test
    public void getTaskByIdTest() {
        String filePath = "resources/loadTest.csv";
        File file = new File(filePath);
        FileBackedTaskManager taskManager = FileBackedTaskManager.loadFromFile(file);
        HttpTaskServer.startServer(taskManager);
        URI uri = URI.create("http://localhost:8080/tasks?id=1");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            try {
                response = client.send(request, handler);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Тело ответа: " + response.body());
    }


}
