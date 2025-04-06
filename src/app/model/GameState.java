package app.model;

import app.utils.AppConstans;

public class GameState {
    private int lives;
    private int money;
    private int currentWave;
    private boolean gameContinues = false;
    private boolean roundContinues = false;

    public GameState()
    {
        lives = 40;
        money = 650;
        currentWave = 1;
    }

    public int getCurrentWave(){
        return currentWave;
    }

    public int getLives()
    {
        return lives;
    }

    public int getMoney()
    {
        return money;
    }

    public boolean getGameContinues()
    {
        return gameContinues;
    }

    public void setGameContinues()
    {
        gameContinues = !gameContinues;
    }

    public boolean getRoundContinues()
    {
        return roundContinues;
    }

    public void setRoundContinues()
    {
        roundContinues = !roundContinues;
    }

    public void setRoundContinues(boolean roundContinues)
    {
        this.roundContinues = roundContinues;
    }

    public void loseLife()
    {
        lives--;
    }

    public void addCoin()
    {
        money++;
    }

    public void restartGame(){
        lives = 40;
        money = 650;
        currentWave = 1;
        gameContinues = false;
        roundContinues = false;
    }
}
