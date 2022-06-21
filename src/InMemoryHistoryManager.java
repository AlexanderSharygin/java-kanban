import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<HistoryEntry> history;

    public InMemoryHistoryManager() {
        this.history = new ArrayList<>();
    }

    @Override
    public void add(Task task) {
        HistoryEntry historyEntry = new HistoryEntry(task);
        if (history.size() == 10) {
            history.remove(0);
        }
        history.add(historyEntry);
    }

    @Override
    public List<HistoryEntry> getHistory() {
        return history;
    }
}
