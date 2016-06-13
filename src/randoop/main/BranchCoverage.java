package randoop.main;

/**
 * Created by alipourm on 6/13/16.
 */


import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.objectweb.asm.*;


public class BranchCoverage extends MethodVisitor {
    Logger logger = Logger.getLogger(BranchCoverage.class);
    String name;
    int access;
    static int branchID = 0;// Global branch id
    static int switchLabelID = 0;// Global switch label id
    static int lineNum = -1;
    static Set<Label> switchLabels = new HashSet<Label>();




    public static void main(String[] args){
        InputStream in= BranchCoverage.class.getResourceAsStream("/java/lang/String.class");
        try {
            ClassReader cr = new ClassReader(in);
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);

           // MethodVisitor mv = new BranchCoverage();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public BranchCoverage(){
        super(0);
    }

    public BranchCoverage(MethodVisitor mv, int access, String n) {
        super(0,mv);
        name = n;
        this.access = access;
    }

    @Override
    public void visitCode() {
        mv.visitCode();
        switchLabels.clear();
    }

    @Override
    public void visitJumpInsn(int type, Label target) {
        if (type == org.objectweb.asm.Opcodes.GOTO) {
            mv.visitJumpInsn(type, target);
            return;
        }
        branchID++;
        if (true) {
            this.visitMethodInsn(Opcodes.INVOKESTATIC,
                    TRACER_CLASS_NAME, "getInstance", "()L"
                            + TRACER_CLASS_NAME + ";");
            mv.visitLdcInsn("false");
            mv.visitLdcInsn(name + ":" + lineNum + ":" + branchID + "");
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                    TRACER_CLASS_NAME, "logBranchInfo",
                    "(Ljava/lang/String;Ljava/lang/String;)V");
        }
        mv.visitJumpInsn(type, target);
        this.visitMethodInsn(Opcodes.INVOKESTATIC,
                    TRACER_CLASS_NAME, "getInstance", "()L"
                            + TRACER_CLASS_NAME + ";");
            mv.visitLdcInsn("true");
            mv.visitLdcInsn(name + ":" + lineNum + ":" + branchID + "");
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                    TRACER_CLASS_NAME, "logBranchInfo",
                    "(Ljava/lang/String;Ljava/lang/String;)V");

    }

    @Override
    public void visitTableSwitchInsn(int type, int arg2, Label defaultTarget,
                                     Label[] larray) {
        for (Label l : larray)
            switchLabels.add(l);
        switchLabels.remove(defaultTarget);
        mv.visitTableSwitchInsn(type, arg2, defaultTarget, larray);

    }

    @Override
    public void visitLookupSwitchInsn(Label defaultTarget, int[] iarray,
                                      Label[] larray) {
        for (Label l : larray)
            switchLabels.add(l);
        switchLabels.remove(defaultTarget);
        mv.visitLookupSwitchInsn(defaultTarget, iarray, larray);
    }
    public static final String TRACER_CLASS_NAME = "randoop/main/Tracer";


    @Override
    public void visitLabel(Label l) {
        mv.visitLabel(l);
        if (switchLabels.contains(l) ) {
            switchLabelID++;
            this.visitMethodInsn(Opcodes.INVOKESTATIC,
                    TRACER_CLASS_NAME, "getInstance", "()L"
                            + TRACER_CLASS_NAME + ";");
            mv.visitLdcInsn("");
            mv.visitLdcInsn(name + ":" + (lineNum + 1) + ":" + switchLabelID);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                    TRACER_CLASS_NAME, "logMethodInfo",
                    "(Ljava/lang/String;Ljava/lang/String;)V");
        }
    }

    @Override
    public void visitLineNumber(int line, Label start) {
        mv.visitLineNumber(line, start);
        lineNum = line;
    }

    public void visitMaxs(int maxStack, int maxLocals) {
        mv.visitMaxs(maxStack + 4, maxLocals);
    }

}