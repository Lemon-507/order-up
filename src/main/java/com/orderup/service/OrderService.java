package com.orderup.service;

import com.orderup.model.Order;
import com.orderup.model.OrderResult;
import com.orderup.model.Plate;

import java.util.List;

public interface OrderService {
    /**
     * 随机创建订单
     * @return Order
     */
    Order createRandomOrder();
    /**
     * 获取正在进行的订单
     * @return List<Order>
     */
    List<Order> getActiveOrders();
    /**
     * 更新订单计时
     */
    void updateOrders(double deltaSeconds);

    /**
     * 提交订单
     * @param plate 提交盘子
     * @return OrderResult订单结果
     */
    OrderResult submitPlate(Plate plate);
}
