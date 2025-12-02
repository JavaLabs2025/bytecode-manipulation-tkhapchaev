package org.example.service;

import org.example.model.ClassMetrics;
import org.example.visitor.ClassMetricsCollector;
import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarFile;

public class JarAnalyzer {
    private final String jarPath;

    public JarAnalyzer(String jarPath) {
        this.jarPath = jarPath;
    }

    public Map<String, ClassMetrics> analyze() throws IOException {
        Map<String, ClassMetrics> classes = new HashMap<>();

        try (var jarFile = new JarFile(jarPath)) {
            var jarEntryEnumeration = jarFile.entries();

            while (jarEntryEnumeration.hasMoreElements()) {
                var jarEntry = jarEntryEnumeration.nextElement();

                if (!jarEntry.getName().endsWith(".class")) {
                    continue;
                }

                try (var inputStream = jarFile.getInputStream(jarEntry)) {
                    var classReader = new ClassReader(inputStream);

                    var className = classReader.getClassName();
                    var superName = classReader.getSuperName();

                    var isInterface = (classReader.getAccess() & org.objectweb.asm.Opcodes.ACC_INTERFACE) != 0;

                    var classMetrics = new ClassMetrics(className, superName, isInterface);
                    var classMetricsCollector = new ClassMetricsCollector(classMetrics);

                    classReader.accept(classMetricsCollector, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
                    classes.put(className, classMetrics);
                }
            }
        }

        return classes;
    }
}