package app.utils;

import app.model.DeffenceTower;
import app.model.GameState;

import java.util.*;

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

    private static List<Map.Entry<Double, Double>> extendKeyPoints(List<Map.Entry<Double, Double>> original) {
        List<Map.Entry<Double, Double>> result = new ArrayList<>();
        int stepSize = 2;

        for (int i = 0; i < original.size() - 1; i++) {
            double x1 = original.get(i).getKey();
            double y1 = original.get(i).getValue();
            double x2 = original.get(i + 1).getKey();
            double y2 = original.get(i + 1).getValue();

            double dx = x2 - x1;
            double dy = y2 - y1;
            double distance = Math.hypot(dx, dy);
            int steps = (int) (distance / stepSize);

            for (int j = 0; j <= steps; j++) {
                double x = Math.round(x1 + j * dx / steps);
                double y = Math.round(y1 + j * dy / steps);
                result.add(new AbstractMap.SimpleEntry<>(x, y));
            }
        }

        result.add(original.getLast());
        return result;
    }

    private static List<Map.Entry<Double, Double>> keyPoints = new ArrayList<>();
    static {
        keyPoints.add(new AbstractMap.SimpleEntry<>(350.0, -50.0));
        keyPoints.add(new AbstractMap.SimpleEntry<>(346.0, 50.0));
        keyPoints.add(new AbstractMap.SimpleEntry<>(337.0, 70.0));
        keyPoints.add(new AbstractMap.SimpleEntry<>(299.0, 82.0));
        keyPoints.add(new AbstractMap.SimpleEntry<>(229.0, 86.0));
        keyPoints.add(new AbstractMap.SimpleEntry<>(220.0, 108.0));
        keyPoints.add(new AbstractMap.SimpleEntry<>(222.0, 168.0));
        keyPoints.add(new AbstractMap.SimpleEntry<>(246.0, 191.0));
        keyPoints.add(new AbstractMap.SimpleEntry<>(428.0, 203.0));
        keyPoints.add(new AbstractMap.SimpleEntry<>(443.0, 218.0));
        keyPoints.add(new AbstractMap.SimpleEntry<>(440.0, 295.0));
        keyPoints.add(new AbstractMap.SimpleEntry<>(414.0,  315.0));
        keyPoints.add(new AbstractMap.SimpleEntry<>(349.0,  328.0));
        keyPoints.add(new AbstractMap.SimpleEntry<>(337.0,  341.0));
        keyPoints.add(new AbstractMap.SimpleEntry<>(326.0,  460.0));
        keyPoints.add(new AbstractMap.SimpleEntry<>(307.0,  480.0));
        keyPoints.add(new AbstractMap.SimpleEntry<>(208.0,  480.0));
        keyPoints.add(new AbstractMap.SimpleEntry<>(191.0,  505.0));
        keyPoints.add(new AbstractMap.SimpleEntry<>(191.0,  650.0));
    }

    public static List<Map.Entry<Double, Double>> roadPoints = extendKeyPoints(keyPoints);

    public static List<DeffenceTower> boughtTowers = new ArrayList<>();
}
