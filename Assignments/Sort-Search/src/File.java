import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class File {

    static int[] getData(String fileName, int size) {
        int[] datas = new int[size];
        String line;
        int lineNum = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            br.readLine();
            while ((line = br.readLine()) != null && lineNum < size) {
                String[] values = line.split(",");
                datas[lineNum] = Integer.parseInt(values[6]);

                lineNum++;
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return datas;
    }
}
