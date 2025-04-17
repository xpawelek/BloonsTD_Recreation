package app.model;

import app.utils.AppConstans;
import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class TackTower extends DefenceTower {
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
        this.towerImagePath = "/app/view/assets/images/tack_tower.png";
        this.towerImageGifPath = "/app/view/assets/images/tack_tower_animation.gif";
        this.firstUpgradeImagePath = "/app/view/assets/images/tack_tower_first_upgrade.png";
        this.secondUpgradeImagePath = "/app/view/assets/images/tack_tower_second_upgrade.png";
        this.towerImg = new ImageView(towerImagePath);
        this.towerImg.setFitWidth(65);
        this.towerImg.setFitHeight(65);
        this.priceValue = 350;
        this.firstUpgradePrice = 100;
        this.secondUpgradePrice = 150;
        this.range = 80;
    }

    @Override
    public void manageHitting(Balloon balloon, Pane mapPane)
    {
        balloonInRange(balloon);
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
