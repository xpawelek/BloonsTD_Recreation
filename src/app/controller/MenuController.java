package app.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;
import app.core.Main;


public class MenuController {
    @FXML
    private Label titleLabel;
    @FXML private Button startGameButton;
    @FXML private Button settingsButton;
    @FXML private Button exitButton;

    @FXML
    public void initialize() {
        System.out.println("GameController initialized!");
    }

    @FXML
    private void startGame() throws IOException{
        System.out.println("Start Game button clicked!");
        Main.switchScene("/app/view/fxml/game.fxml");
    }

    @FXML
    private void openSettings() {
        System.out.println("Settings button clicked!");
    }

    @FXML
    private void exitGame() {
        System.out.println("Exit button clicked!");
        System.exit(0);
    }
}
