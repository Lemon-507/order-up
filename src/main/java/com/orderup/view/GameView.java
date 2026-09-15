package com.orderup.view;

import com.orderup.config.GameConfig;
import com.orderup.controller.GameController;
import com.orderup.model.Direction;
import com.orderup.model.GameState;
import com.orderup.model.GameResult;
import com.orderup.model.InteractionResult;
import com.orderup.model.Order;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 游戏页面的 JavaFX 显示层：接收输入、驱动主循环并绘制画面。
 */
public class GameView {
    // FXML 只注入显示控件；游戏数据由 GameController 管理。
    @FXML
    private Canvas gameCanvas;
    @FXML
    private Label timeLabel;
    @FXML
    private VBox ordersBox;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label interactionMessageLabel;
    @FXML
    private StackPane gameRoot;
    @FXML
    private StackPane pauseOverlay;
    @FXML
    private Button continueButton;
    @FXML
    private Button musicToggleButton;

    private final GameMapView gameMapView = new GameMapView();
    private final InteractionAreaView interactionAreaView = new InteractionAreaView();
    private final GameItemView gameItemView = new GameItemView();
    private final PlayerView playerView = new PlayerView();

    // 场景切换由 Launcher 通过回调注入，View 不直接依赖 Launcher。
    private Consumer<GameResult> onGameFinished = result -> { };
    private Runnable returnToMenu = () -> { };
    private Runnable returnToLevelSelect = () -> { };
    private Consumer<Boolean> changeSound = enabled -> { };
    private boolean soundEnabled;
    private GameController controller;
    private AnimationTimer gameLoop;
    private long lastTime;
    private double accumulatedSeconds;
    private int lastRenderedSeconds = -1;
    private boolean interactKeyPressed;
    private boolean pauseKeyPressed;
    private boolean dashKeyPressed;
    private boolean disposed;
    private final List<OrderCard> orderCards = new ArrayList<>();
    private List<String> renderedOrderIds = List.of();

    /**
     * 按选中的关卡创建游戏，并在 FXML 控件加载完成后启动主循环。
     *
     * @param level 关卡编号
     * @param onGameFinished 游戏结束后切换页面的回调
     * @param returnToMenu 返回主菜单的回调
     * @param returnToLevelSelect 返回关卡选择页的回调
     * @param soundEnabled 当前音乐开关状态
     * @param changeSound 切换音乐开关的回调
     */
    public void configure(
            int level,
            Consumer<GameResult> onGameFinished,
            Runnable returnToMenu,
            Runnable returnToLevelSelect,
            boolean soundEnabled,
            Consumer<Boolean> changeSound
    ) {
        if (controller != null) {
            throw new IllegalStateException("GameView 已经配置过");
        }
        this.onGameFinished = onGameFinished;
        this.returnToMenu = returnToMenu;
        this.returnToLevelSelect = returnToLevelSelect;
        this.soundEnabled = soundEnabled;
        this.changeSound = changeSound;
        updateMusicButton();
        controller = new GameController(level, this::finishGame);
        controller.startGame();

        configureInput();
        renderFrame(gameCanvas.getGraphicsContext2D());
        startGameLoop();
        Platform.runLater(gameCanvas::requestFocus);
    }

    private void configureInput() {
        gameCanvas.setFocusTraversable(true);
        gameRoot.addEventFilter(KeyEvent.KEY_PRESSED, this::onKeyPressed);
        gameRoot.addEventFilter(KeyEvent.KEY_RELEASED, this::onKeyReleased);
        gameCanvas.focusedProperty().addListener((observable, wasFocused, isFocused) -> {
            if (!isFocused) {
                controller.clearInput();
                interactKeyPressed = false;
                pauseKeyPressed = false;
                dashKeyPressed = false;
            }
        });
    }

