package com.orderup.view;

import com.orderup.controller.SceneNavigator;

/**
 * 由 FXML 创建的页面视图统一实现该接口。
 */
public interface SceneView {
    void setNavigator(SceneNavigator navigator);

    default void dispose() {
        // Most views do not own resources that need to be released.
    }
}
