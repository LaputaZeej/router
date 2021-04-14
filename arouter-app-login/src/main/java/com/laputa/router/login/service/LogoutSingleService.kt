package com.laputa.router.login.service

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.template.IProvider
import com.laputa.arouter_common_service.i

/**
 * 测试单类注入
 * 只有一个实现的service，直接继承IProvider
 * Author by xpl, Date on 2021/4/14.
 */
@Route(path = "/login/logout")
class LogoutSingleService : IProvider{
    private var mContext: Context? = null
    override fun init(context: Context?) {
        this.mContext = context
    }

    fun logout() :Boolean{
        i("logout")
        return true
    }
}