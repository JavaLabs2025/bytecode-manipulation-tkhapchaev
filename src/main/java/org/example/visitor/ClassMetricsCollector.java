package org.example.visitor;

import org.example.model.ClassMetrics;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.ASM9;

public class ClassMetricsCollector extends ClassVisitor {
    private final ClassMetrics classMetrics;

    public ClassMetricsCollector(ClassMetrics classMetrics) {
        super(ASM9);

        this.classMetrics = classMetrics;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        classMetrics.fieldCount++;

        return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        var key = ClassMetrics.methodKey(name, descriptor);

        classMetrics.methodSignatures.add(key);
        classMetrics.methodsAccesses.put(key, access);

        var methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);

        return new MethodMetricsCollector(methodVisitor, classMetrics.abcMetric);
    }
}