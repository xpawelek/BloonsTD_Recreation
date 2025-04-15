package app.model;

import app.utils.AppConstans;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public abstract class DeffenceTower {
    protected int price;
    protected double positionX;
    protected double positionY;
    protected ImageView towerImg;
    protected boolean isOnMapPane = false;
    protected boolean sellTower = false;

    public DeffenceTower() {}
    public DeffenceTower(double positionX, double positionY)
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

    public int getPrice(){
        return price;
    }

    public void setSellTower(){
        sellTower = true;
    }

    public void sellingTower()
    {
        AppConstans.gameState.updateMoneyAfterSelling((int)(this.getPrice() * 0.8));
    }

    public boolean getSellTower(){
        return sellTower;
    }

    public abstract Balloon balloonInRange(Balloon balloon);
    public abstract void manageHitting(Balloon balloon, Pane mapPane);
}
