package com.laputa.router.auto;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * Author by xpl, Date on 2021/4/15.
 */
public class ZeejMethodVisitor extends AdviceAdapter {
    private String methodName;

    protected ZeejMethodVisitor(int api, MethodVisitor methodVisitor, int access, String name, String descriptor) {
        super(api, methodVisitor, access, name, descriptor);
       this.methodName = name;

    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
        System.out.println("************ onMethodEnter *************");
        mv.visitLdcInsn("zeeeej");
        mv.visitLdcInsn("method->" + methodName);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "android/util/Log", "i", "(Ljava/lang/String;Ljava/lang/String;)I", false);
        mv.visitInsn(Opcodes.POP);

    }

    @Override
    protected void onMethodExit(int opcode) {
        mv.visitLdcInsn("TAG");
        mv.visitLdcInsn("this is end");
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "android/util/Log", "i", "(Ljava/lang/String;Ljava/lang/String;)I", false);
        mv.visitInsn(Opcodes.POP);
        System.out.println("************ onMethodExit *************");
        super.onMethodExit(opcode);
    }
}
