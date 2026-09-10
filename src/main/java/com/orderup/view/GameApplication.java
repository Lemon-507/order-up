package com.orderup.view;

import com.orderup.controller.SceneNavigator;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * JavaFX 应用外壳，负责窗口、页面资源和菜单音乐。
 */
public class GameApplication extends Application implements SceneNavigator {
    private static final double WINDOW_WIDTH = 1280;
    private static final double WINDOW_HEIGHT = 720;
    private static final String START_FXML = "/com/orderup/fxml/start.fxml";
    private static final String GAME_FXML = "/com/orderup/fxml/game.fxml";
    private static final String RESULT_FXML = "/com/orderup/fxml/result.fxml";

    private Stage primaryStage;
    private SceneView currentView;
    private MediaPlayer menuMusic;

    public static void launchApp(String[] args) {
        Application.launch(GameApplication.class, args);
    }

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("Order Up!");
        primaryStage.setMinWidth(960);
        primaryStage.setMinHeight(540);

        showStartScene();
        primaryStage.show();
    }

    @Override
    public void showStartScene() {
        showScene(START_FXML);
        playMenuMusic();
    }

    @Override
    public void showGameScene() {
        stopMenuMusic();
        showScene(GAME_FXML);
    }

    @Override
    public void showResultScene() {
        stopMenuMusic();
        showScene(RESULT_FXML);
    }

    @Override
    public void exitApplication() {
        Platform.exit();
    }

    private void showScene(String fxmlPath) {
        URL fxmlResource = GameApplication.class.getResource(fxmlPath);
        if (fxmlResource == null) {
            throw new IllegalStateException("FXML resource was not found: " + fxmlPath);
        }

        FXMLLoader loader = new FXMLLoader(fxmlResource);
        try {
            Parent root = loader.load();
            Object fxmlController = loader.getController();
            if (!(fxmlController instanceof SceneView nextView)) {
                throw new IllegalStateException("FXML controller must implement SceneView: " + fxmlPath);
            }
            nextView.setNavigator(this);

            if (currentView != null) {
                currentView.dispose();
            }

            Scene currentScene = primaryStage.getScene();
            if (currentScene == null) {
                primaryStage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
            } else {
                currentScene.setRoot(root);
            }
            currentView = nextView;
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to load scene: " + fxmlPath, exception);
        }
    }

    private void playMenuMusic() {
        if (menuMusic != null) {
            menuMusic.play();
            return;
        }

        URL musicResource = GameApplication.class.getResource("/com/orderup/audio/startmenu.mp3");
        if (musicResource == null) {
            System.err.println("Start menu music was not found: /com/orderup/audio/startmenu.mp3");
            return;
        }

        menuMusic = new MediaPlayer(new Media(musicResource.toExternalForm()));
        menuMusic.setCycleCount(MediaPlayer.INDEFINITE);
        menuMusic.setVolume(0.35);
        menuMusic.play();
    }

    private void stopMenuMusic() {
        if (menuMusic != null) {
            menuMusic.stop();
        }
    }

    @Override
    public void stop() {
        if (currentView != null) {
            currentView.dispose();
            currentView = null;
        }
        if (menuMusic != null) {
            menuMusic.dispose();
            menuMusic = null;
        }
    }
}
