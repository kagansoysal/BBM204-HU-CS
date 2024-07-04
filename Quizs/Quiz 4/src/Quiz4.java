import java.io.*;
import java.util.*;

public class Quiz4 {

    public static void main(String[] args) throws IOException {
        String databaseTXT = args[0];
        String queryTXT = args[1];
        Trie trie = new Trie();

        try (BufferedReader dbReader = new BufferedReader(new FileReader(databaseTXT))) {
            int N = Integer.parseInt(dbReader.readLine().trim());

            for (int i = 0; i < N; i++) {
                String[] infos = dbReader.readLine().split("\t");
                long weight = Long.parseLong(infos[0]);
                String word = infos[1].toLowerCase(Locale.US);
                trie.insert(word, weight);
            }
        }

        try (BufferedReader queryReader = new BufferedReader(new FileReader(queryTXT))) {
            String line;
            while ((line = queryReader.readLine()) != null) {
                String[] parts = line.split("\t");
                String query = parts[0].toLowerCase(Locale.US);
                int limit = Integer.parseInt(parts[1]);
                List<Word> results = trie.search(query, limit);

                System.out.println("Query received: \"" + query + "\" with limit " + limit + ". Showing results:");
                if (results.isEmpty()) System.out.println("No results.");
                else for (Word result : results) System.out.println("- " + result.weight + " " + result.word);
            }
        }
    }

    static class Char {
        HashMap<Character, Char> children = new HashMap<>();
        Word word;
    }

    static class Word {
        long weight;
        String word;

        Word(long weight, String word) {
            this.weight = weight;
            this.word = word;
        }
    }

    static class Trie {
        Char root = new Char();
        public void insert(String word, long weight) {
            Char node = root;
            for (char c : word.toCharArray()) {
                node.children.putIfAbsent(c, new Char());
                node = node.children.get(c);
            }
            node.word = new Word(weight, word);
        }

        public List<Word> search(String prefix, int limit) {
            Char node = root;
            for (char c : prefix.toCharArray()) {
                node = node.children.get(c);
                if (node == null) return Collections.emptyList();
            }
            return getTopResults(node, limit);
        }

        private List<Word> getTopResults(Char node, int limit) {
            PriorityQueue<Word> maxHeap = new PriorityQueue<>((a, b) -> Long.compare(b.weight, a.weight));
            addWords(node, maxHeap);
            List<Word> topResults = new ArrayList<>();
            while (limit != 0 && !maxHeap.isEmpty()) {
                topResults.add(maxHeap.poll());
                limit--;
            }
            return topResults;
        }

        private void addWords(Char node, PriorityQueue<Word> maxHeap) {
            if (node.word != null) maxHeap.add(node.word);
            for (Char child : node.children.values()) addWords(child, maxHeap);
        }
    }
}
