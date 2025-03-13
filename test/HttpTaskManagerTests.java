import com.google.gson.*;
import history.manager.Managers;
import http.adapters.DurationAdapter;
import http.adapters.LocalDateTimeAdapter;
import http.server.HttpTaskServer;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

import static java.time.ZoneId.of;
import static java.time.format.DateTimeFormatter.ofPattern;
import static task.TaskStatus.IN_PROGRESS;

public class HttpTaskManagerTests {

    private TaskManager taskManager;
    private Task task;
    private Task task2;
    private ZonedDateTime now;
    private SubTask subTask1;
    private SubTask subTask2;
    private Epic epic;
    private Epic epic2;

    @BeforeEach
    public void setUp() {
        now = ZonedDateTime.now();
        taskManager = Managers.getDefault();
        task = new Task("New", TaskStatus.NEW, "Desc", 32, now);
        task2 = new Task("New2", TaskStatus.DONE, "Desc2", 33, now.plusHours(1));
        epic = new Epic("EpicOne", "one");
        epic2 = new Epic("EpicTwo", "two");
        subTask1 = new SubTask("ST1", IN_PROGRESS, "one", 1, now.plusHours(3), 1);
        subTask2 = new SubTask("ST2", IN_PROGRESS, "two", 2, now.plusHours(4), 1);
        HttpTaskServer.startServer(taskManager);
    }

    @AfterEach
    public void tearDown() {
        HttpTaskServer.stop();
    }

    @Test
    public void getEpicWithoutSubtasksByCorrectIdTest() {
        taskManager.addEpic(epic);

        URI uri = URI.create("http://localhost:8080/epics?id=1");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).version(HttpClient.Version.HTTP_1_1).build();
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            try {
                response = client.send(request, handler);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String taskName = jsonObject.get("name").getAsString();
        String description = jsonObject.get("description").getAsString();
        Integer duration = jsonObject.get("duration").getAsInt();
        Assertions.assertEquals(response.statusCode(), 200);
        Assertions.assertEquals(taskName, "EpicOne");
        Assertions.assertEquals(description, "one");
        Assertions.assertEquals(duration, 0);
    }

    @Test
    public void getEpicWithSubtasksByCorrectIdTest() {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        URI uri = URI.create("http://localhost:8080/epics?id=1");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).version(HttpClient.Version.HTTP_1_1).build();
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            try {
                response = client.send(request, handler);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String taskName = jsonObject.get("name").getAsString();
        String description = jsonObject.get("description").getAsString();
        String startTime = jsonObject.get("startTime").getAsString();
        String endTime = jsonObject.get("endTime").getAsString();
        Integer duration = jsonObject.get("duration").getAsInt();
        Assertions.assertEquals(response.statusCode(), 200);
        Assertions.assertEquals(taskName, "EpicOne");
        Assertions.assertEquals(description, "one");
        Assertions.assertEquals(duration, 62);
        Assertions.assertEquals(startTime,
                now.plusHours(3).withZoneSameInstant(of("UTC")).format(ofPattern("dd.MM.yyy HH:mm:ss")));
        Assertions.assertEquals(endTime, now.plusHours(4).plusMinutes(2)
                .withZoneSameInstant(of("UTC")).format(ofPattern("dd.MM.yyy HH:mm:ss")));
    }

    @Test
    public void getEpicByWrongIdTest() {
        URI uri = URI.create("http://localhost:8080/epics?id=1");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).version(HttpClient.Version.HTTP_1_1).build();
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            try {
                response = client.send(request, handler);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Assertions.assertEquals(response.statusCode(), 404);
    }

