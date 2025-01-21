import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class SearchExperiment extends Experiment{
    ArrayList<Double> linearRandomTimes = new ArrayList<>();
    ArrayList<Double> linearSortedTimes = new ArrayList<>();
    ArrayList<Double> binarySortedTimes = new ArrayList<>();

    String[] searchAlgorithms = Utilities.gatherStrings("Linear Random", "Linear Sorted", "Binary Sorted");

    void doRandomExp(int[] datas) {
        double time = 0;


        for (int i = 0; i < 10; i++) {
            int valueToSearch = datas[datas.length/10 * i];
            time += runAlgorithms(() -> SearchAlgorithms.linearSearch(datas, valueToSearch), 100, true);
        }
        linearRandomTimes.add((time/10));

    }

    void doSortedExp(int[] datas) {
        double linearTime = 0, binaryTime = 0;

        for (int i = 0; i < 10; i++) {
            int valueToSearch = datas[datas.length/10 * i];
            linearTime += runAlgorithms(() -> SearchAlgorithms.linearSearch(datas, valueToSearch), 100, true);
            binaryTime += runAlgorithms(() -> SearchAlgorithms.binarySearch(datas, valueToSearch), 100, true);
        }
        linearSortedTimes.add((linearTime/10));
        binarySortedTimes.add((binaryTime/10));
    }

    Graphic createAndSaveGraphic(int[] inputSizes, String title) throws IOException {
        chart.giveYAxis(linearRandomTimes, linearSortedTimes, binarySortedTimes);
        chart.giveParams(inputSizes, title, searchAlgorithms);
        chart.save();
        return chart;
    }

    @Override
    void print(String title) {
        System.out.println(title.toUpperCase(Locale.US) + "\n");
        System.out.println("Linear (Random): " + linearRandomTimes);
        System.out.println("Linear (Sorted): " + linearSortedTimes);
        System.out.println("Binary (Sorted): " + binarySortedTimes);
    }
}