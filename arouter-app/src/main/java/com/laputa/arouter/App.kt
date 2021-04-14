package com.laputa.arouter

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter

/**
 * Author by xpl, Date on 2021/4/13.
 */
class App :Application() {

    override fun onCreate() {
        super.onCreate()
        // 调试模式不是必须开启，但是为了防止有用户开启了InstantRun，但是
        // 忘了开调试模式，导致无法使用Demo，如果使用了InstantRun，必须在
        // 初始化之前开启调试模式，但是上线前需要关闭，InstantRun仅用于开
        // 发阶段，线上开启调试模式有安全风险，可以使用BuildConfig.DEBUG
        // 来区分环境
        ARouter.openDebug()
        ARouter.openLog()
        ARouter.init(this)
    }
}