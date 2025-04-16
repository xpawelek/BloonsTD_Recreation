package app.model;

import app.utils.AppConstans;
import javafx.animation.PathTransition;
import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

import java.util.*;

public class TackTower extends DeffenceTower {
    private long lastShotTime = 0;
    private long fire_cooldown = 3000;
    private String towerImageGifPath;

    public TackTower() {
        init();
    }

    public TackTower(double positionX, double positionY) {
        super(positionX, positionY);
        init();
    }

    private void init() {
        towerImagePath = "/app/view/assets/images/tack_tower.png";
        towerImageGifPath = "/app/view/assets/images/tack_tower_animation.gif";
        towerImg = new ImageView(towerImagePath);
        towerImg.setFitWidth(65);
        towerImg.setFitHeight(65);
        firstUpgradeImagePath = "/app/view/assets/images/tack_tower_first_upgrade.png";
        secondUpgradeImagePath = "/app/view/assets/images/tack_tower_second_upgrade.png";
        priceValue = 300;
        firstUpgradePrice = 100;
        secondUpgradePrice = 150;
        range = 80;
    }

    @Override
    public void manageHitting(Balloon balloon, Pane mapPane)
    {
        balloonInRange(balloon);
        System.out.println(balloonsInRange.size());
        long now = System.currentTimeMillis();

        if(now - lastShotTime < fire_cooldown)
            return;

        if(!balloonsInRange.isEmpty()) {
            towerImg.setImage(new Image(towerImageGifPath));

            PauseTransition pause = new PauseTransition(Duration.millis(200));
            pause.setOnFinished(e -> {
                towerImg.setImage(new Image(towerImagePath));
            });
            pause.play();
        }

        for(Balloon b : balloonsInRange)
        {
            if(balloonStillInRange(b))
                b.updateBalloonLives();
           // System.out.println(b.getBalloonLives());
        }

        balloonsInRange.clear();

        lastShotTime = now;
    }

    public void manageFirstUpgrade()
    {
        this.priceValue += getFirstUpgradePrice();
        this.setFirstUpgradeBought();
        AppConstans.gameState.updateMoneyAfterBuying(getFirstUpgradePrice());

        this.fire_cooldown = 2000;
    }

    public void manageSecondUpgrade()
    {
        this.priceValue += getSecondUpgradePrice();
        this.setSecondUpgradeBought();
        AppConstans.gameState.updateMoneyAfterBuying(getSecondUpgradePrice());

        this.range = 110;
    }
}
