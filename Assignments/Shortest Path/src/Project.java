import java.io.Serializable;
import java.util.*;

public class Project implements Serializable {
    static final long serialVersionUID = 33L;
    private final String name;
    private final List<Task> tasks;

    int [] startDays;

    List<Task> topologicalOrder = new ArrayList<>();

    public Project(String name, List<Task> tasks) {
        this.name = name;
        this.tasks = tasks;
    }

    /**
     * @return the total duration of the project in days
     */
    public int getProjectDuration() {
        HashMap<Integer, Task> taskHashMap = new HashMap<>();
        for (Task task : tasks) taskHashMap.put(task.getTaskID(), task);

        return findMaxDuration(tasks.get(tasks.size() - 1), taskHashMap);
    }

    private int findMaxDuration(Task task, HashMap<Integer, Task> taskHashMap) {
        int duration = 0;
        for (int taskID : task.getDependencies()) {
            Task adj = taskHashMap.get(taskID);
            int adjDuration = findMaxDuration(adj, taskHashMap);
            if (adjDuration > duration) duration = adjDuration;
        }
        return duration + task.getDuration();
    }

    /**
     * Schedule all tasks within this project such that they will be completed as early as possible.
     *
     * @return An integer array consisting of the earliest start days for each task.
     */
    public int[] getEarliestSchedule() {
        startDays = new int[tasks.size()];

        HashMap<Integer, Task> taskHashMap = new HashMap<>();
        for (Task task : tasks) taskHashMap.put(task.getTaskID(), task);

        for (Task task : tasks) if (!task.visited) dfs(task, taskHashMap);

        for (Task task : topologicalOrder) {
            int startDay = task.getDependencies().stream()
                    .mapToInt(adj -> startDays[taskHashMap.get(adj).getTaskID()] + taskHashMap.get(adj).getDuration())
                    .max()
                    .orElse(0);
            startDays[task.getTaskID()] = startDay;
        }
        return startDays;
    }

    private void dfs(Task task, HashMap<Integer, Task> taskHashMap) {
        task.visited = true;

        for (int taskID : task.getDependencies()) {
            Task adj = taskHashMap.get(taskID);
            if (!adj.visited) dfs(adj, taskHashMap);
        }
        topologicalOrder.add(task);
    }

    public static void printlnDash(int limit, char symbol) {
        for (int i = 0; i < limit; i++) System.out.print(symbol);
        System.out.println();
    }

    /**
     * Some free code here. YAAAY! 
     */
    public void printSchedule(int[] schedule) {
        int limit = 65;
        char symbol = '-';
        printlnDash(limit, symbol);
        System.out.println(String.format("Project name: %s", name));
        printlnDash(limit, symbol);

        // Print header
        System.out.println(String.format("%-10s%-45s%-7s%-5s","Task ID","Description","Start","End"));
        printlnDash(limit, symbol);
        for (int i = 0; i < schedule.length; i++) {
            Task t = tasks.get(i);
            System.out.println(String.format("%-10d%-45s%-7d%-5d", i, t.getDescription(), schedule[i], schedule[i]+t.getDuration()));
        }
        printlnDash(limit, symbol);
        System.out.println(String.format("Project will be completed in %d days.", tasks.get(schedule.length-1).getDuration() + schedule[schedule.length-1]));
        printlnDash(limit, symbol);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;

        int equal = 0;

        for (Task otherTask : ((Project) o).tasks) {
            if (tasks.stream().anyMatch(t -> t.equals(otherTask))) {
                equal++;
            }
        }

        return name.equals(project.name) && equal == tasks.size();
    }
}
