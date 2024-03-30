import java.util.*;

public class Utilities {
    static int findMax(int[] datas) {
        return Arrays.stream(datas).max().orElse(0);
    }

    static void reverseArray(int[] datas) {
        List<Integer> list = new ArrayList<>();
        for (int element : datas) {
            list.add(element);
        }
        Collections.reverse(list);
        for (int i = 0; i < datas.length; i++) {
            datas[i] = list.get(i);
        }
    }

    static String[] gatherStrings(String... strings) {
        return strings;
    }
}