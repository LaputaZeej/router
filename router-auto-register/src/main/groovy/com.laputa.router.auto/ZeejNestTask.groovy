package com.laputa.router.auto

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class ZeejNestTask extends DefaultTask {
    @TaskAction
    void output() {
        println(">>>>>>>>>>> Sender is ${project.zeejArgs.sender}\nmessage:${project.zeejArgs.message} <<<<<<<<<<")
        println(">>>>>>>>>>> id is ${project.zeejArgs.zeejNestArgs.id}\nemail:${project.zeejArgs.zeejNestArgs.email} <<<<<<<<<<")
    }
}