package app.utils;

import app.model.DeffenceTower;
import app.model.GameState;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AppConstans {
    private AppConstans() {}

    public static final int SCREEN_WIDTH = 850;
    public static final int SCREEN_HEIGHT = 600;
    public static GameState gameState = new GameState();
    public static String ballons_path =  "/app/view/assets/images/";
    public static Map<String, Integer> ballon_img_list = new LinkedHashMap<>();

    static {
        ballon_img_list.put(ballons_path + "dead_balloon.png", 0);
        ballon_img_list.put(ballons_path + "red_balloon.png", 12);
        ballon_img_list.put(ballons_path + "blue_balloon.png", 11);
        ballon_img_list.put(ballons_path + "green_balloon.png", 10);
        ballon_img_list.put(ballons_path + "yellow_balloon.png", 8);
        ballon_img_list.put(ballons_path + "black_balloon.png", 7);
        ballon_img_list.put(ballons_path + "white_balloon.png", 7);
    }

    public static List<DeffenceTower> boughtTowers = new ArrayList<>();
}
