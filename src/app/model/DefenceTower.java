package app.model;

import app.utils.AppConstans;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import java.util.ArrayList;
import java.util.List;

public abstract class DefenceTower {
    protected ImageView towerImg;
    protected int priceValue;
    protected int range;
    protected int firstUpgradePrice;
    protected int secondUpgradePrice;
    protected double positionX, positionY;
    protected boolean isOnMapPane = false;
    protected boolean sellTower = false;
    protected boolean firstUpgrade = false;
    protected boolean secondUpgrade = false;
    protected String towerImagePath;
    protected String firstUpgradeImagePath;
    protected String secondUpgradeImagePath;
    protected List<Balloon> balloonsInRange = new ArrayList<>();


    public DefenceTower() {}
    public DefenceTower(double positionX, double positionY)
    {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public ImageView getTowerImg(){
        return towerImg;
    }

    public double getTowerX(){
        return positionX;
    }

    public double getTowerY(){
        return positionY;
    }

    public void setIsOnMapPane(){
        isOnMapPane = true;
    }

    public boolean getIsOnMapPane(){
        return isOnMapPane;
    }

    public int getPriceValue(){
        return priceValue;
    }

    public void setSellTower(){
        sellTower = true;
    }

    public void sellingTower()
    {
        AppConstans.gameState.updateMoneyAfterSelling((int)(this.getPriceValue() * 0.8));
    }

    public boolean getSellTower(){
        return sellTower;
    }

    public int getRange()
    {
        return range;
    }

    public String getTowerImagePath()
    {
        return towerImagePath;
    }

    public String getFirstUpgradeImage() {
        return firstUpgradeImagePath;
    }

    public String getSecondUpgradeImage() {
        return secondUpgradeImagePath;
    }

    public void setPositionX(double positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }

    public int getFirstUpgradePrice()
    {
        return firstUpgradePrice;
    }

    public int getSecondUpgradePrice()
    {
        return secondUpgradePrice;
    }

    public boolean isFirstUpgradeBought()
    {
        return firstUpgrade;
    }

    public void setFirstUpgradeBought()
    {
        firstUpgrade = true;
    }

    public boolean isSecondUpgradeBought()
    {
        return secondUpgrade;
    }

    public void setSecondUpgradeBought()
    {
        secondUpgrade = true;
    }

    public Balloon balloonInRange(Balloon balloon)
    {
        double balloonX = balloon.getBalloonPositionX();
        double balloonY = balloon.getBalloonPositionY();

        double dx = getTowerX() + getTowerImg().getFitWidth() / 2.0 - balloonX;
        double dy = getTowerY() + getTowerImg().getFitHeight() / 2.0 - balloonY;

        if(dx * dx + dy * dy <= range * range)
        {
            if(!balloonsInRange.contains(balloon))
                balloonsInRange.add(balloon);
            return balloon;
        }

        return null;
    }

    public boolean balloonStillInRange(Balloon balloon)
    {
        double balloonX = balloon.getBalloonPositionX();
        double balloonY = balloon.getBalloonPositionY();

        double dx = getTowerX() + getTowerImg().getFitWidth() / 2.0 - balloonX;
        double dy = getTowerY() + getTowerImg().getFitHeight() / 2.0 - balloonY;

        return dx * dx + dy * dy <= (range + range * 0.1) * (range + range * 0.1);
    }

    public void clearAfterWave()
    {
        balloonsInRange.clear();
    }

    public abstract void manageHitting(Balloon balloon, Pane mapPane);
    public abstract void manageFirstUpgrade();
    public abstract void manageSecondUpgrade();
}
