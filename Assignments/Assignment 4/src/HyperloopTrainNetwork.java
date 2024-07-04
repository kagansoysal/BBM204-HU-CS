import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HyperloopTrainNetwork implements Serializable {
    static final long serialVersionUID = 11L;
    public double averageTrainSpeed;
    public final double averageWalkingSpeed = 1000 / 6.0;;
    public int numTrainLines;
    public Station startPoint;
    public Station destinationPoint;
    public List<TrainLine> lines;

    /**
     * Method with a Regular Expression to extract integer numbers from the fileContent
     * @return the result as int
     */
    public int getIntVar(String varName, String fileContent) {
        Pattern p = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=[\\t ]*([0-9]+)");
        Matcher m = p.matcher(fileContent);
        m.find();
        return Integer.parseInt(m.group(1));
    }

    /**
     * Write the necessary Regular Expression to extract string constants from the fileContent
     * @return the result as String
     */
    public String getStringVar(String varName, String fileContent) {
        Pattern p = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=[\\t ]*\"([^\"]+)\"");
        Matcher m = p.matcher(fileContent);
        m.find();
        return m.group(1);
    }

    /**
     * Write the necessary Regular Expression to extract floating point numbers from the fileContent
     * Your regular expression should support floating point numbers with an arbitrary number of
     * decimals or without any (e.g. 5, 5.2, 5.02, 5.0002, etc.).
     * @return the result as Double
     */
    public Double getDoubleVar(String varName, String fileContent) {
        Pattern p = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=[\\t ]*([0-9]+(\\.[0-9]+)?)");
        Matcher m = p.matcher(fileContent);
        m.find();
        return Double.parseDouble(m.group(1));
    }

    /**
     * Write the necessary Regular Expression to extract a Point object from the fileContent
     * points are given as an x and y coordinate pair surrounded by parentheses and separated by a comma
     * @return the result as a Point object
     */
    public Point getPointVar(String varName, String fileContent) {
        String regex = "\\s*" + varName + "\\s*=\\s*\\(\\s*(-?\\d+)\\s*,\\s*(-?\\d+)\\s*\\)\\s*";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(fileContent);
        if(m.find()) {
            int x = Integer.parseInt(m.group(1));
            int y = Integer.parseInt(m.group(2));
            return new Point(x, y);
        } else return null;
    }

    /**
     * Function to extract the train lines from the fileContent by reading train line names and their
     * respective stations.
     * @return List of TrainLine instances
     */
    public List<TrainLine> getTrainLines(String fileContent) {
        List<TrainLine> trainLines = new ArrayList<>();

        ArrayList<String> linesInfo = new ArrayList<>();

        int firstLineIndex = fileContent.indexOf("train_line_name");

        while (firstLineIndex != -1) {
            int firstLineIndex2 = fileContent.indexOf("train_line_name", firstLineIndex + 1);
            if (firstLineIndex2 == -1) firstLineIndex2 = fileContent.length();

            String lineSubstring = fileContent.substring(firstLineIndex, firstLineIndex2);
            linesInfo.add(lineSubstring);

            firstLineIndex = fileContent.indexOf("train_line_name", firstLineIndex2);
        }

        List<Station> stations = new ArrayList<>();

        for (String lineInfo : linesInfo) {
            String lineName = getStringVar("train_line_name", lineInfo);

            String train_line_stations = "train_line_stations=";
            int firstPointIndex = lineInfo.indexOf("(", lineInfo.indexOf("train_line_stations"));

            int i = 1;

            while (firstPointIndex != -1) {
                int firstParanthesisIndex = lineInfo.indexOf(")", firstPointIndex);

                String pointSubstring = lineInfo.substring(firstPointIndex, firstParanthesisIndex + 1);

                Point point = getPointVar("train_line_stations", train_line_stations + pointSubstring);

                stations.add(new Station(point, lineName + " Line Station " + i));

                firstPointIndex = lineInfo.indexOf("(", firstParanthesisIndex + 1);

                i++;
            }
            trainLines.add(new TrainLine(lineName, new ArrayList<>(stations)));
            stations.clear();
        }

        return trainLines;
    }

    /**
     * Function to populate the given instance variables of this class by calling the functions above.
     */
    public void readInput(String filename) {
        StringBuilder trainLineContents = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (line.contains("train_line_")) trainLineContents.append(line);

                String varName = line.substring(0, line.indexOf("="));

                if (line.contains("speed")) averageTrainSpeed = getDoubleVar(varName, line) * 100 / 6.0;
                else if (line.contains("num")) numTrainLines = getIntVar(varName, line);
                else if (line.contains("starting_point")) startPoint = new Station(getPointVar(varName, line), "Starting Point");
                else if (line.contains("destination_point")) destinationPoint = new Station(getPointVar(varName, line), "Final Destination");
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        lines = getTrainLines(trainLineContents.toString());
    }
}