package com.aitbek.mst.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EdgeTest {

    @Test
    void testEdgeCreation() {
        Edge edge = new Edge(0, 1, 5);
        assertEquals(0, edge.getSource());
        assertEquals(1, edge.getDestination());
        assertEquals(5, edge.getWeight());
    }

    @Test
    void testEdgeComparison() {
        Edge edge1 = new Edge(0, 1, 3);
        Edge edge2 = new Edge(2, 3, 5);
        Edge edge3 = new Edge(4, 5, 3);

        assertTrue(edge1.compareTo(edge2) < 0);
        assertTrue(edge2.compareTo(edge1) > 0);
        assertEquals(0, edge1.compareTo(edge3));
    }

    @Test
    void testEdgeEquality() {
        Edge edge1 = new Edge(0, 1, 5);
        Edge edge2 = new Edge(0, 1, 5);
        Edge edge3 = new Edge(1, 0, 5);
        Edge edge4 = new Edge(0, 1, 6);

        assertEquals(edge1, edge2);
        assertEquals(edge1, edge3);
        assertNotEquals(edge1, edge4);
    }

    @Test
    void testEdgeHashCode() {
        Edge edge1 = new Edge(0, 1, 5);
        Edge edge2 = new Edge(1, 0, 5);
        Edge edge3 = new Edge(0, 1, 6);

        assertEquals(edge1.hashCode(), edge2.hashCode());
        assertNotEquals(edge1.hashCode(), edge3.hashCode());
    }

    @Test
    void testEdgeToString() {
        Edge edge = new Edge(0, 1, 5);
        String result = edge.toString();
        assertTrue(result.contains("0"));
        assertTrue(result.contains("1"));
        assertTrue(result.contains("5"));
    }
}