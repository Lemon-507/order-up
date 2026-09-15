package com.orderup;

import com.orderup.config.GameConfig;
import com.orderup.controller.LevelSelectController;
import com.orderup.controller.ResultController;
import com.orderup.controller.StartController;
import com.orderup.model.GameResult;
import com.orderup.view.GameView;
import com.orderup.view.ResultView;
import com.orderup.view.SettingsView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * Order Up 的 JavaFX 启动入口，负责窗口和页面切换。
 */
public class Launcher extends Application {
    private static final String START_FXML = "/com/orderup/fxml/start.fxml";
    private static final String LEVEL_SELECT_FXML = "/com/orderup/fxml/level-select.fxml";
    private static final String SETTINGS_FXML = "/com/orderup/fxml/settings.fxml";
    private static final String GAME_FXML = "/com/orderup/fxml/game.fxml";
    private static final String RESULT_FXML = "/com/orderup/fxml/result.fxml";
    private static final String MENU_MUSIC = "/com/orderup/audio/the-entertainer.mp3";
    private static final String GAME_MUSIC = "/com/orderup/audio/dr-mario.mp3";

    private Stage primaryStage;
    private MediaPlayer musicPlayer;
    private String loadedMusicResource;
    private String requestedMusicResource = MENU_MUSIC;
    private GameView currentGameView;
    private int selectedLevel = GameConfig.DEFAULT_LEVEL;
    private boolean soundEnabled = true;
    private boolean fullScreen;
    private GameResult lastGameResult = GameResult.empty();

    public static void main(String[] args) {
        Application.launch(Launcher.class, args);
    }

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        stage.setTitle("Order Up!");
        stage.setMinWidth(960);
        stage.setMinHeight(540);
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setFullScreenExitHint("");
        stage.fullScreenProperty().addListener((observable, oldValue, newValue) -> fullScreen = newValue);
        showStartScene();
        stage.show();
    }

    public void showStartScene() {
        showScene(START_FXML);
        playMusic(MENU_MUSIC);
    }

    public void showLevelSelectScene() {
        showScene(LEVEL_SELECT_FXML);
        playMusic(MENU_MUSIC);
    }

    public void showSettingsScene() {
        showScene(SETTINGS_FXML);
        playMusic(MENU_MUSIC);
    }

    public void showGameScene() {
        showScene(GAME_FXML);
        playMusic(GAME_MUSIC);
    }

    public void showGameScene(int level) {
        selectedLevel = level;
        showGameScene();
    }

    public void showResultScene() {
        showScene(RESULT_FXML);
        playMusic(MENU_MUSIC);
    }

    public void showResultScene(GameResult result) {
        lastGameResult = result;
        showResultScene();
    }

    private void showScene(String fxmlPath) {
        URL resource = Launcher.class.getResource(fxmlPath);
        if (resource == null) {
            throw new IllegalStateException("FXML resource was not found: " + fxmlPath);
        }

        try {
            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();
            Object nextController = loader.getController();

            if (currentGameView != null) {
                currentGameView.dispose();
                currentGameView = null;
            }
            configureController(nextController);

            Scene scene = primaryStage.getScene();
            if (scene == null) {
                primaryStage.setScene(new Scene(
                        root,
                        GameConfig.WINDOW_WIDTH,
                        GameConfig.WINDOW_HEIGHT
                ));
            } else {
                scene.setRoot(root);
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to load scene: " + fxmlPath, exception);
        }
    }

    private void configureController(Object controller) {
        if (controller instanceof StartController startController) {
            startController.configure(this::showLevelSelectScene, this::showSettingsScene, Platform::exit);
        } else if (controller instanceof LevelSelectController levelSelectController) {
            levelSelectController.configure(this::showGameScene, this::showStartScene);
        } else if (controller instanceof SettingsView settingsView) {
            settingsView.configure(
                    soundEnabled,
                    fullScreen,
                    this::setSoundEnabled,
                    this::setFullScreen,
                    this::showStartScene
            );
        } else if (controller instanceof ResultView resultView) {
            ResultController resultController = new ResultController();
            resultController.configure(
                    resultView,
                    lastGameResult,
                    this::showGameScene,
                    this::showStartScene
            );
        } else if (controller instanceof GameView gameView) {
            gameView.configure(
                    selectedLevel,
                    this::showResultScene,
                    this::showStartScene,
                    this::showLevelSelectScene,
                    soundEnabled,
                    this::setSoundEnabled
            );
            currentGameView = gameView;
        }
    }

    private void setSoundEnabled(boolean enabled) {
        soundEnabled = enabled;
        if (enabled) {
            playMusic(requestedMusicResource);
        } else {
            stopMusic();
        }
    }

    private void setFullScreen(boolean enabled) {
        fullScreen = enabled;
        primaryStage.setFullScreen(enabled);
    }

    private void playMusic(String resourcePath) {
        requestedMusicResource = resourcePath;
        if (!soundEnabled) {
            stopMusic();
            return;
        }

        if (!resourcePath.equals(loadedMusicResource)) {
            if (musicPlayer != null) {
                musicPlayer.dispose();
            }
            URL resource = Launcher.class.getResource(resourcePath);
            if (resource == null) {
                musicPlayer = null;
                loadedMusicResource = null;
                System.err.println("Music resource was not found: " + resourcePath);
                return;
            }
            musicPlayer = new MediaPlayer(new Media(resource.toExternalForm()));
            musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            musicPlayer.setVolume(0.35);
            loadedMusicResource = resourcePath;
        }
        musicPlayer.play();
    }

    private void stopMusic() {
        if (musicPlayer != null) {
            musicPlayer.stop();
        }
    }

    @Override
    public void stop() {
        if (currentGameView != null) {
            currentGameView.dispose();
        }
        if (musicPlayer != null) {
            musicPlayer.dispose();
        }
    }
}
