package app.utils;

import app.model.GameState;
import javafx.scene.text.Font;

import java.util.List;

public class AppConstans {
    private AppConstans() {}

    public static final int SCREEN_WIDTH = 850;
    public static final int SCREEN_HEIGHT = 600;
    public static GameState gameState = new GameState();
    public static String ballons_path =  "/app/view/assets/images/";
    public static List<String> ballon_img_list = List.of(
            ballons_path + "red_ballon.png",
            ballons_path + "red_ballon.png",
            ballons_path + "blue_ballon.png",
            ballons_path + "green_ballon.png",
            ballons_path + "yellow_ballon.png",
            ballons_path + "white_ballon.png",
            ballons_path + "black_ballon.png"
    );
}
