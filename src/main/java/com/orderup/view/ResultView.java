package com.orderup.view;

import com.orderup.model.GameResult;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.List;
import java.util.Objects;

public final class ResultView {
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

    public void configure(
            GameResult result,
            int starCount,
            Runnable restartGame,
            Runnable returnToMenu
    ) {
        Objects.requireNonNull(result, "result must not be null");
        this.restartGame = Objects.requireNonNull(restartGame, "restartGame must not be null");
        this.returnToMenu = Objects.requireNonNull(returnToMenu, "returnToMenu must not be null");

        completedOrdersLabel.setText(Integer.toString(result.completedOrders()));
        tipsLabel.setText("+" + result.earnedTips());
        finalScoreLabel.setText(Integer.toString(result.finalScore()));

        finalScoreLabel.getStyleClass().remove("negative-score");
        if (result.finalScore() < 0) {
            finalScoreLabel.getStyleClass().add("negative-score");
        }

        updateStars(starCount);
    }

    private void updateStars(int starCount) {
        List<Label> stars = List.of(firstStarLabel, secondStarLabel, thirdStarLabel);
        for (int index = 0; index < stars.size(); index++) {
            Label star = stars.get(index);
            star.getStyleClass().remove("star-lit");
            if (index < starCount) {
                star.getStyleClass().add("star-lit");
            }
        }
    }

    @FXML
    private void onMenuButtonClick() {
        returnToMenu.run();
    }

    @FXML
    private void onRestartButtonClick() {
        restartGame.run();
    }
}
