package com.orderup.view;

import com.orderup.model.GameResult;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.List;

public class ResultView {
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

    public void configure(GameResult result, Runnable restartGame, Runnable returnToMenu) {
        this.restartGame = restartGame;
        this.returnToMenu = returnToMenu;
        completedOrdersLabel.setText(Integer.toString(result.completedOrders()));
        tipsLabel.setText("+" + result.earnedTips());
        finalScoreLabel.setText(Integer.toString(result.finalScore()));
        if (result.finalScore() < 0) {
            finalScoreLabel.getStyleClass().add("negative-score");
        }
    }

    public void showStars(int count) {
        List<Label> stars = List.of(firstStarLabel, secondStarLabel, thirdStarLabel);
        for (int index = 0; index < stars.size(); index++) {
            Label star = stars.get(index);
            star.getStyleClass().remove("star-lit");
            if (index < count) {
                star.getStyleClass().add("star-lit");
            }
        }
    }

    public void onMenuButtonClick() {
        returnToMenu.run();
    }

    public void onRestartButtonClick() {
        restartGame.run();
    }
}
