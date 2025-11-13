package com.aitbek.mst.algorithm;

import com.aitbek.mst.model.Edge;
import com.aitbek.mst.model.Graph;

import java.util.*;

public class MSTAlgorithm {
    private final Graph graph;
    private List<Edge> mstEdges;

    public MSTAlgorithm(Graph graph) {
        this.graph = graph;
        this.mstEdges = new ArrayList<>();
    }

    public List<Edge> buildMST() {
        List<Edge> edges = graph.getEdges();
        Collections.sort(edges);

        UnionFind uf = new UnionFind(graph.getVertices());
        mstEdges = new ArrayList<>();

        for (Edge edge : edges) {
            if (uf.union(edge.getSource(), edge.getDestination())) {
                mstEdges.add(edge);
            }

            if (mstEdges.size() == graph.getVertices() - 1) {
                break;
            }
        }

        return new ArrayList<>(mstEdges);
    }

    public List<Set<Integer>> getComponentsAfterRemoval(Edge removedEdge) {
        UnionFind uf = new UnionFind(graph.getVertices());

        for (Edge edge : mstEdges) {
            if (!edge.equals(removedEdge)) {
                uf.union(edge.getSource(), edge.getDestination());
            }
        }

        Map<Integer, Set<Integer>> componentMap = new HashMap<>();
        for (int i = 0; i < graph.getVertices(); i++) {
            int root = uf.find(i);
            componentMap.putIfAbsent(root, new HashSet<>());
            componentMap.get(root).add(i);
        }

        return new ArrayList<>(componentMap.values());
    }

    public Edge findReplacementEdge(Edge removedEdge) {
        List<Set<Integer>> components = getComponentsAfterRemoval(removedEdge);

        if (components.size() != 2) {
            return null;
        }

        Set<Integer> component1 = components.get(0);
        Set<Integer> component2 = components.get(1);

        Edge bestReplacement = null;
        int minWeight = Integer.MAX_VALUE;

        for (Edge edge : graph.getEdges()) {
            if (edge.equals(removedEdge)) {
                continue;
            }

            boolean connects = (component1.contains(edge.getSource()) && component2.contains(edge.getDestination())) ||
                    (component2.contains(edge.getSource()) && component1.contains(edge.getDestination()));

            if (connects && edge.getWeight() < minWeight) {
                minWeight = edge.getWeight();
                bestReplacement = edge;
            }
        }

        return bestReplacement;
    }

    public List<Edge> getMSTWithReplacement(Edge removedEdge, Edge replacementEdge) {
        List<Edge> newMST = new ArrayList<>();

        for (Edge edge : mstEdges) {
            if (!edge.equals(removedEdge)) {
                newMST.add(edge);
            }
        }

        if (replacementEdge != null) {
            newMST.add(replacementEdge);
        }

        return newMST;
    }

    public int calculateMSTWeight(List<Edge> edges) {
        return edges.stream().mapToInt(Edge::getWeight).sum();
    }

    public List<Edge> getCurrentMST() {
        return new ArrayList<>(mstEdges);
    }
}