    @Test
    public void getSubTaskByCorrectIdTest() {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTask1);
        URI uri = URI.create("http://localhost:8080/subtasks?id=2");
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
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String taskName = jsonObject.get("name").getAsString();
        String description = jsonObject.get("description").getAsString();
        String dateTime = jsonObject.get("startTime").getAsString();
        Integer duration = jsonObject.get("duration").getAsInt();
        Assertions.assertEquals(response.statusCode(), 200);
        Assertions.assertEquals(taskName, "ST1");
        Assertions.assertEquals(description, "one");
        Assertions.assertEquals(dateTime,
                now.plusHours(3).withZoneSameInstant(of("UTC")).format(ofPattern("dd.MM.yyy HH:mm:ss")));
        Assertions.assertEquals(duration, 1);
    }

    @Test
    public void getEpicsNotEmptyListTest() {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.addEpic(epic2);
        URI uri = URI.create("http://localhost:8080/epics");
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
        JsonElement jsonElement = JsonParser.parseString(response.body());
        List<JsonElement> jsonObjects = jsonElement.getAsJsonArray().asList();
        String taskName = jsonObjects.get(0).getAsJsonObject().get("name").getAsString();
        String description = jsonObjects.get(0).getAsJsonObject().get("description").getAsString();
        Assertions.assertEquals(response.statusCode(), 200);
        Assertions.assertEquals(taskName, "EpicOne");
        Assertions.assertEquals(description, "one");
        taskName = jsonObjects.get(1).getAsJsonObject().get("name").getAsString();
        description = jsonObjects.get(1).getAsJsonObject().get("description").getAsString();
        Assertions.assertEquals(taskName, "EpicTwo");
        Assertions.assertEquals(description, "two");
        Assertions.assertEquals(jsonObjects.size(), 2);
    }

    @Test
    public void getEpicsEmptyListTest() {
        URI uri = URI.create("http://localhost:8080/epics");
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
        JsonElement jsonElement = JsonParser.parseString(response.body());
        List<JsonElement> jsonObjects = jsonElement.getAsJsonArray().asList();
        Assertions.assertEquals(jsonObjects.size(), 0);
    }

    @Test
    public void deleteEpicTasksCorrectIdTest() {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTask1);
        URI uri = URI.create("http://localhost:8080/epics?id=1");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            try {
                response = client.send(request, handler);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Assertions.assertEquals(response.statusCode(), 200);
        Assertions.assertEquals(taskManager.getSubTasks().size(), 0);
        Assertions.assertEquals(taskManager.getEpics().size(), 0);
    }

    @Test
    public void deleteEpicTasksWrongIdTest() {
        URI uri = URI.create("http://localhost:8080/epics?id=1");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            try {
                response = client.send(request, handler);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Assertions.assertEquals(response.statusCode(), 404);
    }

    @Test
    public void addEpicTaskSuccessTest() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        Gson gson = gsonBuilder.create();
        String taskJson = gson.toJson(epic);
        URI uri = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            try {
                response = client.send(request, handler);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Assertions.assertEquals(response.statusCode(), 201);
        Assertions.assertEquals(taskManager.getEpics().get(0).getName(), "EpicOne");
        Assertions.assertEquals(taskManager.getEpics().get(0).getDescription(), "one");
        Assertions.assertEquals(taskManager.getEpics().get(0).getDuration().toMinutes(), 0);
    }


    @Test
    public void getSubTaskByWrongIdTest() {
        URI uri = URI.create("http://localhost:8080/subtasks?id=1");
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
        Assertions.assertEquals(response.statusCode(), 404);
    }

    @Test
    public void getSubTasksNotEmptyListTest() {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        URI uri = URI.create("http://localhost:8080/subtasks");
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
        JsonElement jsonElement = JsonParser.parseString(response.body());
        List<JsonElement> jsonObjects = jsonElement.getAsJsonArray().asList();
        String taskName = jsonObjects.get(0).getAsJsonObject().get("name").getAsString();
        String description = jsonObjects.get(0).getAsJsonObject().get("description").getAsString();
        String dateTime = jsonObjects.get(0).getAsJsonObject().get("startTime").getAsString();
        Integer duration = jsonObjects.get(0).getAsJsonObject().get("duration").getAsInt();
        Assertions.assertEquals(response.statusCode(), 200);
        Assertions.assertEquals(taskName, "ST1");
        Assertions.assertEquals(description, "one");
        Assertions.assertEquals(dateTime,
                now.plusHours(3).withZoneSameInstant(of("UTC")).format(ofPattern("dd.MM.yyy HH:mm:ss")));
        Assertions.assertEquals(duration, 1);
        taskName = jsonObjects.get(1).getAsJsonObject().get("name").getAsString();
        description = jsonObjects.get(1).getAsJsonObject().get("description").getAsString();
        dateTime = jsonObjects.get(1).getAsJsonObject().get("startTime").getAsString();
        duration = jsonObjects.get(1).getAsJsonObject().get("duration").getAsInt();
        Assertions.assertEquals(response.statusCode(), 200);
        Assertions.assertEquals(taskName, "ST2");
        Assertions.assertEquals(description, "two");
        Assertions.assertEquals(dateTime,
                now.plusHours(4).withZoneSameInstant(of("UTC")).format(ofPattern("dd.MM.yyy HH:mm:ss")));
        Assertions.assertEquals(duration, 2);
        Assertions.assertEquals(jsonObjects.size(), 2);
    }

    @Test
    public void getSubTasksEmptyListTest() {
        URI uri = URI.create("http://localhost:8080/subtasks");
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
        JsonElement jsonElement = JsonParser.parseString(response.body());
        List<JsonElement> jsonObjects = jsonElement.getAsJsonArray().asList();
        Assertions.assertEquals(response.statusCode(), 200);
        Assertions.assertEquals(jsonObjects.size(), 0);
    }

    @Test
    public void getSubTasksByEPicIdNotEmptyListTest() {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.addEpic(epic2);
        subTask2 = new SubTask("ST3", IN_PROGRESS, "three", 5, now.plusHours(5), 4);
        URI uri = URI.create("http://localhost:8080/epics/subtasks/?id=1");
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
        JsonElement jsonElement = JsonParser.parseString(response.body());
        List<JsonElement> jsonObjects = jsonElement.getAsJsonArray().asList();
        String taskName = jsonObjects.get(0).getAsJsonObject().get("name").getAsString();
        String description = jsonObjects.get(0).getAsJsonObject().get("description").getAsString();
        String dateTime = jsonObjects.get(0).getAsJsonObject().get("startTime").getAsString();
        Integer duration = jsonObjects.get(0).getAsJsonObject().get("duration").getAsInt();
        Assertions.assertEquals(response.statusCode(), 200);
        Assertions.assertEquals(taskName, "ST1");
        Assertions.assertEquals(description, "one");
        Assertions.assertEquals(dateTime,
                now.plusHours(3).withZoneSameInstant(of("UTC")).format(ofPattern("dd.MM.yyy HH:mm:ss")));
        Assertions.assertEquals(duration, 1);
        taskName = jsonObjects.get(1).getAsJsonObject().get("name").getAsString();
        description = jsonObjects.get(1).getAsJsonObject().get("description").getAsString();
        dateTime = jsonObjects.get(1).getAsJsonObject().get("startTime").getAsString();
        duration = jsonObjects.get(1).getAsJsonObject().get("duration").getAsInt();
        Assertions.assertEquals(taskName, "ST2");
        Assertions.assertEquals(description, "two");
        Assertions.assertEquals(dateTime,
                now.plusHours(4).withZoneSameInstant(of("UTC")).format(ofPattern("dd.MM.yyy HH:mm:ss")));
        Assertions.assertEquals(duration, 2);
        Assertions.assertEquals(jsonObjects.size(), 2);
    }

    @Test
    public void getSubTasksByEPicIdEmptyListTest() {
        taskManager.addEpic(epic);
        URI uri = URI.create("http://localhost:8080/epics/subtasks/?id=1");
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
        JsonElement jsonElement = JsonParser.parseString(response.body());
        List<JsonElement> jsonObjects = jsonElement.getAsJsonArray().asList();
        Assertions.assertEquals(response.statusCode(), 200);
        Assertions.assertEquals(jsonObjects.size(), 0);
    }

    @Test
    public void getSubTasksByEpicWrongIdTest() {
        URI uri = URI.create("http://localhost:8080/epics/subtasks/?id=1");
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
        Assertions.assertEquals(response.statusCode(), 404);
    }

    @Test
    public void deleteSubTasksCorrectIdTest() {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTask1);
        URI uri = URI.create("http://localhost:8080/subtasks?id=2");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            try {
                response = client.send(request, handler);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Assertions.assertEquals(response.statusCode(), 200);
        Assertions.assertEquals(taskManager.getSubTasks().size(), 0);
    }

    @Test
    public void deleteSubTasksWrongIdTest() {
        URI uri = URI.create("http://localhost:8080/subtasks?id=1");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            try {
                response = client.send(request, handler);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Assertions.assertEquals(response.statusCode(), 404);
    }

    @Test
    public void addSubTaskSuccessTest() {
        taskManager.addEpic(epic);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        Gson gson = gsonBuilder.create();
        String taskJson = gson.toJson(subTask1);
        URI uri = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            try {
                response = client.send(request, handler);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Assertions.assertEquals(response.statusCode(), 201);
        Assertions.assertEquals(taskManager.getSubTasks().get(0).getName(), "ST1");
        Assertions.assertEquals(taskManager.getSubTasks().get(0).getDescription(), "one");
        Assertions.assertEquals(taskManager.getSubTasks().get(0).getUtcStartTime().toString(),
                now.plusHours(3).withZoneSameInstant(of("UTC")).format(ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        Assertions.assertEquals(taskManager.getSubTasks().get(0).getDuration().toMinutes(), 1);
        Assertions.assertEquals(taskManager.getSubTasks().size(), 1);
    }


    @Test
    public void addSubTaskIntersectExceptionTest() {
        SubTask subTask2 = new SubTask("ST2", IN_PROGRESS, "two", 2, now.plusHours(3),
                1);
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTask1);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        Gson gson = gsonBuilder.create();
        String taskJson = gson.toJson(subTask2);
        URI uri = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            try {
                response = client.send(request, handler);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Assertions.assertEquals(response.statusCode(), 406);
    }


    @Test
    public void getTaskByCorrectIdTest() {
        taskManager.addTask(task);
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
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String taskName = jsonObject.get("name").getAsString();
        String description = jsonObject.get("description").getAsString();
        String dateTime = jsonObject.get("startTime").getAsString();
        Integer duration = jsonObject.get("duration").getAsInt();
        Assertions.assertEquals(response.statusCode(), 200);
        Assertions.assertEquals(taskName, "New");
        Assertions.assertEquals(description, "Desc");
        Assertions.assertEquals(dateTime,
                now.withZoneSameInstant(of("UTC")).format(ofPattern("dd.MM.yyy HH:mm:ss")));
        Assertions.assertEquals(duration, 32);
    }

    @Test
    public void getTaskByWrongIdTest() {
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
        Assertions.assertEquals(response.statusCode(), 404);
    }

    @Test
    public void getTasksNotEmptyListTest() {
        taskManager.addTask(task);
        taskManager.addTask(task2);
        URI uri = URI.create("http://localhost:8080/tasks");
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
        JsonElement jsonElement = JsonParser.parseString(response.body());
        List<JsonElement> jsonObjects = jsonElement.getAsJsonArray().asList();
        String taskName = jsonObjects.get(0).getAsJsonObject().get("name").getAsString();
        String description = jsonObjects.get(0).getAsJsonObject().get("description").getAsString();
        String dateTime = jsonObjects.get(0).getAsJsonObject().get("startTime").getAsString();
        Integer duration = jsonObjects.get(0).getAsJsonObject().get("duration").getAsInt();
        Assertions.assertEquals(response.statusCode(), 200);
        Assertions.assertEquals(taskName, "New");
        Assertions.assertEquals(description, "Desc");
        Assertions.assertEquals(dateTime,
                now.withZoneSameInstant(of("UTC")).format(ofPattern("dd.MM.yyy HH:mm:ss")));
        Assertions.assertEquals(duration, 32);
        taskName = jsonObjects.get(1).getAsJsonObject().get("name").getAsString();
        description = jsonObjects.get(1).getAsJsonObject().get("description").getAsString();
        dateTime = jsonObjects.get(1).getAsJsonObject().get("startTime").getAsString();
        duration = jsonObjects.get(1).getAsJsonObject().get("duration").getAsInt();
        Assertions.assertEquals(response.statusCode(), 200);
        Assertions.assertEquals(taskName, "New2");
        Assertions.assertEquals(description, "Desc2");
        Assertions.assertEquals(dateTime,
                now.plusHours(1).withZoneSameInstant(of("UTC")).format(ofPattern("dd.MM.yyy HH:mm:ss")));
        Assertions.assertEquals(duration, 33);
        Assertions.assertEquals(jsonObjects.size(), 2);
    }

    @Test
    public void getTasksEmptyListTest() {
        URI uri = URI.create("http://localhost:8080/tasks");
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
        JsonElement jsonElement = JsonParser.parseString(response.body());
        List<JsonElement> jsonObjects = jsonElement.getAsJsonArray().asList();
        Assertions.assertEquals(jsonObjects.size(), 0);
    }

    @Test
    public void deleteTasksCorrectIdTest() {
        taskManager.addTask(task);
        URI uri = URI.create("http://localhost:8080/tasks?id=1");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            try {
                response = client.send(request, handler);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Assertions.assertEquals(response.statusCode(), 200);
        Assertions.assertEquals(taskManager.getTasks().size(), 0);
    }

    @Test
    public void deleteTasksWrongIdTest() {
        URI uri = URI.create("http://localhost:8080/tasks?id=1");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            try {
                response = client.send(request, handler);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Assertions.assertEquals(response.statusCode(), 404);
    }

    @Test
    public void addTasksSuccessTest() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        Gson gson = gsonBuilder.create();
        String taskJson = gson.toJson(task);
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            try {
                response = client.send(request, handler);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Assertions.assertEquals(response.statusCode(), 201);
        Assertions.assertEquals(taskManager.getTasks().get(0).getName(), "New");
        Assertions.assertEquals(taskManager.getTasks().get(0).getDescription(), "Desc");
        Assertions.assertEquals(taskManager.getTasks().get(0).getUtcStartTime().toString(),
                now.withZoneSameInstant(of("UTC")).format(ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        Assertions.assertEquals(taskManager.getTasks().get(0).getDuration().toMinutes(), 32);
        Assertions.assertEquals(taskManager.getTasks().size(), 1);
    }


    @Test
    public void addTasksIntersectExceptionTest() {
        Task task2 = new Task("New", TaskStatus.NEW, "Desc", 32, now);
        taskManager.addTask(task);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        Gson gson = gsonBuilder.create();
        String taskJson = gson.toJson(task2);
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            try {
                response = client.send(request, handler);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Assertions.assertEquals(response.statusCode(), 406);
    }

    @Test
    public void getNotEmptyHistoryTest() {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.addTask(task);
        taskManager.getTaskById(4);
        taskManager.getSubTaskById(2);
        taskManager.getSubTaskById(3);
        taskManager.getEpicById(1);
        URI uri = URI.create("http://localhost:8080/history");
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
        JsonElement jsonElement = JsonParser.parseString(response.body());
        List<JsonElement> jsonObjects = jsonElement.getAsJsonArray().asList();
        String id = jsonObjects.get(0).getAsJsonObject().get("id").getAsString();
        Assertions.assertEquals(id, "4");
        id = jsonObjects.get(1).getAsJsonObject().get("id").getAsString();
        Assertions.assertEquals(id, "2");
        id = jsonObjects.get(2).getAsJsonObject().get("id").getAsString();
        Assertions.assertEquals(id, "3");
        id = jsonObjects.get(3).getAsJsonObject().get("id").getAsString();
        Assertions.assertEquals(id, "1");
        Assertions.assertEquals(jsonObjects.size(), 4);
        Assertions.assertEquals(response.statusCode(), 200);
    }

    @Test
    public void getEmptyHistoryTest() {
        URI uri = URI.create("http://localhost:8080/history");
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
        JsonElement jsonElement = JsonParser.parseString(response.body());
        List<JsonElement> jsonObjects = jsonElement.getAsJsonArray().asList();
        Assertions.assertEquals(jsonObjects.size(), 0);
        Assertions.assertEquals(response.statusCode(), 200);
    }

    @Test
    public void getNotEmptyPrioritizedTest() {
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.addTask(task);
        taskManager.getTaskById(4);
        taskManager.getSubTaskById(2);
        taskManager.getSubTaskById(3);
        taskManager.getEpicById(1);
        URI uri = URI.create("http://localhost:8080/prioritized");
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
        JsonElement jsonElement = JsonParser.parseString(response.body());
        List<JsonElement> jsonObjects = jsonElement.getAsJsonArray().asList();
        String id = jsonObjects.get(0).getAsJsonObject().get("id").getAsString();
        Assertions.assertEquals(id, "4");
        id = jsonObjects.get(1).getAsJsonObject().get("id").getAsString();
        Assertions.assertEquals(id, "2");
        id = jsonObjects.get(2).getAsJsonObject().get("id").getAsString();
        Assertions.assertEquals(id, "3");
        Assertions.assertEquals(jsonObjects.size(), 3);
        Assertions.assertEquals(response.statusCode(), 200);
    }

    @Test
    public void getEmptyPrioritizedTest() {
        URI uri = URI.create("http://localhost:8080/prioritized");
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
        JsonElement jsonElement = JsonParser.parseString(response.body());
        List<JsonElement> jsonObjects = jsonElement.getAsJsonArray().asList();
        Assertions.assertEquals(jsonObjects.size(), 0);
        Assertions.assertEquals(response.statusCode(), 200);
    }

    @Test
    public void wrongUrlTest() {
        URI uri = URI.create("http://localhost:8080/test");
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
        Assertions.assertEquals(response.statusCode(), 404);
    }
}
