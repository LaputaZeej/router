package com.laouta.plugin.log

import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.AppExtension
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.io.FileOutputStream

/**
 * Author by xpl, Date on 2021/4/16.
 * https://blog.csdn.net/qq_38713396/article/details/113878634
 */
class LogTransform : Transform(), Plugin<Project> {
    override fun getName(): String {
        return "zeej_laputa_plugin_log_transform"
    }

    //transform要处理的输入类型,有class,resource,dex
    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_CLASS
    }

    /**
     * 输入文件的范围
     * PROJECT 当前工程
     * SUB_PROJECTS 子工程
     * EXTERNAL_LIBRARIES lib
     * LOCAL_DEPS jar
     */
    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    override fun isIncremental(): Boolean = false

    override fun apply(target: Project) {
        println("##################### LogTransform apply a #####################")
        println("project.version = ${target.version}")
        target.extensions.getByType(AppExtension::class.java)
            .registerTransform(this)
        println("##################### LogTransform apply z #####################")
    }

    override fun transform(transformInvocation: TransformInvocation?) {
        super.transform(transformInvocation)
        println("##################### LogTransform transform a #####################")
        val inputs = transformInvocation?.inputs
        val outputProvider = transformInvocation?.outputProvider

        if (!isIncremental) {
            outputProvider?.deleteAll()
        }

        inputs?.forEach { input ->
            println("input${input}")
            input.directoryInputs.forEach {
                println("   directoryInput=${it.name}")
                if (it.file.isDirectory) {
                    FileUtils.getAllFiles(it.file).forEach { file ->
                        println("       file=${file.name}")
                        val name = file.name
                        if (name.endsWith(".class") && name != "R.class"
                            && !name.startsWith("R\$") && name != ("BuildConfig.class")
                        ) {//过滤出需要的class,将一些基本用不到的class去掉
                            val classPath = file.absoluteFile
                            println("       file#classPath=${classPath}")
                            val cr = ClassReader(file.readBytes())
                            val cw = ClassWriter(cr, ClassWriter.COMPUTE_MAXS)
                            //需要处理的类使用自定义的visitor来处理
                            val visitor = LogClassVisitor(cw)
                            cr.accept(visitor, ClassReader.EXPAND_FRAMES)
                            val bytes = cw.toByteArray()
                            val fos = FileOutputStream(classPath)
                            fos.write(bytes)
                            fos.close()
                        }
                    }
                }

                val dest = outputProvider?.getContentLocation(
                    it.name,
                    it.contentTypes,
                    it.scopes,
                    Format.DIRECTORY
                )
                FileUtils.copyDirectoryToDirectory(it.file, dest)
            }

            //将jar也加进来,androidx需要这个
            input.jarInputs.forEach {
                val dest = outputProvider?.getContentLocation(
                    it.name,
                    it.contentTypes,
                    it.scopes,
                    Format.JAR
                )
                FileUtils.copyFile(it.file, dest)
            }
        }


        println("##################### LogTransform transform z #####################")
    }
}