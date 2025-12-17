package com.ovidius.xorealis;

import java.util.*;
import java.util.stream.Collectors;

public class DcelPipeline {

    public static Pair<List<Point>, List<Edge>> step1(List<Point> rawP, List<Edge> rawE) {
        Visualizer.logHeader("STEP 1: SORTARE & RENUMEROTARE");

        Visualizer.log("1. Sortare vârfuri după coordonata Y (ascendent), apoi X (ascendent)...");
        List<Point> sorted = rawP.stream()
                .sorted(Comparator.comparingInt(Point::y).thenComparingInt(Point::x))
                .toList();

        List<Point> mappedP = new ArrayList<>();
        Map<Integer, Integer> oldToNew = new HashMap<>();

        Visualizer.log("2. Atribuire ID-uri noi:");
        int id = 1;
        for (Point p : sorted) {
            Visualizer.logSub(String.format("P_vechi %-2d (y=%3d)  ---> P_nou %-2d", p.oldId(), p.y(), id));
            mappedP.add(p.withNewId(id));
            oldToNew.put(p.oldId(), id);
            id++;
        }

        Visualizer.log("3. Actualizare muchii cu noile ID-uri:");
        List<Edge> mappedE = new ArrayList<>();
        for (Edge e : rawE) {
            int newV1 = oldToNew.get(e.v1());
            int newV2 = oldToNew.get(e.v2());
            Visualizer.logSub(String.format("Muchia %-2d: %2d-%-2d  --->  %2d-%-2d", e.id(), e.v1(), e.v2(), newV1, newV2));
            mappedE.add(new Edge(e.id(), newV1, newV2, e.v1(), e.v2(), e.r1(), e.r2(), e.m1(), e.m2(), 0));
        }

        return new Pair<>(mappedP, mappedE);
    }

    public static List<Edge> step2(List<Edge> edges) {
        Visualizer.logHeader("STEP 2: ORIENTARE (V1 < V2)");
        Visualizer.log("Verificăm fiecare muchie. Dacă V1 > V2, inversăm vârfurile și regiunile.");

        List<Edge> processed = new ArrayList<>();
        for (Edge e : edges) {
            if (e.v1() > e.v2()) {
                Visualizer.logSub(String.format("Muchia %-2d: %d-%d (V1 > V2) -> SWAP! -> %d-%d | R1,R2 si M1,M2 inversate",
                        e.id(), e.v1(), e.v2(), e.v2(), e.v1()));

                processed.add(new Edge(e.id(), e.v2(), e.v1(), e.v1Old(), e.v2Old(), e.r2(), e.r1(), e.m2(), e.m1(), e.weight()));
            } else {
                Visualizer.logSub(String.format("Muchia %-2d: %d-%d (OK)", e.id(), e.v1(), e.v2()));
                processed.add(e);
            }
        }
        return processed;
    }

    public static List<Edge> step3(List<Edge> edges) {
        Visualizer.logHeader("STEP 3: INITIALIZARE PONDERI");
        Visualizer.log("Setam greutatea implicita w=1 pentru toate muchiile.");
        return edges.stream().map(e -> e.withWeight(1)).toList();
    }

    public static List<Edge> updateW(List<Edge> edges, Map<Integer, Integer> map, String stepName) {
        Visualizer.logHeader(stepName + ": ACTUALIZARE PONDERI");
        List<Edge> updated = new ArrayList<>();
        for (Edge e : edges) {
            if (map.containsKey(e.id())) {
                int newW = map.get(e.id());
                Visualizer.logSub(String.format("Muchia %-2d: Ponderea w se schimba din %d in %d", e.id(), e.weight(), newW));
                updated.add(e.withWeight(newW));
            } else {
                updated.add(e);
            }
        }
        return updated;
    }

    public static void calcChain(List<Edge> edges, String name, List<Integer> ids) {
        Map<Integer, Edge> map = edges.stream().collect(Collectors.toMap(Edge::id, e -> e));
        int sum = 0;
        List<String> formula = new ArrayList<>();

        System.out.print("   Calcul " + name + ": ");
        for (int id : ids) {
            int w = map.get(id).weight();
            sum += w;
            formula.add(String.valueOf(id));
            System.out.print(w + (ids.indexOf(id) == ids.size()-1 ? "" : " + "));
        }
        System.out.println(" = " + sum);

        Visualizer.printChain(name, sum, String.join(",", formula));
    }

    public record Pair<A, B>(A p, B e) {}
}