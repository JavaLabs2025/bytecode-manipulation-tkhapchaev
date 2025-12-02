package org.example.model;

import java.util.Map;

public class MetricsReport {
    public String analyzedJarPath;

    public double averageInheritanceDepth;
    public double averageFieldCount;
    public double averageOverriddenMethodCount;

    public int maxInheritanceDepth;

    public ABCMetric totalABCMetric;

    private MetricsReport(String analyzedJarPath, double averageInheritanceDepth, double averageFieldCount, double averageOverriddenMethodCount, int maxInheritanceDepth, ABCMetric totalABCMetric) {
        this.analyzedJarPath = analyzedJarPath;

        this.averageInheritanceDepth = averageInheritanceDepth;
        this.averageFieldCount = averageFieldCount;
        this.averageOverriddenMethodCount = averageOverriddenMethodCount;

        this.maxInheritanceDepth = maxInheritanceDepth;

        this.totalABCMetric = totalABCMetric;
    }

    public static MetricsReport generate(String jarPath, Map<String, ClassMetrics> classesMetrics, Map<String, Integer> inheritanceDepths, Map<String, Integer> overriddenMethods, ABCMetric totalABCMetric) {
        var averageInheritanceDepth = inheritanceDepths.values().stream().mapToInt(Integer::intValue).average().orElse(0.0);

        var averageFieldCount = classesMetrics.values().stream().mapToInt(classMetrics -> classMetrics.fieldCount).average().orElse(0.0);

        var averageOverriddenMethodCount = overriddenMethods.values().stream().mapToInt(Integer::intValue).average().orElse(0.0);

        var maxInheritanceDepth = inheritanceDepths.values().stream().mapToInt(Integer::intValue).max().orElse(0);

        return new MetricsReport(jarPath, averageInheritanceDepth, averageFieldCount, averageOverriddenMethodCount, maxInheritanceDepth, totalABCMetric);
    }
}