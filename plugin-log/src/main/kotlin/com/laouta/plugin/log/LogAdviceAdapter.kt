package com.laouta.plugin.log

import com.laputa.plugin.log.Cat
import jdk.internal.org.objectweb.asm.Opcodes
import jdk.internal.org.objectweb.asm.Type
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.commons.AdviceAdapter

/**
 * Author by xpl, Date on 2021/4/16.
 */
class LogAdviceAdapter(
    methodVisitor: MethodVisitor, access: Int, name: String?,
    descriptor: String?
) : AdviceAdapter(
    Opcodes.ASM5, methodVisitor,
    access,
    name, descriptor
) {

    private var inject: Boolean = false

    override fun visitCode() {
        super.visitCode()
        println("               -> LogAdviceAdapter::visitCode")
    }

    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
        println("               -> LogAdviceAdapter::visitAnnotation ${descriptor}")
        if ("Lcom/laputa/module01/util/Logger;" == descriptor
            // todo 怎么直接用注解类型？
            // || Type.getDescriptor(com.laputa.annotations.Dog::class.java) == descriptor
            // todo 只能用这种方式么？如何获取注解上的信息？
            || "Lcom/laputa/annotations/Dog;" == descriptor
            || Type.getDescriptor(Cat::class.java) == descriptor
        ) {
            inject = true
        }
        return super.visitAnnotation(descriptor, visible)
    }

    override fun onMethodEnter() {
        super.onMethodEnter()
        println("               -> LogAdviceAdapter::onMethodEnter ")
        if (inject) {

            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitLdcInsn("========start=========" + name + "==>des:");
            mv.visitMethodInsn(
                INVOKEVIRTUAL, "java/io/PrintStream", "println",
                "(Ljava/lang/String;)V", false
            );

            mv.visitLdcInsn(name);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
            mv.visitMethodInsn(
                INVOKESTATIC, "com/laputa/module01/util/TimeCache", "setStartTime",
                "(Ljava/lang/String;J)V", false
            );
        }
    }


    override fun onMethodExit(opcode: Int) {
        super.onMethodExit(opcode)
        println("               -> LogAdviceAdapter::onMethodExit")
        if (inject) {
            mv.visitLdcInsn(name);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
            mv.visitMethodInsn(
                INVOKESTATIC, "com/laputa/module01/util/TimeCache", "setEndTime",
                "(Ljava/lang/String;J)V", false
            );

            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitLdcInsn(name);
            mv.visitMethodInsn(
                INVOKESTATIC, "com/laputa/module01/util/TimeCache", "getCostTime",
                "(Ljava/lang/String;)Ljava/lang/String;", false
            );
            mv.visitMethodInsn(
                INVOKEVIRTUAL, "java/io/PrintStream", "println",
                "(Ljava/lang/String;)V", false
            );

            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitLdcInsn("========end=========");
            mv.visitMethodInsn(
                INVOKEVIRTUAL, "java/io/PrintStream", "println",
                "(Ljava/lang/String;)V", false
            );
        }
    }

    override fun visitEnd() {
        super.visitEnd()
    }

}