package com.laputa.router.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.os.bundleOf
import com.alibaba.android.arouter.facade.annotation.Route

@Route(path = "/login/module01")
class Module01Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_module01ctivity)

        setResult(999, Intent().apply {
            putExtras(bundleOf("zeej" to "i am replay from LoginActivity"))
        })
    }
}