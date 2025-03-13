package http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.ManagerSaveException;
import exception.TaskIsIntersectingException;
import task.Epic;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import static http.server.HttpTaskServer.*;
import static task.TaskStatus.NEW;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String[] pathData = httpExchange.getRequestURI().getPath().split("/");
        int length = pathData.length;
        String query = httpExchange.getRequestURI().getQuery();
        try {
            if (httpExchange.getRequestMethod().equals("GET")) {
                if (pathData[length - 1].equals("epics") && query == null) {
                    mapGetEpicsQueryToGetAllEpicsMethods(httpExchange);
                } else if (pathData[length - 1].equals("epics") && query.contains("id")) {
                    mapGetEpicIdQueryToGetEpicByIdMethods(httpExchange);
                } else if (pathData[length - 2].equals("epics") && pathData[length - 1]
                        .equals("subtasks") && query.contains("id")) {
                    mapGetTasksSubtaskEpicIdQueryToGetSubtasksByEpicIdMethods(httpExchange);
                } else {
                    sendBadRequest(httpExchange);
                }
            } else if (httpExchange.getRequestMethod().equals("POST")) {
                if (pathData[length - 1].equals("epics")) {
                    mapPostEpicQueryToAddEpicMethods(httpExchange);
                } else {
                    sendBadRequest(httpExchange);
                }
            } else if (httpExchange.getRequestMethod().equals("DELETE")) {
                if (pathData[length - 1].equals("epics") && query.contains("id")) {
                    mapDeleteEpicIdQueryToDeleteEpicByIdMethods(httpExchange);
                } else {
                    sendBadRequest(httpExchange);
                }
            }
        } catch (UncheckedIOException exception) {
            sendException(httpExchange);
        }
    }


    private void mapGetEpicsQueryToGetAllEpicsMethods(HttpExchange httpExchange) {
        try {
            String epics = gson.toJson(taskManager.getEpics());
            sendText(httpExchange, epics);
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    private void mapGetEpicIdQueryToGetEpicByIdMethods(HttpExchange httpExchange) {
        try {
            final int id = Integer.parseInt(httpExchange.getRequestURI().getQuery().split("=")[1]);
            try {
                String epic = gson.toJson(taskManager.getEpicById(id));
                sendText(httpExchange, epic);
            } catch (NoSuchElementException exception) {
                sendNotFound(httpExchange);
            }
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    private void mapGetTasksSubtaskEpicIdQueryToGetSubtasksByEpicIdMethods(HttpExchange httpExchange) {
        try {
            final int id = Integer.parseInt(httpExchange.getRequestURI().getQuery().split("=")[1]);
            try {
                String epicSubtasks = gson.toJson(taskManager.getSubTasksByEpicId(id));
                sendText(httpExchange, epicSubtasks);
            } catch (NoSuchElementException exception) {
                sendNotFound(httpExchange);
            }
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    private void mapPostEpicQueryToAddEpicMethods(HttpExchange httpExchange) {
        try {
            InputStream inputStream = httpExchange.getRequestBody();
            Epic epic = null;
            try {
                String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                epic = gson.fromJson(body, Epic.class);
            } catch (RuntimeException exception) {
                sendBadRequest(httpExchange);
            }
            epic.setStatus(NEW);
            epic.setDuration(Duration.ZERO);
            epic.setStartTime(LocalDateTime.MAX);
            try {
                taskManager.addEpic(epic);
                sendItemAddedOrUpdated(httpExchange);
            } catch (TaskIsIntersectingException ex) {
                sendIntersection(httpExchange);
            } catch (ManagerSaveException e) {
                sendException(httpExchange);
            }
        } catch (IOException exc) {
            throw new UncheckedIOException(exc);
        }
    }

    private void mapDeleteEpicIdQueryToDeleteEpicByIdMethods(HttpExchange httpExchange) {
        try {
            final int id = Integer.parseInt(httpExchange.getRequestURI().getQuery().split("=")[1]);
            try {
                taskManager.removeEpicById(id);
                sendSuccess(httpExchange);
            } catch (NoSuchElementException ex) {
                sendNotFound(httpExchange);
            } catch (ManagerSaveException e) {
                sendException(httpExchange);
            }
        } catch (IOException exc) {
            throw new UncheckedIOException(exc);
        }
    }
}