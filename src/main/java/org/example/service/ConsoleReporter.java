package org.example.service;

import org.example.model.MetricsReport;

public class ConsoleReporter {
    public static void printSummary(MetricsReport metricsReport) {
        System.out.println("====================================");
        System.out.println(" JAR Metrics");
        System.out.println("====================================");
        System.out.println("Analyzed JAR path: " + metricsReport.analyzedJarPath);
        System.out.println("------------------------------------");
        System.out.println("Max inheritance depth: " + metricsReport.maxInheritanceDepth);
        System.out.println("Average inheritance depth: " + String.format("%.3f", metricsReport.averageInheritanceDepth));
        System.out.println("Average fields per class: " + String.format("%.3f", metricsReport.averageFieldCount));
        System.out.println("Average overridden methods per class: " + String.format("%.3f", metricsReport.averageOverriddenMethodCount));
        System.out.println("------------------------------------");
        System.out.println("Total ABC metric:");
        System.out.println("  A = " + metricsReport.totalABCMetric.A);
        System.out.println("  B = " + metricsReport.totalABCMetric.B);
        System.out.println("  C = " + metricsReport.totalABCMetric.C);
        System.out.println("====================================");
    }
}