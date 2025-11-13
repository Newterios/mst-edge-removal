package com.aitbek.mst.metrics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PerformanceMetricsTest {
    private PerformanceMetrics metrics;

    @BeforeEach
    void setUp() {
        metrics = new PerformanceMetrics();
    }

    @Test
    void testRecordOperation() {
        assertDoesNotThrow(() -> {
            metrics.recordOperation("Test Operation", 1000000L, 10, 20);
        });
    }

    @Test
    void testMultipleRecords() {
        metrics.recordOperation("Operation 1", 1000000L, 10, 20);
        metrics.recordOperation("Operation 2", 2000000L, 20, 30);
        metrics.recordOperation("Operation 3", 3000000L, 30, 40);

        assertDoesNotThrow(() -> metrics.printSummary());
    }

    @Test
    void testExportToCSV(@TempDir Path tempDir) throws IOException {
        metrics.recordOperation("Build MST", 1500000L, 10, 15);
        metrics.recordOperation("Find Replacement", 500000L, 10, 15);

        Path csvFile = tempDir.resolve("test_metrics.csv");
        metrics.exportToCSV(csvFile.toString());

        assertTrue(Files.exists(csvFile));

        List<String> lines = Files.readAllLines(csvFile);
        assertEquals(3, lines.size());
        assertTrue(lines.get(0).contains("Operation"));
        assertTrue(lines.get(1).contains("Build MST"));
        assertTrue(lines.get(2).contains("Find Replacement"));
    }

    @Test
    void testCSVFormat(@TempDir Path tempDir) throws IOException {
        metrics.recordOperation("Test", 1000000L, 5, 10);

        Path csvFile = tempDir.resolve("format_test.csv");
        metrics.exportToCSV(csvFile.toString());

        List<String> lines = Files.readAllLines(csvFile);
        String[] headers = lines.get(0).split(",");

        assertEquals(5, headers.length);
        assertEquals("Operation", headers[0]);
        assertEquals("Duration(ns)", headers[1]);
    }

    @Test
    void testPrintSummary() {
        metrics.recordOperation("Test 1", 1000000L, 5, 10);
        metrics.recordOperation("Test 2", 2000000L, 10, 20);

        assertDoesNotThrow(() -> metrics.printSummary());
    }
}