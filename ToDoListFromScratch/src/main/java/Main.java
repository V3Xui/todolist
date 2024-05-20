import java.io.*;
import java.util.*;

public class Main {
    public static String FILE_NAME = "tasks.txt";
    public static Scanner scanner = new Scanner(System.in);

    public static final List<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {
        loadTasksFromFile();
        showMenu();
    }

    public static void showMenu() {
        int choice;
        do {
            System.out.println("\n===== To-Do List Menu =====");
            System.out.println("1. Add Task");
            System.out.println("2. Remove Task");
            System.out.println("3. Mark as Done");
            System.out.println("4. Edit Task");
            System.out.println("5. Sort Alphabetically");
            System.out.println("6. Sort by Priority");
            System.out.println("7. Save to File");
            System.out.println("8. Load from File");
            System.out.println("9. Display Tasks");
            System.out.println("10. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            // New Line
            scanner.nextLine();
            switch (choice) {
                case 1:
                    addTask();
                    break;
                case 2:
                    removeTask();
                    break;
                case 3:
                    markAsDone();
                    break;
                case 4:
                    editTask();
                    break;
                case 5:
                    sortTasksAlphabetically();
                    break;
                case 6:
                    sortTasksByPriority();
                    break;
                case 7:
                    saveTasksToFile();
                    break;
                case 8:
                    loadTasksFromFile();
                    break;
                case 9:
                    displayTasks();
                    break;
                case 10:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 10.");
            }
        } while (choice != 10);
    }

    public static void addTask() {
        System.out.print("Enter task to add: ");
        String taskDescription = scanner.nextLine();
        System.out.print("Enter priority (low, medium, high): ");
        String priority = scanner.nextLine().toLowerCase();
        tasks.add(new Task(taskDescription, "Priority level: " + priority));
        System.out.println("Task added successfully.");
    }

    public static void removeTask() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks to remove.");
            return;
        }
        System.out.print("Enter index of task to remove: ");
        int index = scanner.nextInt();
        if (index < 0 || index >= tasks.size()) {
            System.out.println("Invalid index.");
            return;
        }
        tasks.remove(index);
        System.out.println("Task removed successfully.");
    }

    public static void markAsDone() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks to mark as done.");
            return;
        }
        System.out.print("Enter index of task to mark as done: ");
        int index = scanner.nextInt();
        if (index < 0 || index >= tasks.size()) {
            System.out.println("Invalid index.");
            return;
        }
        Task task = tasks.get(index);
        if (!task.isDone()) {
            task.setDone(true);
            System.out.println("Task marked as done.");
        } else {
            System.out.println("Task is already marked as done.");
        }
    }

    public static void editTask() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks to edit.");
            return;
        }
        System.out.print("Enter index of task to edit: ");
        int index = scanner.nextInt();
        if (index < 0 || index >= tasks.size()) {
            System.out.println("Invalid index.");
            return;
        }
        scanner.nextLine(); // Consume newline character
        System.out.print("Enter new task: ");
        String newTaskDescription = scanner.nextLine();
        System.out.print("Enter new priority (low, medium, high): ");
        String newPriority = scanner.nextLine().toLowerCase();
        Task task = tasks.get(index);
        task.setDescription(newTaskDescription);
        task.setPriority(newPriority);
        System.out.println("Task edited successfully.");
    }

    public static void sortTasksAlphabetically() {
        tasks.sort(Comparator.comparing(Task::getDescription));
        System.out.println("Tasks sorted alphabetically.");
    }

    public static void sortTasksByPriority() {
        tasks.sort(Comparator.comparing(Task::getPriority));
        System.out.println("Tasks sorted by priority.");
    }

    public static void loadTasksFromFile() {
        tasks.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" \\| ");
                if (parts.length == 3) {
                    String description = parts[0];
                    String priority = parts[1];
                    boolean isDone = Boolean.parseBoolean(parts[2]);
                    tasks.add(new Task(description, priority, isDone));
                }
            }
            System.out.println("Tasks loaded from file.");
        } catch (IOException e) {
            System.out.println("No existing tasks found.");
        }
    }

    public static void saveTasksToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Task task : tasks) {
                writer.write(task.getDescription() + " | " + task.getPriority() + " | " + task.isDone());
                writer.newLine();
            }
            System.out.println("Tasks saved to file.");
        } catch (IOException e) {
            System.out.println("Error saving tasks to file.");
        }
    }

    public static void displayTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks to display.");
            return;
        }
        System.out.println("\n===== To-Do List =====");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            System.out.println(i + ": " + task.getDescription() + " [" + task.getPriority() + "]" + (task.isDone() ? " (Done)" : ""));
        }
    }

    static class Task {
         String description;
         String priority;
         boolean done;

        public Task(String description, String priority) {
            this.description = description;
            this.priority = priority;
            this.done = false;
        }

        public Task(String description, String priority, boolean done) {
            this.description = description;
            this.priority = priority;
            this.done = done;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPriority() {
            return priority;
        }

        public void setPriority(String priority) {
            this.priority = priority;
        }

        public boolean isDone() {
            return done;
        }

        public void setDone(boolean done) {
            this.done = done;
        }
    }
}
