package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.TimeZone;

import static task.TaskType.TASK;

public class Task implements Cloneable {

    protected static final int MILLISECONDS_IN_HOUR = 1000 * 60 * 60;

    protected Integer id;
    protected String name;
    protected String description;
    protected TaskStatus status;
    protected Duration duration;
    protected LocalDateTime startTime;


    public Task(String name, TaskStatus status, String description, int durationInMinutes, ZonedDateTime startTime) {
        id = null;
        this.name = name;
        this.status = status;
        this.description = description;
        this.duration = Duration.ofMinutes(durationInMinutes);
        this.startTime = LocalDateTime.ofInstant(startTime.toInstant(), ZoneOffset.UTC);

    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getUtcStartTime() {
        return startTime;
    }

    public LocalDateTime getStartTime(String timeZoneName) {
        TimeZone tz = TimeZone.getTimeZone(timeZoneName);
        int hours = tz.getRawOffset() / MILLISECONDS_IN_HOUR;
        return startTime.plusHours(hours);
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime(Duration duration) {
        return this.startTime.plusSeconds(duration.getSeconds());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return String.join(",", id.toString(), TASK.toString(), name, status.toString(), description,
                startTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), String.valueOf(duration.getSeconds() / 60));
    }

    @Override
    public Task clone() {
        try {
            return (Task) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
