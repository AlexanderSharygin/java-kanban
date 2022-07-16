package com.taskTracker.taskMmanager;

import com.taskTracker.historyManager.HistoryManager;
import com.taskTracker.model.Epic;
import com.taskTracker.model.SubTask;
import com.taskTracker.model.Task;

import java.util.List;

public interface TaskManger {

    List<Task> getTasks();

    List<Epic> getEpics();

    List<SubTask> getSubTasks();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    SubTask getSubTaskById(int id);

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubTask(SubTask subtask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(SubTask subtask);

    void removeSubTaskById(int id);

    void removeTaskById(int id);

    void removeEpicById(int id);

    List<SubTask> getEpicSubtasks(int epicId);

    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubTasks();

    HistoryManager getHistoryManager();

    void setHistoryManager(HistoryManager historyManager);

    List<Task> getHistory();
}