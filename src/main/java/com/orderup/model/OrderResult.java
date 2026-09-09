package com.orderup.model;

import lombok.Data;

@Data
public class OrderResult {
    private String success;
    private String expired;
    private String message;
}
