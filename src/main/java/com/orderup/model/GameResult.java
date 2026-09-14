package com.orderup.model;

/**
 * 一局游戏结束后展示在结算页上的不可变统计数据。
 */
public record GameResult(int completedOrders, int earnedTips, int finalScore) {
    public static GameResult empty() {
        return new GameResult(0, 0, 0);
    }
}
