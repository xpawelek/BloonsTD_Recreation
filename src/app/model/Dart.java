package app.model;
import app.utils.AppConstans;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Optional;

public class Dart {
    private final ImageView dartImage;
    private double speed, towerPositionX, towerPositionY, targetPositionX, targetPositionY;
    private Timeline dartFireTimeline;

    public Dart(double towerPositionX, double towerPositionY) {
        dartImage = new ImageView(new Image("/app/view/assets/images/dart_image.png"));
        speed = 0.5;
        this.towerPositionX = towerPositionX;
        this.towerPositionY = towerPositionY;
    }

    public Dart(double towerPositionX, double towerPositionY, double targetPositionX, double targetPositionY) {
        dartImage = new ImageView(new Image("/app/view/assets/images/dart_image.png"));
        speed = 0.5;
        this.towerPositionX = towerPositionX;
        this.towerPositionY = towerPositionY;
        this.targetPositionX = targetPositionX;
        this.targetPositionY = targetPositionY;
    }

    public void throwDart(Balloon balloon, Pane mapPane) {
        double dx = targetPositionX - towerPositionX;
        double dy = targetPositionY - towerPositionY;
        double distance = Math.hypot(dx, dy);

        double vx = dx / distance;
        double vy = dy / distance;
        double speed = 6;

        Timeline timeline = new Timeline();
        KeyFrame keyFrame = new KeyFrame(Duration.millis(16), event -> {
            double x = dartImage.getX() + vx * speed;
            double y = dartImage.getY() + vy * speed;
            dartImage.setX(x);
            dartImage.setY(y);

            if (Math.hypot(x - targetPositionX, y - targetPositionY) < 5) {
                mapPane.getChildren().remove(dartImage);
                balloon.updateBalloonLives();
                //AppConstans.gameState.addCoin();
                timeline.stop();
            }
        });

        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }


    public ImageView getDartImage() {
        return dartImage;
    }

    public void setRotate(double angle)
    {
        dartImage.setRotate(angle);
    }

    public void setDartStartingPosition(double towerX, double towerY, double towerWidth, double towerHeight)
    {
        double dartWidth = dartImage.getFitWidth();
        double dartHeight = dartImage.getFitHeight();

        dartImage.setX(towerX + towerWidth / 2 - dartWidth / 2);
        dartImage.setY(towerY + towerHeight / 2 - dartHeight / 2);
    }

}
