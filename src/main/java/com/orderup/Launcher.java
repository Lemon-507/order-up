package com.orderup;

import com.orderup.controller.SceneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * Order Up 的应用入口，统一管理主窗口和页面切换。
 */
public class Launcher extends Application {
    private static final double WINDOW_WIDTH = 1280;
    private static final double WINDOW_HEIGHT = 720;

    private Stage primaryStage;
    private MediaPlayer menuMusic;

    public static void main(String[] args) {
        Application.launch(Launcher.class, args);
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

    public void showStartScene() {
        showScene("/com/orderup/fxml/start.fxml");
        playMenuMusic();
    }

    public void showGameScene() {
        stopMenuMusic();
        showScene("/com/orderup/fxml/game.fxml");
    }

    public void showResultScene() {
        stopMenuMusic();
        showScene("/com/orderup/fxml/result.fxml");
    }

    private void showScene(String fxmlPath) {
        URL fxmlResource = Launcher.class.getResource(fxmlPath);
        if (fxmlResource == null) {
            throw new IllegalStateException("FXML resource was not found: " + fxmlPath);
        }

        FXMLLoader loader = new FXMLLoader(fxmlResource);
        try {
            Parent root = loader.load();
            Object controller = loader.getController();
            if (controller instanceof SceneController sceneController) {
                sceneController.setApplication(this);
            }

            Scene currentScene = primaryStage.getScene();
            if (currentScene == null) {
                primaryStage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
            } else {
                currentScene.setRoot(root);
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to load scene: " + fxmlPath, exception);
        }
    }

    private void playMenuMusic() {
        if (menuMusic != null) {
            menuMusic.play();
            return;
        }

        URL musicResource = Launcher.class.getResource("/com/orderup/audio/startmenu.mp3");
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
        if (menuMusic != null) {
            menuMusic.dispose();
            menuMusic = null;
        }
    }
}
