package com.orderup.view;

import com.orderup.model.Direction;
import com.orderup.model.Plate;
import com.orderup.model.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.net.URL;
import java.util.EnumMap;
import java.util.Map;

/**
 * 玩家在画布上的显示方式。
 */
public class PlayerView {
    private static final String SPRITE_ROOT = "/com/orderup/images/player/";
    private static final long FRAME_DURATION_NANOS = 120_000_000L;
    private static final double VISUAL_WIDTH = 92;
    private static final double VISUAL_HEIGHT = 92;

    private final Map<Direction, Image[]> walkingSprites = new EnumMap<>(Direction.class);
    private final Map<Direction, Image[]> dishSprites = new EnumMap<>(Direction.class);
    private long animationStartNanos = System.nanoTime();
    private Direction previousDirection;
    private boolean previouslyMoving;
    private boolean previouslyCarryingDish;

    public PlayerView() {
        Image[] left = loadFrames("left/frame-%d.png", 6);
        Image[] leftWithDish = loadFrames("left-with-dish/frame-%d.png", 6);

        walkingSprites.put(Direction.UP, loadFrames("up/frame-%d.png", 6));
        walkingSprites.put(Direction.DOWN, loadFrames("down/frame-%d.png", 3));
        walkingSprites.put(Direction.LEFT, left);
        // RIGHT 在绘制时水平翻转 LEFT，不额外保存一套重复资源。
        walkingSprites.put(Direction.RIGHT, left);

        dishSprites.put(Direction.DOWN, loadFrames("down-with-dish/frame-%d.png", 4));
        dishSprites.put(Direction.LEFT, leftWithDish);
        dishSprites.put(Direction.RIGHT, leftWithDish);
    }

    public void render(GraphicsContext graphics, Player player) {
        Direction direction = player.getFacingDirection();
        boolean moving = isMoving(player);
        boolean carryingDish = player.getHeldItem() instanceof Plate;
        long now = System.nanoTime();

        if (direction != previousDirection
                || moving != previouslyMoving
                || carryingDish != previouslyCarryingDish) {
            animationStartNanos = now;
            previousDirection = direction;
            previouslyMoving = moving;
            previouslyCarryingDish = carryingDish;
        }

        Image[] frames = framesFor(direction, carryingDish);
        int frameIndex = moving
                ? (int) ((now - animationStartNanos) / FRAME_DURATION_NANOS % frames.length)
                : 0;
        drawSprite(graphics, player, frames[frameIndex], direction == Direction.RIGHT);
    }

    private Image[] framesFor(Direction direction, boolean carryingDish) {
        if (carryingDish && dishSprites.containsKey(direction)) {
            return dishSprites.get(direction);
        }
        return walkingSprites.get(direction);
    }

    private boolean isMoving(Player player) {
        return player.isMoving(Direction.UP)
                || player.isMoving(Direction.DOWN)
                || player.isMoving(Direction.LEFT)
                || player.isMoving(Direction.RIGHT);
    }

    private void drawSprite(
            GraphicsContext graphics,
            Player player,
            Image image,
            boolean mirrorHorizontally
    ) {
        double scale = Math.min(
                VISUAL_WIDTH / image.getWidth(),
                VISUAL_HEIGHT / image.getHeight()
        );
        double drawWidth = image.getWidth() * scale;
        double drawHeight = image.getHeight() * scale;
        double drawX = player.getX() + (Player.WIDTH - drawWidth) / 2;
        double drawY = player.getY() + Player.HEIGHT - drawHeight;

        graphics.save();
        graphics.setImageSmoothing(false);
        if (mirrorHorizontally) {
            graphics.translate(drawX + drawWidth, 0);
            graphics.scale(-1, 1);
            graphics.drawImage(image, 0, drawY, drawWidth, drawHeight);
        } else {
            graphics.drawImage(image, drawX, drawY, drawWidth, drawHeight);
        }
        graphics.restore();
    }

    private Image[] loadFrames(String pathPattern, int frameCount) {
        Image[] frames = new Image[frameCount];
        for (int index = 0; index < frameCount; index++) {
            String path = SPRITE_ROOT + pathPattern.formatted(index + 1);
            URL resource = PlayerView.class.getResource(path);
            if (resource == null) {
                throw new IllegalStateException("找不到玩家动画资源：" + path);
            }
            frames[index] = new Image(resource.toExternalForm());
        }
        return frames;
    }
}
