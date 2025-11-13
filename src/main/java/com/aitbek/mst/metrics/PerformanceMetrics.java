package com.aitbek.mst.metrics;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class PerformanceMetrics {
    private final List<MetricRecord> records;

    public PerformanceMetrics() {
        this.records = new ArrayList<>();
    }

    public void recordOperation(String operation, long durationNanos, int vertices, int edges) {
        records.add(new MetricRecord(operation, durationNanos, vertices, edges));
    }

    public void exportToCSV(String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Operation,Duration(ns),Duration(ms),Vertices,Edges");
            for (MetricRecord record : records) {
                writer.printf("%s,%d,%.3f,%d,%d%n",
                        record.operation,
                        record.durationNanos,
                        record.durationNanos / 1_000_000.0,
                        record.vertices,
                        record.edges);
            }
        }
    }

    public void printSummary() {
        System.out.println("\n=== Performance Metrics ===");
        for (MetricRecord record : records) {
            System.out.printf("%s: %.3f ms (vertices: %d, edges: %d)%n",
                    record.operation,
                    record.durationNanos / 1_000_000.0,
                    record.vertices,
                    record.edges);
        }
    }

    private static class MetricRecord {
        final String operation;
        final long durationNanos;
        final int vertices;
        final int edges;

        MetricRecord(String operation, long durationNanos, int vertices, int edges) {
            this.operation = operation;
            this.durationNanos = durationNanos;
            this.vertices = vertices;
            this.edges = edges;
        }
    }
}