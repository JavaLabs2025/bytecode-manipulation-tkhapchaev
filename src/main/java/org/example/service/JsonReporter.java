package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.MetricsReport;

import java.io.FileWriter;
import java.io.IOException;

public class JsonReporter {
    public static void writeSummary(MetricsReport metricsReport, String outputFilePath) {
        var objectMapper = new ObjectMapper();
        var rootNode = objectMapper.createObjectNode();

        rootNode.put("analyzedJarPath", metricsReport.analyzedJarPath);
        rootNode.put("maxInheritanceDepth", metricsReport.maxInheritanceDepth);
        rootNode.put("averageInheritanceDepth", metricsReport.averageInheritanceDepth);
        rootNode.put("averageFieldsPerClass", metricsReport.averageFieldCount);
        rootNode.put("averageOverriddenMethodsPerClass", metricsReport.averageOverriddenMethodCount);

        var totalABCMetricNode = rootNode.putObject("totalABCMetric");

        totalABCMetricNode.put("A", metricsReport.totalABCMetric.A);
        totalABCMetricNode.put("B", metricsReport.totalABCMetric.B);
        totalABCMetricNode.put("C", metricsReport.totalABCMetric.C);

        try (var fileWriter = new FileWriter(outputFilePath)) {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(fileWriter, rootNode);
            System.out.println("Metrics were written to " + outputFilePath);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }
}