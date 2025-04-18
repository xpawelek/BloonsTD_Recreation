package app.model;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Dart {
    private final ImageView dartImage;
    private double towerPositionX;
    private double towerPositionY;
    private double targetPositionX;
    private double targetPositionY;
    private double currentX;
    private double currentY;

    public Dart(double towerPositionX, double towerPositionY) {
        this.dartImage = new ImageView(new Image("/app/view/assets/images/dart_image.png"));
        this.towerPositionX = towerPositionX;
        this.towerPositionY = towerPositionY;
    }

    public Dart(double towerPositionX, double towerPositionY, double targetPositionX, double targetPositionY) {
        this.dartImage = new ImageView(new Image("/app/view/assets/images/dart_image.png"));
        this.towerPositionX = towerPositionX;
        this.towerPositionY = towerPositionY;
        this.targetPositionX = targetPositionX;
        this.targetPositionY = targetPositionY;
        this.dartImage.setFitWidth(30);
        this.dartImage.setFitHeight(30);
        this.dartImage.setTranslateX(-dartImage.getFitWidth() / 2);
        this.dartImage.setTranslateY(-dartImage.getFitHeight() / 2);
        this.dartImage.setRotate(0);
    }

    public void throwDart(Balloon balloon, Pane mapPane, DartTower owner, int range) {
        double dx = targetPositionX - towerPositionX;
        double dy = targetPositionY - towerPositionY;
        double distance = Math.hypot(dx, dy);

        double vx = dx / distance;
        double vy = dy / distance;
        double speed = 10;

        double angle = Math.toDegrees(Math.atan2(vy, vx));
        dartImage.setRotate(angle);

        currentX += vx * speed;
        currentY += vy * speed;
        dartImage.setX(currentX - dartImage.getFitWidth() / 2);
        dartImage.setY(currentY - dartImage.getFitHeight() / 2);

        if(!owner.balloonStillInRange(balloon)) {
            mapPane.getChildren().remove(dartImage);
            return;
        }

        Timeline timeline = new Timeline();
        KeyFrame keyFrame = new KeyFrame(Duration.millis(16), event -> {
            currentX += vx * speed;
            currentY += vy * speed;

            dartImage.setX(currentX);
            dartImage.setY(currentY);

            if (Math.hypot(currentX - targetPositionX, currentY - targetPositionY) < 5) {
                mapPane.getChildren().remove(dartImage);
                balloon.updateBalloonLives();
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

    public void setDartStartingPosition(double centerX, double centerY) {
        double dartWidth = dartImage.getFitWidth();
        double dartHeight = dartImage.getFitHeight();

        this.currentX = centerX;
        this.currentY = centerY;

        dartImage.setX(centerX - dartWidth / 2);
        dartImage.setY(centerY - dartHeight / 2);
    }
}
