package com.laputa.router.auto

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class PluginImpl implements Plugin<Project> {
    @Override
    void apply(Project project) {
        // todo asm 不好弄哈 试试aspectsj
//        def android = project.extensions.getByType(AppExtension);
//        def transform = new com.laputa.router.auto.AutoRegisterTransform()
//        android.registerTransform(transform)

        project.task('test_task_zeej') {
            doFirst {
                println "hello gradle plugin !  "
            }
        }
        project.gradle.addListener(new TimeListener())

        project.extensions.create("zeejArgs", ZeejPluginExtension)
        project.task("zeejTask", type: ZeejsTask)

        project.zeejArgs.extensions.create("zeejNestArgs", ZeejNestPluginExtension)
        project.task("zeejNestTask", type: ZeejNestTask)

    }
}
