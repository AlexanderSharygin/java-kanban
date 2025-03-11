package http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.ManagerSaveException;
import exception.TaskIsIntersectingException;
import task.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.NoSuchElementException;

import static http.server.HttpTaskServer.*;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String[] pathData = httpExchange.getRequestURI().getPath().split("/");
        int length = pathData.length;
        String query = httpExchange.getRequestURI().getQuery();
        try {
            if (httpExchange.getRequestMethod().equals("GET")) {
                if (pathData[length - 1].equals("tasks") && query == null) {
                    mapGetTasksTaskQueryToGetAllTasksMethods(httpExchange);
                } else if (pathData[length - 1].equals("tasks") && query.contains("id")) {
                    mapGetTasksTaskIdQueryToGetTaskByIdMethods(httpExchange);
                } else {
                    sendBadRequest(httpExchange);
                }
            } else if (httpExchange.getRequestMethod().equals("POST")) {
                if (pathData[length - 1].equals("tasks") && query == null) {
                    mapPostTasksQueryToAddTaskMethods(httpExchange);
                } else if (pathData[length - 1].equals("tasks") && query.contains("id")) {
                    mapPostTasksQueryToUpdateTaskMethods(httpExchange);
                } else {
                    sendBadRequest(httpExchange);
                }
            } else if (httpExchange.getRequestMethod().equals("DELETE")) {
                if (pathData[length - 1].equals("tasks") && query.contains("id")) {
                    mapDeleteTaskIdQueryToDeleteTaskByIdMethods(httpExchange);
                } else {
                    sendBadRequest(httpExchange);
                }
            }
        } catch (UncheckedIOException exception) {
            sendException(httpExchange);
        }
    }

    private void mapGetTasksTaskQueryToGetAllTasksMethods(HttpExchange httpExchange) {
        try {
            String tasks = gson.toJson(taskManager.getTasks());
            sendText(httpExchange, tasks);
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    private void mapGetTasksTaskIdQueryToGetTaskByIdMethods(HttpExchange httpExchange) {
        try {
            final int id = Integer.parseInt(httpExchange.getRequestURI().getQuery().split("=")[1]);
            try {
                String task = gson.toJson(taskManager.getTaskById(id));
                sendText(httpExchange, task);
            } catch (NoSuchElementException exception) {
                sendNotFound(httpExchange);
            }
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    private void mapPostTasksQueryToAddTaskMethods(HttpExchange httpExchange) {
        try {
            Task task = null;
            InputStream inputStream = httpExchange.getRequestBody();
            try {
                String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                task = gson.fromJson(body, Task.class);
            } catch (RuntimeException exception) {
                sendBadRequest(httpExchange);
            }
            try {
                taskManager.addTask(task);
                sendItemAddedOrUpdated(httpExchange);
            } catch (TaskIsIntersectingException e) {
                sendIntersection(httpExchange);
            } catch (ManagerSaveException e) {
                sendException(httpExchange);
            }
        } catch (IOException exc) {
            throw new UncheckedIOException(exc);
        }
    }


    private void mapPostTasksQueryToUpdateTaskMethods(HttpExchange httpExchange) {
        try {
            Task task = null;
            InputStream inputStream = httpExchange.getRequestBody();
            final int id = Integer.parseInt(httpExchange.getRequestURI().getQuery().split("=")[1]);
            try {
                String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                task = gson.fromJson(body, Task.class);
                task.setId(id);
            } catch (RuntimeException exception) {
                sendBadRequest(httpExchange);
            }
            try {
                taskManager.updateTask(task);
                sendItemAddedOrUpdated(httpExchange);
            } catch (TaskIsIntersectingException e) {
                sendIntersection(httpExchange);
            } catch (ManagerSaveException e) {
                sendException(httpExchange);
            }
        } catch (IOException exc) {
            throw new UncheckedIOException(exc);
        }
    }

    private void mapDeleteTaskIdQueryToDeleteTaskByIdMethods(HttpExchange httpExchange) {
        try {
            final int id = Integer.parseInt(httpExchange.getRequestURI().getQuery().split("=")[1]);
            try {
                taskManager.removeTaskById(id);
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