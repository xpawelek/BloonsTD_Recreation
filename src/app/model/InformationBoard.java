package app.model;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;


public class InformationBoard {
    private String information;
    private final Label currentLabel = new Label();
    private SequentialTransition currentTransition = null;

    public InformationBoard(){};


    public void displayInformation(Pane pane, VBox sideGamePanel) {
        if(pane == null){
            return;
        }
        if(currentTransition != null){
            return;
        }

        pane.getChildren().remove(currentLabel);
        currentLabel.setText(information);
        currentLabel.getStyleClass().add("information-label");
        currentLabel.applyCss();
        currentLabel.layout();

        Platform.runLater(() -> {
            PauseTransition delay = new PauseTransition(Duration.millis(20));
            delay.setOnFinished(event -> {
                double x = (pane.getWidth() - sideGamePanel.getWidth() - currentLabel.getWidth()) / 2;
                double y = (pane.getHeight() - currentLabel.getHeight()) / 2;

                currentLabel.setLayoutX(x);
                currentLabel.setLayoutY(y);
            });
            delay.play();
        });
        pane.getChildren().add(currentLabel);

        currentLabel.setOpacity(0);
        Timeline fadeIn = new Timeline(
                new KeyFrame(Duration.millis(500),
                        new KeyValue(currentLabel.opacityProperty(), 1)
                )
        );

        PauseTransition pause = new PauseTransition(Duration.millis(800));

        Timeline fadeOut = new Timeline(
                new KeyFrame(Duration.millis(500),
                        new KeyValue(currentLabel.opacityProperty(), 0)
                )
        );

        SequentialTransition sequence = new SequentialTransition(fadeIn, pause, fadeOut);
        currentTransition = sequence;
        sequence.setOnFinished(e -> {
            pane.getChildren().remove(currentLabel);
            currentTransition = null;
        });
        sequence.play();
    }

    public void setInformation(String information) {
        this.information = information;
    }
}
