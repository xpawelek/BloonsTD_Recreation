package app.model;

import javafx.scene.image.ImageView;

public class DartTower extends DeffenceTower {

    public DartTower() {
        price = 350;
    }

    public DartTower(double positionX, double positionY) {
        super(positionX, positionY);
        towerImg = new ImageView("/app/view/assets/images/dartDefender.png");
        price = 350;
    }
}
