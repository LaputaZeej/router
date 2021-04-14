package com.laputa.router.login.service

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.laputa.arouter_common_service.LoginService

/**
 * 会有多个实现
 * 对比单个[LogoutSingleService]
 * todo 怎么获取不同的实现？
 * Author by xpl, Date on 2021/4/13.
 */
@Route(path = "/login/LoginService")
class LoginServiceImpl : LoginService {
    private var mContext: Context? = null

    override fun login(username: String, password: String, other: Any): Boolean {
        if (("zeej" == username) && ("123456" == password)) {
            return true
        }
        return false
    }

    override fun init(context: Context?) {
        mContext = context
    }
}