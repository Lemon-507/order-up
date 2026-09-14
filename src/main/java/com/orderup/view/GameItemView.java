package com.orderup.view;

import com.orderup.model.GameItem;
import com.orderup.model.Ingredient;
import com.orderup.model.IngredientStatus;
import com.orderup.model.Plate;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.List;

/**
 * 使用食物素材绘制食材、盘子内容和完成菜品。
 */
public class GameItemView {
    private static final String FOOD_ROOT = "/com/orderup/images/food/";
    private static final double INGREDIENT_VISUAL_SIZE = 48;
    private static final double DISH_VISUAL_SIZE = 46;

    private final Image rawFish = loadImage("raw_fish.png");
    private final Image cutFish = loadImage("cut_fish.png");
    private final Image rawRice = loadImage("raw_rice.png");
    private final Image cookedRice = loadImage("cooked_rice.png");
    private final Image nori = loadImage("nori.png");
    private final Image onigiri = loadImage("onigiri.png");

    public void render(GraphicsContext graphics, List<GameItem> items) {
        graphics.save();
        graphics.setImageSmoothing(false);
        for (GameItem item : items) {
            if (item instanceof Ingredient ingredient) {
                renderIngredient(graphics, ingredient);
            } else if (item instanceof Plate plate) {
                renderPlate(graphics, plate);
            } else {
                graphics.setFill(Color.SLATEBLUE);
                graphics.fillRect(item.getX(), item.getY(), item.getWidth(), item.getHeight());
            }
        }
        graphics.restore();
    }

    private void renderIngredient(GraphicsContext graphics, Ingredient ingredient) {
        double x = ingredient.getX()
                + (ingredient.getWidth() - INGREDIENT_VISUAL_SIZE) / 2;
        double y = ingredient.getY()
                + (ingredient.getHeight() - INGREDIENT_VISUAL_SIZE) / 2;
        graphics.drawImage(
                ingredientImage(ingredient),
                x,
                y,
                INGREDIENT_VISUAL_SIZE,
                INGREDIENT_VISUAL_SIZE
        );
    }

    private Image ingredientImage(Ingredient ingredient) {
        return switch (ingredient.getType()) {
            case FISH -> ingredient.getStatus() == IngredientStatus.CUT
                    ? cutFish
                    : rawFish;
            case RICE -> ingredient.getStatus() == IngredientStatus.COOKED
                    ? cookedRice
                    : rawRice;
            case KELP -> nori;
        };
    }

    private void renderPlate(GraphicsContext graphics, Plate plate) {
        graphics.setFill(Color.web("#F2F4F3"));
        graphics.fillOval(plate.getX(), plate.getY(), plate.getWidth(), plate.getHeight());
        graphics.setStroke(Color.web("#82919A"));
        graphics.setLineWidth(3);
        graphics.strokeOval(plate.getX(), plate.getY(), plate.getWidth(), plate.getHeight());

        renderPlateContents(graphics, plate);
    }

    private void renderPlateContents(GraphicsContext graphics, Plate plate) {
        if (plate.getDishType() != null) {
            Image dishImage = switch (plate.getDishType()) {
                case SASHIMI -> cutFish;
                case ROLL -> onigiri;
            };
            graphics.drawImage(
                    dishImage,
                    plate.getX() + (plate.getWidth() - DISH_VISUAL_SIZE) / 2,
                    plate.getY() + (plate.getHeight() - DISH_VISUAL_SIZE) / 2 - 3,
                    DISH_VISUAL_SIZE,
                    DISH_VISUAL_SIZE
            );
            return;
        }

        List<Ingredient> contents = plate.getContents();
        int visibleCount = Math.min(contents.size(), 3);
        if (visibleCount == 0) {
            return;
        }

        double markerSize = visibleCount <= 2 ? 18 : 12;
        double gap = 2;
        double totalWidth = visibleCount * markerSize + (visibleCount - 1) * gap;
        double startX = plate.getX() + (plate.getWidth() - totalWidth) / 2;
        double markerY = plate.getY() + (plate.getHeight() - markerSize) / 2;

        for (int index = 0; index < visibleCount; index++) {
            Ingredient ingredient = contents.get(index);
            double markerX = startX + index * (markerSize + gap);
            graphics.drawImage(
                    ingredientImage(ingredient),
                    markerX,
                    markerY,
                    markerSize,
                    markerSize
            );
        }
    }

    private Image loadImage(String fileName) {
        String path = FOOD_ROOT + fileName;
        URL resource = GameItemView.class.getResource(path);
        if (resource == null) {
            throw new IllegalStateException("找不到食物素材：" + path);
        }
        return new Image(resource.toExternalForm());
    }
}
