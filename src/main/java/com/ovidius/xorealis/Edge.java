package com.ovidius.xorealis;

public record Edge(
        int id,
        int v1, int v2,
        int v1Old, int v2Old,
        int r1, int r2,
        int m1, int m2,
        int weight
) {
    public Edge(int id, int v1, int v2, int r1, int r2, int m1, int m2) {
        this(id, v1, v2, v1, v2, r1, r2, m1, m2, 0);
    }
    public Edge withWeight(int w) {
        return new Edge(id, v1, v2, v1Old, v2Old, r1, r2, m1, m2, w);
    }
}