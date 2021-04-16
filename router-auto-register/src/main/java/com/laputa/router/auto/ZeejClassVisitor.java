package com.laputa.router.auto;


import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Author by xpl, Date on 2021/4/15.
 */
public class ZeejClassVisitor extends ClassVisitor {
    private String className;
    private String superName;

    public ZeejClassVisitor(int api) {
        super(api);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.className = name;
        this.superName = superName;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = cv.visitMethod(access, name, descriptor, signature, exceptions);
        System.out.println("************ visitMethod *************");
        if (className.endsWith("MainActivity") && superName.endsWith("AppCompatActivity")) {
            if (name.startsWith("testAsm")) {
                return new ZeejMethodVisitor(Opcodes.ASM5, methodVisitor, access, name, descriptor);
            }
        }

        return methodVisitor;
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
        System.out.println("************ visitEnd *************");
    }
}
