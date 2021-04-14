package com.laputa.arouter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter

@Route(path = Demo01Activity.PATH)
class Demo01Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo01)
    }

    companion object {
        internal const val PATH = "/app/Demo01Activity"

        @JvmStatic
        fun skip() {
            ARouter.getInstance().build(PATH).navigation()
        }
    }
}