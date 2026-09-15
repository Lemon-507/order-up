package com.orderup.controller;

import com.orderup.model.GameResult;
import com.orderup.view.ResultView;

import java.util.Objects;

public final class ResultController {
    private static final int POINTS_PER_STAR = 1000;
    private static final int MAX_STAR_COUNT = 3;

    private final ResultView resultView;

    public ResultController(ResultView resultView) {
        this.resultView = Objects.requireNonNull(resultView, "resultView must not be null");
    }

    /**
     * 注入结算页所需的页面操作。
     *
     * @param result 本局完成订单、小费与最终得分
     * @param restartGame 重新开始一局游戏的回调
     * @param returnToMenu 返回开始菜单的回调
     */
    public void configure(
            GameResult result,
            Runnable restartGame,
            Runnable returnToMenu
    ) {
        Objects.requireNonNull(result, "result must not be null");
        resultView.configure(
                result,
                calculateStarCount(result.finalScore()),
                restartGame,
                returnToMenu
        );
    }

    static int calculateStarCount(int score) {
        return Math.min(MAX_STAR_COUNT, Math.max(0, score / POINTS_PER_STAR));
    }
}
