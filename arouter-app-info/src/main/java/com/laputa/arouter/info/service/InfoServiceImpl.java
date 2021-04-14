package com.laputa.arouter.info.service;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.laputa.arouter_common_service.InfoService;

/**
 * Author by xpl, Date on 2021/4/13.
 */
@Route(path = "/info/InfoService")
public class InfoServiceImpl implements InfoService {

    private Context mContext;
    private String info = "";


    @Override
    public String info(String id) {
        if (info == null || TextUtils.isEmpty(info)) {
            info = "用户信息 id = " + id;
        }
        return info;
    }

    @Override
    public boolean edit(String id, String value) {
        if ("zeej".equals(id)) {
            info = id + value;
        } else {
            info = value;
        }
        return true;
    }

    @Override
    public void init(Context context) {
        this.mContext = context;
    }
}
