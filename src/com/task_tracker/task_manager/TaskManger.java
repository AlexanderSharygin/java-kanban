package com.task_tracker.task_manager;

import com.task_tracker.history_manager.HistoryManager;
import com.task_tracker.model.Epic;
import com.task_tracker.model.SubTask;
import com.task_tracker.model.Task;

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