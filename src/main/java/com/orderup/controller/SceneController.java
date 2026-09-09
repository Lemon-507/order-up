package com.orderup.controller;

import com.orderup.Launcher;

/**
 * 需要进行页面切换的 FXML 控制器统一实现该接口。
 */
public interface SceneController {
    void setApplication(Launcher application);
}
