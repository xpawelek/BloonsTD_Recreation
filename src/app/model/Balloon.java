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
    private int ballon_lives;
    private int ballon_speed;
    private ImageView ballon_img;
    private double offset_y = 100;
    private Path path = new Path();
    private double balloonPositionX;
    private double balloonPositionY;

    public Balloon() {}
    public Balloon(int ballon_lives) {
        this.ballon_lives = ballon_lives;
        this.ballon_speed =  new ArrayList<>(AppConstans.ballon_img_list.values()).get(ballon_lives);
        this.ballon_img = new ImageView(new ArrayList<>(AppConstans.ballon_img_list.keySet()).get(ballon_lives));
        this.ballon_img.setId("balloon");
        //this.ballon_img.setVisible(false); -> migajace balony ogarnac
        //this.ballon_img.setFitHeight(30);
        //this.ballon_img.setFitWidth(30);
    }

    public ImageView getImageView() {
        return ballon_img;
    }

    public void updateImage(String imagePath) {
        ballon_img.setImage(new Image(imagePath));
    }

    public int getBalloonLives()
    {
        return ballon_lives;
    }


    public void updateBalloonLives()
    {
        ballon_lives--;
        if(ballon_lives <= 0)
        {
            Balloon obj = new Balloon();
            obj = null;
            return;
        }

        updateImage(new ArrayList<>(AppConstans.ballon_img_list.keySet()).get(ballon_lives));
        //speed hash map?
        ballon_speed -= (int)(Math.random() * 2);
    }

    public void followPath() {
        Path path = new Path();
        path.getElements().add(new MoveTo(AppConstans.roadPoints.getFirst().getKey(), AppConstans.roadPoints.getFirst().getValue()));
        for (Map.Entry<Double, Double> point : AppConstans.roadPoints) {
            path.getElements().add(new LineTo(point.getKey(), point.getValue()));
        }

        PathTransition pathTransition = new PathTransition();
        pathTransition.setPath(path);
        pathTransition.setDuration(Duration.seconds(ballon_speed));
        pathTransition.setCycleCount(1);
        pathTransition.setNode(getImageView());
        pathTransition.play();
    }

    public void setBalloonPositionX(double balloonPositionX)
    {
        this.balloonPositionX = balloonPositionX;
    }

    public double getBalloonPositionX()
    {
        return balloonPositionX;
    }

    public void setBalloonPositionY(double balloonPositionY)
    {
        this.balloonPositionY = balloonPositionY;
    }

    public double getBalloonPositionY()
    {
        return balloonPositionY;
    }
}
