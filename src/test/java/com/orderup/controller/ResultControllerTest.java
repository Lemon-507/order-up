package com.orderup.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResultControllerTest {
    @Test
    void lightsOneStarForEachThousandPointsUpToThreeStars() {
        assertEquals(0, ResultController.calculateStarCount(-100));
        assertEquals(0, ResultController.calculateStarCount(999));
        assertEquals(1, ResultController.calculateStarCount(1000));
        assertEquals(1, ResultController.calculateStarCount(1999));
        assertEquals(2, ResultController.calculateStarCount(2000));
        assertEquals(2, ResultController.calculateStarCount(2999));
        assertEquals(3, ResultController.calculateStarCount(3000));
        assertEquals(3, ResultController.calculateStarCount(9000));
    }
}
