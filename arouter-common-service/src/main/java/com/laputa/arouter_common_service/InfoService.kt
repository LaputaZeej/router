package com.laputa.arouter_common_service

import com.alibaba.android.arouter.facade.template.IProvider

/**
 * Author by xpl, Date on 2021/4/13.
 */
interface InfoService : IProvider {

    fun info(id: String): String

    fun edit(id: String, value: String): Boolean
}