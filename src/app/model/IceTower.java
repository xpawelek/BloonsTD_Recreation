package app.model;

import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class IceTower extends DeffenceTower {

    private double angle = -1;
    private long lastShotTime = 0;
    private long fire_cooldown = 600;
    private List<Balloon> balloonsInRange = new ArrayList<>();

    public IceTower() {
        init();
    }

    public IceTower(double positionX, double positionY) {
        super(positionX, positionY);
        init();
    }

    private void init() {
        towerImagePath = "/app/view/assets/images/ice_tower.png";
        towerImg = new ImageView(towerImagePath);
        firstUpgradeImagePath = "/app/view/assets/images/ice_tower_first_upgrade.png";
        secondUpgradeImagePath = "/app/view/assets/images/ice_tower_second_upgrade.png";
        priceValue = 300;
        firstUpgradePrice = 100;
        secondUpgradePrice = 150;
        range = 100;
    }

    @Override
    public Balloon balloonInRange(Balloon balloon) {
        System.out.println(balloon);
        return balloon;
    }

    @Override
    public void manageHitting(Balloon balloon, Pane mapPane) {
        System.out.println(balloon);
    }

    @Override
    public void manageFirstUpgrade() {
        System.out.println("x");
    }

    @Override
    public void manageSecondUpgrade() {
        System.out.println("x");
    }
}
