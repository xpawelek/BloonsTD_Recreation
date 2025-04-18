package app.model;

import app.utils.AppConstans;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;

public class IceTower extends DefenceTower {

    private long lastShotTime = 0;
    private long fire_cooldown = 5000;
    private List<Balloon> balloonsHit;

    public IceTower() {
        init();
    }

    public IceTower(double positionX, double positionY) {
        super(positionX, positionY);
        init();
    }

    private void init() {
        this.towerImagePath = "/app/view/assets/images/ice_tower.png";
        this.firstUpgradeImagePath = "/app/view/assets/images/ice_tower_first_upgrade.png";
        this.secondUpgradeImagePath = "/app/view/assets/images/ice_tower_second_upgrade.png";
        this.towerImg = new ImageView(towerImagePath);
        this.towerImg.setFitWidth(60);
        this.towerImg.setFitHeight(60);
        this.priceValue = 250;
        this.firstUpgradePrice = 100;
        this.secondUpgradePrice = 100;
        this.range = 120;
        this.balloonsHit = new ArrayList<>();
    }

    @Override
    public void manageHitting(Balloon balloon, Pane mapPane)
    {
        balloonInRange(balloon);
        long now = System.currentTimeMillis();

        if(now - lastShotTime < fire_cooldown)
            return;

        int animationLength = 500;
        
        if(!balloonsInRange.isEmpty()) {
            int colors = 7;
            Circle circle = new Circle();
            circle.setCenterX(getTowerX() + getTowerImg().getFitWidth() / 2.0);
            circle.setCenterY(getTowerY() + getTowerImg().getFitHeight() / 2.0);
            mapPane.getChildren().add(circle);
            circle.setOpacity(0.3);

            PauseTransition delay = new PauseTransition(Duration.millis(animationLength));
            delay.setOnFinished(event -> {
                mapPane.getChildren().remove(circle);
            });
            delay.play();

            Timeline iceAnimation = new Timeline(
                    new KeyFrame(Duration.millis(0),
                            new KeyValue(circle.radiusProperty(), range / colors),
                            new KeyValue(circle.fillProperty(), Color.TRANSPARENT)
                    ),
                    new KeyFrame(Duration.millis((double) animationLength / colors * 2),
                            new KeyValue(circle.radiusProperty(), range / colors * 2),
                            new KeyValue(circle.fillProperty(), Color.TRANSPARENT)
                    ),
                    new KeyFrame(Duration.millis((double) animationLength / colors * 3),
                            new KeyValue(circle.radiusProperty(), range / colors * 3),
                            new KeyValue(circle.fillProperty(), Color.web("#4FC3F7"))
                    ),
                    new KeyFrame(Duration.millis((double) animationLength / colors * 4),
                            new KeyValue(circle.radiusProperty(), range / colors * 4),
                            new KeyValue(circle.fillProperty(), Color.TRANSPARENT)
                    ),
                    new KeyFrame(Duration.millis((double) animationLength / colors * 5),
                            new KeyValue(circle.radiusProperty(), range / colors * 5),
                            new KeyValue(circle.fillProperty(), Color.web("#03A9F4"))
                    ),
                    new KeyFrame(Duration.millis((double) animationLength / colors * 6),
                            new KeyValue(circle.radiusProperty(), range / colors * 6),
                            new KeyValue(circle.fillProperty(), Color.web("#0288D1"))
                    ),
                    new KeyFrame(Duration.millis(animationLength),
                            new KeyValue(circle.radiusProperty(), range),
                            new KeyValue(circle.fillProperty(), Color.web("#01579B"))
                    )
            );

            iceAnimation.play();
        }

        PauseTransition delay = getPauseTransition_HandleSlowingDown((double) animationLength);

        delay.play();
        balloonsInRange.clear();

        lastShotTime = now;
    }

    private PauseTransition getPauseTransition_HandleSlowingDown(double animationLength) {
        PauseTransition delay = new PauseTransition(Duration.millis(animationLength / 2));
        delay.setOnFinished(event -> {
            for(Balloon b : balloonsInRange)
            {
                if(balloonStillInRange(b) && !balloonsHit.contains(b))
                {
                    b.pauseMovingAnimation();
                    balloonsHit.add(b);
                    PauseTransition resumeAfter = new PauseTransition(Duration.millis(1500));
                    resumeAfter.setOnFinished(e -> {
                        b.resumeMovingAnimation();
                    });
                    resumeAfter.play();
                }
            }
        });
        return delay;
    }

    public void manageFirstUpgrade()
    {
        this.priceValue += getFirstUpgradePrice();
        this.setFirstUpgradeBought();
        AppConstans.gameState.updateMoneyAfterBuying(getFirstUpgradePrice());

        this.fire_cooldown = 3800;
    }

    public void manageSecondUpgrade()
    {
        this.priceValue += getSecondUpgradePrice();
        this.setSecondUpgradeBought();
        AppConstans.gameState.updateMoneyAfterBuying(getSecondUpgradePrice());

        this.range = 160;
    }

    @Override
    public void clearAfterWave()
    {
        balloonsInRange.clear();
        balloonsHit.clear();
    }
}
