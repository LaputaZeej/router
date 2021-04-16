package com.laouta.plugin.log

import jdk.internal.org.objectweb.asm.Opcodes
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.*

/**
 * Author by xpl, Date on 2021/4/16.
 */
class LogMethodVisitor(methodVisitor: MethodVisitor) :
    MethodVisitor(Opcodes.ASM5, methodVisitor) {

    override fun visitCode() {
        super.visitCode()
        println("               -> LogMethodVisitor::visitMethod")
        mv.visitLdcInsn("MainActivity");
        mv.visitLdcInsn("ttt");
        mv.visitMethodInsn(
            INVOKESTATIC,
            "android/util/Log",
            "i",
            "(Ljava/lang/String;Ljava/lang/String;)I",
            false
        );
        mv.visitInsn(POP)

    }

    //指令操作,这里可以判断拦截return,并在方法尾部插入字节码
    override fun visitInsn(opcode: Int) {
        println("               -> LogMethodVisitor::visitInsn : opcode = $opcode")
        if (opcode == ARETURN || opcode == RETURN) {
            // android.util.Log.i("MainActivity", "ttt run3")
            mv.visitLdcInsn("MainActivity");
            mv.visitLdcInsn("tttInsn");
            mv.visitMethodInsn(
                INVOKESTATIC,
                "android/util/Log",
                "i",
                "(Ljava/lang/String;Ljava/lang/String;)I",
                false
            );
            mv.visitInsn(POP);
        }
        super.visitInsn(opcode)
    }

    override fun visitMaxs(maxStack: Int, maxLocals: Int) {
        super.visitMaxs(maxStack, maxLocals)
    }

    override fun visitEnd() {
        super.visitEnd()
        println("               -> LogMethodVisitor::visitEnd :)")
    }

}