import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Quiz1 {
    public static void main(String[] args) {
        // TODO: Use the first command line argument (args[0]) as the file to read the input from
        // TODO: Your code goes here
        // TODO: Print the solution to STDOUT


        ArrayList<String> inputs = getData(args[0]);
        ArrayList<String> ignoredWords = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        ArrayList<String> wordsToEnlarge = new ArrayList<>();
        ArrayList<String> titlesToWrite = new ArrayList<>();

        boolean three_points = false;

        for (int i = 0; i < inputs.size(); i++) {
            if (Objects.equals(inputs.get(i), "...")) {
                three_points = true;
            } else if (!three_points) {
                ignoredWords.add(inputs.get(i).toLowerCase(Locale.US));
            } else {
                titles.add(inputs.get(i).toLowerCase(Locale.US));
            }
        }

        for (String title : titles) {
            String[] words = title.split("\\s+");
            for (String word : words) {
                if (!ignoredWords.contains(word)) {
                    wordsToEnlarge.add(word);
                }
            }
        }

        Collections.sort(wordsToEnlarge);

        for (int i = 0; i < wordsToEnlarge.size(); i++) {
            int order = findOrder(wordsToEnlarge, wordsToEnlarge.get(i), i);
            int count = 1;
            boolean success = false;

            for (int j = 0; j < titles.size(); j++) {
                String[] words = titles.get(j).split("\\s+");

                for (int k = 0; k < words.length; k++) {
                    String word = words[k];

                    if (word.equals(wordsToEnlarge.get(i)) && order == count) {
                        words[k] = word.toUpperCase(Locale.US);
                        success = true;
                        titlesToWrite.add(String.join(" ", words));
                        break;

                    } else if (word.equals(wordsToEnlarge.get(i))) {
                        count++;
                    }
                }

                if (success) break;
            }
        }

        for (String title : titlesToWrite) {
            System.out.println(title);
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

    static int findOrder(ArrayList<String> words, String word, int index) {
        int count = 0;
        for (int i = 0; i < words.size(); i++) {
            if (words.get(i).equals(word)) {
                count++;
                if (i == index) {
                    return count;
                }
            }
        }
        return -1;
    }
}