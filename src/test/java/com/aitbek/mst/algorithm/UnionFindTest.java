package com.aitbek.mst.algorithm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UnionFindTest {
    private UnionFind uf;

    @BeforeEach
    void setUp() {
        uf = new UnionFind(5);
    }

    @Test
    void testInitialState() {
        for (int i = 0; i < 5; i++) {
            assertEquals(i, uf.find(i));
        }
    }

    @Test
    void testUnion() {
        assertTrue(uf.union(0, 1));
        assertTrue(uf.isConnected(0, 1));
    }

    @Test
    void testUnionReturnsFalseForSameComponent() {
        uf.union(0, 1);
        assertFalse(uf.union(0, 1));
    }

    @Test
    void testMultipleUnions() {
        uf.union(0, 1);
        uf.union(1, 2);
        uf.union(3, 4);

        assertTrue(uf.isConnected(0, 2));
        assertTrue(uf.isConnected(3, 4));
        assertFalse(uf.isConnected(0, 3));
    }

    @Test
    void testPathCompression() {
        uf.union(0, 1);
        uf.union(1, 2);
        uf.union(2, 3);

        int root = uf.find(3);
        assertEquals(root, uf.find(0));
        assertEquals(root, uf.find(1));
        assertEquals(root, uf.find(2));
    }

    @Test
    void testIsConnected() {
        assertFalse(uf.isConnected(0, 1));
        uf.union(0, 1);
        assertTrue(uf.isConnected(0, 1));
        assertTrue(uf.isConnected(1, 0));
    }

    @Test
    void testLargeUnionFind() {
        UnionFind largeUF = new UnionFind(1000);

        for (int i = 0; i < 999; i++) {
            largeUF.union(i, i + 1);
        }

        assertTrue(largeUF.isConnected(0, 999));
    }
}