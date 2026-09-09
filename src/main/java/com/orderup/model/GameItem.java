package com.orderup.model;

import lombok.Data;

/**
 * 食材、盘子及成品等可拿取物品的抽象父类。
 */
@Data
public class GameItem {
    private String itemId;
    private String itemType;
}
