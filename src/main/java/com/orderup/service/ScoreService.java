package com.orderup.service;

import com.orderup.model.Order;
import com.orderup.model.OrderResult;

public interface ScoreService {
    /**
     * 计算成功奖励
     * @param order
     * @return 奖励
     */
    int calculateSuccessScore(Order order);

    /**
     * 计算小费
     * @param order
     * @return 小费
     */
    int calculateTip(Order order);

    /**
     * 计算罚金
     * @param result
     * @return 罚金
     */
    int calculatePenalty(OrderResult result);
}
