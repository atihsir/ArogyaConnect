public class Graph {
    private final int V; // Number of vertices (doctors + patient)
    private double[][] adjMatrix; // Distance matrix

    public Graph(int V) {
        this.V = V;
        adjMatrix = new double[V][V];
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                adjMatrix[i][j] = Double.MAX_VALUE; // Initialize with large distances
            }
        }
    }

    // Add edge between two nodes (doctors or patient)
    public void addEdge(int src, int dest, double weight) {
        adjMatrix[src][dest] = weight;
        adjMatrix[dest][src] = weight; // Undirected graph since distance works both ways
    }

    // Dijkstra algorithm to find shortest path from a source (can be the patient or doctor)
    public double[] dijkstra(int src) {
        boolean[] visited = new boolean[V]; // To track visited nodes
        double[] distance = new double[V];  // Shortest distances from src
        for (int i = 0; i < V; i++) {
            distance[i] = Double.MAX_VALUE; // Initialize all distances to infinity
        }
        distance[src] = 0; // Distance to self is 0

        for (int count = 0; count < V - 1; count++) {
            int u = minDistance(distance, visited); // Get the unvisited node with the smallest distance
            visited[u] = true; // Mark this node as visited

            // Update distance for adjacent nodes
            for (int v = 0; v < V; v++) {
                // Update distance[v] only if it hasn't been visited, there's an edge, and total cost is smaller
                if (!visited[v] && adjMatrix[u][v] != Double.MAX_VALUE && distance[u] != Double.MAX_VALUE && distance[u] + adjMatrix[u][v] < distance[v]) {
                    distance[v] = distance[u] + adjMatrix[u][v];
                }
            }
        }
        return distance; // Return the computed shortest distances
    }

    // Helper function to find the node with the smallest distance that hasn't been visited
    private int minDistance(double[] dist, boolean[] visited) {
        double min = Double.MAX_VALUE;
        int minIndex = -1;
        for (int v = 0; v < V; v++) {
            if (!visited[v] && dist[v] <= min) {
                min = dist[v];
                minIndex = v;
            }
        }
        return minIndex;
    }}
//
