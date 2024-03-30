import java.io.IOException;
import java.util.Arrays;

public class Main {
    static int[] inputSizes = {500,1_000,2_000,4_000,8_000,16_000,32_000,64_000,128_000,250_000};

    public static void main(String[] args) throws IOException {
        SortExperiment randomExp = new SortExperiment();
        SortExperiment sortedExp = new SortExperiment();
        SortExperiment reverseExp = new SortExperiment();
        SearchExperiment searchExp = new SearchExperiment();

        measureTimes(randomExp, sortedExp, reverseExp, searchExp);
        createCharts(randomExp, sortedExp, reverseExp, searchExp);
        printResults(randomExp, sortedExp, reverseExp, searchExp);
    }

    static void measureTimes(SortExperiment randomExp, SortExperiment sortedExp, SortExperiment reverseExp, SearchExperiment searchExp) {
        for (int size : inputSizes) {
            int[] datas = File.getData("TrafficFlowDataset.csv", size);

            randomExp.doSortExp(datas);
            searchExp.doRandomExp(datas);

            Arrays.sort(datas);
            sortedExp.doSortExp(datas);
            searchExp.doSortedExp(datas);

            Utilities.reverseArray(datas);
            reverseExp.doSortExp(datas);
        }
    }

    static void createCharts(Experiment randomExp, Experiment sortedExp, Experiment reverseExp, Experiment searchExp) throws IOException {
        randomExp.createAndSaveGraphic(inputSizes, "Tests on Random Data");
        sortedExp.createAndSaveGraphic(inputSizes, "Tests on Sort Data");
        reverseExp.createAndSaveGraphic(inputSizes, "Tests on Reverse Data");
        searchExp.createAndSaveGraphic(inputSizes, "Tests of Search Algorithms");
    }
    static void printResults(Experiment randomExp, Experiment sortedExp, Experiment reverseExp, Experiment searchExp) {
        System.out.println("SORT ALGORITHMS\n");

        randomExp.print("Times on Random Data");
        sortedExp.print("Times on Sorted Data");
        reverseExp.print("Times on Reverse Data");

        System.out.println("SEARCH ALGORITHMS\n");

        searchExp.print("Times of Search Algorithms");
    }
}