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
    // 游戏世界大小与一局游戏的持续时间。
    private static final double WORLD_WIDTH = 1280;
    private static final double WORLD_HEIGHT = 720;
    private static final int GAME_SECONDS = 60;
    private static final double FIXED_STEP_SECONDS = 1.0 / 300.0;
    private static final double MAX_ACCUMULATED_SECONDS = 0.25;

    // 由 game.fxml 注入：Canvas 绘制游戏内容，Label 显示剩余时间。
    @FXML
    private Canvas gameCanvas;
    @FXML
    private Label timeLabel;

    // 不同游戏对象分别交给对应的 View 绘制。
    private final GameMapView gameMapView = new GameMapView();
    private final InteractBlockView interactBlockView = new InteractBlockView();
    private final GameItemView gameItemView = new GameItemView();
    private final PlayerView playerView = new PlayerView();

    // 页面切换回调由 Launcher 设置，View 不直接负责切换场景。
    private Runnable onGameFinished = () -> { };
    private GameController controller;
    private AnimationTimer gameLoop;

    // 主循环运行过程中需要保存的显示层状态。
    private long lastTime;
    private double accumulatedSeconds;
    private int lastRenderedSeconds = -1;
    private boolean interactKeyPressed;
    private boolean disposed;




    /**
     * FXML 加载完成后自动调用：创建游戏、绑定输入、绘制首帧并启动主循环。
     */
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

        // 等页面真正显示后再获取焦点，否则 Canvas 可能接收不到键盘事件。
        Platform.runLater(gameCanvas::requestFocus);
    }

    /**
     * 接收 Launcher 提供的游戏结束回调。
     */
    public void setOnGameFinished(Runnable onGameFinished) {
        this.onGameFinished = onGameFinished;
        gameCanvas.requestFocus();
    }

    /**
     * 将键盘事件绑定到 Canvas；失去焦点时清空按键状态，防止角色持续移动。
     */
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

    /**
     * 处理按键按下：E 键只触发一次交互，WASD 记录为持续移动方向。
     */
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

    /**
     * 处理按键松开，解除对应的移动或交互状态。
     */
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

    /**
     * 把 JavaFX 按键转换为与 JavaFX 无关的游戏方向。
     */
    private Direction toDirection(KeyCode keyCode) {
        return switch (keyCode) {
            case W -> Direction.UP;
            case S -> Direction.DOWN;
            case A -> Direction.LEFT;
            case D -> Direction.RIGHT;
            default -> null;
        };
    }

    /**
     * 启动游戏主循环。JavaFX 每次刷新画面前都会调用 handle 方法。
     */
    private void startGameLoop() {
        GraphicsContext graphics = gameCanvas.getGraphicsContext2D();
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }

                double elapsedSeconds =
                        (now - lastTime) / 1_000_000_000.0;

                lastTime = now;

                // 避免窗口卡顿后一次补算过多帧
                accumulatedSeconds += Math.min(
                        elapsedSeconds,
                        MAX_ACCUMULATED_SECONDS
                );

                // 游戏逻辑固定每 1/60 秒更新一次
                while (accumulatedSeconds >= FIXED_STEP_SECONDS) {
                    controller.update(FIXED_STEP_SECONDS);
                    accumulatedSeconds -= FIXED_STEP_SECONDS;
                }

                // 按 JavaFX 实际刷新频率绘制
                if (!disposed) {
                    renderFrame(graphics);
                }
            }
        };
        gameLoop.start();
    }

    /**
     * 绘制一帧：先清空旧画面，再按照地图、交互区、物品、玩家的顺序绘制。
     */
    private void renderFrame(GraphicsContext graphics) {
        graphics.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
        gameMapView.render(graphics, controller.getGameMap());
        interactBlockView.render(graphics, controller.getInteractBlock());
        gameItemView.render(graphics, controller.getItems());
        playerView.render(graphics, controller.getPlayer());
        renderTime(controller.getRemainingSeconds());
    }

    /**
     * 只在秒数变化时刷新计时器文本；最后 10 秒显示为红色。
     */
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

    /**
     * 生成倒计时标签的 JavaFX 内联样式。
     */
    private String timerStyle(String textColor) {
        return "-fx-font-size: 48px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: " + textColor + ";"
                + "-fx-background-color: rgba(0,0,0,0.55);"
                + "-fx-padding: 8 18;"
                + "-fx-background-radius: 10;";
    }

    /**
     * 游戏时间结束：停止逐帧刷新并通知 Launcher 切换到结算页面。
     */
    private void finishGame() {
        stopGameLoop();
        onGameFinished.run();
    }

    /**
     * 停止 JavaFX 主循环。
     */
    private void stopGameLoop() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }

    /**
     * 离开游戏页面时释放循环和控制器状态，避免旧页面继续运行。
     */
    public void dispose() {
        disposed = true;
        stopGameLoop();
        if (controller != null) {
            controller.stopGame();
        }
    }
}
