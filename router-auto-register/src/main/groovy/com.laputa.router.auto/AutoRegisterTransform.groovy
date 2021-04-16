package com.laputa.router.auto

import com.android.annotations.NonNull
import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
// https://www.ctolib.com/topics-115830.html
class AutoRegisterTransform extends Transform   {

    @Override
    String getName() {
        println("************* getName ***********")
        return "router_zeej_transform"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        def content_class = TransformManager.CONTENT_CLASS
        return content_class
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(@NonNull TransformInvocation transformInvocation)
            throws TransformException, InterruptedException, IOException {
        println("************* transform ***********")
        println '//===============asm visit start===============//'
        //遍历inputs里的TransformInput
       /* inputs.each { TransformInput input ->
            println '//===============asm visit start 1===============//'
            //遍历input里边的DirectoryInput
            input.directoryInputs.each {

                DirectoryInput directoryInput ->
                    println '//===============asm visit start 2===============//'
                    //是否是目录
                    if (directoryInput.file.isDirectory()) {
                        //遍历目录
                        println '//===============asm visit start 3===============//'
                        directoryInput.file.eachFileRecurse {
                            File file ->
                                def filename = file.name;
                                def name = file.name
                                println '//===============asm visit start 4===============//' + name + ","+filename
                                //这里进行我们的处理 TODO
                                if (name.endsWith(".class") && !name.startsWith("R\$") &&
                                        !"R.class".equals(name) && !"BuildConfig.class".equals(name)) {
                                    println '//===============asm visit start 5===============//'
                                    ClassReader classReader = new ClassReader(file.bytes)
                                    ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                                    ClassVisitor cv = new ZeejClassVisitor(classWriter)
                                    classReader.accept(cv, EXPAND_FRAMES)
                                    byte[] code = classWriter.toByteArray()
                                    FileOutputStream fos = new FileOutputStream(
                                            file.parentFile.absolutePath + File.separator + name)
                                    fos.write(code)
                                    fos.close()
//                                    CostMethodClassVisitor
                                    println '//===============asm visit start 6===============//'
                                }
                                println '//PluginImpl find file:' + file.getAbsolutePath()
                                //project.logger.
                        }
                    }
                    //处理完输入文件之后，要把输出给下一个任务
                    def dest = outputProvider.getContentLocation(directoryInput.name,
                            directoryInput.contentTypes, directoryInput.scopes,
                            Format.DIRECTORY)
                    FileUtils.copyDirectory(directoryInput.file, dest)


            }

            input.jarInputs.each { JarInput jarInput ->
                *//**
                 * 重名名输出文件,因为可能同名,会覆盖
                 *//*
                def jarName = jarInput.name
                def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
                if (jarName.endsWith(".jar")) {
                    jarName = jarName.substring(0, jarName.length() - 4)
                }
                println '//PluginImpl find Jar:' + jarInput.getFile().getAbsolutePath()

                //处理jar进行字节码注入处理 TODO

                def dest = outputProvider.getContentLocation(jarName + md5Name,
                        jarInput.contentTypes, jarInput.scopes, Format.JAR)

                FileUtils.copyFile(jarInput.file, dest)
            }
        }*/
    }
}
