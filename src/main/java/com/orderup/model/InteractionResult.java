package com.orderup.model;

import lombok.Data;

/**
 * 返回玩家与工作台交互是否成功及对应提示。
 */
@Data
public class InteractionResult {
    private String success;
    private String message;
}
