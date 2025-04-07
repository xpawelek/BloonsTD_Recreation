package app.model;

import app.controller.GameController;
import app.utils.AppConstans;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static java.lang.Thread.sleep;


public class GameLoop extends AnimationTimer {
    private final GameController gameController;
    private ArrayList<Balloon> balloons = new ArrayList<>();
    private int baseBallonPower = 1;
    private List<Integer> currentBallonsAllowed = List.of(baseBallonPower);
    private int currentMaxBallonsAllowed = 5;
    private Random random = new Random();
    private Timeline timeline = new Timeline();
    private int allowableRange = 30;

    public GameLoop(GameController gameController) {
        this.gameController = gameController;
    }

    public void updateGameInfo()
    {
        gameController.updateRoundLabel("Current Wave : " + AppConstans.gameState.getCurrentWave());
        gameController.updateMoneyLabel("Money : " + AppConstans.gameState.getMoney());
        gameController.updateLivesLabel("Lives : " + AppConstans.gameState.getLives());
    }

    public void incomingWave()
    {
        AppConstans.gameState.changeWaveStarted();
        this.currentMaxBallonsAllowed += (int)(Math.random() * 10) + 1;

        if(AppConstans.gameState.getCurrentWave() % 6 == 0)
        {
            this.currentMaxBallonsAllowed -= (int)(Math.random() * 10) + 5;
            if(baseBallonPower < AppConstans.ballon_img_list.size())
                this.baseBallonPower += 1;
            this.currentBallonsAllowed.add(baseBallonPower);
        }

        for(int i = 0; i < this.currentMaxBallonsAllowed; i++)
        {
            this.balloons.add(new Balloon(baseBallonPower));
        }

        int delay = 0;
        for(Balloon balloon : balloons)
        {
            int randomDelay = 200 + random.nextInt(800);
            delay += randomDelay;
            KeyFrame keyFrame = new KeyFrame(Duration.millis(delay), event -> {
                balloon.followPath();
                gameController.addElementOnGridPane(balloon);
            });
            /*
            KeyFrame keyFrame2 = new KeyFrame(Duration.millis(delay + 8000), event -> {
                clearBalloonsAfterWave();
            });
             */
            timeline.getKeyFrames().add(keyFrame);
           // timeline.getKeyFrames().add(keyFrame2);
        }
        timeline.play();
        //po tym wuzej kod poniezej od razu sie wykonuje

        //jesli sie skonczy runda tzn wszystkie dotra do konca, albo wszystkie zostana zbite, to
        //robimy clearAllAfterWave

        //za kazdym razem zwieksza sie ilosc balonow
        //co 6 leveli dodajemy mozliowsc dodania kolejnego levelu do losowania
        //co 6 leveli losujemy dodatkowo randomowo : wiecej leveli, albo wiecej mocniejszych balonow
        // albo wyjatkowo na jedna runde dajemy mozliwosc dolosowania niewielu sposrod niedodanych leveli
    }

    public void balloonsGoesThroughWave()
    {
        //tutaj implementujemy zmniejszanie zycia, zbicie balonu etc
        Iterator<Balloon> iterator = balloons.iterator();
        while(iterator.hasNext())
        {
            Balloon balloon = iterator.next();
            if(checkIfBallonReachedFinish(balloon))
            {
                clearBalloon(balloon);
                iterator.remove();
            }
        }

        if(areAllBallonsDestroyed())
        {
            clearBalloonsAfterWave();
            if(isAnyLifeLeft())
            {
                AppConstans.gameState.updateCurrentWave();
                updateGameInfo();
            }
        }


    }

    public boolean checkIfBallonReachedFinish(Balloon balloon)
    {
        if(Math.abs(gameController.getPositionY(balloon)) > AppConstans.SCREEN_HEIGHT + this.allowableRange)
        {
            return true;
        }

        return false;
    }

    public boolean areAllBallonsDestroyed()
    {
        return this.balloons.isEmpty();
    }

    public void clearBalloonsAfterWave()
    {
        System.out.println("removing all balloons");
        for(Balloon balloon : balloons)
        {
            gameController.removeElementFromGridPane(balloon);
        }

        System.out.println("executed clear balloons after wave");
        AppConstans.gameState.setRoundContinues(false);
        this.balloons.clear();
    }

    public void clearBalloon(Balloon balloon)
    {
        gameController.removeElementFromGridPane(balloon);
        AppConstans.gameState.loseLife(balloon.getBallonLives());
        updateGameInfo();
    }

    public boolean isAnyLifeLeft()
    {
        return AppConstans.gameState.getLives() > 0;
    }

    @Override
    public void handle(long now)
    {
        if(AppConstans.gameState.getGameContinues()) {
            updateGameInfo();
            if(AppConstans.gameState.getRoundContinues()) {
                if(!isAnyLifeLeft())
                {
                    //komunnikat + czszyczenie + freeze game
                    AppConstans.gameState.changeWaveStarted(false);
                    AppConstans.gameState.setRoundContinues(false);
                    AppConstans.gameState.setGameContinues();
                }
                //tutaj kolejne fale
                System.out.println("game continues");
                gameController.setStartRoundButtonDisabled();
                if(AppConstans.gameState.getWaveStarted())
                {
                    incomingWave();
                }
                balloonsGoesThroughWave();
            }
            if(!AppConstans.gameState.getRoundContinues())
            {
                gameController.setStartRoundButtonEnabled();
            }
        }
        else {
            //clear game loop elepments in class
            stop();
        }
    }

}
