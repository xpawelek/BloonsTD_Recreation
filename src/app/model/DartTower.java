package app.model;

import app.utils.AppConstans;
import javafx.animation.PauseTransition;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import java.util.Comparator;
import java.util.Optional;

public class DartTower extends DefenceTower {
    private double angle = -1;
    private long lastShotTime = 0;
    private long fire_cooldown = 600;

    public DartTower() {
        init();
    }

    public DartTower(double positionX, double positionY) {
        super(positionX, positionY);
        init();
    }

    private void init() {
        this.towerImagePath = "/app/view/assets/images/dartDefender_tower.png";
        this.firstUpgradeImagePath = "/app/view/assets/images/dart_tower_first_upgrade.png";
        this.secondUpgradeImagePath = "/app/view/assets/images/dart_tower_second_upgrade.png";
        this.towerImg = new ImageView(towerImagePath);
        this.priceValue = 250;
        this.firstUpgradePrice = 100;
        this.secondUpgradePrice = 150;
        this.range = 100;
    }

    @Override
    public void manageHitting(Balloon balloon, Pane mapPane)
    {
        long now = System.currentTimeMillis();

        if(now - lastShotTime < fire_cooldown)
            return;

        Balloon b = balloonInRange(balloon);
        if (b == null) {
            return;
        }

        Optional<Balloon> strongest = balloonsInRange.stream()
                .max(Comparator.comparing(Balloon::getBalloonLives));

        if(strongest.isPresent())
        {
            b = strongest.get();
        }

        angle = Math.toDegrees(Math.atan2(getTowerY() - b.getBalloonPositionY(), getTowerX() - b.getBalloonPositionX()));

        Dart dart = new Dart(super.positionX, super.positionY, balloon.getBalloonPositionX(), balloon.getBalloonPositionY());
        if (!mapPane.getChildren().contains(dart.getDartImage())) {
            mapPane.getChildren().add(dart.getDartImage());
        }

        PauseTransition pause = new PauseTransition(Duration.millis(5));
        Balloon finalB = b;

        pause.setOnFinished(event -> {
            dart.setDartStartingPosition(super.positionX, super.positionY, towerImg.getFitWidth(), towerImg.getFitHeight());
            towerImg.setRotate(angle + 90);
            dart.setRotate(angle + 90);
            dart.throwDart(finalB, mapPane);
        });
        pause.play();

        lastShotTime = now;
    }

    public void manageFirstUpgrade()
    {
        this.priceValue += getFirstUpgradePrice();
        this.setFirstUpgradeBought();
        AppConstans.gameState.updateMoneyAfterBuying(getFirstUpgradePrice());

        this.fire_cooldown *= 0.8;
        //todo: pierce more balloons
    }

    public void manageSecondUpgrade()
    {
        this.priceValue += getSecondUpgradePrice();
        this.setSecondUpgradeBought();
        AppConstans.gameState.updateMoneyAfterBuying(getSecondUpgradePrice());

        this.range = 130;
    }

    public double getAngle()
    {
        return angle;
    }
}
