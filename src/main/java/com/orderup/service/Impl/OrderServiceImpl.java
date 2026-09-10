package com.orderup.service.Impl;

import com.orderup.model.Order;
import com.orderup.model.OrderResult;
import com.orderup.model.Plate;
import com.orderup.service.OrderService;

import java.util.List;

public class OrderServiceImpl implements OrderService {
    @Override
    public Order createRandomOrder() {
        return null;
    }

    @Override
    public List<Order> getActiveOrders() {
        return List.of();
    }

    @Override
    public void updateOrders(double deltaSeconds) {

    }

    @Override
    public OrderResult submitPlate(Plate plate) {
        return null;
    }
}
