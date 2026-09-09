package com.orderup.util;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class GameTimer {
    private final Timeline timeline;
    private final Label timeLabel;
    private final Runnable onTimeUp;

    private int secondsCount;
    private int initialCountDownSeconds;

    public GameTimer(Label timeLabel, Runnable onTimeUp) {
        this.timeLabel = timeLabel;
        this.onTimeUp = onTimeUp;

        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> updateTimer())
        );
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    public void startCountDown(int totalSeconds) {
        timeline.stop();

        initialCountDownSeconds = totalSeconds;
        secondsCount = totalSeconds;

        renderTime();
        timeline.playFromStart();
    }

    private void updateTimer() {
        secondsCount--;
        renderTime();

        if (secondsCount <= 0) {
            secondsCount = 0;
            renderTime();
            timeline.stop();
            onTimeUp.run();
        }
    }

    private void renderTime() {
        int minutes = secondsCount / 60;
        int seconds = secondsCount % 60;

        timeLabel.setText(
                String.format("%02d:%02d", minutes, seconds)
        );

        if (secondsCount <= 10) {
            timeLabel.setStyle(
                    "-fx-font-size: 48px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-text-fill: red;" +
                            "-fx-background-color: rgba(0,0,0,0.55);" +
                            "-fx-padding: 8 18;" +
                            "-fx-background-radius: 10;"
            );
        }
    }

    public void pause() {
        timeline.pause();
    }

    public void resume() {
        timeline.play();
    }

    public void reset() {
        timeline.stop();
        secondsCount = initialCountDownSeconds;
        renderTime();
    }

    public void stop() {
        timeline.stop();
    }
}