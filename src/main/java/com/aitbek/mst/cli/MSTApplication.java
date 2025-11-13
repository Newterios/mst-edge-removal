package com.aitbek.mst.cli;

import com.aitbek.mst.algorithm.MSTAlgorithm;
import com.aitbek.mst.metrics.PerformanceMetrics;
import com.aitbek.mst.model.Edge;
import com.aitbek.mst.model.Graph;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class MSTApplication {
    private final PerformanceMetrics metrics;
    private final Graph graph;
    private final MSTAlgorithm mstAlgorithm;

    public MSTApplication(Graph graph) {
        this.graph = graph;
        this.mstAlgorithm = new MSTAlgorithm(graph);
        this.metrics = new PerformanceMetrics();
    }

    public void run() {
        System.out.println("=== MST Edge Removal and Replacement ===\n");

        long startTime = System.nanoTime();
        List<Edge> mst = mstAlgorithm.buildMST();
        long duration = System.nanoTime() - startTime;
        metrics.recordOperation("Build MST", duration, graph.getVertices(), graph.getEdges().size());

        displayMST(mst);

        Edge edgeToRemove = selectEdgeToRemove(mst);
        if (edgeToRemove == null) {
            return;
        }

        startTime = System.nanoTime();
        List<Set<Integer>> components = mstAlgorithm.getComponentsAfterRemoval(edgeToRemove);
        duration = System.nanoTime() - startTime;
        metrics.recordOperation("Find Components", duration, graph.getVertices(), mst.size());

        displayComponents(components);

        startTime = System.nanoTime();
        Edge replacement = mstAlgorithm.findReplacementEdge(edgeToRemove);
        duration = System.nanoTime() - startTime;
        metrics.recordOperation("Find Replacement", duration, graph.getVertices(), graph.getEdges().size());

        displayReplacement(replacement);

        List<Edge> newMST = mstAlgorithm.getMSTWithReplacement(edgeToRemove, replacement);
        displayNewMST(newMST);

        metrics.printSummary();

        try {
            metrics.exportToCSV("mst_metrics.csv");
            System.out.println("\nMetrics exported to mst_metrics.csv");
        } catch (Exception e) {
            System.err.println("Failed to export metrics: " + e.getMessage());
        }
    }

    private void displayMST(List<Edge> mst) {
        System.out.println("Original MST:");
        for (int i = 0; i < mst.size(); i++) {
            System.out.printf("  %d. %s%n", i + 1, mst.get(i));
        }
        System.out.printf("Total MST Weight: %d%n%n", mstAlgorithm.calculateMSTWeight(mst));
    }

    private Edge selectEdgeToRemove(List<Edge> mst) {
        System.out.println("Select an edge to remove (enter number 1-" + mst.size() + "):");
        Scanner scanner = new Scanner(System.in);

        try {
            int choice = scanner.nextInt();
            if (choice < 1 || choice > mst.size()) {
                System.out.println("Invalid choice.");
                return null;
            }

            Edge selected = mst.get(choice - 1);
            System.out.printf("%nRemoving edge: %s%n%n", selected);
            return selected;
        } catch (Exception e) {
            System.out.println("Invalid input.");
            return null;
        }
    }

    private void displayComponents(List<Set<Integer>> components) {
        System.out.println("Components after edge removal:");
        for (int i = 0; i < components.size(); i++) {
            System.out.printf("  Component %d: %s%n", i + 1, components.get(i));
        }
        System.out.println();
    }

    private void displayReplacement(Edge replacement) {
        if (replacement != null) {
            System.out.printf("Replacement edge found: %s%n%n", replacement);
        } else {
            System.out.println("No replacement edge found!%n%n");
        }
    }

    private void displayNewMST(List<Edge> newMST) {
        System.out.println("New MST after replacement:");
        for (int i = 0; i < newMST.size(); i++) {
            System.out.printf("  %d. %s%n", i + 1, newMST.get(i));
        }
        System.out.printf("Total MST Weight: %d%n", mstAlgorithm.calculateMSTWeight(newMST));
    }

    public static void main(String[] args) {
        Graph graph = Graph.createSampleGraph();
        MSTApplication app = new MSTApplication(graph);
        app.run();
    }
}