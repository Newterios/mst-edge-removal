package com.aitbek.mst.algorithm;

public class UnionFind {
    private final int[] parent;
    private final int[] rank;

    public UnionFind(int size) {
        parent = new int[size];
        rank = new int[size];
        for (int i = 0; i < size; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    public int find(int vertex) {
        if (parent[vertex] != vertex) {
            parent[vertex] = find(parent[vertex]);
        }
        return parent[vertex];
    }

    public boolean union(int vertex1, int vertex2) {
        int root1 = find(vertex1);
        int root2 = find(vertex2);

        if (root1 == root2) {
            return false;
        }

        if (rank[root1] < rank[root2]) {
            parent[root1] = root2;
        } else if (rank[root1] > rank[root2]) {
            parent[root2] = root1;
        } else {
            parent[root2] = root1;
            rank[root1]++;
        }

        return true;
    }

    public boolean isConnected(int vertex1, int vertex2) {
        return find(vertex1) == find(vertex2);
    }
}