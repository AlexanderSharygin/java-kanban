import org.junit.jupiter.api.Test;
import task.Epic;
import task.SubTask;
import task.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static task.TaskStatus.NEW;


public class TaskTests {

    Epic epic;
    Epic epic2;
    SubTask subTaskNew1;
    SubTask subTaskNew2;
    Task taskNew1;
    Task taskNew2;

    public TaskTests() {
        epic = new Epic("EpicOne", "one");
        subTaskNew1 = new SubTask("SubTaskN1", NEW, "stN1", 1);
        subTaskNew2 = new SubTask("SubTaskN2", NEW, "stN2", 1);
        epic2 = new Epic("EpicTwo", "Two");
        taskNew1 = new Task("FirstTask", NEW, "t1");
        taskNew2 = new Task("SecondTask", NEW, "t2");
    }

    @Test
    public void taskEqualsTest() {
        taskNew1.setId(1);
        taskNew2.setId(1);
        assertEquals(taskNew1, taskNew2);
    }

    @Test
    public void taskNotEqualsTest() {
        taskNew1.setId(1);
        taskNew2.setId(2);
        assertNotEquals(taskNew1, taskNew2);
    }

    @Test
    public void subTaskEqualsTest() {
        subTaskNew1.setId(2);
        subTaskNew2.setId(2);
        assertEquals(subTaskNew1, subTaskNew2);
    }

    @Test
    public void subTaskNotEqualsTest() {
        subTaskNew1.setId(2);
        subTaskNew2.setId(3);
        assertNotEquals(subTaskNew1, subTaskNew2);
    }

    @Test
    public void epicEqualsTest() {
        epic.setId(3);
        epic2.setId(3);
        assertEquals(epic, epic2);
    }

    @Test
    public void epicNotEqualsTest() {
        epic.setId(3);
        epic2.setId(4);
        assertNotEquals(epic, epic2);
    }
}