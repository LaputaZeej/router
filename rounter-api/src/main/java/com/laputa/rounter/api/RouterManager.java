package com.laputa.rounter.api;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.LruCache;

import com.laputa.annotations.RouterBean;

/**
 * Author by xpl, Date on 2021/4/9.
 */
public class RouterManager {

    private static final String RouterPath_PATH_NAME = "HRouter$$Path$$";
    private static final String RouterPath_Group_NAME = "HRouter$$Group$$";

    private LruCache<String, RouterPath> pathCache;
    private LruCache<String, RouterGroup> groupCache;

    public static RouterManager getInstance() {
        return RouterManager.RouterManagerHolder.INSTANCE;
    }

    private RouterManager() {
        pathCache = new LruCache<>(100);
        groupCache = new LruCache<>(100);
    }

    // 可以分装
    public BundleBox create(String path) {

        if (check(path)) {
            String group = path.substring(1, path.indexOf("/", 1));
            return new BundleBox(path, group);
        }
        return null;
    }

    public void load(Activity activity) {
        FieldManager.getInstance().load(activity);
    }

    public Object navigation(Context context, BundleBox bundleBox) {
        String path = bundleBox.getPath();
        String group = bundleBox.getGroup();
        try {

//            String groupClassName = context.getPackageName() + "." + RouterPath_Group_NAME + group;
            String groupClassName = BuildConfig.packageNameForAPT + "." + RouterPath_Group_NAME + group;

            RouterGroup routerGroup = groupCache.get(group);
            if (routerGroup == null) {
                Class<?> aClass = Class.forName(groupClassName);
                routerGroup = (RouterGroup) aClass.newInstance();
                if (routerGroup.getGroupPath().isEmpty()) {
                    throw new RuntimeException("group is empty");
                }
                groupCache.put(group, routerGroup);
            }
            RouterPath routerPath = pathCache.get(path);
            if (routerPath == null) {
                Class<? extends RouterPath> aClass = routerGroup.getGroupPath().get(group);
                if (aClass == null) {
                    throw new RuntimeException("group clz  is null");
                }
                routerPath = aClass.newInstance();
                if (routerPath.getPathMap().isEmpty()) {
                    throw new RuntimeException("path is empty");
                }
                pathCache.put(path, routerPath);
            }

            RouterBean routerBean = routerPath.getPathMap().get(path);
            if (routerBean == null) {
                throw new RuntimeException("path clz  is null");
            }
            switch (routerBean.getType()) {
                case ACTIVITY:
                    Intent intent = new Intent(context, routerBean.getAnnotationClass());
                    intent.putExtras(bundleBox.getData());
                    context.startActivity(intent);
                    break;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private boolean check(String path) {
        // todo 校验
        return true;
    }

    private static final class RouterManagerHolder {
        private final static RouterManager INSTANCE = new RouterManager();
    }
}
