package com.orderup.controller;

/**
 * 需要发起页面切换的控制器统一实现该接口。
 */
public interface SceneController {
    void setNavigator(SceneNavigator navigator);
}
