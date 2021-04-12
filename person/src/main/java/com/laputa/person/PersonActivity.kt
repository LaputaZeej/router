package com.laputa.person

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.laputa.annotations.HField
import com.laputa.annotations.HRouter
import com.laputa.rounter.api.RouterManager

@HRouter(path = "/person/PersonActivity")
class PersonActivity : AppCompatActivity() {
    @JvmField
    @HField
    var name: String? = null

    @HField
    lateinit var msg: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person)
        RouterManager.getInstance().load(this)


        Handler().postDelayed({
            Log.i("xxxxxxxx", "msg = $name")
            Toast.makeText(this, "msg = $msg", Toast.LENGTH_SHORT).show()
        }
            , 1000L);
    }

    companion object {

    }
}