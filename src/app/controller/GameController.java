package app.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;
import app.core.Main;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;

import javafx.scene.layout.HBox;

public class GameController {
    @FXML private Button goBack;
    @FXML private ImageView backgroundGameImage;
    @FXML private GridPane gridPane;

    @FXML
    public void initialize() {
        Image image = new Image(getClass().getResourceAsStream("/app/view/assets/images/bloons_map2.png"));
        backgroundGameImage.setImage(image);

        backgroundGameImage.fitWidthProperty().bind(gridPane.widthProperty().multiply(1));
        backgroundGameImage.fitHeightProperty().bind(gridPane.heightProperty().multiply(1)); // Full height



    }
    @FXML
    public void goBack() throws IOException
    {
        System.out.println("Go back button clicked!");
        Main.switchScene("/app/view/fxml/menu.fxml");
    }
}
