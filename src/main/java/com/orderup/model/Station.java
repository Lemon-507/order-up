package com.orderup.model;

import lombok.Data;

/**
 * 表示食材库、烹饪工具、出餐口和垃圾桶等交互位置。
 */
@Data
public class Station {
    private String stationId;
    private StationType stationType;
    private String position;
    private String currentItem;
    private String processingProgress;








}
