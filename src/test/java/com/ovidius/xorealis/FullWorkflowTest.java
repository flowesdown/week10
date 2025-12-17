package com.ovidius.xorealis;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;

class FullWorkflowTest {

    @Test
    void executeWithExplanations() {
        List<Point> points = List.of(
                new Point(1, 4, 10), new Point(2, -4, 0), new Point(3, 2, -4),
                new Point(4, 8, -2), new Point(5, 1, -7), new Point(6, -1, 6),
                new Point(7, 6, 13), new Point(8, -2, 11), new Point(9, 8, 3),
                new Point(10, 1, 15), new Point(11, -6, 8)
        );

        List<Edge> edges = List.of(
                new Edge(1, 9, 3, 6, 5, 2, 3),   new Edge(2, 9, 4, 0, 6, 10, 15),
                new Edge(3, 3, 2, 7, 5, 17, 4),  new Edge(4, 2, 9, 2, 5, 5, 1),
                new Edge(5, 6, 2, 2, 1, 8, 6),   new Edge(6, 2, 11, 0, 1, 16, 7),
                new Edge(7, 8, 11, 1, 0, 8, 6),  new Edge(8, 6, 8, 1, 2, 5, 9),
                new Edge(9, 8, 9, 3, 2, 13, 4),  new Edge(10, 9, 1, 3, 0, 9, 11),
                new Edge(11, 1, 7, 4, 0, 14, 12), new Edge(12, 7, 10, 4, 0, 11, 13),
                new Edge(13, 8, 10, 0, 3, 7, 14),  new Edge(14, 10, 1, 4, 3, 12, 10),
                new Edge(15, 5, 4, 6, 0, 17, 2),   new Edge(16, 2, 5, 7, 0, 3, 15),
                new Edge(17, 3, 5, 6, 7, 1, 16)
        );

        Visualizer.printInitial(edges, points);

        var res1 = DcelPipeline.step1(points, edges);
        Visualizer.printStep1(res1.e(), res1.p());

        var e2 = DcelPipeline.step2(res1.e());
        Visualizer.printStep2(e2);

        var e3 = DcelPipeline.step3(e2);
        Visualizer.printStep3(e3, points.size());

        var e4 = DcelPipeline.updateW(e3, Map.of(9, 2, 13, 4), "PASUL 4");
        Visualizer.printFinalStep("DCEL - după pasul 4", e4);

        var e5 = DcelPipeline.updateW(e4, Map.of(4, 2, 10, 2, 16, 3, 17, 2), "PASUL 5");
        Visualizer.printFinalStep("DCEL - după pasul 5", e5);

        Visualizer.logHeader("CALCUL LANTURI (C1 - C6)");
        DcelPipeline.calcChain(e5, "C1", List.of(16, 6, 7, 13));
        DcelPipeline.calcChain(e5, "C2", List.of(16, 5, 8, 13));
        DcelPipeline.calcChain(e5, "C3", List.of(16, 4, 9, 13));
        DcelPipeline.calcChain(e5, "C4", List.of(17, 3, 4, 9, 13));
        DcelPipeline.calcChain(e5, "C5", List.of(17, 1, 10, 14));
        DcelPipeline.calcChain(e5, "C6", List.of(15, 2, 10, 11, 12));
    }
}