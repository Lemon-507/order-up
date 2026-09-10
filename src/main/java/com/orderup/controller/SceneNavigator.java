package com.orderup.controller;

/**
 * 控制器用于发起页面操作的抽象接口，不依赖具体显示框架。
 */
public interface SceneNavigator {
    void showStartScene();

    void showGameScene();

    void showResultScene();

    void exitApplication();
}
