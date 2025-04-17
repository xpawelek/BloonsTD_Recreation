package app.model;

import app.utils.AppConstans;
import javafx.animation.PathTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Map;

public class Balloon {
    private int balloon_lives, balloon_speed;
    private ImageView balloon_img;
    private double balloonPositionX, balloonPositionY;
    private final PathTransition pathTransition;

    public Balloon() {
        pathTransition = new PathTransition();
    }
    public Balloon(int balloon_lives) {
        this.balloon_speed =  new ArrayList<>(AppConstans.ballon_img_list.values()).get(balloon_lives);
        this.balloon_img = new ImageView(new ArrayList<>(AppConstans.ballon_img_list.keySet()).get(balloon_lives));
        this.balloon_img.setId("balloon");
        this.balloon_lives = balloon_lives;
        this.pathTransition = new PathTransition();
    }

    public ImageView getImageView() {
        return balloon_img;
    }

    public void updateImage(String imagePath) {
        balloon_img.setImage(new Image(imagePath));
    }

    public int getBalloonLives()
    {
        return balloon_lives;
    }

    public void updateBalloonLives()
    {
        balloon_lives--;
        if(balloon_lives <= 0)
        {
            Balloon obj = new Balloon();
            obj = null;
            return;
        }
        updateImage(new ArrayList<>(AppConstans.ballon_img_list.keySet()).get(balloon_lives));
        balloon_speed -= (int)(Math.random() * 2);
    }

    public void followPath() {
        Path path = new Path();
        path.getElements().add(new MoveTo(AppConstans.roadPoints.getFirst().getKey(), AppConstans.roadPoints.getFirst().getValue()));
        for (Map.Entry<Double, Double> point : AppConstans.roadPoints) {
            path.getElements().add(new LineTo(point.getKey(), point.getValue()));
        }

        pathTransition.setPath(path);
        pathTransition.setDuration(Duration.seconds(balloon_speed));
        pathTransition.setCycleCount(1);
        pathTransition.setNode(getImageView());
        pathTransition.play();
    }

    public void pauseMovingAnimation()
    {
        pathTransition.pause();
    }

    public void resumeMovingAnimation()
    {
        pathTransition.play();
    }

    public double getBalloonPositionX()
    {
        return balloonPositionX;
    }

    public void setBalloonPositionX(double balloonPositionX)
    {
        this.balloonPositionX = balloonPositionX;
    }

    public double getBalloonPositionY()
    {
        return balloonPositionY;
    }
    public void setBalloonPositionY(double balloonPositionY)
    {
        this.balloonPositionY = balloonPositionY;
    }
}
