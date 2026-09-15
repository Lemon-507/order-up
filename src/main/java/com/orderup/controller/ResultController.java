package com.orderup.controller;

import com.orderup.model.GameResult;
import com.orderup.view.ResultView;

public class ResultController {

    private ResultView resultView;

    private GameResult gameResult;
    /**
     * 注入结算页所需的页面操作。
     *
     * @param result 本局完成订单、小费与最终得分
     * @param restartGame 重新开始一局游戏的回调
     * @param returnToMenu 返回开始菜单的回调
     */
    public void configure(
            ResultView resultView,
            GameResult result,
            Runnable restartGame,
            Runnable returnToMenu
    ) {
        this.resultView = resultView;
        this.gameResult = result;
        resultView.configure(result, restartGame, returnToMenu);
        resultView.showStars(calculateStarCount(result.finalScore()));
    }

    static int calculateStarCount(int score) {
        return Math.min(3, Math.max(0, score / 1000));
    }

    public GameResult getGameResult() {
        return gameResult;
    }
}
