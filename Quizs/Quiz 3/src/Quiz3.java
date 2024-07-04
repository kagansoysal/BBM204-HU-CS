import java.io.*;
import java.util.*;

class Vertex {
    int x, y;
    ArrayList<Vertex> adj = new ArrayList<>();
    boolean scanned = false;

    public Vertex(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

class Edge {
    Vertex vertex1;
    Vertex vertex2;
    double distance;

    public Edge(Vertex vertex1, Vertex vertex2, double distance) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.distance = distance;
    }
}

class UnionFind {
    private int[] parent;
    private int[] rank;

    public UnionFind(int size) {
        parent = new int[size];
        rank = new int[size];
        for (int i = 0; i < size; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    private int find(int node) {
        if (parent[node] != node) {
            parent[node] = find(parent[node]);
        }
        return parent[node];
    }

    public void union(int node1, int node2) {
        int root1 = find(node1);
        int root2 = find(node2);

        if (root1 != root2) {
            if (rank[root1] < rank[root2]) {
                parent[root1] = root2;
            } else if (rank[root1] > rank[root2]) {
                parent[root2] = root1;
            } else {
                parent[root2] = root1;
                rank[root1]++;
            }
        }
    }

    public boolean isConnected(int node1, int node2) {
        return find(node1) == find(node2);
    }
}

public class Quiz3 {
    public static void main(String[] args) {
        ArrayList<String> inputTXT = getData(args[0]);
        int lineNum = 0;

        int testCaseNum = Integer.parseInt(inputTXT.get(lineNum++));
        for (int k = 0; k < testCaseNum; k++) {
            String[] stationInfo = inputTXT.get(lineNum++).split("\\s+");
            int stationWithDroneNum = Integer.parseInt(stationInfo[0]);
            int stationNum = Integer.parseInt(stationInfo[1]);

            ArrayList<Vertex> vertices = new ArrayList<>();
            ArrayList<Edge> edges = new ArrayList<>();

            for (int i = 0; i < stationNum; i++) {
                String[] parts = inputTXT.get(lineNum++).split("\\s+");
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);

                vertices.add(new Vertex(x, y));
            }

            for (int i = 0; i < vertices.size(); i++) {
                for (int j = i + 1; j < vertices.size(); j++) {
                    double distance = calculateDist(vertices.get(i), vertices.get(j));
                    edges.add(new Edge(vertices.get(i), vertices.get(j), distance));
                }
            }

            edges.sort(Comparator.comparingDouble(e -> e.distance));

            UnionFind uf = new UnionFind(stationNum);

            ArrayList<Edge> addedEdge = new ArrayList<>();

            for (Edge edge : edges) {
                if (!uf.isConnected(vertices.indexOf(edge.vertex1), vertices.indexOf(edge.vertex2))) {
                    addedEdge.add(edge);
                    uf.union(vertices.indexOf(edge.vertex1), vertices.indexOf(edge.vertex2));
                }
            }

            double minT = addedEdge.get(addedEdge.size() - stationWithDroneNum).distance;
            System.out.printf("%.2f%n", minT);
        }
    }

    static double calculateDist(Vertex vertex1, Vertex vertex2) {
        int diffOfX = vertex1.x - vertex2.x;
        int diffOfY = vertex1.y - vertex2.y;
        return Math.sqrt(diffOfX * diffOfX + diffOfY * diffOfY);
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
