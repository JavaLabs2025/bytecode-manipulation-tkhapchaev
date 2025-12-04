package org.example.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ClassMetrics {
    public final String name;
    public final String superName;

    public final boolean isInterface;

    public final Map<String, Integer> methodsAccesses;
    public final Set<String> methodSignatures;

    public int fieldCount;

    public ABCMetric abcMetric;

    public ClassMetrics(String name, String superName, boolean isInterface) {
        this.name = name;
        this.superName = superName;
        this.isInterface = isInterface;

        methodsAccesses = new HashMap<>();
        methodSignatures = new HashSet<>();

        fieldCount = 0;
        abcMetric = new ABCMetric();
    }

    public static String getMethodKey(String name, String descriptor) {
        return name + ":" + descriptor;
    }
}