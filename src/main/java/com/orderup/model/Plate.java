package com.orderup.model;

import lombok.Data;

import java.util.Set;

/**
 * 保存已装入盘中的食材组合，用于形成菜肴并提交订单。
 */
@Data
public class Plate {
    private Set<String> contents;
}
