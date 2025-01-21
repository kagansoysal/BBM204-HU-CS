import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class UrbanInfrastructureDevelopment implements Serializable {
    static final long serialVersionUID = 88L;

    /**
     * Given a list of Project objects, prints the schedule of each of them.
     * Uses getEarliestSchedule() and printSchedule() methods of the current project to print its schedule.
     * @param projectList a list of Project objects
     */
    public void printSchedule(List<Project> projectList) {
        for (Project project : projectList) {
            project.printSchedule(project.getEarliestSchedule());
        }
    }

    /**
     * TODO: Parse the input XML file and return a list of Project objects
     *
     * @param filename the input XML file
     * @return a list of Project objects
     */
    public List<Project> readXML(String filename) {
        List<Project> projectList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            String name = null;
            List<Task> tasks = new ArrayList<>();

            int taskID = 0;
            String description = null;
            int duration = 0;
            List<Integer> dependencies = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                if (line.contains("<Name>")) {
                    name = line.substring(line.indexOf('>') + 1, line.lastIndexOf('<'));
                } else if (line.contains("<TaskID>")) {
                    taskID = Integer.parseInt(line.substring(line.indexOf('>') + 1, line.lastIndexOf('<')));
                } else if (line.contains("<Description>")) {
                    description = line.substring(line.indexOf('>') + 1, line.lastIndexOf('<'));
                } else if (line.contains("<Duration>")) {
                    duration = Integer.parseInt(line.substring(line.indexOf('>') + 1, line.lastIndexOf('<')));
                } else if (line.contains("<DependsOnTaskID>")) {
                    dependencies.add(Integer.parseInt(line.substring(line.indexOf('>') + 1, line.lastIndexOf('<'))));
                } else if (line.contains("</Task>")) {
                    tasks.add(new Task(taskID, description, duration, new ArrayList<>(dependencies)));
                    dependencies.clear();
                } else if (line.contains("</Project>")) {
                    projectList.add(new Project(name, new ArrayList<>(tasks)));
                    tasks.clear();
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return projectList;
    }
}
