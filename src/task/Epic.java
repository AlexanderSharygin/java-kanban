package task;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import static task.TaskType.EPIC;

public class Epic extends Task {

    protected LocalDateTime EndTime;
    private final List<Integer> subtasksId;

    public List<Integer> getSubtasksId() {
        return subtasksId;
    }

    public void clearSubtasksId() {
        subtasksId.clear();
        status = TaskStatus.NEW;
    }

    public Epic(String name, String description) {
        super(name, TaskStatus.NEW, description, 0, LocalDateTime.MAX.atZone(ZoneId.systemDefault()));
        subtasksId = new ArrayList<>();
    }

    public Epic(String name, String description, TaskStatus status) {
        super(name, status, description, 0, LocalDateTime.MAX.atZone(ZoneId.systemDefault()));
        subtasksId = new ArrayList<>();
    }

    public void addSubtask(SubTask subtask) {
        if (!subtasksId.contains(subtask.getId())) {
            subtasksId.add(subtask.getId());
        }
    }

    public LocalDateTime getEndTime(String timeZoneName) {
        TimeZone tz = TimeZone.getTimeZone(timeZoneName);
        int hours = tz.getRawOffset() / MILLISECONDS_IN_HOUR;
        return getUtcEndTime().plusHours(hours);
    }

    public void setEndTime(LocalDateTime endTime) {
        EndTime = endTime;
    }

    public LocalDateTime getUtcEndTime() {
        return EndTime;
    }

    @Override
    public String toString() {
        return String.join(",", id.toString(), EPIC.toString(), name, status.toString(), description,
                startTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), String.valueOf(duration.getSeconds() / 60));
    }
}