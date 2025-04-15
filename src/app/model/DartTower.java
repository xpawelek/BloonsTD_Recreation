package app.model;

import javafx.animation.PauseTransition;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class DartTower extends DeffenceTower {
    private int range = 100;
    private double angle = -1;
    private long lastShotTime = 0;
    private long fire_cooldown = 600;
    private List<Balloon> balloonsInRange = new ArrayList<>();

    public DartTower() {
        price = 300;
    }

    public DartTower(double positionX, double positionY) {
        super(positionX, positionY);
        towerImg = new ImageView("/app/view/assets/images/dartDefender.png");
        price = 300;
    }

    @Override
    public Balloon balloonInRange(Balloon balloon)
    {
        double dx = getTowerX() - balloon.getBalloonPositionX();
        double dy = getTowerY() - balloon.getBalloonPositionY();
        if(dx * dx + dy * dy <= range * range)
        {
            balloonsInRange.add(balloon);
            return balloon;
        }
        return null;
    }

    @Override
    public void manageHitting(Balloon balloon, Pane mapPane)
    {
        long now = System.currentTimeMillis();

        if(now - lastShotTime < fire_cooldown)
            return;

        Balloon b = balloonInRange(balloon);
        if (b == null) {
            //angle = 0;
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
        dart.setDartStartingPosition(super.positionX, super.positionY, towerImg.getFitWidth(), towerImg.getFitHeight());
        towerImg.setRotate(angle + 90);

        if (!mapPane.getChildren().contains(dart.getDartImage())) {
            mapPane.getChildren().add(dart.getDartImage());
        }

        PauseTransition pause = new PauseTransition(Duration.millis(5));
        Balloon finalB = b;

        pause.setOnFinished(event -> {
            dart.setRotate(angle + 90);
            dart.throwDart(finalB, mapPane);
        });
        pause.play();

        lastShotTime = now;
    }

    public double getAngle()
    {
        return angle;
    }

    //to do - piercing more + range
}
