package com.laputa.login

import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.laputa.annotations.HField
import com.laputa.annotations.HRouter
import com.laputa.rounter.api.RouterManager

@HRouter(path = "/login/LoginFkActivity")
class LoginFkActivity : AppCompatActivity() {


    @HField(name = "name1")
    lateinit var name: String

    @JvmField
    @HField
    var a:Int = 0

    @JvmField
    @HField
    var b: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_fk)
        RouterManager.getInstance().load(this)
        Looper.myQueue().addIdleHandler {
//            val info =
//                findViewById<TextView>(R.id.tv_info)
//            val msg = "name = $name , a = $a ,b = $b"
//            Log.i("xxxxx", "$msg , ->$info")
//            info.text = msg
            false
        }
    }
}