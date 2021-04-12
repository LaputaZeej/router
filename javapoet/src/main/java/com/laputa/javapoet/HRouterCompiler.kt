package com.laputa.javapoet

import com.google.auto.service.AutoService
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.tools.Diagnostic

/**
 * Author by xpl, Date on 2021/4/7.
 */
@AutoService(Processor::class)
@SupportedAnnotationTypes(("com.laputa.annotations.HRouter"))
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions("hid") // gradle
class HRouterCompiler : AbstractProcessor() {

    private lateinit var messager: Messager
    private lateinit var filer: Filer
    private lateinit var elementUtils: Elements

    override fun init(environment: ProcessingEnvironment) {
        super.init(environment)
        messager = environment.messager
        filer = environment.filer
        elementUtils = environment.elementUtils

        val hid = environment.options["hid"]
        messager.printMessage(Diagnostic.Kind.NOTE, "xxxxxxxxxxkotlin hid = $hid")
    }

    override fun process(
        set: MutableSet<out TypeElement>?,
        roundEnvironment: RoundEnvironment?
    ): Boolean {
        println("size = ${set?.size}")
        return false
    }

    private fun println(msg: String?) {
        messager.printMessage(Diagnostic.Kind.NOTE, "$msg \n")
    }
}