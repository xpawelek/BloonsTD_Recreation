package app.model;

import app.controller.GameController;
import app.utils.AppConstans;
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class GameLoop extends AnimationTimer {
    private final GameController gameController;

    public GameLoop(GameController gameController) {
        this.gameController = gameController;
    }

    public void updateGameInfo()
    {
        gameController.updateRoundLabel("Current Wave : " + AppConstans.gameState.getCurrentWave());
        gameController.updateMoneyLabel("Money : " + AppConstans.gameState.getMoney());
        gameController.updateLivesLabel("Lives : " + AppConstans.gameState.getLives());
    }

    @Override
    public void handle(long now)
    {
        if(AppConstans.gameState.getGameContinues()) {
            updateGameInfo();
            if(AppConstans.gameState.getRoundContinues()) {
                //tutaj kolejne fale
                System.out.println("game continues");
                gameController.setStartRoundButtonDisabled();
            }
            if(!AppConstans.gameState.getRoundContinues())
            {
                gameController.setStartRoundButtonEnabled();
            }
        }
        else {
            stop();
        }
    }

}
