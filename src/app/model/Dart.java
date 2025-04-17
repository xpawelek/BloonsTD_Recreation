package app.model;
import app.utils.AppConstans;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Dart {
    private final ImageView dartImage;
    private double dartSpeed;
    private double towerPositionX;
    private double towerPositionY;
    private double targetPositionX;
    private double targetPositionY;
    private Timeline dartFireTimeline;

    public Dart(double towerPositionX, double towerPositionY) {
        this.dartImage = new ImageView(new Image("/app/view/assets/images/dart_image.png"));
        this.dartSpeed = 0.5;
        this.towerPositionX = towerPositionX;
        this.towerPositionY = towerPositionY;
    }

    public Dart(double towerPositionX, double towerPositionY, double targetPositionX, double targetPositionY) {
        this.dartImage = new ImageView(new Image("/app/view/assets/images/dart_image.png"));
        this.dartImage.setOpacity(0);
        this.dartSpeed = 0.5;
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
            dartImage.setOpacity(1);

            if (Math.hypot(x - targetPositionX, y - targetPositionY) < 5) {
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
