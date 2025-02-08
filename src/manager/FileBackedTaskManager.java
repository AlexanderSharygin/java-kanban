package manager;

import exception.ManagerLoadException;
import task.Epic;
import task.SubTask;
import task.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final List<String> HEADER_COLUMNS = List.of("id", "type", "name", "status", "description", "epic");
    private final String filePath;

    public FileBackedTaskManager(String filePath) throws IOException {
        super();
        this.filePath = filePath;
        Path path = Paths.get(filePath);
        if (Files.notExists(path)) {
            Files.createFile(path);
        }
    }


    public void loadFromFile() {
        File file = new File(filePath);
        try (Reader fileReader = new FileReader(file.getAbsolutePath(), StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            while (bufferedReader.ready()) {
                String taskString = bufferedReader.readLine();
                if (taskString.startsWith("id")) {
                    continue;
                }
                Task task = Converter.taskFromString(taskString);
                if (task instanceof SubTask) {
                    addSubTask((SubTask) task);
                } else if (task instanceof Epic) {
                    addEpic((Epic) task);
                } else {
                    addTask(task);
                }

            }
            //   isSourceFileLocked = false;
        } catch (IOException exception) {
            throw new ManagerLoadException("Что то пошло не так во время чтения файла");
        }
    }
}
