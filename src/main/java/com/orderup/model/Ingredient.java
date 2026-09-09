package com.orderup.model;

import lombok.Data;

/**
 * 表示食材类型、当前加工状态和加工进度。
 */
@Data
public class Ingredient {
    private String ingredientType;
    private String processState;
    private String processProgress;

}
