package app.model;

import app.controller.GameController;
import app.utils.AppConstans;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.*;

import static java.lang.Thread.sleep;


public class GameLoop extends AnimationTimer {
    private final GameController gameController;
    private ArrayList<Balloon> balloons = new ArrayList<>();
    private int baseBalloonPower = 1;
    private List<Integer> currentBalloonsAllowed = new ArrayList<>(List.of(baseBalloonPower));
    private List<Integer> ballonsPowerProbability = new ArrayList<>(Collections.nCopies(AppConstans.ballon_img_list.size() - 1, 0));
    private int currentMaxBalloonsAllowed = 5;
    private boolean turnOnProbability = false;
    private int probabilityIndex = 0;
    private final Random random = new Random();
    private final Timeline timeline = new Timeline();

    public GameLoop(GameController gameController) {
        this.gameController = gameController;
        this.ballonsPowerProbability.set(0, 100);
        System.out.println(AppConstans.ballon_img_list.size() - 3);
    }

    public int drawBalloonIndexBasedOnWeight() {
        int[] availableBalloonsNums = currentBalloonsAllowed.stream()
                .mapToInt(i -> i)
                .toArray();

        int[] weights = ballonsPowerProbability.stream().mapToInt(i -> i)
                .filter(i -> i > 0)
                .toArray();

        int totalWeight = Arrays.stream(weights).sum();
        int random_num = random.nextInt(totalWeight);

        int current = 0;
        for (int i = 0; i < availableBalloonsNums.length; i++) {
            current += weights[i];
            if (random_num < current) {
                return availableBalloonsNums[i];
            }
        }

        throw new RuntimeException("Something went wrong!");
    }

    public void updateGameInfo()
    {
        gameController.updateRoundLabel("Current Wave : " + AppConstans.gameState.getCurrentWave());
        gameController.updateMoneyLabel("Money : " + AppConstans.gameState.getMoney());
        gameController.updateLivesLabel("Lives : " + AppConstans.gameState.getLives());
    }

    private void checkIfJump()
    {
        if(AppConstans.gameState.getCurrentWave() % 6 == 0)
        {
            this.turnOnProbability = true;
            if((this.probabilityIndex < this.ballonsPowerProbability.size() - 1))
            {
                probabilityIndex += 1;
            }

            this.currentMaxBalloonsAllowed -= (int)(Math.random() * 10) + 5;
            if(baseBalloonPower < AppConstans.ballon_img_list.size())
                this.baseBalloonPower += 1;

            this.currentBalloonsAllowed.add(baseBalloonPower);
        }
    }

    private void addRandomAdditionalBallons()
    {
        int rand_num = (int)(Math.random() * 5) + 1;

        //losujemy dodatkowo możliwosc ze pojawia sie od 1 do 3 balonow z kazdego mozliwego lvl
        if(rand_num == (int)(Math.random() * 5) + 1)
        {
            for(int i = 0; i < (int)(Math.random() * 8) + 1; i++)
            {
                //nie puszczamy najmocniejszych i pierwszego (to bedzie img - koniec)
                this.balloons.add(new Balloon((int)(Math.random() * AppConstans.ballon_img_list.size() - 3) + 1));
            }
        }
    }

    private void updateWeights()
    {
        if(turnOnProbability)
        {
            if(this.probabilityIndex <= AppConstans.ballon_img_list.size() - 1 && this.ballonsPowerProbability.get(this.probabilityIndex) < 50)
            {
                int currentProbabilityOfIndex = this.ballonsPowerProbability.get(this.probabilityIndex);
                this.ballonsPowerProbability.set(probabilityIndex, currentProbabilityOfIndex + 10);

                for(int i = 0; i < this.probabilityIndex; i++)
                {
                    this.ballonsPowerProbability.set(i, (100 - this.ballonsPowerProbability.get(probabilityIndex)) / probabilityIndex);
                }
            }
        }
    }

    public void incomingWave()
    {
        AppConstans.gameState.changeWaveStarted();
        this.currentMaxBalloonsAllowed += (int)(Math.random() * 5) + 1;

        checkIfJump();
        addRandomAdditionalBallons();
        updateWeights();


        for(int i = 0; i < this.currentMaxBalloonsAllowed; i++)
        {
            this.balloons.add(new Balloon(drawBalloonIndexBasedOnWeight()));
        }

        int delay = 0;
        for(Balloon balloon : balloons)
        {
            int randomDelay = 50 + random.nextInt(1000);
            delay += randomDelay;
            KeyFrame keyFrame = new KeyFrame(Duration.millis(delay), event -> {
                balloon.followPath();
                gameController.addBalloonToMapPane(balloon);
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
        int allowableRange = 30;
        if(Math.abs(gameController.getPositionY(balloon)) > AppConstans.SCREEN_HEIGHT + allowableRange)
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
        for(Balloon balloon : balloons)
        {
            gameController.removeBalloonFromMapPane(balloon);
        }

        AppConstans.gameState.setRoundContinues(false);
        this.balloons.clear();
        this.timeline.getKeyFrames().clear(); // <- musi byc clearoweane
    }

    public void clearBalloon(Balloon balloon)
    {
        gameController.removeBalloonFromMapPane(balloon);
        AppConstans.gameState.loseLife(balloon.getBallonLives());
        updateGameInfo();
    }

    public boolean isAnyLifeLeft()
    {
        return AppConstans.gameState.getLives() > 0;
    }

    void addTower()
    {
        //System.out.println("sprawdzam");
        for(DeffenceTower tower : AppConstans.boughtTowers)
        {
            if(!tower.getIsOnMapPane())
            {
                tower.setIsOnMapPane();
                gameController.addTowerToMapPane(tower);
            }
        }
    }


    @Override
    public void handle(long now)
    {
        if(AppConstans.gameState.getGameContinues()) {
            updateGameInfo();
            addTower();
            if(AppConstans.gameState.getRoundContinues()) {
                if(!isAnyLifeLeft())
                {
                    //komunnikat + czszyczenie + freeze game
                    AppConstans.gameState.changeWaveStarted(false);
                    AppConstans.gameState.setRoundContinues(false);
                    AppConstans.gameState.setGameContinues();
                }
                //tutaj kolejne fale
                //System.out.println("game continues");
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
            clearBalloonsAfterWave();
            AppConstans.boughtTowers.clear();
            stop();
        }
    }

}