    private void onKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            if (!pauseKeyPressed) {
                pauseKeyPressed = true;
                togglePause();
            }
            event.consume();
            return;
        }

        if (controller.getState() != GameState.RUNNING) {
            if (event.getCode() == KeyCode.E
                    || toDirection(event.getCode()) != null
                    || isDashKey(event.getCode())) {
                if (isDashKey(event.getCode())) {
                    dashKeyPressed = true;
                }
                event.consume();
            }
            return;
        }

        if (event.getCode() == KeyCode.E) {
            // 按住 E 时 JavaFX 会重复发送事件，这里限制为每次按下只交互一次。
            if (!interactKeyPressed) {
                interactKeyPressed = true;
                controller.setInteracting(true);
                showInteractionResult(controller.interact());
            }
            event.consume();
            return;
        }

        if (isDashKey(event.getCode())) {
            if (!dashKeyPressed) {
                dashKeyPressed = true;
                controller.requestDash();
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
        if (event.getCode() == KeyCode.ESCAPE) {
            pauseKeyPressed = false;
            event.consume();
            return;
        }

        if (event.getCode() == KeyCode.E) {
            interactKeyPressed = false;
            controller.setInteracting(false);
            event.consume();
            return;
        }

        if (isDashKey(event.getCode())) {
            dashKeyPressed = false;
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

    private boolean isDashKey(KeyCode keyCode) {
        return keyCode == KeyCode.SHIFT;
    }

    /**
     * JavaFX 负责触发渲染；累加器保证游戏逻辑按固定 60 Hz 更新。
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

                double elapsedSeconds = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;
                if (controller.getState() != GameState.RUNNING) {
                    accumulatedSeconds = 0;
                    renderFrame(graphics);
                    return;
                }
                accumulatedSeconds += Math.min(
                        elapsedSeconds,
                        GameConfig.MAX_ACCUMULATED_SECONDS
                );

                while (!disposed
                        && controller.getState() == GameState.RUNNING
                        && accumulatedSeconds >= GameConfig.FIXED_STEP_SECONDS) {
                    controller.update(GameConfig.FIXED_STEP_SECONDS);
                    accumulatedSeconds -= GameConfig.FIXED_STEP_SECONDS;
                }

                if (!disposed) {
                    renderFrame(graphics);
                }
            }
        };
        gameLoop.start();
    }

    @FXML
    private void onPauseButtonClick() {
        pauseGame();
    }

    @FXML
    private void onContinueButtonClick() {
        resumeGame();
    }

    @FXML
    private void onMusicToggleButtonClick() {
        soundEnabled = !soundEnabled;
        changeSound.accept(soundEnabled);
        updateMusicButton();
        Platform.runLater(gameCanvas::requestFocus);
    }

    private void updateMusicButton() {
        musicToggleButton.setText(soundEnabled ? "♫" : "×");
        musicToggleButton.setAccessibleText(soundEnabled ? "关闭音乐" : "开启音乐");
        musicToggleButton.getStyleClass().remove("muted");
        if (!soundEnabled) {
            musicToggleButton.getStyleClass().add("muted");
        }
    }

    @FXML
    private void onMenuButtonClick() {
        returnToMenu.run();
    }

    @FXML
    private void onLevelSelectButtonClick() {
        returnToLevelSelect.run();
    }

    private void togglePause() {
        if (controller.getState() == GameState.PAUSED) {
            resumeGame();
        } else if (controller.getState() == GameState.RUNNING) {
            pauseGame();
        }
    }

    private void pauseGame() {
        controller.pauseGame();
        interactKeyPressed = false;
        dashKeyPressed = false;
        setPauseOverlayVisible(true);
        Platform.runLater(continueButton::requestFocus);
    }

    private void resumeGame() {
        controller.resumeGame();
        accumulatedSeconds = 0;
        lastTime = 0;
        setPauseOverlayVisible(false);
        Platform.runLater(gameCanvas::requestFocus);
    }

    private void setPauseOverlayVisible(boolean visible) {
        pauseOverlay.setManaged(visible);
        pauseOverlay.setVisible(visible);
    }

    private void renderFrame(GraphicsContext graphics) {
        // 绘制顺序即层级顺序：地图 -> 交互区 -> 物品 -> 玩家 -> HUD。
        graphics.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
        gameMapView.render(graphics, controller.getGameMap());
        interactionAreaView.render(graphics, controller.getInteractionArea());
        gameItemView.render(graphics, controller.getGameMap().getItems());
        playerView.render(graphics, controller.getPlayer());
        renderTime(controller.getRemainingSeconds());
        renderOrders(controller.getActiveOrders(), controller.getScore());
    }

    private void renderOrders(List<Order> orders, int score) {
        scoreLabel.setText("得分 " + score);
        scoreLabel.setTextFill(score < 0 ? Color.RED : Color.web("#F4C95D"));
        List<String> orderIds = orders.stream().map(Order::getId).toList();
        if (!orderIds.equals(renderedOrderIds)) {
            rebuildOrderCards(orders);
            renderedOrderIds = orderIds;
        }

        for (int index = 0; index < orders.size(); index++) {
            Order order = orders.get(index);
            Label timeLabel = orderCards.get(index).timeLabel();
            int remainingSeconds = (int) Math.ceil(order.getRemainingSeconds());
            timeLabel.setText(String.format(
                    "%02d:%02d",
                    remainingSeconds / 60,
                    remainingSeconds % 60
            ));
            timeLabel.setTextFill(
                    remainingSeconds <= 10 ? Color.RED : Color.web("#F4C95D")
            );
        }
    }

    private void rebuildOrderCards(List<Order> orders) {
        ordersBox.getChildren().clear();
        orderCards.clear();

        for (int index = 0; index < orders.size(); index++) {
            Order order = orders.get(index);
            Label indexLabel = new Label("ORDER " + (index + 1));
            indexLabel.getStyleClass().add("order-index");
            Label nameLabel = new Label(order.getRecipe().getDishName());
            nameLabel.getStyleClass().add("order-name");
            Label recipeLabel = new Label(recipeText(order));
            recipeLabel.getStyleClass().add("order-recipe");
            Label timeLabel = new Label();
            timeLabel.getStyleClass().add("order-time");

            HBox header = new HBox(12, indexLabel, timeLabel);
            header.getStyleClass().add("order-card-header");
            VBox card = new VBox(3, header, nameLabel, recipeLabel);
            card.getStyleClass().add("order-card");
            ordersBox.getChildren().add(card);
            orderCards.add(new OrderCard(timeLabel));
        }
    }

    private String recipeText(Order order) {
        return switch (order.getRecipe().getDishType()) {
            case SASHIMI -> "需要：切鱼";
            case ROLL -> "需要：熟米 + 海苔";
        };
    }

    private void showInteractionResult(InteractionResult result) {
        interactionMessageLabel.setText(result.message());
        interactionMessageLabel.setTextFill(result.success()
                ? Color.web("#9BE28F")
                : Color.web("#FF8A80"));
        interactionMessageLabel.setVisible(true);
    }

    private void renderTime(int totalSeconds) {
        if (totalSeconds == lastRenderedSeconds) {
            return;
        }
        lastRenderedSeconds = totalSeconds;
        timeLabel.setText(String.format("%02d:%02d", totalSeconds / 60, totalSeconds % 60));
        timeLabel.setTextFill(totalSeconds <= 10 ? Color.RED : Color.WHITE);
    }

    private void finishGame() {
        stopGameLoop();
        onGameFinished.accept(controller.getResult());
    }

    private void stopGameLoop() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }

    public void dispose() {
        // 换页时同时停止 JavaFX 循环和业务状态，避免旧页面继续更新。
        disposed = true;
        stopGameLoop();
        if (controller != null) {
            controller.stopGame();
        }
    }

    private record OrderCard(Label timeLabel) {
    }
}
