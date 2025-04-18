package app.model;

import app.controller.GameController;
import app.utils.AppConstans;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.io.IOException;
import java.util.*;


public class GameLoop extends AnimationTimer {
    private final GameController gameController;
    private int baseBalloonPower = 1;
    private int probabilityIndex = 0;
    private int currentMaxBalloonsAllowed = 5;
    private List<Integer> currentBalloonsAllowed = new ArrayList<>(List.of(baseBalloonPower));
    private List<Integer> ballonsPowerProbability = new ArrayList<>(Collections.nCopies(AppConstans.ballon_img_list.size() - 1, 0));;
    private boolean turnOnProbability = false;
    private ArrayList<Balloon> balloons = new ArrayList<>();
    private final Random random = new Random();
    private final Timeline timelineBalloons = new Timeline();
    

    public GameLoop(GameController gameController) {
        this.gameController = gameController;
        this.ballonsPowerProbability.set(0, 100);
    }
    
    //game methods
    public void updateGameInfo()
    {
        gameController.updateRoundLabel("Current Wave : " + AppConstans.gameState.getCurrentWave());
        gameController.updateMoneyLabel("Money : " + AppConstans.gameState.getMoney());
        gameController.updateLivesLabel("Lives : " + AppConstans.gameState.getLives());
    }

    public void incomingWave()
    {
        AppConstans.gameState.changeWaveStarted();
        this.currentMaxBalloonsAllowed += (int)(Math.random() * 5) + 1;

        checkIfJump();
        addRandomAdditionalBalloons();
        updateWeights();


        for(int i = 0; i < this.currentMaxBalloonsAllowed; i++)
        {
            int index = drawBalloonIndexBasedOnWeight();
            this.balloons.add(new Balloon(index));
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
            timelineBalloons.getKeyFrames().add(keyFrame);
        }
        timelineBalloons.play();
    }

    public boolean areAllBalloonsDestroyed()
    {
        return this.balloons.isEmpty();
    }

    public void clearTowersFromMap()
    {
        for(DefenceTower tower : AppConstans.boughtTowers)
        {
            tower.clearAfterWave();
        }
    }

    public void clearBalloonsAfterWave()
    {
        for(Balloon balloon : balloons)
        {
            gameController.removeBalloonFromMapPane(balloon);
        }
        
        AppConstans.gameState.setRoundContinues(false);
        this.balloons.clear();
        this.timelineBalloons.getKeyFrames().clear();
    }

    public void clearBalloon(Balloon balloon)
    {
        gameController.removeBalloonFromMapPane(balloon);
        updateGameInfo();
    }

    public void clearAfterGame()
    {
        clearBalloonsAfterWave();
        clearTowersFromMap();
    }
    
    public boolean isAnyLifeLeft()
    {
        return AppConstans.gameState.getLives() > 0;
    }

    public void restartGameAfterLoss() throws IOException {
        if (!isAnyLifeLeft()) {
            gameController.restartGameButton.setDisable(true);

            AppConstans.informationBoard.setInformation("You lost, restarting in 3 seconds...");
            AppConstans.informationBoard.displayInformation(gameController.mapPane, gameController.sideGamePanel);

            PauseTransition delay = new PauseTransition(Duration.seconds(3));
            delay.setOnFinished(event -> {
                try {
                    gameController.restartGame();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                gameController.restartGameButton.setDisable(false);
            });
            delay.play();
        }
    }

    //balloons handling methods

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

    private void addRandomAdditionalBalloons()
    {
        int rand_num = (int)(Math.random() * 25) + 1;

        if(rand_num == (int)(Math.random() * 25) + 1)
        {
            for(int i = 0; i < (int)(Math.random() * 5) + 1; i++)
            {
                int randBalloonIndex = (int)(Math.random() * (AppConstans.ballon_img_list.size() - 3)) + 1;
                this.balloons.add(new Balloon(randBalloonIndex));
            }
        }
    }

    public void balloonsGoesThroughWave()
    {
        Iterator<Balloon> iterator = balloons.iterator();
        while(iterator.hasNext())
        {
            Balloon balloon = iterator.next();
            if(checkIfBalloonReachedFinish(balloon))
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
            modifyBalloonsPosition(balloon);
            checkIfBalloonInRangeOfTower(balloon);
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
        balloon.setBalloonPositionX(gameController.getBalloonPositionX(balloon));
        balloon.setBalloonPositionY(gameController.getBalloonPositionY(balloon));
        // Circle debugPoint = new Circle(balloon.getBalloonPositionX(), balloon.getBalloonPositionY(), 1); // mała kropka
        // debugPoint.setFill(Color.RED); // np. czerwony środek wieżyczki
        // gameController.mapPane.getChildren().add(debugPoint);
    }

    public boolean checkIfBalloonReachedFinish(Balloon balloon)
    {
        int allowableRange = 30;
        return Math.abs(balloon.getBalloonPositionY()) > AppConstans.SCREEN_HEIGHT + allowableRange;
    }
    
    //towers handling methods

    public void checkIfBalloonInRangeOfTower(Balloon balloon)
    {
        for(DefenceTower tower : AppConstans.boughtTowers)
        {
            tower.manageHitting(balloon, gameController.getMapPane());
        }
    }

    void addTowerToMap()
    {
        for(DefenceTower tower : AppConstans.boughtTowers)
        {
            if(!tower.getIsOnMapPane())
            {
                tower.setIsOnMapPane();
                gameController.addTowerToMapPane(tower);
            }
        }
    }

    void manageCurrentChosenTowers(){
        if(AppConstans.boughtTowers.contains(AppConstans.currentClickedDeffenceTower)){
            if(AppConstans.currentClickedDeffenceTower.getSellTower()) {
                gameController.removeTowerFromMapPane(AppConstans.currentClickedDeffenceTower);
                AppConstans.boughtTowers.remove(AppConstans.currentClickedDeffenceTower);
                AppConstans.currentClickedDeffenceTower.setSellTower();
                AppConstans.currentClickedDeffenceTower.sellingTower();
                AppConstans.currentClickedDeffenceTower = null;
            }
        }
    }

    void checkPossibilityOfBuyingUpgrades()
    {
        if(AppConstans.boughtTowers.isEmpty() || AppConstans.currentClickedDeffenceTower == null) {
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
            addTowerToMap();
            manageCurrentChosenTowers();
            checkPossibilityOfBuyingUpgrades();

            if(AppConstans.gameState.getRoundContinues()) {
                if(!isAnyLifeLeft())
                {
                    AppConstans.gameState.changeWaveStarted(false);
                    AppConstans.gameState.setRoundContinues(false);
                    AppConstans.gameState.setGameContinues();
                }

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
            clearAfterGame();
            try {
                restartGameAfterLoss();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            AppConstans.boughtTowers.clear();
            stop();
        }
    }

}
