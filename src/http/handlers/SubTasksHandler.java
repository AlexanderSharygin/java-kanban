package http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.ManagerSaveException;
import exception.TaskIsIntersectingException;
import task.SubTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.NoSuchElementException;

import static http.server.HttpTaskServer.*;


public class SubTasksHandler extends BaseHttpHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String[] pathData = httpExchange.getRequestURI().getPath().split("/");
        int length = pathData.length;
        String query = httpExchange.getRequestURI().getQuery();
        try {
            if (httpExchange.getRequestMethod().equals("GET")) {
                if (pathData[length - 1].equals("subtasks") && query == null) {
                    mapGetSubtasksQueryToGetAllSubtasksMethods(httpExchange);
                } else if (pathData[length - 1].equals("subtasks") && query.contains("id")) {
                    mapGetSubTaskIdQueryToGetSubTaskByIdMethods(httpExchange);
                } else {
                    sendBadRequest(httpExchange);
                }
            } else if (httpExchange.getRequestMethod().equals("POST")) {
                if (pathData[length - 1].equals("subtasks") && query == null) {
                    mapPostSubTasksQueryToAddSubTaskMethods(httpExchange);
                } else if (pathData[length - 1].equals("subtasks") && query.contains("id")) {
                    mapPostSubTasksQueryToUpdateSubTaskMethods(httpExchange);
                } else {
                    sendBadRequest(httpExchange);
                }
            } else if (httpExchange.getRequestMethod().equals("DELETE")) {
                if (pathData[length - 1].equals("subtasks") && query.contains("id")) {
                    mapDeleteTasksSubTaskIdQueryToDeleteSubTaskByIdMethods(httpExchange);
                } else {
                    sendBadRequest(httpExchange);
                }
            }
        } catch (UncheckedIOException exception) {
            sendException(httpExchange);
        }
    }

    private void mapGetSubtasksQueryToGetAllSubtasksMethods(HttpExchange httpExchange) {
        try {
            String subtasks = gson.toJson(taskManager.getSubTasks());
            sendText(httpExchange, subtasks);
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    private void mapGetSubTaskIdQueryToGetSubTaskByIdMethods(HttpExchange httpExchange) {
        try {
            final int id = Integer.parseInt(httpExchange.getRequestURI().getQuery().split("=")[1]);
            try {
                String subtask = gson.toJson(taskManager.getSubTaskById(id));
                sendText(httpExchange, subtask);
            } catch (NoSuchElementException exception) {
                sendNotFound(httpExchange);
            }
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    private void mapPostSubTasksQueryToAddSubTaskMethods(HttpExchange httpExchange) {
        try {
            InputStream inputStream = httpExchange.getRequestBody();
            SubTask subTask = null;
            try {
                String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                subTask = gson.fromJson(body, SubTask.class);
            } catch (RuntimeException exception) {
                sendBadRequest(httpExchange);
            }
            try {
                taskManager.addSubTask(subTask);
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

    private void mapPostSubTasksQueryToUpdateSubTaskMethods(HttpExchange httpExchange) {
        try {
            InputStream inputStream = httpExchange.getRequestBody();
            final int id = Integer.parseInt(httpExchange.getRequestURI().getQuery().split("=")[1]);
            SubTask subTask = null;
            try {
                String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                subTask = gson.fromJson(body, SubTask.class);
                subTask.setId(id);
            } catch (RuntimeException exception) {
                sendBadRequest(httpExchange);
            }
            try {
                taskManager.updateSubtask(subTask);
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

    private void mapDeleteTasksSubTaskIdQueryToDeleteSubTaskByIdMethods(HttpExchange httpExchange) {
        try {
            final int id = Integer.parseInt(httpExchange.getRequestURI().getQuery().split("=")[1]);
            try {
                taskManager.removeSubTaskById(id);
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
