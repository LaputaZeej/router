package com.laputa.arouter

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavigationCallback
import com.alibaba.android.arouter.launcher.ARouter
import com.laputa.arouter_common_service.LoginService
import com.laputa.arouter_common_service.i
import com.laputa.arouter_common_service.model.ObjectBean
import com.laputa.arouter_common_service.model.ParcelableBean
import com.laputa.arouter_common_service.model.SerializableBean
import com.laputa.arouter_common_service.toast
import com.laputa.arouter_common_service.tryCatching
import com.laputa.router.login.LoginActivity
import com.laputa.router.login.service.LoginServiceImpl
import com.laputa.router.login.service.LogoutSingleService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_01.text = "普通跳转"
        btn_01.setOnClickListener {
            // ARouter.getInstance().build("/app/Demo01Activity").navigation()
            Demo01Activity.skip()
        }

        btn_02.text = "login模块"
        btn_02.setOnClickListener {
            ARouter.getInstance().build("/login/LoginActivity")
                .withString("username", "zeej")
                .withString("password", "123456")
                .withInt("age1", 18)
                .navigation()
        }

        btn_03.text = "login模块  from URI"
        btn_03.setOnClickListener {
            ARouter.getInstance().build(Uri.parse("arouterxxxxx://m.xxx.com/login/LoginActivity"))
                .withString("username", "zeej")
                .withString("password", "123456")
                .withInt("age", 18)
                .navigation()
        }

        btn_04.text = "login模块 transition old"
        btn_04.setOnClickListener {
            ARouter.getInstance().build(Uri.parse("arouterxxxxx://m.xxx.com/login/LoginActivity"))
                .withString("username", "zeej")
                .withString("password", "123456")
                .withInt("age", 18)
                .withTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                .navigation()
        }

        btn_05.text = "login模块 transition new"
        if (Build.VERSION.SDK_INT >= 16) {
            btn_05.setOnClickListener {
                ARouter.getInstance()
                    .build(Uri.parse("arouterxxxxx://m.xxx.com/login/LoginActivity"))
                    .withString("username", "zeej")
                    .withString("password", "123456")
                    .withInt("age", 18)
                    .withOptionsCompat(
                        ActivityOptionsCompat.makeScaleUpAnimation(
                            it,
                            it.width / 2,
                            it.height / 2,
                            0,
                            0
                        )
                    )
                    .navigation()
            }
        } else {
            Toast.makeText(this, "API < 16,不支持新版本动画", Toast.LENGTH_SHORT).show()
        }

        btn_06.text = "login模块  interceptor"
        btn_06.setOnClickListener {
            ARouter.getInstance().build(Uri.parse("arouterxxxxx://m.xxx.com/login/LoginActivity"))
                .withString("username", "zeej")
                .withString("password", "123456")
                .withInt("age", 18)
                .withTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                .navigation(this, defaultCallback())
        }

        btn_07.text = "info模块  webView"
        btn_07.setOnClickListener {
            ARouter.getInstance().build("/info/webview")
                .withString("url", "https://www.baidu.com")
                .withTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                .navigation(this, defaultCallback())
        }

        btn_08.text = "Login模块  auto inject"
        btn_08.setOnClickListener {
            ARouter.getInstance().build("/login/LoginActivity")
                .withString("username", "zeej")
                .withString("password", "123456")
                .withInt("age", 18)
                .withString("url", "https://www.baidu.com")
                .withSerializable("ser", SerializableBean("laputa", 55))
                .withParcelable("par", ParcelableBean("Li", 111L))
                .withObject("obj", ObjectBean("Luban", 11f))
                .withObject("list", listOf(SerializableBean("a1", 2), ObjectBean("a2", 1f)))
                .withObject("map", mapOf("hello" to ObjectBean("s", 1f)))
                .withTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                .navigation(this, defaultCallback())
        }

        addButton("Login模块 nav By Name") {
            val loginService = ARouter.getInstance()
                .build("/login/LoginService")
                .navigation() as LoginService
            val login = loginService.login("zeej", "123456", "")
            i(if (login) "login success" else "login fail")
            toast(if (login) "login success" else "login fail")
        }


        addButton("Login模块 Module01") {
            ARouter.getInstance().build("/login/module01").navigation(this, defaultCallback())
        }

        addButton("Login模块 Module02 group =\"laputa\"") {
            ARouter.getInstance().build("/login/mudule02", "laputa").navigation(
                this,
                defaultCallback()
            )
        }

        addButton("destroy()") {
            ARouter.getInstance().destroy()
        }

        addButton("init()") {
            // 调试模式不是必须开启，但是为了防止有用户开启了InstantRun，但是
            // 忘了开调试模式，导致无法使用Demo，如果使用了InstantRun，必须在
            // 初始化之前开启调试模式，但是上线前需要关闭，InstantRun仅用于开
            // 发阶段，线上开启调试模式有安全风险，可以使用BuildConfig.DEBUG
            // 来区分环境
            ARouter.openDebug()
            ARouter.openLog()
            ARouter.init(this.application)
        }

        addButton("call single") {
            val logout = ARouter.getInstance().navigation(LogoutSingleService::class.java).logout()
            toast(if (logout) "logout success" else "logout fail")
        }

        addButton("试试直接build类型class") {
            tryCatching {
                val login = ARouter.getInstance().navigation(LoginServiceImpl::class.java)
                    .login("z", "12", "")
                toast(if (login) "login success" else "login fail")
            }
        }

        addButton("错误的path") {
            ARouter.getInstance().build("/xx/xx").navigation()
        }

        addButton("错误的class") {
//            tryCatching {
                ARouter.getInstance().navigation(LoginActivity::class.java)
//            }
        }

        addButton {
            tryCatching {

            }
        }
    }

    private fun addButton(text: String = "点我", block: (View) -> Unit) {
        ll_buttons.addView(Button(this).apply {
            this.text = text
            setOnClickListener(block)
        })
        ll_buttons.invalidate()
    }

    companion object {
        private const val TAG = "ARouter_demo"
        fun defaultCallback(): NavigationCallback = object : NavigationCallback {
            override fun onFound(postcard: Postcard?) {
                i("//onFound $postcard")
            }

            override fun onLost(postcard: Postcard?) {
                i("//onLost $postcard")
            }

            override fun onArrival(postcard: Postcard?) {
                i("//onArrival $postcard")
            }

            override fun onInterrupt(postcard: Postcard?) {
                i("//onInterrupt $postcard")
            }

        }
    }
}
