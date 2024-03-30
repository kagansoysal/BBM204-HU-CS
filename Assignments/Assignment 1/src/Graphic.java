import org.knowm.xchart.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Graphic {
    double[][] yAxis = new double[3][10];
    XYChart chart = new XYChartBuilder().width(800).height(600)
            .yAxisTitle("Time in Milliseconds").xAxisTitle("Input Size").build();

    void giveParams(int[] xAxis, String title, String[] algorithms){
        double[] doubleX = Arrays.stream(xAxis).asDoubleStream().toArray();
        chart.setTitle(title);
        chart.addSeries(algorithms[0], doubleX, yAxis[0]);
        chart.addSeries(algorithms[1], doubleX, yAxis[1]);
        chart.addSeries(algorithms[2], doubleX, yAxis[2]);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
    }

    void giveYAxis(ArrayList<Double>... times) {
        yAxis[0] = times[0].stream().mapToDouble(Double::doubleValue).toArray();;
        yAxis[1] = times[1].stream().mapToDouble(Double::doubleValue).toArray();
        yAxis[2] = times[2].stream().mapToDouble(Double::doubleValue).toArray();
    }

    void show() {
        new SwingWrapper(chart).displayChart();
    }

    void save() throws IOException {
        BitmapEncoder.saveBitmap(this.chart, chart.getTitle() + ".png", BitmapEncoder.BitmapFormat.PNG);
    }
}