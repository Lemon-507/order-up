package com.orderup.util;

import lombok.Data;

@Data
public class CalculateScore {
    private int score=0;
    private int level1;
    private int level2;
    private int level3;

    public int calculateLevel(){
        if(score>=level3) {
            return 3;
        }else if (score>=level2){
            return 2;
        }else if(score>=level1){
            return 1;
        }else {
            return 0;
        }
    }
}
