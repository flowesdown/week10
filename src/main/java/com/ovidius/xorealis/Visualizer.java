package com.ovidius.xorealis;

import java.util.*;
import java.util.stream.Collectors;

public class Visualizer {

    public static void logHeader(String title) {
        System.out.println("\n\n" + "=".repeat(60));
        System.out.println("   >>> LOGICA PROCESULUI: " + title);
        System.out.println("=".repeat(60));
    }

    public static void log(String message) {
        System.out.println(" [LOG] " + message);
    }

    public static void logSub(String message) {
        System.out.println("       -> " + message);
    }

    private static void printLine(int len) {
        System.out.println("-".repeat(len));
    }

    public static void printInitial(List<Edge> edges, List<Point> points) {
        System.out.println("\n=== DCEL inițial ===");
        System.out.println("\n[Vârfurile]");
        System.out.println("|  #   |  x  |  y  |");
        printLine(22);
        points.forEach(p -> System.out.println(p.toRowInitial()));

        System.out.println("\n[DCEL]");
        System.out.println("| Muchia | V1 | V2 | R1 | R2 | M1 | M2 |");
        printLine(42);
        edges.stream().sorted(Comparator.comparingInt(Edge::id))
                .forEach(e -> System.out.printf("| %-6d | %-2d | %-2d | %-2d | %-2d | %-2d | %-2d |%n",
                        e.id(), e.v1(), e.v2(), e.r1(), e.r2(), e.m1(), e.m2()));
    }

    public static void printStep1(List<Edge> edges, List<Point> points) {
        System.out.println("\n=== DCEL - după pasul 1 ===");
        System.out.println("\n[Vârfurile]");
        System.out.println("|  #   |  x  |  y  | Nou  |");
        printLine(29);
        points.stream()
                .sorted(Comparator.comparingInt(Point::y).thenComparingInt(Point::x))
                .forEach(p -> System.out.println(p.toRowStep1()));

        System.out.println("\n[DCEL]");
        System.out.println("| Muchia | V1-vechi | V2-vechi | V1 | V2 | R1 | R2 | M1 | M2 |");
        printLine(64);
        edges.stream().sorted(Comparator.comparingInt(Edge::id))
                .forEach(e -> System.out.printf("| %-6d | %-8d | %-8d | %-2d | %-2d | %-2d | %-2d | %-2d | %-2d |%n",
                        e.id(), e.v1Old(), e.v2Old(), e.v1(), e.v2(), e.r1(), e.r2(), e.m1(), e.m2()));
    }

    public static void printStep2(List<Edge> edges) {
        System.out.println("\n=== DCEL - după pasul 2 (Orientare V1 < V2) ===");
        System.out.println("| Muchia | V1 | V2 | R1 | R2 | M1 | M2 |");
        printLine(42);
        edges.stream().sorted(Comparator.comparingInt(Edge::id))
                .forEach(e -> System.out.printf("| %-6d | %-2d | %-2d | %-2d | %-2d | %-2d | %-2d |%n",
                        e.id(), e.v1(), e.v2(), e.r1(), e.r2(), e.m1(), e.m2()));
    }

    public static void printStep3(List<Edge> edges, int numVertices) {
        System.out.println("\n=== DCEL - pasul 3 (Obținerea mulțimilor A și B) ===");
        System.out.println("| Muchia | V1 | V2 | R1 | R2 | M1 | M2 | Ponderea w |");
        printLine(55);
        edges.stream().sorted(Comparator.comparingInt(Edge::id))
                .forEach(e -> System.out.printf("| %-6d | %-2d | %-2d | %-2d | %-2d | %-2d | %-2d | %-10d |%n",
                        e.id(), e.v1(), e.v2(), e.r1(), e.r2(), e.m1(), e.m2(), e.weight()));

        System.out.println("\n[Mulțimile A și B]");
        Map<Integer, List<Integer>> A = edges.stream().collect(Collectors.groupingBy(Edge::v1, Collectors.mapping(Edge::id, Collectors.toList())));
        Map<Integer, List<Integer>> B = edges.stream().collect(Collectors.groupingBy(Edge::v2, Collectors.mapping(Edge::id, Collectors.toList())));

        for(int i=1; i<=numVertices; i++) {
            System.out.printf("A_%-2d = %-12s  B_%-2d = %-12s%n", i, formatSet(A.get(i)), i, formatSet(B.get(i)));
        }
    }

    public static void printFinalStep(String title, List<Edge> edges) {
        System.out.println("\n=== " + title + " ===");
        System.out.println("| Muchia | V1 | V2 | R1 | R2 | M1 | M2 | Ponderea w |");
        printLine(55);
        edges.stream().sorted(Comparator.comparingInt(Edge::id))
                .forEach(e -> System.out.printf("| %-6d | %-2d | %-2d | %-2d | %-2d | %-2d | %-2d | %-10d |%n",
                        e.id(), e.v1(), e.v2(), e.r1(), e.r2(), e.m1(), e.m2(), e.weight()));
    }

    public static void printChain(String name, int sum, String formula) {
        System.out.printf("%s = {%s} = %d%n", name, formula, sum);
    }

    private static String formatSet(List<Integer> list) {
        if (list == null || list.isEmpty()) return "Ø";
        return "{" + list.stream().sorted().map(String::valueOf).collect(Collectors.joining(",")) + "}";
    }
}