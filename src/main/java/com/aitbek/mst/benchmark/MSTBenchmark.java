package com.aitbek.mst.benchmark;

import com.aitbek.mst.algorithm.MSTAlgorithm;
import com.aitbek.mst.model.Edge;
import com.aitbek.mst.model.Graph;
import org.openjdk.jmh.annotations.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
public class MSTBenchmark {

    @Param({"10", "50", "100", "500"})
    private int vertices;

    private Graph graph;
    private MSTAlgorithm mstAlgorithm;
    private Edge edgeToRemove;

    @Setup(Level.Trial)
    public void setUp() {
        graph = createRandomGraph(vertices, vertices * 2);
        mstAlgorithm = new MSTAlgorithm(graph);
        List<Edge> mst = mstAlgorithm.buildMST();
        if (!mst.isEmpty()) {
            edgeToRemove = mst.get(0);
        }
    }

    @Benchmark
    public List<Edge> benchmarkBuildMST() {
        MSTAlgorithm algo = new MSTAlgorithm(graph);
        return algo.buildMST();
    }

    @Benchmark
    public Edge benchmarkFindReplacement() {
        return mstAlgorithm.findReplacementEdge(edgeToRemove);
    }

    private Graph createRandomGraph(int vertices, int edges) {
        Graph g = new Graph(vertices);
        Random random = new Random(42);

        for (int i = 0; i < vertices - 1; i++) {
            g.addEdge(i, i + 1, random.nextInt(100) + 1);
        }

        int additionalEdges = edges - (vertices - 1);
        for (int i = 0; i < additionalEdges; i++) {
            int src = random.nextInt(vertices);
            int dst = random.nextInt(vertices);
            if (src != dst) {
                g.addEdge(src, dst, random.nextInt(100) + 1);
            }
        }

        return g;
    }
}