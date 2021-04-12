package com.laputa.rounter.api;

import com.laputa.annotations.RouterBean;

import java.util.Map;

/**
 * Author by xpl, Date on 2021/4/7.
 */
public interface RouterPath {

    Map<String, RouterBean> getPathMap();
}
