import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class File {

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
