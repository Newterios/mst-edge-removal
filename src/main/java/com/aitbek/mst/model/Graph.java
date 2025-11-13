package com.aitbek.mst.model;

import java.util.*;

public class Graph {
    private final int vertices;
    private final List<Edge> edges;
    private final Map<Integer, List<Edge>> adjacencyList;

    public Graph(int vertices) {
        this.vertices = vertices;
        this.edges = new ArrayList<>();
        this.adjacencyList = new HashMap<>();
        for (int i = 0; i < vertices; i++) {
            adjacencyList.put(i, new ArrayList<>());
        }
    }

    public void addEdge(int source, int destination, int weight) {
        Edge edge = new Edge(source, destination, weight);
        edges.add(edge);
        adjacencyList.get(source).add(edge);
        adjacencyList.get(destination).add(new Edge(destination, source, weight));
    }

    public int getVertices() {
        return vertices;
    }

    public List<Edge> getEdges() {
        return new ArrayList<>(edges);
    }

    public List<Edge> getAdjacentEdges(int vertex) {
        return new ArrayList<>(adjacencyList.get(vertex));
    }

    public static Graph createSampleGraph() {
        Graph graph = new Graph(6);
        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 2, 3);
        graph.addEdge(1, 2, 1);
        graph.addEdge(1, 3, 2);
        graph.addEdge(2, 3, 4);
        graph.addEdge(3, 4, 2);
        graph.addEdge(3, 5, 6);
        graph.addEdge(4, 5, 3);
        return graph;
    }
}