package com.laputa.arouter_common_service

import android.content.Context
import android.util.Log
import android.widget.Toast

/**
 * Author by xpl, Date on 2021/4/13.
 */

fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun i(msg: String, tag: String = "Route-Demo") {
    Log.i(tag, msg)
}

private fun tryCatching(block: () -> Unit, toast: ((String) -> Unit)? = null) {
    try {
        block.invoke()
    } catch (e: Throwable) {
        toast?.invoke(e.localizedMessage ?: "error")
    }
}

fun Context.tryCatching(block: () -> Unit) =
    tryCatching(block){
        toast(it)
    }
