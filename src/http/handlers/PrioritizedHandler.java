package http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.UncheckedIOException;

import static http.server.HttpTaskServer.gson;
import static http.server.HttpTaskServer.taskManager;


public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String[] pathData = httpExchange.getRequestURI().getPath().split("/");
        int length = pathData.length;
        try {
            if (httpExchange.getRequestMethod().equals("GET")) {
                if (pathData[length - 1].equals("prioritized")) {
                    mapGetPrioritizedQueryToGetPrioritizedMethods(httpExchange);
                } else {
                    sendBadRequest(httpExchange);
                }
            }
        } catch (UncheckedIOException exception) {
            sendException(httpExchange);
        }
    }

    private void mapGetPrioritizedQueryToGetPrioritizedMethods(HttpExchange httpExchange) {
        try {
            String tasks = gson.toJson(taskManager.getPrioritizedTasks());
            sendText(httpExchange, tasks);
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }
}

