import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class SortExperiment extends Experiment{
    ArrayList<Double> insertionTimes = new ArrayList<>();
    ArrayList<Double> mergeTimes = new ArrayList<>();
    ArrayList<Double> countingTimes = new ArrayList<>();

    String[] sortAlgorithms = Utilities.gatherStrings("Insertion Sort", "Merge Sort", "Counting Sort");

    void doSortExp(int[] datas) {
        insertionTimes.add(runAlgorithms(() -> SortAlgorithms.insertionSort(datas.clone()), 10, false));
        mergeTimes.add(runAlgorithms(() -> SortAlgorithms.mergeSort(datas.clone()), 10, false));
        countingTimes.add(runAlgorithms(() -> SortAlgorithms.countingSort(datas.clone()), 10, false));
    }

    Graphic createAndSaveGraphic(int[] inputSizes, String title) throws IOException {
        chart.giveYAxis(insertionTimes, mergeTimes, countingTimes);
        chart.giveParams(inputSizes, title, sortAlgorithms);
        chart.save();
        return chart;
    }

    @Override
    void print(String title) {
        System.out.println(title.toUpperCase(Locale.US) + "\n");
        System.out.println("Insertion Sort: " + insertionTimes);
        System.out.println("Merge Sort: " + mergeTimes);
        System.out.println("Counting Sort: " + countingTimes + "\n");
    }
}