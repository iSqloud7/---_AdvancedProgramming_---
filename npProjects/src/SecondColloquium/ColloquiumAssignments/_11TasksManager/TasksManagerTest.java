package SecondColloquium.ColloquiumAssignments._11TasksManager;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

class DeadlineNotValidException extends Exception {

    // The deadline 2020-06-01T23:59:59 has already passed
    public DeadlineNotValidException(String deadline) {
        super(String.format("The deadline %s has already passed", deadline));
    }
}

class Task {

    private String taskCategory;
    private String taskName;
    private String taskDescription;
    private LocalDateTime taskDeadline;
    private int taskPriority;

    public Task(String taskCategory, String taskName, String taskDescription, LocalDateTime taskDeadline, int taskPriority) {
        this.taskCategory = taskCategory;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskDeadline = taskDeadline;
        this.taskPriority = taskPriority;
    }

    public String getTaskCategory() {
        return taskCategory;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public LocalDateTime getTaskDeadline() {
        return taskDeadline;
    }

    public int getTaskPriority() {
        return taskPriority;
    }

    @Override
    public String toString() {
        return "Task{" +
                "category='" + taskCategory + '\'' +
                ", name='" + taskName + '\'' +
                ", description='" + taskDescription + '\'' +
                ", deadline=" + taskDeadline +
                ", priority=" + taskPriority +
                '}';
    }
}

class TaskManager {

    private List<Task> tasks;
    private static final LocalDateTime MAX_DEADLINE;

    public TaskManager() {
        this.tasks = new ArrayList<>();
    }

    static {
        MAX_DEADLINE = LocalDateTime.of(2020, 6, 2, 23, 59, 59);
    }

    // Task: [категорија][име_на_задача],[oпис],[рок_за_задачата],[приоритет]
    // Task{name='NP', description='lab 1 po NP', deadline=2020-06-23T23:59:59, priority=1}
    // School,NP,lab 1 po NP,2020-06-23T23:59:59.000,1
    public void readTasks(InputStream inputStream) throws DeadlineNotValidException {
        Scanner scanner = new Scanner(inputStream);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime maxDeadline = LocalDateTime.of(2020, 6, 2, 23, 59, 59);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(",", -1); // -1 to keep empty fields

            if (parts.length < 3) {
                System.out.println("Invalid input line, skipping: " + line);
                continue;
            }

            String category = parts[0];
            String name = parts[1];
            String description = parts[2];
            LocalDateTime deadline = null;
            if (parts.length > 3 && !parts[3].isEmpty()) {
                deadline = LocalDateTime.parse(parts[3], formatter);
                if (deadline.isBefore(LocalDateTime.now())) {
                    // The deadline 2020-06-01T23:59:59 has already passed
                    throw new DeadlineNotValidException(parts[3]);
                }
            }
            Integer priority = null;
            if (parts.length > 4 && !parts[4].isEmpty()) {
                priority = Integer.parseInt(parts[4]);
            }

            tasks.add(new Task(category, name, description, deadline, priority));
        }
    }

    public void printTasks(OutputStream os, boolean includePriority, boolean includeCategory) {
        PrintWriter writer = new PrintWriter(os);
        Map<String, List<Task>> categorizedTasks = new HashMap<>();

        for (Task task : tasks) {
            categorizedTasks.computeIfAbsent(task.getTaskCategory(),
                    k -> new ArrayList<>()).add(task);
        }

        categorizedTasks.forEach((category, taskList) -> {
            if (includeCategory) {
                writer.println("Category: " + category);
            }

            taskList.sort(Comparator
                    .comparing(Task::getTaskPriority,
                            Comparator.nullsLast(Integer::compareTo))
                    .thenComparing(Task::getTaskDeadline,
                            Comparator.nullsLast(Comparator.naturalOrder())));

            for (Task task : taskList) {
                if (includePriority) {
                    writer.println(task);
                } else {
                    writer.println(task.getTaskName() + ", " +
                            task.getTaskDescription() +
                            (task.getTaskDeadline() != null ? ", " + task.getTaskDeadline() : ""));
                }
            }
        });

        writer.flush();
    }
}

public class TasksManagerTest {

    public static void main(String[] args) {

        TaskManager manager = new TaskManager();

        System.out.println("Tasks reading");
        try {
            manager.readTasks(System.in);
        } catch (DeadlineNotValidException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("By categories with priority");
        manager.printTasks(System.out, true, true);
        System.out.println("-------------------------");
        System.out.println("By categories without priority");
        manager.printTasks(System.out, false, true);
        System.out.println("-------------------------");
        System.out.println("All tasks without priority");
        manager.printTasks(System.out, false, false);
        System.out.println("-------------------------");
        System.out.println("All tasks with priority");
        manager.printTasks(System.out, true, false);
        System.out.println("-------------------------");
    }
}