package com.orderup.service.Impl;

import com.orderup.config.GameConfig;
import com.orderup.model.*;
import com.orderup.service.OrderService;

import java.util.*;

/**
 * 创建、更新并提交订单。
 */
public class OrderServiceImpl implements OrderService {
    private final List<Order> activeOrders = new ArrayList<>();
    private final ScoreServiceImpl scoreService;
    private final Random random;
    private final List<Recipe> recipes;

    /**
     * 使用默认计分服务和随机数生成器创建订单服务。
     */
    public OrderServiceImpl() {
        this(GameConfig.DEFAULT_LEVEL);
    }

    /**
     * 创建只会生成指定关卡可完成菜谱的订单服务。
     */
    public OrderServiceImpl(int level) {
        this(new ScoreServiceImpl(), new Random(), GameConfig.getRecipes(level));
    }

    /**
     * 创建可注入计分策略和随机数生成器的订单服务，便于测试。
     *
     * @param scoreService 订单成功后使用的计分服务
     * @param random 随机选择菜谱时使用的随机数生成器
     */
    OrderServiceImpl(ScoreServiceImpl scoreService, Random random) {
        this(scoreService, random, GameConfig.RECIPES);
    }

    private OrderServiceImpl(
            ScoreServiceImpl scoreService,
            Random random,
            List<Recipe> recipes
    ) {
        this.scoreService = scoreService;
        this.random = random;
        this.recipes = recipes;
    }

    /** {@inheritDoc} */
    @Override
    public Order createRandomOrder() {
        Recipe recipe = recipes.get(random.nextInt(recipes.size()));
        Order order = new Order(UUID.randomUUID().toString(), recipe);
        activeOrders.add(order);
        return order;
    }

    /** {@inheritDoc} */
    @Override
    public List<Order> getActiveOrders() {
        return Collections.unmodifiableList(activeOrders);
    }

    /** {@inheritDoc} */
    @Override
    public int updateOrders(double deltaSeconds) {
        if (deltaSeconds < 0) {
            throw new IllegalArgumentException("Delta seconds cannot be negative.");
        }
        activeOrders.forEach(order -> order.update(deltaSeconds));
        long expiredCount = activeOrders.stream()
                .filter(order -> order.getStatus() == OrderStatus.EXPIRED)
                .count();
        activeOrders.removeIf(order -> order.getRemainingSeconds() == 0);
        int penalty = scoreService.calculatePenalty(
                OrderResult.expired("订单超时", 0)
        );
        return (int) expiredCount * -penalty;
    }

    /** {@inheritDoc} */
    @Override
    public OrderResult submitPlate(Plate plate) {
        if (plate == null || plate.isEmpty()) {
            return failedSubmission("盘子为空");
        }

        Iterator<Order> iterator = activeOrders.iterator();
        while (iterator.hasNext()) {
            Order order = iterator.next();
            if (!plate.matches(order.getRecipe())) {
                continue;
            }
            int score = scoreService.calculateSuccessScore(order) + scoreService.calculateTip(order);
            order.complete();
            iterator.remove();
            plate.clear();
            return OrderResult.success("订单完成", score);
        }

        plate.clear();
        return failedSubmission("菜品与订单不匹配");
    }

    private OrderResult failedSubmission(String message) {
        OrderResult result = OrderResult.failed(message);
        int penalty = scoreService.calculatePenalty(result);
        return OrderResult.failed(message, -penalty);
    }
}
