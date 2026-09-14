package com.orderup.controller;

import com.orderup.config.GameConfig;
import com.orderup.model.Direction;
import com.orderup.model.GameMap;
import com.orderup.model.GameResult;
import com.orderup.model.GameState;
import com.orderup.model.InteractionArea;
import com.orderup.model.InteractionResult;
import com.orderup.model.Order;
import com.orderup.model.OrderResult;
import com.orderup.model.Plate;
import com.orderup.model.Player;
import com.orderup.model.Tile;
import com.orderup.model.TileType;
import com.orderup.service.GameService;
import com.orderup.service.Impl.GameServiceImpl;
import com.orderup.service.Impl.KitchenServiceImpl;
import com.orderup.service.Impl.OrderServiceImpl;
import com.orderup.service.Impl.PlayerServiceImpl;
import com.orderup.service.Impl.ScoreServiceImpl;
import com.orderup.service.OrderService;
import com.orderup.service.PlayerService;
import com.orderup.service.ScoreService;
import com.orderup.util.GameTimer;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * 编排一局游戏，不包含 JavaFX 显示代码。
 */
public class GameController {
    private final Player player;
    private final InteractionArea interactionArea;
    private final GameMap gameMap;
    private final GameService gameService;
    private final PlayerService playerService;
    private final com.orderup.service.KitchenService kitchenService;
    private final OrderService orderService;
    private final ScoreService scoreService;
    private final GameTimer gameTimer;
    private final Runnable onGameFinished;
    private final List<Double> plateRespawnTimers = new ArrayList<>();

    private GameState state = GameState.READY;
    private boolean interacting;
    private double orderSpawnSeconds = GameConfig.ORDER_SPAWN_INTERVAL_SECONDS;
    private int score;
    private int completedOrderCount;
    private int earnedTips;

    /**
     * 按默认关卡创建一局新游戏。
     *
     * @param onGameFinished 游戏结束后执行的回调，通常用于切换到结算页
     */
    public GameController(Runnable onGameFinished) {
        this(GameConfig.DEFAULT_LEVEL, onGameFinished);
    }

    /**
     * 按指定关卡创建一局新游戏，并初始化玩家、地图、交互区域和服务。
     *
     * @param level 关卡编号
     * @param onGameFinished 游戏结束后执行的回调，通常用于切换到结算页
     */
    public GameController(int level, Runnable onGameFinished) {
        this.onGameFinished = onGameFinished;

        gameService = new GameServiceImpl();
        playerService = new PlayerServiceImpl();
        kitchenService = new KitchenServiceImpl();
        orderService = new OrderServiceImpl(level);
        scoreService = new ScoreServiceImpl();

        player = new Player(GameConfig.PLAYER_START_X, GameConfig.PLAYER_START_Y);
        interactionArea = new InteractionArea();
        gameMap = gameService.createMap(level);
        gameTimer = new GameTimer(this::finishGame);
        interactionArea.updateFrom(player);
    }

    /**
     * 将游戏切换为运行状态，并按配置的总时长启动倒计时。
     */
    public void startGame() {
        state = GameState.RUNNING;
        gameTimer.start(GameConfig.GAME_SECONDS);
        while (orderService.getActiveOrders().size() < GameConfig.INITIAL_ACTIVE_ORDER_COUNT) {
            orderService.createRandomOrder();
        }
    }

    /**
     * 更新一次游戏逻辑：移动玩家、同步交互区和手持物品、
     * 推进加工设施、刷新可交互格子，最后推进倒计时。
     *
     * @param deltaSeconds 本次逻辑更新要推进的秒数
     */
    public void update(double deltaSeconds) {
        if (state != GameState.RUNNING) {
            return;
        }
        playerService.move(
                player,
                deltaSeconds,
                GameConfig.WINDOW_WIDTH,
                GameConfig.WINDOW_HEIGHT,
                gameMap
        );
        interactionArea.updateFrom(player);
        kitchenService.updateHeldItem(player, interactionArea);
        kitchenService.updateProcessing(
                interactionArea,
                gameMap,
                interacting,
                deltaSeconds
        );
        score += orderService.updateOrders(deltaSeconds);
        ensureAtLeastOneActiveOrder();
        updateOrderSpawning(deltaSeconds);
        updatePlateRespawn(deltaSeconds);
        updateInteractableTiles();
        gameTimer.update(deltaSeconds);
    }

    /**
     * 记录一个移动方向已被按下。
     *
     * @param direction 按下的移动方向
     */
    public void press(Direction direction) {
        player.press(direction);
    }

    /**
     * 记录一个移动方向已被松开。
     *
     * @param direction 松开的移动方向
     */
    public void release(Direction direction) {
        player.release(direction);
    }

    /**
     * 清空当前所有移动输入，防止窗口失去焦点后玩家继续移动。
     */
    public void clearInput() {
        player.clearInput();
        interacting = false;
    }

    /**
     * 记录玩家是否持续按住交互键，供切菜板累计加工时间。
     */
    public void setInteracting(boolean interacting) {
        this.interacting = interacting;
    }

