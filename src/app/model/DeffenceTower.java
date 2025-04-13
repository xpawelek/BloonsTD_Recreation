package app.model;

import javafx.scene.image.ImageView;

public abstract class DeffenceTower {
    protected int price;
    protected double positionX;
    protected double positionY;
    protected ImageView towerImg;
    protected boolean isOnMapPane = false;

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
}
