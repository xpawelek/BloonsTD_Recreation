package app.model;

import app.utils.AppConstans;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public abstract class DeffenceTower {
    protected int priceValue;
    protected double positionX;
    protected double positionY;
    protected ImageView towerImg;
    protected boolean isOnMapPane = false;
    protected boolean sellTower = false;
    protected int range;
    protected String towerImagePath;
    protected String firstUpgradeImagePath;
    protected String secondUpgradeImagePath;
    protected int firstUpgradePrice;
    protected int secondUpgradePrice;
    protected boolean firstUpgrade = false;
    protected boolean secondUpgrade = false;
    protected List<Balloon> balloonsInRange = new ArrayList<>();
    private double towerMiddleX;
    private double towerMiddleY;

    public DeffenceTower() {}
    public DeffenceTower(double positionX, double positionY)
    {
        this.positionX = positionX;
        this.positionY = positionY;
        towerMiddleX = getTowerX() + getTowerImg().getFitWidth() / 2.0;
        towerMiddleY = getTowerY() + getTowerImg().getFitHeight() / 2.0;
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

    public void addPriceValue(int price){
        this.priceValue += price;
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

    public void setNewRange(int range)
    {
        this.range = range;
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
        double towerX = getTowerX() + getTowerImg().getFitWidth() / 2.0;
        double towerY = getTowerY() + getTowerImg().getFitHeight() / 2.0;

        double balloonX = balloon.getBalloonPositionX();
        double balloonY = balloon.getBalloonPositionY();

        double dx = towerX - balloonX;
        double dy = towerY - balloonY;

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
        double towerX = getTowerX() + getTowerImg().getFitWidth() / 2.0;
        double towerY = getTowerY() + getTowerImg().getFitHeight() / 2.0;

        double balloonX = balloon.getBalloonPositionX();
        double balloonY = balloon.getBalloonPositionY();

        double dx = towerX - balloonX;
        double dy = towerY - balloonY;

        return dx * dx + dy * dy <= (range + range * 0.1) * (range + range * 0.1);
    }

    public ImageView getImageView()
    {
        return towerImg;
    }

    public abstract void manageHitting(Balloon balloon, Pane mapPane);
    public abstract void manageFirstUpgrade();
    public abstract void manageSecondUpgrade();
}
