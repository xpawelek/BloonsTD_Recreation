package app.controller;

import app.model.*;
import app.utils.AppConstans;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import app.core.Main;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import javafx.scene.Node;

public class GameController {
    public VBox sideGamePanel;
    public GridPane panelContainer;
    public Button restartGameButton;
    public Button startRoundButton;
    public Label roundLabel;
    public Label moneyLabel;
    public Label livesLabel;
    public Button dartDefender;
    public AnchorPane mapPane;
    public FlowPane towersBoard;
    public FlowPane towerModifyBoard;
    public Button sellTower;
    public Button firstUpgrade;
    public Button secondUpgrade;
    @FXML private Button goBack;
    @FXML private ImageView backgroundGameImage;
    @FXML private GridPane gridPane;
    private final GameLoop gameLoop = new GameLoop(this);
    private boolean placingTower = false;
    private Group currentGhost = null;
    private Circle rangeCircle = null;
    private ImageView selectedTower = null;
    @FXML
    public void initialize() {
        Image image = new Image(getClass().getResourceAsStream("/app/view/assets/images/bloons_map.png"));
        backgroundGameImage.setImage(image);
        backgroundGameImage.setFitWidth(AppConstans.SCREEN_WIDTH);
        backgroundGameImage.setFitHeight(AppConstans.SCREEN_HEIGHT);
        towersBoard.setVisible(true);
        towerModifyBoard.setVisible(false);

        dartDefender.getStyleClass().add("defense-button");
        panelContainer.getStyleClass().add("panel-container");
        sideGamePanel.getStyleClass().add("side-game-panel");
        firstUpgrade.getStyleClass().add("upgrades");
        secondUpgrade.getStyleClass().add("upgrades");
        dartDefender.setUserData(TowerType.DART);

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

    public void addBalloonToMapPane(Balloon balloon)
    {
        //gridPane.getChildren().add(balloon.getImageView());
        //gridPane.add(balloon.getImageView(), 0,0);
        mapPane.getChildren().add(balloon.getImageView());
        /*
        System.out.println("Dzieci gridPane:");
        for (Node node : gridPane.getChildren()) {
            System.out.println("- " + node + ", id: " + node.getId());
        }
         */
        //grid pane add with row and column?
    }

    public Pane getMapPane()
    {
        return mapPane;
    }

    public void removeBalloonFromMapPane(Balloon balloon)
    {
        mapPane.getChildren().remove(balloon.getImageView());
        //po zmianie image nie wiadomo czy nie inny sposob usuniecia np po id
    }

    public double getPositionX(Balloon balloon)
    {
        Bounds bounds = balloon.getImageView().localToScene(balloon.getImageView().getBoundsInLocal());
        return bounds.getMinX() + bounds.getWidth() / 2.0;
    }

    public double getPositionY(Balloon balloon)
    {
        Bounds bounds = balloon.getImageView().localToScene(balloon.getImageView().getBoundsInLocal());
        return bounds.getMinY() + bounds.getHeight() / 2.0;
    }

    public void addTowerToMapPane(DeffenceTower deffenceTower)
    {
        System.out.println("dodawniae??");
        ImageView towerImage = deffenceTower.getTowerImg();
        towerImage.setLayoutX(deffenceTower.getTowerX());
        towerImage.setLayoutY(deffenceTower.getTowerY());
        towerImage.setUserData(deffenceTower);
        towerImage.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> towerClickedHandler(event));
        mapPane.getChildren().add(towerImage);
    }

    public void removeTowerFromMapPane(DeffenceTower deffenceTower) {
        if (mapPane.getChildren().contains(deffenceTower.getTowerImg()))
        {
            mapPane.getChildren().remove(deffenceTower.getTowerImg());
            mapPane.getChildren().remove(rangeCircle);
        }
    }

    public void upadateTowerAngle(DeffenceTower deffenceTower)
    {
        if (mapPane.getChildren().contains(deffenceTower.getTowerImg()))
        {
            if(deffenceTower instanceof DartTower)
            {
                deffenceTower.getTowerImg().setRotate(((DartTower) deffenceTower).getAngle() + 90);
            }
        }
    }

