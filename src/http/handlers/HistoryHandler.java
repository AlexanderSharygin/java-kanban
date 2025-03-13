package http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.UncheckedIOException;

import static http.server.HttpTaskServer.gson;
import static http.server.HttpTaskServer.taskManager;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String[] pathData = httpExchange.getRequestURI().getPath().split("/");
        int length = pathData.length;
        try {
            if (httpExchange.getRequestMethod().equals("GET")) {
                if (pathData[length - 1].equals("history")) {
                    mapGetTasksHistoryQueryToGetHistoryMethods(httpExchange);
                } else {
                    sendBadRequest(httpExchange);
                }
            }
        } catch (UncheckedIOException exception) {
            sendException(httpExchange);
        }
    }


    private void mapGetTasksHistoryQueryToGetHistoryMethods(HttpExchange httpExchange) {
        try {
            String history = gson.toJson(taskManager.getHistory());
            sendText(httpExchange, history);
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }


}
