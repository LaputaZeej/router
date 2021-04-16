package com.laputa.router.login

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.alibaba.android.arouter.facade.annotation.Route

/**
 * Author by xpl, Date on 2021/4/16.
 */
@Route(path = "/login/LoginFragment")
class LoginFragment :DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return TextView(requireContext()).apply {
            text = "fragment"
            textSize = 22f
            setTextColor(Color.RED)
        }
    }
}