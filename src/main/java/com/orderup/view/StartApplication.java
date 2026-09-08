package com.orderup.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class StartApplication extends Application {
    private MediaPlayer backgroundMusic;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("/com/orderup/fxml/start.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("Order Up!");
        stage.setMinWidth(960);
        stage.setMinHeight(540);
        stage.setScene(scene);
        playBackgroundMusic();
        stage.show();
    }

    private void playBackgroundMusic() {
        URL musicResource = StartApplication.class.getResource("/com/orderup/audio/startmenu.mp3");
        if (musicResource == null) {
            System.err.println("Start menu music was not found: /com/orderup/audio/startmenu.mp3");
            return;
        }

        backgroundMusic = new MediaPlayer(new Media(musicResource.toExternalForm()));
        backgroundMusic.setCycleCount(MediaPlayer.INDEFINITE);
        backgroundMusic.setVolume(0.35);
        backgroundMusic.play();
    }

    @Override
    public void stop() throws Exception {
        // Release native audio resources when the application exits.
        if (backgroundMusic != null) {
            backgroundMusic.stop();
            backgroundMusic.dispose();
        }
        super.stop();
    }
}
