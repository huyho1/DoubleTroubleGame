package com.example.doubletroublegame;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class PlayScreenController {

    @FXML
    private Button playButton;

    @FXML
    private Button youButton;

    @FXML
    private Button compButton;

    //loads the next window
    @FXML
    protected void onPlayButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("turn.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) playButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    //allows you to play first
    @FXML
    protected void onYouButton() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("game.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) youButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    //allows computer to play first
    @FXML
    protected void onCompButton() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("game.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) compButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
        GameController gameController = fxmlLoader.getController();
        gameController.play();
    }
}
