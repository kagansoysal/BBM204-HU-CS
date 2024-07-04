import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class accomplishes Mission POWER GRID OPTIMIZATION
 */
public class PowerGridOptimization {
    private ArrayList<Integer> amountOfEnergyDemandsArrivingPerHour;

    public PowerGridOptimization(ArrayList<Integer> amountOfEnergyDemandsArrivingPerHour){
        this.amountOfEnergyDemandsArrivingPerHour = amountOfEnergyDemandsArrivingPerHour;
    }

    public ArrayList<Integer> getAmountOfEnergyDemandsArrivingPerHour() {
        return amountOfEnergyDemandsArrivingPerHour;
    }
    /**
     *     Function to implement the given dynamic programming algorithm
     *     SOL(0) <- 0
     *     HOURS(0) <- [ ]
     *     For{j <- 1...N}
     *         SOL(j) <- max_{0<=i<j} [ (SOL(i) + min[ E(j), P(j − i) ] ]
     *         HOURS(j) <- [HOURS(i), j]
     *     EndFor
     *
     * @return OptimalPowerGridSolution
     */
    public OptimalPowerGridSolution getOptimalPowerGridSolutionDP(){
        // TODO: YOUR CODE HERE

        ArrayList<Integer> solutions = new ArrayList<>(List.of(0));
        ArrayList<ArrayList<Integer>> hours = new ArrayList<>(List.of(new ArrayList<Integer>()));

        for (int j = 1; j <= amountOfEnergyDemandsArrivingPerHour.size(); j++) {
            int maxValue = 0;
            int optimalHour = 0;

            for (int i = 0; i < j; i++) {
                int newSol = (int) (solutions.get(i) + Math.min(amountOfEnergyDemandsArrivingPerHour.get(j-1), Math.pow(j-i, 2)));

                if (newSol > maxValue) {
                    maxValue = newSol;
                    optimalHour = i;
                }
            }
            solutions.add(maxValue);
            ArrayList<Integer> newHourList = new ArrayList<>(hours.get(optimalHour));
            newHourList.add(j);
            hours.add(newHourList);
        }

        int index = solutions.indexOf(Collections.max(solutions));
        int maxNumberOfSatisfiedDemands = Collections.max(solutions);
        ArrayList<Integer> hoursToDischargeBatteriesForMaxEfficiency = hours.get(index);

        return new OptimalPowerGridSolution(maxNumberOfSatisfiedDemands, hoursToDischargeBatteriesForMaxEfficiency);
    }
}
