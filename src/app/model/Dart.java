package app.model;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

public class Dart {
    private ImageView dartImage;
    private double speed, towerPositionX, towerPositionY, targetPositionX, targetPositionY;
    private boolean finished = false;
    private Timeline dartFireTimeline;

    public Dart(double towerPositionX, double towerPositionY, double targetPositionX, double targetPositionY) {
        dartImage = new ImageView(new Image("/app/view/assets/images/dart_image.png"));
        speed = 0.5;
        this.towerPositionX = towerPositionX;
        this.towerPositionY = towerPositionY;
        this.targetPositionX = targetPositionX;
        this.targetPositionY = targetPositionY;
    }

    public void throwDart(Balloon balloon) {
        double dx = targetPositionX - towerPositionX;
        double dy = targetPositionY - towerPositionY;
        double distance = Math.hypot(dx, dy);

        double vx = dx / distance;
        double vy = dy / distance;
        double speed = 5; // pixels per frame (tune this)

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(200), event -> {
            double x = dartImage.getX() + vx * speed;
            double y = dartImage.getY() + vy * speed;
            dartImage.setX(x);
            dartImage.setY(y);

            // Check if close to target
            if (Math.hypot(x - targetPositionX, y - targetPositionY) < 8) {
                this.finished = true;
                balloon.updateBallonLives();
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }


    public ImageView getDartImage() {
        return dartImage;
    }

    public boolean isFinished() {
        return finished;
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
