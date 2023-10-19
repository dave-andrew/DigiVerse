package helper;

import javafx.scene.Scene;

public class ThemeManager {

    private static String theme = "light";
    public static final String LIGHT_THEME = "file:resources/light_theme.css";
    public static final String DARK_THEME = "file:resources/dark_theme.css";

    public static void getTheme(Scene scene) {
        scene.getStylesheets().clear();
        if(theme.equals("light")) {
            scene.getStylesheets().add(LIGHT_THEME);
        } else if(theme.equals("dark")) {
            scene.getStylesheets().add(DARK_THEME);
        }
    }

    public static void toggleTheme(Scene scene) {
        scene.getStylesheets().clear();
        if(theme.equals("light")) {
            theme = "dark";
            scene.getStylesheets().add(DARK_THEME);
        } else if(theme.equals("dark")) {
            theme = "light";
            scene.getStylesheets().add(LIGHT_THEME);
        }
    }

}
