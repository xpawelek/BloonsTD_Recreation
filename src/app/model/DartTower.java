package app.model;

import app.utils.AppConstans;
import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import java.util.Comparator;
import java.util.Optional;

public class DartTower extends DefenceTower {
    private double angle = -1;
    private long lastShotTime = 0;
    private long fire_cooldown = 600;
    //private boolean canRotate = true;

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
        this.priceValue = 300;
        this.firstUpgradePrice = 150;
        this.secondUpgradePrice = 100;
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

        Bounds bounds = towerImg.localToScene(towerImg.getBoundsInLocal());
        double centerX = bounds.getMinX() + bounds.getWidth() / 2;
        double centerY = bounds.getMinY() + bounds.getHeight() / 2;

        angle = Math.toDegrees(Math.atan2(getTowerY() - b.getBalloonPositionY(), getTowerX() - b.getBalloonPositionX()));
        towerImg.setRotate(angle + 90);

        Dart dart = new Dart(centerX, centerY, b.getBalloonPositionX(), b.getBalloonPositionY());
        dart.setDartStartingPosition(centerX, centerY);

        mapPane.getChildren().add(dart.getDartImage());
        dart.throwDart(b, mapPane, range);

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
}