    public void towerClickedHandler(MouseEvent event) {
        event.consume();

        Node clickedTower = (Node) event.getSource();
        mapPane.getChildren().remove(rangeCircle);
        AppConstans.currentClickedDeffenceTower = null;
        //clickedTower.setRotate(45);

        if (selectedTower == clickedTower) {
            selectedTower = null;
            return;
        }

        selectedTower = (ImageView)event.getSource();
        AppConstans.currentClickedDeffenceTower = (DeffenceTower) selectedTower.getUserData();

        int range = AppConstans.currentClickedDeffenceTower.getRange();
        rangeCircle = new Circle(range);

        firstUpgrade.setStyle("-fx-background-image: url('" + AppConstans.currentClickedDeffenceTower.getFirstUpgradeImage() + "');");
        secondUpgrade.setStyle("-fx-background-image: url('" + AppConstans.currentClickedDeffenceTower.getSecondUpgradeImage() + "');");

        towersBoard.setVisible(false);
        towerModifyBoard.setVisible(true);

        Point2D towerCenterInMapPane = clickedTower.localToParent(
                clickedTower.getBoundsInLocal().getWidth() / 2,
                clickedTower.getBoundsInLocal().getHeight() / 2
        );

        rangeCircle.setStroke(Color.color(1, 1, 1, 0.5));
        rangeCircle.setFill(Color.color(1, 1, 1, 0.1));
        rangeCircle.setMouseTransparent(true);
        rangeCircle.setCenterX(towerCenterInMapPane.getX());
        rangeCircle.setCenterY(towerCenterInMapPane.getY());

        if (!mapPane.getChildren().contains(rangeCircle))
        {
            mapPane.getChildren().add(rangeCircle);
        }

        mapPane.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY || e.getButton() == MouseButton.SECONDARY) {
                mapPane.getChildren().remove(rangeCircle);
                selectedTower = null;
                AppConstans.currentClickedDeffenceTower = null;
                towersBoard.setVisible(true);
                towerModifyBoard.setVisible(false);
            }
        });

        sellTower.setOnMouseClicked(e -> {
            mapPane.getChildren().remove(rangeCircle);
            selectedTower = null;
            AppConstans.currentClickedDeffenceTower.setSellTower();
            towersBoard.setVisible(true);
            towerModifyBoard.setVisible(false);
        });

        firstUpgrade.setOnMouseClicked(e -> {
            if(AppConstans.gameState.getMoney() < AppConstans.currentClickedDeffenceTower.getFirstUpgradePrice()
            || AppConstans.currentClickedDeffenceTower.isFirstUpgradeBought())
                return;

            AppConstans.currentClickedDeffenceTower.manageFirstUpgrade();
            rangeCircle.setRadius(AppConstans.currentClickedDeffenceTower.getRange());
            //check if enough money + buy + add more worth
            //AppConstans.currentClickedDeffenceTower.setNewRange(150);
        });

        secondUpgrade.setOnMouseClicked(e -> {
            //check if enough money + buy + add more worth
            if(AppConstans.gameState.getMoney() < AppConstans.currentClickedDeffenceTower.getSecondUpgradePrice()
            || AppConstans.currentClickedDeffenceTower.isSecondUpgradeBought())
                return;

            AppConstans.currentClickedDeffenceTower.manageSecondUpgrade();
            rangeCircle.setRadius(AppConstans.currentClickedDeffenceTower.getRange());
        });

    }

    private void cancelPlacing()
    {
        placingTower = false;

        mapPane.setOnMouseClicked(null);
        mapPane.setOnMouseMoved(null);

        if(currentGhost != null)
        {
            mapPane.getChildren().remove(currentGhost);
            currentGhost = null;
        }
    }

    public void defenderIconClickedOnBoard(MouseEvent mouseEvent) {

        TowerType type = (TowerType) ((Node) mouseEvent.getSource()).getUserData();
        DeffenceTower clickedTower = type.create();

        if(clickedTower.getPriceValue() > AppConstans.gameState.getMoney()){
            return;
        }

        System.out.println(clickedTower.getPriceValue());
        System.out.println(clickedTower.getTowerImagePath());

        AtomicBoolean canPlace = new AtomicBoolean(false);

        if(placingTower)
        {
            cancelPlacing();
            return;
        }

        mapPane.getChildren().remove(rangeCircle);
        placingTower = true;

        System.out.println(clickedTower.getTowerImagePath());
        Image towerImage = new Image(getClass().getResource(clickedTower.getTowerImagePath()).toExternalForm());
        ImageView ghostMonkey = new ImageView(towerImage);
        ghostMonkey.setOpacity(0.8);
        ghostMonkey.setMouseTransparent(true);

        double centerX = towerImage.getWidth() / 2;
        double centerY = towerImage.getHeight() / 2;
        double range = clickedTower.getRange();
        int roadWidth = 50;

        rangeCircle = new Circle(range);
        //rangeCircle.setStroke(Color.color(1, 1, 1, 0.5));
        //rangeCircle.setFill(Color.color(1, 1, 1, 0.1));
        rangeCircle.setStroke(Color.color(1, 0, 0, 0.5));
        rangeCircle.setFill(Color.color(1, 0, 0, 0.3));
        rangeCircle.setMouseTransparent(true);
        rangeCircle.setCenterX(centerX);
        rangeCircle.setCenterY(centerY);

        Group ghost = new Group(rangeCircle, ghostMonkey);
        ghost.setLayoutX(mouseEvent.getX() - centerX);
        ghost.setLayoutY(mouseEvent.getY() - centerY);
        ghost.setManaged(false);
        currentGhost = ghost;

        mapPane.getChildren().add(ghost);

        mapPane.setOnMouseMoved(e -> {
            if(e.getY() < AppConstans.SCREEN_HEIGHT - centerY)
            {
                ghost.setLayoutX(e.getX() - centerX);
                ghost.setLayoutY(e.getY() - centerY);

                rangeCircle.setStroke(Color.color(1, 0, 0, 0.5));
                rangeCircle.setFill(Color.color(1, 0, 0, 0.3));

                canPlace.set(false);

                boolean isInRange = false;
                boolean isOnThePath = false;

                //jesli w ktorymkolwiek punkcie odleglosc > range -> mniejsza od
                //wielkosci drogi - tez nie mozna, mniedzy range a droga -> mozna
                for (Map.Entry<Double, Double> point : AppConstans.roadPoints) {
                    double dx = point.getKey() - e.getX();
                    double dy = point.getValue() - e.getY();
                    //euklides ^2, forbid putting
                    if(dx * dx + dy * dy <= range * range)
                    {
                        isInRange = true;
                    }

                    if(dx * dx + dy * dy <= roadWidth * roadWidth)
                    {
                        isOnThePath = true;
                        break;
                    }
                }
                if(isInRange && !isOnThePath)
                {
                    rangeCircle.setStroke(Color.color(1, 1, 1, 0.5));
                    rangeCircle.setFill(Color.color(1, 1, 1, 0.1));
                    canPlace.set(true);
                }
            }
            return;
        });

        mapPane.setOnMouseClicked(e -> {
            if(e.getButton() == MouseButton.SECONDARY) {
                cancelPlacing();
                return;
            }

            if(canPlace.get())
            {
                //DartTower tower = new DartTower(e.getX() - centerX, e.getY() - centerY);
                //AppConstans.boughtTowers.add(tower);
                mapPane.getChildren().remove(ghost);
                //ImageView monkey = new ImageView(dartImage);
                //monkey.setLayoutX(e.getX() - centerX);
                // monkey.setLayoutY(e.getY() - centerY);
                // monkey.setMouseTransparent(false);
                // mapPane.getChildren().add(monkey);

                clickedTower.setPositionX(e.getX() - centerX);
                clickedTower.setPositionY(e.getY() - centerY);
                AppConstans.boughtTowers.add(clickedTower);
                AppConstans.gameState.updateMoneyAfterBuying(clickedTower.getPriceValue());
                System.out.println("dodano tower");

                cancelPlacing();
            }
        });

    }
}
