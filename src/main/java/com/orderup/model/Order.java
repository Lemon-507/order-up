package com.orderup.model;

import lombok.Data;

/**
 * 保存顾客所需菜肴、剩余时间和订单状态。
 */
@Data
public class Order {
    private String orderId;
    private Recipe requiredDish;
    private String remainingTime;
    private String orderStatus;
}
