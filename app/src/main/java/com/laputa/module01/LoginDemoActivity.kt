package com.laputa.module01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.laputa.annotations.HField
import com.laputa.annotations.HRouter

/**
 * [[MainActivity.name]]
 */
@HRouter(path="/app/LoginActivity1")
class LoginDemoActivity : AppCompatActivity() {


    @HField(name = "name1")
    lateinit var name: String

    @JvmField
    @HField
    var cc1: Int = 0

    @JvmField
    @HField
    var hello1: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity_login)
    }
}