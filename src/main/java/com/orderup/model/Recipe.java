package com.orderup.model;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class Recipe {
    private String dishName;
    private Set<Ingredient> requirements;
    private int baseScore;
    private int timeLimit;
}