    /** 暂停游戏逻辑和倒计时推进。 */
    public void pauseGame() {
        if (state == GameState.RUNNING) {
            state = GameState.PAUSED;
            clearInput();
        }
    }

    /** 从暂停状态继续游戏。 */
    public void resumeGame() {
        if (state == GameState.PAUSED) {
            state = GameState.RUNNING;
        }
    }

    /**
     * 让玩家与面前的设施或物品交互。
     *
     * @return 交互是否成功及对应提示信息
     */
    public InteractionResult interact() {
        if (state != GameState.RUNNING) {
            return InteractionResult.failed("游戏未运行");
        }
        Tile tile = kitchenService.findTile(interactionArea, gameMap);
        if (tile != null && tile.getType() == TileType.ORDER_COUNTER) {
            return submitHeldPlate();
        }
        return kitchenService.interact(player, interactionArea, gameMap);
    }

    private InteractionResult submitHeldPlate() {
        if (!(player.getHeldItem() instanceof Plate plate)) {
            return InteractionResult.failed("请手持盘子到出餐口提交");
        }

        Order matchedOrder = orderService.getActiveOrders().stream()
                .filter(order -> plate.matches(order.getRecipe()))
                .findFirst()
                .orElse(null);
        int tip = scoreService.calculateTip(matchedOrder);
        OrderResult result = orderService.submitPlate(plate);
        player.releaseHeldItem();
        gameMap.removeItem(plate);
        plateRespawnTimers.add(GameConfig.PLATE_RESPAWN_SECONDS);

        score += result.scoreDelta();
        if (result.success()) {
            completedOrderCount++;
            earnedTips += tip;
            ensureAtLeastOneActiveOrder();
            return InteractionResult.ok(result.message() + "，得分 +" + result.scoreDelta());
        }
        return InteractionResult.failed(
                result.message() + "，扣分 " + Math.abs(result.scoreDelta())
        );
    }

    private void ensureAtLeastOneActiveOrder() {
        if (orderService.getActiveOrders().isEmpty()) {
            orderService.createRandomOrder();
        }
    }

    private void updateOrderSpawning(double deltaSeconds) {
        orderSpawnSeconds -= deltaSeconds;
        if (orderSpawnSeconds <= 0) {
            orderService.createRandomOrder();
            orderSpawnSeconds = GameConfig.ORDER_SPAWN_INTERVAL_SECONDS;
        }
    }

    private void updatePlateRespawn(double deltaSeconds) {
        ListIterator<Double> iterator = plateRespawnTimers.listIterator();
        while (iterator.hasNext()) {
            double remainingSeconds = iterator.next() - deltaSeconds;
            if (remainingSeconds <= 0) {
                gameService.addEmptyPlate(gameMap);
                iterator.remove();
            } else {
                iterator.set(remainingSeconds);
            }
        }
    }

    /**
     * 正常结束本局游戏，停止运行状态后通知显示层切换页面。
     */
    public void finishGame() {
        if (state == GameState.FINISHED) {
            return;
        }
        stopGame();
        onGameFinished.run();
    }

    /**
     * 停止本局游戏和倒计时，但不触发页面切换回调。
     */
    public void stopGame() {
        state = GameState.FINISHED;
        gameTimer.stop();
        clearInput();
    }

    /**
     * 根据玩家面前的交互区域，刷新每个地图格子的范围标记。
     */
    private void updateInteractableTiles() {
        for (Tile[] row : gameMap.getTiles()) {
            for (Tile tile : row) {
                tile.setInteractable(interactionArea.intersects(
                        tile.getX(),
                        tile.getY(),
                        tile.getSize(),
                        tile.getSize()
                ));
            }
        }
    }

    /** @return 本局游戏的玩家对象 */
    public Player getPlayer() {
        return player;
    }

    /** @return 玩家当前面朝方向的交互检测区域 */
    public InteractionArea getInteractionArea() {
        return interactionArea;
    }

    /** @return 本局游戏使用的地图 */
    public GameMap getGameMap() {
        return gameMap;
    }

    /** @return 本局倒计时剩余的整秒数 */
    public int getRemainingSeconds() {
        return gameTimer.getRemainingSeconds();
    }

    /** @return 当前所有未完成的活动订单 */
    public List<Order> getActiveOrders() {
        return orderService.getActiveOrders();
    }

    /** @return 本局已经获得的分数 */
    public int getScore() {
        return score;
    }

    /** @return 已完成的订单数量 */
    public int getCompletedOrderCount() {
        return completedOrderCount;
    }

    /** @return 完成订单时累计获得的小费 */
    public int getEarnedTips() {
        return earnedTips;
    }

    /** @return 当前一局可供结算页展示的快照 */
    public GameResult getResult() {
        return new GameResult(completedOrderCount, earnedTips, score);
    }

    /** @return 当前游戏状态 */
    public GameState getState() {
        return state;
    }

}
