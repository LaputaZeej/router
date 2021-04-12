package com.laputa.rounter.api;

import java.util.Map;

/**
 * Author by xpl, Date on 2021/4/7.
 */
public interface RouterGroup {
    Map<String,Class<? extends RouterPath>> getGroupPath();
}
