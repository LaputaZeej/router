package com.laputa.module01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.os.bundleOf
import com.bugull.javapoet.JavaPoetDemo
import com.laputa.annotations.HField
import com.laputa.annotations.HRouter
import com.laputa.login.LoginActivity
import com.laputa.login.LoginFkActivity
import com.laputa.rounter.api.RouterManager
import kotlinx.android.synthetic.main.activity_main.*

/**
 * a[android.Manifest.permission.CALL_PHONE]
 * [android.app.Activity.RESULT_OK]
 */
@HRouter(path = "/app/MainActivity")
class MainActivity : AppCompatActivity() {
    @HField(name = "name")
    lateinit var name: String

    @JvmField
    @HField
    var cc: Int = 0

    @JvmField
    @HField
    var hello: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        JavaPoetDemo.hello(arrayOf("haha"))

        btn_to_login.setOnClickListener {
            RouterManager.getInstance().create("/login/LoginActivity")
                .with("name1", "xpl")
                .with("a", 1)
                .with("b", 1L)
                .navigation(this)

        }

        btn_to_person.setOnClickListener {
            /*     RouterManager.getInstance().create("/person/PersonActivity")
                     .with("name", "xpl_person")
                     .navigation(this)*/

            startActivity(
                Intent(this, com.laputa.login.LoginActivity::class.java),
                bundleOf("name" to "jajajaja", "a" to 1, "b" to 1L)
            )
        }
    }
}