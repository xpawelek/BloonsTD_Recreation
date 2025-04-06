package app.core;

import app.controller.GameController;
import app.model.GameLoop;
import app.utils.AppConstans;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private static Stage primaryStage;;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        switchScene("/app/view/fxml/menu.fxml");
    }

    public static void switchScene(String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlFile));
        Parent root = loader.load();

        Scene scene = new Scene(root, AppConstans.SCREEN_WIDTH, AppConstans.SCREEN_HEIGHT);
        scene.getStylesheets().add(Main.class.getResource("/app/view/assets/css/game-style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
