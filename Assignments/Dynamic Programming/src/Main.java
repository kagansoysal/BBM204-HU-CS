import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Main class
 */
// FREE CODE HERE
public class Main {
    public static void main(String[] args) throws IOException {

       /** MISSION POWER GRID OPTIMIZATION BELOW **/

        // TODO: Your code goes here
        // You are expected to read the file given as the first command-line argument to read 
        // the energy demands arriving per hour. Then, use this data to instantiate a 
        // PowerGridOptimization object. You need to call getOptimalPowerGridSolutionDP() method
        // of your PowerGridOptimization object to get the solution, and finally print it to STDOUT.

        PowerGridOptimization powerGridOptimization = new PowerGridOptimization(edit(getData(args[0]).get(0).split("\\s+")));
        OptimalPowerGridSolution optimalPowerGridSolution = powerGridOptimization.getOptimalPowerGridSolutionDP();

        int demandedGW = powerGridOptimization.getAmountOfEnergyDemandsArrivingPerHour().stream().mapToInt(Integer::intValue).sum();
        int satisfiedGW = optimalPowerGridSolution.getmaxNumberOfSatisfiedDemands();
        int unsatisfiedGW = demandedGW - satisfiedGW;

        ArrayList<Integer> hours = optimalPowerGridSolution.getHoursToDischargeBatteriesForMaxEfficiency();
        String dischargedHours = String.join(", ", hours.stream().map(Object::toString).toArray(String[]::new));

        System.out.println("##MISSION POWER GRID OPTIMIZATION##");

        System.out.println("The total number of demanded gigawatts: " + demandedGW);
        System.out.println("Maximum number of satisfied gigawatts: " + satisfiedGW);
        System.out.println("Hours at which the battery bank should be discharged: " + dischargedHours);
        System.out.println("The number of unsatisfied gigawatts: " + unsatisfiedGW);

        System.out.println("##MISSION POWER GRID OPTIMIZATION COMPLETED##");

        /** MISSION ECO-MAINTENANCE BELOW **/

        // TODO: Your code goes here
        // You are expected to read the file given as the second command-line argument to read
        // the number of available ESVs, the capacity of each available ESV, and the energy requirements 
        // of the maintenance tasks. Then, use this data to instantiate an OptimalESVDeploymentGP object.
        // You need to call getMinNumESVsToDeploy(int maxNumberOfAvailableESVs, int maxESVCapacity) method
        // of your OptimalESVDeploymentGP object to get the solution, and finally print it to STDOUT.

        ArrayList<String> input = getData(args[1]);

        int maxNumberOfAvailableESVs = edit(input.get(0).split("\\s+")).get(0);
        int maxESVCapacity = edit(input.get(0).split("\\s+")).get(1);
        ArrayList<Integer> maintenanceTaskEnergyDemands = edit(input.get(1).split("\\s+"));

        OptimalESVDeploymentGP optimalESVDeploymentGP = new OptimalESVDeploymentGP(maintenanceTaskEnergyDemands);
        int minESV = optimalESVDeploymentGP.getMinNumESVsToDeploy(maxNumberOfAvailableESVs, maxESVCapacity);

        printESVMission(minESV, optimalESVDeploymentGP);
    }

    static void printESVMission(int minESV, OptimalESVDeploymentGP optimalESVDeploymentGP) {
        System.out.println("##MISSION ECO-MAINTENANCE##");

        switch (minESV) {
            case -1:
                System.out.println("Warning: Mission Eco-Maintenance Failed.");
                break;
            default:
                System.out.println("The minimum number of ESVs to deploy: " + minESV);

                for (int i = 0; i < optimalESVDeploymentGP.getMaintenanceTasksAssignedToESVs().size(); i++)
                    System.out.println("ESV " + (i + 1) + " tasks: " + optimalESVDeploymentGP.getMaintenanceTasksAssignedToESVs().get(i));

                break;
        }
        System.out.println("##MISSION ECO-MAINTENANCE COMPLETED##");
    }

    static ArrayList<String> getData(String fileName) {
        ArrayList<String> datas = new ArrayList<>();
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            while ((line = br.readLine()) != null) {
                datas.add(line);
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return datas;
    }

    static ArrayList<Integer> edit(String[] datas) {
        ArrayList<Integer> intDatas = Arrays.stream(datas).map(Integer::valueOf).collect(Collectors.toCollection(ArrayList::new));
        return intDatas;
    }
}
