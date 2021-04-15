package com.laputa.router.auto

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class ZeejsTask extends DefaultTask {
    @TaskAction
    void output() {
        println(">>>>>>>>>>> Sender is ${project.zeejArgs.sender}\nmessage:${project.zeejArgs.message} <<<<<<<<<<")
    }
}