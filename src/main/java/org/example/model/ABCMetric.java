package org.example.model;

public class ABCMetric {
    public long A;
    public long B;
    public long C;

    public ABCMetric() {
        A = 0;
        B = 0;
        C = 0;
    }

    public void add(ABCMetric otherABCMetric) {
        this.A += otherABCMetric.A;
        this.B += otherABCMetric.B;
        this.C += otherABCMetric.C;
    }
}