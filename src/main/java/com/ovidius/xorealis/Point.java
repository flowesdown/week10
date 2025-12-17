package com.ovidius.xorealis;

public record Point(int oldId, int x, int y, int newId) {
    public Point(int oldId, int x, int y) {
        this(oldId, x, y, 0);
    }
    public Point withNewId(int newId) {
        return new Point(oldId, x, y, newId);
    }
    public String toRowStep1() {
        String nId = (newId > 0) ? "P" + newId : "-";
        return String.format("| P%-3d | %-3d | %-3d | %-4s |", oldId, x, y, nId);
    }
    public String toRowInitial() {
        return String.format("| P%-3d | %-3d | %-3d |", oldId, x, y);
    }
}