package com.laputa.router.auto

import org.gradle.api.Plugin
import org.gradle.api.Project

class PluginImpl implements Plugin<Project> {
    @Override
    void apply(Project project) {
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
