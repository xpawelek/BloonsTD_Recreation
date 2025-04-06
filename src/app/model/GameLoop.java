package app.model;

import app.controller.GameController;
import app.utils.AppConstans;
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

import java.util.ArrayList;

public class GameLoop extends AnimationTimer {
    private final GameController gameController;
    private ArrayList<Ballon> ballons = new ArrayList<>();

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
                if(ballons.size() < 1) {
                    Ballon balloon = new Ballon(5);
                    gameController.addElementOnGridPane(balloon);
                    ballons.add(balloon);
                    balloon.followPath();

                    PauseTransition pause = new PauseTransition(Duration.seconds(2));
                    pause.setOnFinished(event -> {
                        balloon.updateBallonLives();
                    });
                    pause.play();
                    //update lives, check if 0, if then remove
                }
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
