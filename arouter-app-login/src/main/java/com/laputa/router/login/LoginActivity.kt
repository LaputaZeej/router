package com.laputa.router.login

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.laputa.arouter_common_service.InfoService
import com.laputa.arouter_common_service.LoginService
import com.laputa.arouter_common_service.i
import com.laputa.arouter_common_service.model.ParcelableBean
import com.laputa.arouter_common_service.model.SerializableBean
import com.laputa.arouter_common_service.toast
import kotlinx.android.synthetic.main.activity_login.*
import kotlin.math.log

@Route(path = LoginActivity.PATH)
class LoginActivity : AppCompatActivity() {

    @Autowired(name = "username")
    lateinit var username: String

    @Autowired
    lateinit var password: String

    @JvmField
    @Autowired
    var age: Int = 0

    @JvmField
    @Autowired
    var ser: SerializableBean? = null

    @JvmField
    @Autowired
    var par: ParcelableBean? = null

    @JvmField
    @Autowired
    var list: List<Any>? = null

    @JvmField
    @Autowired
    var map: Map<String, Any>? = null

    @Autowired
    lateinit var loginService: LoginService

    @Autowired
    lateinit var infoService: InfoService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ARouter.getInstance().inject(this)
        et_username.setText(username)
        et_password.setText(password)
        val msg =
            "\nusername=$username\npassword=$password\nage=$age\nser=$ser\npar=$par\nlist=$list\nmap=$map"
        i(msg)
        tv_info.append(msg)

        btn_01.text = "登录"
        btn_01.setOnClickListener {
            val login = loginService.login("zeej", "123456", "")
            toast(if (login) "登录成功！" else "登录失败！")
            info(if (login) "登录成功！" else "登录失败！")
        }

        btn_02.text = "信息"
        btn_02.setOnClickListener {
            val info = infoService.info("zeej")
            toast(info)
            info(info)
        }

        btn_03.text = "修改信息"
        btn_03.setOnClickListener {
            val edit = infoService.edit("zeej", "新的信息 哈哈哈")
            toast(if (edit) "修改成功！" else "修改失败！")
            info(if (edit) "修改成功！" else "修改失败！")
        }
    }

    private fun info(msg: String) {
        tv_info.text = msg
    }

    companion object {
        internal const val PATH = "/login/LoginActivity"
        private const val USERNAME = "username"
        private const val PASSWORD = "password"
        private const val AGE = "age"

        @JvmStatic
        fun skip(username: String, password: String, age: Int = 100) {
            ARouter.getInstance().build(PATH)
                .withString(USERNAME, username)
                .withString(PASSWORD, password)
                .withInt(AGE, age)
                .navigation()
        }
    }
}