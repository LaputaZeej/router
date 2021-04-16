package com.laputa.module01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import com.laputa.annotations.Dog
import com.laputa.annotations.HField
import com.laputa.annotations.HRouter
import com.laputa.login.LoginActivity
import com.laputa.login.LoginFkActivity
import com.laputa.module01.util.Logger
import com.laputa.rounter.api.RouterManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.log
import kotlin.random.Random

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

//        JavaPoetDemo.hello(arrayOf("haha"))

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
            testAsm()
        }
    }

    fun testAsm() {
        Log.i("MainActivity", "only test asm")
        testTimeCache()
        testTimeCacheCat()
        testTimeCacheDog()
    }

    @Logger(tag = "haha")
    fun testTimeCache() {
        Log.i("TimeCache", "only test asm for TimeCache")
        //Thread.sleep(Random(100L).nextLong())
        Thread.sleep(100L)
    }


    fun testTimeCacheCat() {
        Log.i("TimeCache", "only test asm for testTimeCacheCat")
        //Thread.sleep(Random(100L).nextLong())
        Thread.sleep(200L)
    }

    @Dog(foot = "4")
    fun testTimeCacheDog() {
        Log.i("TimeCache", "only test asm for testTimeCacheDog")
        //Thread.sleep(Random(100L).nextLong())
        Thread.sleep(200L)
    }
}