package app.model;

import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

public class Ballon {
    private int starting_x;
    private int starting_y;
    private int ballon_lives;
    private int ballon_speed;
    private ImageView ballon_img;

    public Ballon(int ballon_lives, int ballon_speed, ImageView ballon_img) {
        this.ballon_lives = ballon_lives;
        this.ballon_speed = ballon_speed;
        this.ballon_img = ballon_img;
        this.ballon_img.setFitHeight(30);
        this.ballon_img.setFitWidth(30);
    }


    public ImageView getImageView() {
        return ballon_img;
    }

    public void followPath() {
        Path path = new Path();
        double offset_y = -290 - (-100);
        path.getElements().add(new MoveTo(350, -100 + offset_y));
        path.getElements().add(new LineTo(346, -50 + offset_y));
        path.getElements().add(new LineTo(337, -30 + offset_y));
        path.getElements().add(new LineTo(299, -18 + offset_y));//82
        path.getElements().add(new LineTo(229, -14 + offset_y)); //86
        path.getElements().add(new LineTo(220, 8 + offset_y)); // 108
        path.getElements().add(new LineTo(222, 68 + offset_y)); // 168
        path.getElements().add(new LineTo(246, 91 + offset_y)); // 191
        path.getElements().add(new LineTo(428, 103 + offset_y)); // 203
        path.getElements().add(new LineTo(443, 118 + offset_y)); // 218
        path.getElements().add(new LineTo(440,  195 + offset_y)); // 295
        path.getElements().add(new LineTo(414,  215 + offset_y)); // 315
        path.getElements().add(new LineTo(349,  228 + offset_y)); // 328
        path.getElements().add(new LineTo(337,  241 + offset_y)); // 341
        path.getElements().add(new LineTo(326,  360 + offset_y)); // 460
        path.getElements().add(new LineTo(307,  380 + offset_y)); // 480
        path.getElements().add(new LineTo(208,  389 + offset_y)); // 480
        path.getElements().add(new LineTo(191,  414 + offset_y)); // 505
        path.getElements().add(new LineTo(191,  559 + offset_y)); // 650


        PathTransition pathTransition = new PathTransition();
        pathTransition.setNode(getImageView());
        pathTransition.setPath(path);
        pathTransition.setDuration(Duration.seconds(5));
        pathTransition.setCycleCount(Timeline.INDEFINITE); //1
        pathTransition.play();
    }
}
