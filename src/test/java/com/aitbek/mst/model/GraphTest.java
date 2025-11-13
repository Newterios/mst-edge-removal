package com.aitbek.mst.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class GraphTest {
    private Graph graph;

    @BeforeEach
    void setUp() {
        graph = new Graph(4);
    }

    @Test
    void testGraphCreation() {
        assertEquals(4, graph.getVertices());
        assertEquals(0, graph.getEdges().size());
    }

    @Test
    void testAddEdge() {
        graph.addEdge(0, 1, 5);
        assertEquals(1, graph.getEdges().size());

        List<Edge> edges = graph.getEdges();
        Edge edge = edges.get(0);
        assertEquals(0, edge.getSource());
        assertEquals(1, edge.getDestination());
        assertEquals(5, edge.getWeight());
    }

    @Test
    void testMultipleEdges() {
        graph.addEdge(0, 1, 5);
        graph.addEdge(1, 2, 3);
        graph.addEdge(2, 3, 7);

        assertEquals(3, graph.getEdges().size());
    }

    @Test
    void testAdjacentEdges() {
        graph.addEdge(0, 1, 5);
        graph.addEdge(0, 2, 3);

        List<Edge> adjacent = graph.getAdjacentEdges(0);
        assertEquals(2, adjacent.size());
    }

    @Test
    void testSampleGraph() {
        Graph sampleGraph = Graph.createSampleGraph();
        assertEquals(6, sampleGraph.getVertices());
        assertEquals(8, sampleGraph.getEdges().size());
    }

    @Test
    void testEmptyGraph() {
        Graph emptyGraph = new Graph(0);
        assertEquals(0, emptyGraph.getVertices());
        assertEquals(0, emptyGraph.getEdges().size());
    }

    @Test
    void testSingleVertex() {
        Graph singleVertex = new Graph(1);
        assertEquals(1, singleVertex.getVertices());
        assertEquals(0, singleVertex.getEdges().size());
    }
}