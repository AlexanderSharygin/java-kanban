package manager;

import exception.CreateFileException;
import exception.ManagerLoadException;
import exception.ManagerSaveException;
import task.Epic;
import task.SubTask;
import task.Task;
import utils.TasksConverter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final List<String> HEADER_COLUMNS = List.of("id", "type", "name", "status", "description", "epic");
    private final String filePath;

    public FileBackedTaskManager(String filePath) {
        super();
        this.filePath = filePath;
        Path path = Paths.get(filePath);
        if (Files.notExists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                throw new CreateFileException("Невозможно создать новый файл");
            }
        }
    }

    @Override
    public List<Task> getTasks() {
        return super.getTasks();
    }

    @Override
    public List<Epic> getEpics() {
        return super.getEpics();
    }

    @Override
    public List<SubTask> getSubTasks() {
        return super.getSubTasks();
    }

    @Override
    public List<SubTask> getSubTasksByEpicId(int epicId) {
        return super.getSubTasksByEpicId(epicId);
    }

    @Override
    public Task getTaskById(int id) {
        return super.getTaskById(id);
    }

    @Override
    public Epic getEpicById(int id) {
        return super.getEpicById(id);
    }

    @Override
    public SubTask getSubTaskById(int id) {
        return super.getSubTaskById(id);
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubTask(SubTask subtask) {
        super.addSubTask(subtask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(SubTask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void removeAllSubTasks() {
        super.removeAllSubTasks();
        save();
    }

    @Override
    public void removeSubTaskById(Integer id) {
        super.removeSubTaskById(id);
        save();
    }

    @Override
    public void removeTaskById(Integer id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeEpicById(Integer id) {
        super.removeEpicById(id);
        save();
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
                Task task = TasksConverter.taskFromString(taskString);
                if (task != null) {
                    if (task instanceof SubTask) {
                        addSubTask((SubTask) task);
                    } else if (task instanceof Epic) {
                        addEpic((Epic) task);
                    } else {
                        addTask(task);
                    }
                }
            }
        } catch (IOException exception) {
            throw new ManagerSaveException("Что то пошло не так во время чтения файла");
        }
    }

    private void save() {
        try (Writer fileWriter = new FileWriter(filePath, StandardCharsets.UTF_8);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            String header = String.join(",", HEADER_COLUMNS);
            bufferedWriter.write(header);
            bufferedWriter.newLine();
            for (var task : tasks.values()) {
                bufferedWriter.write(task.toString());
                bufferedWriter.newLine();
            }
            for (var epic : epics.values()) {
                bufferedWriter.write(epic.toString());
                bufferedWriter.newLine();
            }
            for (var subtask : subTasks.values()) {
                bufferedWriter.write(subtask.toString());
                bufferedWriter.newLine();
            }
        } catch (IOException exception) {
            throw new ManagerLoadException("Что то пошло не так во время записи файла");
        }

    }
}