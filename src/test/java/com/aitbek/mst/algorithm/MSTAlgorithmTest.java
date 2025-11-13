package com.aitbek.mst.algorithm;

import com.aitbek.mst.model.Edge;
import com.aitbek.mst.model.Graph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MSTAlgorithmTest {
    private Graph graph;
    private MSTAlgorithm mstAlgorithm;

    @BeforeEach
    void setUp() {
        graph = Graph.createSampleGraph();
        mstAlgorithm = new MSTAlgorithm(graph);
    }

    @Test
    void testBuildMST() {
        List<Edge> mst = mstAlgorithm.buildMST();
        assertEquals(5, mst.size());
    }

    @Test
    void testMSTWeight() {
        List<Edge> mst = mstAlgorithm.buildMST();
        int totalWeight = mstAlgorithm.calculateMSTWeight(mst);
        assertEquals(11, totalWeight);
    }

    @Test
    void testGetComponentsAfterRemoval() {
        List<Edge> mst = mstAlgorithm.buildMST();
        Edge edgeToRemove = mst.get(0);

        List<Set<Integer>> components = mstAlgorithm.getComponentsAfterRemoval(edgeToRemove);
        assertEquals(2, components.size());
    }

    @Test
    void testFindReplacementEdge() {
        List<Edge> mst = mstAlgorithm.buildMST();
        Edge edgeToRemove = mst.get(0);

        Edge replacement = mstAlgorithm.findReplacementEdge(edgeToRemove);
        assertNotNull(replacement);
    }

    @Test
    void testGetMSTWithReplacement() {
        List<Edge> mst = mstAlgorithm.buildMST();
        Edge edgeToRemove = mst.get(0);
        Edge replacement = mstAlgorithm.findReplacementEdge(edgeToRemove);

        List<Edge> newMST = mstAlgorithm.getMSTWithReplacement(edgeToRemove, replacement);
        assertEquals(5, newMST.size());
        assertFalse(newMST.contains(edgeToRemove));
    }

    @Test
    void testSimpleGraph() {
        Graph simpleGraph = new Graph(3);
        simpleGraph.addEdge(0, 1, 1);
        simpleGraph.addEdge(1, 2, 2);
        simpleGraph.addEdge(0, 2, 3);

        MSTAlgorithm simpleMST = new MSTAlgorithm(simpleGraph);
        List<Edge> mst = simpleMST.buildMST();

        assertEquals(2, mst.size());
        assertEquals(3, simpleMST.calculateMSTWeight(mst));
    }

    @Test
    void testDisconnectedComponents() {
        Graph disconnected = new Graph(4);
        disconnected.addEdge(0, 1, 1);
        disconnected.addEdge(2, 3, 1);

        MSTAlgorithm mst = new MSTAlgorithm(disconnected);
        List<Edge> result = mst.buildMST();

        assertEquals(2, result.size());
    }

    @Test
    void testSingleEdgeGraph() {
        Graph singleEdge = new Graph(2);
        singleEdge.addEdge(0, 1, 5);

        MSTAlgorithm mst = new MSTAlgorithm(singleEdge);
        List<Edge> result = mst.buildMST();

        assertEquals(1, result.size());
        assertEquals(5, mst.calculateMSTWeight(result));
    }

    @Test
    void testGetCurrentMST() {
        mstAlgorithm.buildMST();
        List<Edge> current = mstAlgorithm.getCurrentMST();
        assertEquals(5, current.size());
    }
}