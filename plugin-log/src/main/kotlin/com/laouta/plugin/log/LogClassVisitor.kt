package com.laouta.plugin.log

import jdk.internal.org.objectweb.asm.Opcodes
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor

/**
 * Author by xpl, Date on 2021/4/16.
 */
class LogClassVisitor(classVisitor: ClassVisitor) :
    org.objectweb.asm.ClassVisitor(Opcodes.ASM5, classVisitor) {
    private var className: String? = null
    private var forTimeCache: Boolean = true

    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        super.visit(version, access, name, signature, superName, interfaces)
        className = name
        println("           -> LogClassVisitor::visit")
    }


    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {


        val visitMethod = super.visitMethod(access, name, descriptor, signature, exceptions)
        if (forTimeCache) {
            println("           -> LogClassVisitor::visitMethod forTimeCache = true")
            return LogAdviceAdapter(visitMethod, access, name, descriptor)
        }
        if (className?.endsWith("MainActivity") == true && (name == "onCreate" || name == "testAsm")) {//过滤需要操作的类名和方法名
            //替换成自定义的方法扫描
            println("           -> LogClassVisitor::visitMethod")
            return LogMethodVisitor(visitMethod)
        }

        return visitMethod
    }
}