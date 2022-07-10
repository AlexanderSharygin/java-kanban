import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int HISTORY_LENGTH = 10;

    private final List<Task> history;

    public InMemoryHistoryManager() {
        this.history = new LinkedList<>();
    }

    @Override
    public void add(Task task) {
        if (history.size() == HISTORY_LENGTH) {
            history.remove(0);
        }
        history.add(task);
    }

    @Override
    public void remove(int id) {
        List<Task> tasks = history.stream().filter(k->k.id==id).collect(Collectors.toList());
        tasks.forEach(history::remove);
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
