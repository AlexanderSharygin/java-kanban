package task;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static task.TaskType.SUBTASK;

public class SubTask extends Task {

    private Integer epicId;

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public SubTask(String name, TaskStatus status, String description, int durationInMinutes,
                   ZonedDateTime startTime, int epicId) {
        super(name, status, description, durationInMinutes, startTime);
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return String.join(",", id.toString(), SUBTASK.toString(), name, status.toString(), description,
                startTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), String.valueOf(duration.getSeconds() / 60),
                String.valueOf(epicId));
    }
}