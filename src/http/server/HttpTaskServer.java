package http.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import http.adapters.DurationAdapter;
import http.adapters.LocalDateTimeAdapter;
import http.handlers.*;
import manager.FileBackedTaskManager;
import manager.TaskManager;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;


public class HttpTaskServer {
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    public static Gson gson;
    private static HttpServer httpServer;
    public static TaskManager taskManager;


    public static void main(String[] args) {
        String filePath = "resources/loadTest.csv";
        File file = new File(filePath);
        FileBackedTaskManager taskManager = FileBackedTaskManager.loadFromFile(file);
        startServer(taskManager);
    }

    public static void startServer(TaskManager manager) {
        try {
            httpServer = HttpServer.create();
            httpServer.bind(new InetSocketAddress(8080), 0);
            httpServer.start();
            taskManager = manager;
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.serializeNulls();
            gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
            gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
            gson = gsonBuilder.create();
            httpServer.createContext("/tasks", new TasksHandler());
            httpServer.createContext("/epics", new EpicsHandler());
            httpServer.createContext("/subtasks", new SubTasksHandler());
            httpServer.createContext("/prioritized", new PrioritizedHandler());
            httpServer.createContext("/history", new HistoryHandler());
        } catch (IOException exc) {
            throw new RuntimeException(exc.getMessage());
        }
    }

    public static void stop() {
        httpServer.stop(0);
    }
}

