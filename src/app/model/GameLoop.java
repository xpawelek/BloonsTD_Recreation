package app.model;

import app.controller.GameController;
import app.utils.AppConstans;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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
    private final Timeline timelineBalloons = new Timeline();

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
                int rand_balloon_index = (int)(Math.random() * (AppConstans.ballon_img_list.size() - 3)) + 1;
                this.balloons.add(new Balloon());
                //System.out.println("Wylosowano maplke : " + rand_balloon_index);
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
            int index = drawBalloonIndexBasedOnWeight();
            this.balloons.add(new Balloon(index));
            System.out.println("Wylosowano index: " + index);
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
            timelineBalloons.getKeyFrames().add(keyFrame);
           // timeline.getKeyFrames().add(keyFrame2);
        }
        timelineBalloons.play();
        System.out.println(balloons.size());
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
                AppConstans.gameState.loseLife(balloon.getBalloonLives());
                iterator.remove();
            }
            if(balloonHasNoMoreLives(balloon))
            {
                clearBalloon(balloon);
                AppConstans.gameState.addCoin();
                iterator.remove();
            }
            //check if balloon got hit
            modifyBalloonsPosition(balloon);
            checkIfBalloonInRangeOfTower(balloon);
            //System.out.println("goes throught wave");
        }

        if(areAllBalloonsDestroyed())
        {
            clearBalloonsAfterWave();
            if(isAnyLifeLeft())
            {
                AppConstans.gameState.updateCurrentWave();
                updateGameInfo();
            }
        }
    }

    private boolean balloonHasNoMoreLives(Balloon balloon) {
        return balloon.getBalloonLives() <= 0;
    }

    public void modifyBalloonsPosition(Balloon balloon)
    {
        balloon.setBalloonPositionX(gameController.getPositionX(balloon));
        balloon.setBalloonPositionY(gameController.getPositionY(balloon));

        Circle debugPoint = new Circle(balloon.getBalloonPositionX(), balloon.getBalloonPositionY(), 1); // mała kropka
        debugPoint.setFill(Color.RED); // np. czerwony środek wieżyczki
        gameController.mapPane.getChildren().add(debugPoint);
    }

    public boolean checkIfBallonReachedFinish(Balloon balloon)
    {
        int allowableRange = 30;
        return Math.abs(balloon.getBalloonPositionY()) > AppConstans.SCREEN_HEIGHT + allowableRange;
    }

    public void checkIfBalloonInRangeOfTower(Balloon balloon)
    {
        for(DeffenceTower tower : AppConstans.boughtTowers)
        {

            tower.manageHitting(balloon, gameController.getMapPane());
            Circle debugPoint = new Circle(tower.getTowerX(), tower.getTowerY(), 1); // mała kropka
            debugPoint.setFill(Color.RED); // np. czerwony środek wieżyczki
            gameController.mapPane.getChildren().add(debugPoint);
            //updateTowerAngle();
        }
    }

    public void updateTowerAngle()
    {
        for(DeffenceTower tower : AppConstans.boughtTowers) {
            if (tower instanceof DartTower)
            {
                if(((DartTower) tower).getAngle() != -1)
                    gameController.upadateTowerAngle(tower);
            }
        }
    }

    public boolean areAllBalloonsDestroyed()
    {
        return this.balloons.isEmpty();
    }

    public void clearBalloonsAfterWave()
    {
        for(Balloon balloon : balloons)
        {
            gameController.removeBalloonFromMapPane(balloon);
        }
        System.out.println(this.balloons.size());
        AppConstans.gameState.setRoundContinues(false);
        this.balloons.clear();
        this.timelineBalloons.getKeyFrames().clear(); // <- musi byc clearoweane
    }

    public void clearBalloon(Balloon balloon)
    {
        gameController.removeBalloonFromMapPane(balloon);
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

    void manageTower(){
        if(AppConstans.boughtTowers.contains(AppConstans.currentClickedDeffenceTower)){
            //System.out.println("contains");
            if(AppConstans.currentClickedDeffenceTower.getSellTower()) {
                gameController.removeTowerFromMapPane(AppConstans.currentClickedDeffenceTower);
                AppConstans.boughtTowers.remove(AppConstans.currentClickedDeffenceTower);
                AppConstans.currentClickedDeffenceTower.setSellTower();
                AppConstans.currentClickedDeffenceTower.sellingTower();
                AppConstans.currentClickedDeffenceTower = null;
                System.out.println("removing, left: " + AppConstans.boughtTowers.size());
            }
        }
    }

    void checkPossibilityOfBuyingUpgrades()
    {
        if(AppConstans.boughtTowers.size() == 0 || AppConstans.currentClickedDeffenceTower == null) {
            return;
        }

        if(AppConstans.currentClickedDeffenceTower.isFirstUpgradeBought()
                || AppConstans.gameState.getMoney() < AppConstans.currentClickedDeffenceTower.getFirstUpgradePrice())
        {
            gameController.firstUpgrade.setDisable(true);
        }
        else
        {
            gameController.firstUpgrade.setDisable(false);
        }

        if(AppConstans.currentClickedDeffenceTower.isSecondUpgradeBought()
                || AppConstans.gameState.getMoney() < AppConstans.currentClickedDeffenceTower.getSecondUpgradePrice())
        {
            gameController.secondUpgrade.setDisable(true);
        }
        else
        {
            gameController.secondUpgrade.setDisable(false);
        }
    }


    @Override
    public void handle(long now)
    {
        if(AppConstans.gameState.getGameContinues()) {
            updateGameInfo();
            addTower();
            manageTower();
            checkPossibilityOfBuyingUpgrades();
            if(AppConstans.gameState.getRoundContinues()) {
                //System.out.println("round goes");
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
