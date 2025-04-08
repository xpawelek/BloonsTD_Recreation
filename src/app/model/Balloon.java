package app.model;

import app.utils.AppConstans;
import javafx.animation.AnimationTimer;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

public class Balloon {
    private int ballon_lives;
    private int ballon_speed;
    private ImageView ballon_img;
    private double offset_y = -290 - (-100);
    private Path path = new Path();

    public Balloon() {}
    public Balloon(int ballon_lives) {
        this.ballon_lives = ballon_lives;
        this.ballon_speed = 3;
        this.ballon_img = new ImageView(AppConstans.ballon_img_list.get(ballon_lives));
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

    public int getBallonLives()
    {
        return ballon_lives;
    }


    public void updateBallonLives()
    {
        ballon_lives--;
        if(ballon_lives <= 0)
        {
            Balloon obj = new Balloon();
            obj = null;
            return;
        }

        updateImage(AppConstans.ballon_img_list.get(ballon_lives));
        //speed hash map?
        ballon_speed -= (int)(Math.random() * 2);
    }

    public void followPath() {
        Path path = new Path();
        path.getElements().add(new MoveTo(350, -150 + this.offset_y));
        path.getElements().add(new LineTo(346, -50 + this.offset_y));
        path.getElements().add(new LineTo(337, -30 + this.offset_y));
        path.getElements().add(new LineTo(299, -18 + this.offset_y));//82
        path.getElements().add(new LineTo(229, -14 + this.offset_y)); //86
        path.getElements().add(new LineTo(220, 8 + this.offset_y)); // 108
        path.getElements().add(new LineTo(222, 68 + this.offset_y)); // 168
        path.getElements().add(new LineTo(246, 91 + this.offset_y)); // 191
        path.getElements().add(new LineTo(428, 103 + this.offset_y)); // 203
        path.getElements().add(new LineTo(443, 118 + this.offset_y)); // 218
        path.getElements().add(new LineTo(440,  195 + this.offset_y)); // 295
        path.getElements().add(new LineTo(414,  215 + this.offset_y)); // 315
        path.getElements().add(new LineTo(349,  228 + this.offset_y)); // 328
        path.getElements().add(new LineTo(337,  241 + this.offset_y)); // 341
        path.getElements().add(new LineTo(326,  360 + this.offset_y)); // 460
        path.getElements().add(new LineTo(307,  380 + this.offset_y)); // 480
        path.getElements().add(new LineTo(208,  389 + this.offset_y)); // 480
        path.getElements().add(new LineTo(191,  414 + this.offset_y)); // 505
        path.getElements().add(new LineTo(191,  559 + this.offset_y)); // 650

        System.out.println(ballon_img.getY());
        PathTransition pathTransition = new PathTransition();
        pathTransition.setPath(path);
        pathTransition.setDuration(Duration.seconds(ballon_speed));
        pathTransition.setCycleCount(1); //1
        pathTransition.setNode(getImageView());
        pathTransition.play();
        //finish reached?
    }
}
