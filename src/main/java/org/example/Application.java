package org.example;

import org.example.model.ClassMetrics;
import org.example.model.MetricsReport;
import org.example.service.ConsoleReporter;
import org.example.service.JarAnalyzer;
import org.example.service.JsonReporter;
import org.example.service.MetricsCalculator;

import java.io.IOException;
import java.util.Map;

public class Application {
    public static void main(String[] arguments) {
        if (arguments.length != 1) {
            System.out.println("Usage: java -jar jar-metrics.jar <path-to-jar>");

            return;
        }

        var jarPath = arguments[0];
        var outputJsonFilePath = jarPath.replaceAll("\\.jar$", ".json");

        if (!jarPath.endsWith(".jar")) {
            throw new IllegalArgumentException("Input file is not .jar");
        }

        var jarAnalyzer = new JarAnalyzer(jarPath);
        Map<String, ClassMetrics> classesMetrics;

        try {
            classesMetrics = jarAnalyzer.analyze();
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }

        var inheritanceDepths = MetricsCalculator.computeInheritanceDepths(classesMetrics);
        var overriddenMethods = MetricsCalculator.computeOverriddenMethods(classesMetrics);
        var totalABCMetric = MetricsCalculator.computeTotalABCMetric(classesMetrics);

        var metricsReport = MetricsReport.generate(jarPath, classesMetrics, inheritanceDepths, overriddenMethods, totalABCMetric);

        ConsoleReporter.printSummary(metricsReport);
        JsonReporter.writeSummary(metricsReport, outputJsonFilePath);
    }
}