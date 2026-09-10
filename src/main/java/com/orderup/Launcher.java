package com.orderup;

import com.orderup.view.GameApplication;

/**
 * Order Up 的纯 Java 启动入口。
 */
public final class Launcher {
    private Launcher() {
    }

    public static void main(String[] args) {
        GameApplication.launchApp(args);
    }
}
