package org.example.service;

import org.example.model.ABCMetric;
import org.example.model.ClassMetrics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MetricsCalculator {
    public static final String JAVA_LANG_OBJECT = "java/lang/Object";

    public static Map<String, Integer> computeInheritanceDepths(Map<String, ClassMetrics> classesMetrics) {
        Map<String, Integer> inheritanceDepths = new HashMap<>();

        for (var className : classesMetrics.keySet()) {
            inheritanceDepths.put(className, computeInheritanceDepth(className, classesMetrics, new HashSet<>()));
        }

        return inheritanceDepths;
    }

    public static Map<String, Integer> computeOverriddenMethods(Map<String, ClassMetrics> classesMetrics) {
        Map<String, Integer> overriddenMethodsCount = new HashMap<>();

        for (var classMetrics : classesMetrics.values()) {
            var count = 0;

            for (var methodSignature : classMetrics.methodSignatures) {
                if (methodSignature.startsWith("<init>") || methodSignature.startsWith("<clinit>")) {
                    continue;
                }

                var accesses = classMetrics.methodsAccesses.get(methodSignature);

                if (accesses != null && ((accesses & org.objectweb.asm.Opcodes.ACC_PRIVATE) != 0 || (accesses & org.objectweb.asm.Opcodes.ACC_STATIC) != 0)) {
                    continue;
                }

                var superName = classMetrics.superName;
                var found = false;

                while (superName != null && !superName.equals(JAVA_LANG_OBJECT)) {
                    var superClassMetrics = classesMetrics.get(superName);

                    if (superClassMetrics == null) {
                        break;
                    }

                    var superAccesses = superClassMetrics.methodsAccesses.get(methodSignature);

                    if (superAccesses != null && (superAccesses & org.objectweb.asm.Opcodes.ACC_PRIVATE) == 0 && (superAccesses & org.objectweb.asm.Opcodes.ACC_STATIC) == 0) {
                        found = true;

                        break;
                    }

                    superName = superClassMetrics.superName;
                }

                if (found) {
                    count++;
                }
            }

            overriddenMethodsCount.put(classMetrics.name, count);
        }

        return overriddenMethodsCount;
    }

    public static ABCMetric computeTotalABCMetric(Map<String, ClassMetrics> classesMetrics) {
        var totalABCMetric = new ABCMetric();

        for (var classMetrics : classesMetrics.values()) {
            totalABCMetric.add(classMetrics.abcMetric);
        }

        return totalABCMetric;
    }

    private static int computeInheritanceDepth(String className, Map<String, ClassMetrics> classesMetrics, Set<String> seenClasses) {
        if (className == null || seenClasses.contains(className)) {
            return 0;
        }

        seenClasses.add(className);

        var classMetrics = classesMetrics.get(className);

        if (classMetrics == null || classMetrics.superName.equals(JAVA_LANG_OBJECT)) {
            return 0;
        }

        return 1 + computeInheritanceDepth(classMetrics.superName, classesMetrics, seenClasses);
    }
}