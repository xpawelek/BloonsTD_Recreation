package app.model;

import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class DartTower extends DeffenceTower {
    private List<Dart> manageableDarts;
    private int range = 100;
    private double angle = -1;
    private long lastShotTime = 0;
    private long fire_cooldown = 500;

    public DartTower() {
        price = 350;
    }

    public DartTower(double positionX, double positionY) {
        super(positionX, positionY);
        this.manageableDarts = new ArrayList<>();
        towerImg = new ImageView("/app/view/assets/images/dartDefender.png");
        price = 350;
    }

    @Override
    public Balloon balloonInRange(Balloon balloon)
    {
        double dx = getTowerX() - balloon.getBalloonPositionX();
        double dy = getTowerY() - balloon.getBalloonPositionY();
        if(dx * dx + dy * dy <= range * range)
        {
            //System.out.println("ballon w zasięgu");
            return balloon;
        }
        //mozna dodawac do listy i wyrzucac tego z najwieksza iloscia zyc
        return null;
    }

    @Override
    public void manageHitting(Balloon balloon)
    {
        //jesli w range jest balon, to wybierz balon wedlug preferencji do zestrzelenia, pobierz jego
        //x oraz y, mamy takze position x i y wiezy, teraz dart wyrzuci strzała w celu targetu
        //nowy dart co 50 ms
        long now = System.currentTimeMillis();
        if(now - lastShotTime >= fire_cooldown) {
            Balloon b = balloonInRange(balloon);
            if (b == null) {
                //angle = 0;
                return;
            }

            System.out.println("dart should go");
            angle = Math.toDegrees(Math.atan2(getTowerY() - b.getBalloonPositionY(), getTowerX() - b.getBalloonPositionX()));
            Dart dart = new Dart(super.positionX, super.positionY, balloon.getBalloonPositionX(), balloon.getBalloonPositionY());
            dart.setDartStartingPosition(super.positionX, super.positionY, towerImg.getFitWidth(), towerImg.getFitHeight());
            manageableDarts.add(dart);
            dart.throwDart(balloon);
            lastShotTime = now;
        }
    }

    public double getAngle()
    {
        return angle;
    }

    public List<Dart> getDarts() {
        if (manageableDarts == null)
            manageableDarts = new ArrayList<>();
        return manageableDarts;
    }

    public void removeDart(Dart dart)
    {
        manageableDarts.remove(dart);
    }

    public long getLastShotTime()
    {
        return lastShotTime;
    }

    public long getFireCooldown()
    {
        return fire_cooldown;
    }
}
