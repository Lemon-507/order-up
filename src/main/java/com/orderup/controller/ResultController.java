package com.orderup.controller;

import com.orderup.model.GameResult;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.List;

public class ResultController {
    @FXML
    private Label completedOrdersLabel;
    @FXML
    private Label tipsLabel;
    @FXML
    private Label finalScoreLabel;
    @FXML
    private Label firstStarLabel;
    @FXML
    private Label secondStarLabel;
    @FXML
    private Label thirdStarLabel;

    private Runnable restartGame = () -> { };
    private Runnable returnToMenu = () -> { };

    /**
     * 注入结算页所需的页面操作。
     *
     * @param result 本局完成订单、小费与最终得分
     * @param restartGame 重新开始一局游戏的回调
     * @param returnToMenu 返回开始菜单的回调
     */
    public void configure(GameResult result, Runnable restartGame, Runnable returnToMenu) {
        this.restartGame = restartGame;
        this.returnToMenu = returnToMenu;
        completedOrdersLabel.setText(Integer.toString(result.completedOrders()));
        tipsLabel.setText("+" + result.earnedTips());
        finalScoreLabel.setText(Integer.toString(result.finalScore()));
        updateStars(result.finalScore());
        if (result.finalScore() < 0) {
            finalScoreLabel.getStyleClass().add("negative-score");
        }
    }

    private void updateStars(int score) {
        int litStars = calculateStarCount(score);
        List<Label> stars = List.of(firstStarLabel, secondStarLabel, thirdStarLabel);
        for (int index = 0; index < stars.size(); index++) {
            Label star = stars.get(index);
            star.getStyleClass().remove("star-lit");
            if (index < litStars) {
                star.getStyleClass().add("star-lit");
            }
        }
    }

    static int calculateStarCount(int score) {
        return Math.min(3, Math.max(0, score / 1000));
    }

    /**
     * 处理 FXML 中的“重新开始”按钮事件。
     */
    public void onRestartButtonClick() {
        restartGame.run();
    }

    /**
     * 处理 FXML 中的“返回菜单”按钮事件。
     */
    public void onMenuButtonClick() {
        returnToMenu.run();
    }
}
