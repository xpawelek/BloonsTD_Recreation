package app.controller;

import app.model.Balloon;
import app.model.DartTower;
import app.model.DeffenceTower;
import app.model.GameLoop;
import app.utils.AppConstans;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.io.IOException;
import app.core.Main;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class GameController {
    public VBox sideGamePanel;
    public GridPane panelContainer;
    public Button restartGameButton;
    public Button startRoundButton;
    public Label roundLabel;
    public Label moneyLabel;
    public Label livesLabel;
    public Button dartDefensorButton;
    public AnchorPane mapPane;
    @FXML private Button goBack;
    @FXML private ImageView backgroundGameImage;
    @FXML private GridPane gridPane;
    private final GameLoop gameLoop = new GameLoop(this);
    @FXML
    public void initialize() {
        Image image = new Image(getClass().getResourceAsStream("/app/view/assets/images/bloons_map.png"));
        backgroundGameImage.setImage(image);
        backgroundGameImage.setFitWidth(AppConstans.SCREEN_WIDTH);
        backgroundGameImage.setFitHeight(AppConstans.SCREEN_HEIGHT);

        dartDefensorButton.getStyleClass().add("defense-button");
        panelContainer.getStyleClass().add("panel-container");
        sideGamePanel.getStyleClass().add("side-game-panel");


        PauseTransition pause = new PauseTransition(Duration.millis(50));
        pause.setOnFinished(event -> {
            AppConstans.gameState.setGameContinues();
            gameLoop.start();
        });
        pause.play();
    }
    @FXML
    public void startRound() throws IOException
    {
        System.out.println("!");
        AppConstans.gameState.setRoundContinues();
        AppConstans.gameState.changeWaveStarted();
    }

    @FXML
    public void restartGame() throws IOException
    {
        System.out.println("!");
        AppConstans.gameState.restartGame();
        Main.switchScene("/app/view/fxml/game.fxml");
    }

    public void updateRoundLabel(String newInfo)
    {
        roundLabel.setText(newInfo);
    }

    public void updateMoneyLabel(String newInfo)
    {
        moneyLabel.setText(newInfo);
    }

    public void updateLivesLabel(String newInfo)
    {
        livesLabel.setText(newInfo);
    }


    public void setStartRoundButtonDisabled()
    {
        startRoundButton.setDisable(true);
    }

    public void setStartRoundButtonEnabled()
    {
        startRoundButton.setDisable(false);
    }

    public void addElementOnGridPane(Balloon balloon)
    {
        //gridPane.getChildren().add(balloon.getImageView());
        gridPane.add(balloon.getImageView(), 0,0);
        /*
        System.out.println("Dzieci gridPane:");
        for (Node node : gridPane.getChildren()) {
            System.out.println("- " + node + ", id: " + node.getId());
        }
         */
        //grid pane add with row and column?
    }

    public GridPane getGridPane()
    {
        return gridPane;
    }

    public void removeElementFromGridPane(Balloon balloon)
    {
        gridPane.getChildren().remove(balloon.getImageView());
        //po zmianie image nie wiadomo czy nie inny sposob usuniecia np po id
    }

    public double getPositionY(Balloon balloon)
    {
        Bounds bounds = balloon.getImageView().localToScene(balloon.getImageView().getBoundsInLocal());
        return bounds.getCenterY();
    }

    public void dartDefensorClicked(MouseEvent mouseEvent) {
        Image dartImage = new Image(getClass().getResource("/app/view/assets/images/dart_defensor.png").toExternalForm());
        ImageView ghostMonkey = new ImageView(dartImage);
        ghostMonkey.setOpacity(0.8);
        ghostMonkey.setMouseTransparent(true);

        double centerX = dartImage.getWidth() / 2;
        double centerY = dartImage.getHeight() / 2;
        double range = 80;

        Circle rangeCircle = new Circle(range);
        rangeCircle.setStroke(Color.color(1, 1, 1, 0.5));
        rangeCircle.setFill(Color.color(1, 1, 1, 0.1));
        rangeCircle.setMouseTransparent(true);
        rangeCircle.setCenterX(centerX);
        rangeCircle.setCenterY(centerY);

        Group ghost = new Group(rangeCircle, ghostMonkey);
        ghost.setLayoutX(mouseEvent.getX() - centerX);
        ghost.setLayoutY(mouseEvent.getY() - centerY);
        ghost.setManaged(false);

        mapPane.getChildren().add(ghost);

        mapPane.setOnMouseMoved(e -> {
            if(e.getY() < AppConstans.SCREEN_HEIGHT - centerY)
            {
                ghost.setLayoutX(e.getX() - centerX);
                ghost.setLayoutY(e.getY() - centerY);
            }
            return;
        });

        mapPane.setOnMouseClicked(e -> {
            //DartTower tower = new DartTower(e.getX() - centerX, e.getY() - centerY);
            //AppConstans.boughtTowers.add(tower);
            mapPane.getChildren().remove(ghost);
            ImageView monkey = new ImageView(dartImage);
            monkey.setLayoutX(e.getX() - centerX);
            monkey.setLayoutY(e.getY() - centerY);
            monkey.setMouseTransparent(false);
            mapPane.getChildren().add(monkey);
        });

    }
}
