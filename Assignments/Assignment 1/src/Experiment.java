import java.io.IOException;

public abstract class Experiment {
    Graphic chart = new Graphic();

    double runAlgorithms(Runnable algorithm, int expTime, boolean useNano) {
        double start, end, totalTime = 0;

        for (int i = 0; i < expTime; i++) {
            start = useNano ? System.nanoTime() : System.currentTimeMillis();
            algorithm.run();
            end = useNano ? System.nanoTime() : System.currentTimeMillis();
            totalTime += end - start;
        }
        totalTime /= expTime;

        return totalTime;
    }

    abstract Graphic createAndSaveGraphic(int[] inputSizes, String title) throws IOException;

    abstract void print(String title);
}