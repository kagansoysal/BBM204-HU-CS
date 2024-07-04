import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Quiz2 {
    static ArrayList<Integer> resources = new ArrayList<>();

    public static void main(String[] args) {
        ArrayList<String> inputTxt = getData(args[0]);
        ArrayList<ArrayList<Integer>> results = new ArrayList<>();
        
        int mass = Integer.parseInt(inputTxt.get(0).split(" ")[0]);
        int resourceNum = Integer.parseInt(inputTxt.get(0).split(" ")[1]);

        for (String str : getData(args[0]).get(1).split(" ")) {
            resources.add(Integer.parseInt(str));
        }


        for (int i = 0; i <= mass; i++) {
            ArrayList<Integer> row = new ArrayList<>();
            for (int j = 0; j <= resourceNum; j++) {
                row.add(loading(i, j) ? 1 : 0);
            }
            results.add(row);
        }

        int m_max = 0;

        for (int i = results.size() - 1; i >= 0; i--) {
            if(results.get(i).contains(1)) {
                m_max = i;
                break;
            }
        }

        System.out.println(m_max);

        for (int i = 0; i < results.size(); i++) {
            for (int j = 0; j < results.get(i).size(); j++) {
                System.out.print(results.get(i).get(j));
            }
            System.out.println();
        }

    }

    static boolean loading(int mass, int i) {
        if (i == 0 && mass == 0) return true;
        else if (i == 0 && mass > 0) return false;
        else if (i > 0 && resources.get(i - 1) > mass) {
            return loading(mass, i - 1);
        } else {
            return loading(mass - resources.get(i - 1), i - 1) || loading(mass, i - 1);
        }
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
}