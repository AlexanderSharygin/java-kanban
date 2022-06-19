import java.text.SimpleDateFormat;
import java.util.Date;

public class HistoryEntry {

    private static final SimpleDateFormat DATA_TIME_FORMATTER = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private final int id;
    private final ItemType itemType;
    private final Date dateTime;

    public HistoryEntry(Task item)
    {
        id = item.getId();
        String className = item.getClass().getName();
        switch (className) {
            case "Task":
                itemType = ItemType.TASK;
                break;
            case "Epic":
                itemType = ItemType.EPIC;
                break;
            case "SubTask":
                itemType = ItemType.SUBTASK;
                break;
            default:
                itemType = ItemType.UNDEFINED;
                break;
        }
        dateTime = new Date();
    }

    @Override
    public String toString() {
        return  "id=" + id +
                ", type=" + itemType +
                ", DateTime=" + DATA_TIME_FORMATTER.format(dateTime);
    }
}
