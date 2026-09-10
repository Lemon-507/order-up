package com.orderup.view;

import com.orderup.controller.GameController;
import com.orderup.model.Direction;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * 游戏页面的 JavaFX 显示层，负责输入、逐帧刷新和画布绘制。
 */
public class GameView {
    private static final double WORLD_WIDTH = 1280;
    private static final double WORLD_HEIGHT = 720;
    private static final int GAME_SECONDS = 60;
    private static final double MAX_FRAME_SECONDS = 0.05;

    @FXML
    private Canvas gameCanvas;
    @FXML
    private Label timeLabel;

    private final GameMapView gameMapView = new GameMapView();
    private final InteractBlockView interactBlockView = new InteractBlockView();
    private final GameItemView gameItemView = new GameItemView();
    private final PlayerView playerView = new PlayerView();
    private Runnable onGameFinished = () -> { };
    private GameController controller;
    private AnimationTimer gameLoop;
    private long lastTime;
    private int lastRenderedSeconds = -1;
    private boolean interactKeyPressed;
    private boolean disposed;

    @FXML
    private void initialize() {
        controller = new GameController(
                WORLD_WIDTH,
                WORLD_HEIGHT,
                GAME_SECONDS,
                this::finishGame
        );
        controller.startGame();

        configureInput();
        renderFrame(gameCanvas.getGraphicsContext2D());
        startGameLoop();
        Platform.runLater(gameCanvas::requestFocus);
    }

    public void setOnGameFinished(Runnable onGameFinished) {
        this.onGameFinished = onGameFinished;
        gameCanvas.requestFocus();
    }

    private void configureInput() {
        gameCanvas.setFocusTraversable(true);
        gameCanvas.setOnKeyPressed(this::onKeyPressed);
        gameCanvas.setOnKeyReleased(this::onKeyReleased);
        gameCanvas.focusedProperty().addListener((observable, wasFocused, isFocused) -> {
            if (!isFocused) {
                controller.clearInput();
                interactKeyPressed = false;
            }
        });
    }

    private void onKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.E) {
            if (!interactKeyPressed) {
                interactKeyPressed = true;
                controller.InteractItem();
            }
            event.consume();
            return;
        }

        Direction direction = toDirection(event.getCode());
        if (direction != null) {
            controller.press(direction);
            event.consume();
        }
    }

    private void onKeyReleased(KeyEvent event) {
        if (event.getCode() == KeyCode.E) {
            interactKeyPressed = false;
            event.consume();
            return;
        }

        Direction direction = toDirection(event.getCode());
        if (direction != null) {
            controller.release(direction);
            event.consume();
        }
    }

    private Direction toDirection(KeyCode keyCode) {
        return switch (keyCode) {
            case W -> Direction.UP;
            case S -> Direction.DOWN;
            case A -> Direction.LEFT;
            case D -> Direction.RIGHT;
            default -> null;
        };
    }

    private void startGameLoop() {
        GraphicsContext graphics = gameCanvas.getGraphicsContext2D();
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double deltaSeconds = lastTime == 0
                        ? 0
                        : Math.min((now - lastTime) / 1_000_000_000.0, MAX_FRAME_SECONDS);
                lastTime = now;

                controller.update(deltaSeconds);
                if (!disposed) {
                    renderFrame(graphics);
                }
            }
        };
        gameLoop.start();
    }

    private void renderFrame(GraphicsContext graphics) {
        graphics.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
        gameMapView.render(graphics, controller.getGameMap());
        interactBlockView.render(graphics, controller.getInteractBlock());
        gameItemView.render(graphics, controller.getItems());
        playerView.render(graphics, controller.getPlayer());
        renderTime(controller.getRemainingSeconds());
    }

    private void renderTime(int totalSeconds) {
        if (totalSeconds == lastRenderedSeconds) {
            return;
        }
        lastRenderedSeconds = totalSeconds;

        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        timeLabel.setText(String.format("%02d:%02d", minutes, seconds));
        timeLabel.setStyle(totalSeconds <= 10
                ? timerStyle("red")
                : timerStyle("white"));
    }

    private String timerStyle(String textColor) {
        return "-fx-font-size: 48px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: " + textColor + ";"
                + "-fx-background-color: rgba(0,0,0,0.55);"
                + "-fx-padding: 8 18;"
                + "-fx-background-radius: 10;";
    }

    private void finishGame() {
        stopGameLoop();
        onGameFinished.run();
    }

    private void stopGameLoop() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }

    public void dispose() {
        disposed = true;
        stopGameLoop();
        if (controller != null) {
            controller.stopGame();
        }
    }
}
