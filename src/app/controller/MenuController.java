package app.controller;

import app.utils.AppConstans;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.IOException;
import app.core.Main;
import javafx.util.Duration;


public class MenuController {
    @FXML
    private Label titleLabel;
    @FXML private Button startGameButton;
    @FXML private ImageView backgroundGameImage;
    @FXML private GridPane gridPane;

    @FXML
    public void initialize() {
        Image image = new Image(getClass().getResourceAsStream("/app/view/assets/images/home_page.png"));

        gridPane.setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        startGameButton.getStyleClass().add("start-button-game");
        startGameButton.setOnMouseEntered(e -> scaleUp(startGameButton));
        startGameButton.setOnMouseExited(e -> scaleDown(startGameButton));
    }

    @FXML
    private void startGame() throws IOException{
        Main.switchScene("/app/view/fxml/game.fxml");
    }

    public void scaleUp(Node node) {
        ScaleTransition st = new ScaleTransition(Duration.millis(120), node);
        st.setFromX(1.0);
        st.setFromY(1.0);
        st.setToX(1.15);
        st.setToY(1.15);
        st.play();
    }

    public void scaleDown(Node node) {
        ScaleTransition st = new ScaleTransition(Duration.millis(120), node);
        st.setFromX(1.15);
        st.setFromY(1.15);
        st.setToX(1.0);
        st.setToY(1.0);
        st.play();
    }

}
