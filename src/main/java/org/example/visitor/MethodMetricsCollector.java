package org.example.visitor;

import org.example.model.ABCMetric;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class MethodMetricsCollector extends MethodVisitor {
    private final ABCMetric abcMetric;

    public MethodMetricsCollector(MethodVisitor methodVisitor, ABCMetric abcMetric) {
        super(ASM9, methodVisitor);

        this.abcMetric = abcMetric;
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        if (opcode == ISTORE || opcode == LSTORE || opcode == FSTORE || opcode == DSTORE || opcode == ASTORE) {
            abcMetric.A++;
        }

        super.visitVarInsn(opcode, var);
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        if (opcode == PUTFIELD || opcode == PUTSTATIC) {
            // abcMetric.A++;
        }

        super.visitFieldInsn(opcode, owner, name, descriptor);
    }

    @Override
    public void visitIincInsn(int var, int increment) {
        abcMetric.A++;

        super.visitIincInsn(var, increment);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        if (!name.equals("<init>") && !name.equals("<clinit>")) {
            abcMetric.B++;
        }

        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String descriptor, Handle bsm, Object... bsmArgs) {
        abcMetric.B++;

        super.visitInvokeDynamicInsn(name, descriptor, bsm, bsmArgs);
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        if (opcode != GOTO) {
            abcMetric.C++;
        }

        super.visitJumpInsn(opcode, label);
    }

    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
        abcMetric.B++;

        super.visitTableSwitchInsn(min, max, dflt, labels);
    }

    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        abcMetric.B++;

        super.visitLookupSwitchInsn(dflt, keys, labels);
    }
}