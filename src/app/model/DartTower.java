package app.model;

import javafx.scene.image.ImageView;

public class DartTower implements DeffenceTower {
    private int price;
    private double positionX;
    private double positionY;
    private ImageView towerImg;

    public DartTower(double positionX, double positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.towerImg = new ImageView("images/dart_tower.png");
    }

    public ImageView getTowerImg() {
        return towerImg;
    }
}
