package com.laputa.arouter_common_service

import com.alibaba.android.arouter.facade.template.IProvider

/**
 * Author by xpl, Date on 2021/4/13.
 */
interface LoginService : IProvider {

    fun login(username: String, password: String, other: Any): Boolean
}