package app.model;

import javafx.animation.*;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;


public class InformationBoard {
    private String information;
    private final Label label = new Label();

    public InformationBoard(){};


    public void displayInformation(Pane pane) {
        if(pane == null){
            return;
        }

        pane.getChildren().remove(label);
        label.setText(information);
        label.getStyleClass().add("information-label");
        label.applyCss();
        label.layout();

        pane.getChildren().add(label);

        label.setLayoutX(pane.getWidth() / 2 - label.getBoundsInLocal().getWidth() / 2);
        label.setLayoutY(pane.getHeight() / 2 - label.getBoundsInLocal().getHeight() / 2);

        label.setOpacity(0);
        Timeline fadeIn = new Timeline(
                new KeyFrame(Duration.millis(500),
                        new KeyValue(label.opacityProperty(), 1)
                )
        );

        PauseTransition pause = new PauseTransition(Duration.millis(1200));

        Timeline fadeOut = new Timeline(
                new KeyFrame(Duration.millis(500),
                        new KeyValue(label.opacityProperty(), 0)
                )
        );

        SequentialTransition sequence = new SequentialTransition(fadeIn, pause, fadeOut);
        sequence.setOnFinished(e -> pane.getChildren().remove(label));
        sequence.play();
    }

    public void setInformation(String information) {
        this.information = information;
    }
}
