package helper;

import javafx.animation.FadeTransition;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class ThemeManager {

    private static String theme = "light";
    public static final String LIGHT_THEME = "file:resources/light_theme.css";
    public static final String DARK_THEME = "file:resources/dark_theme.css";

    public static void getTheme(Scene scene) {
        scene.getStylesheets().clear();
        if (theme.equals("light")) {
            scene.getStylesheets().add(LIGHT_THEME);
        } else if (theme.equals("dark")) {
            scene.getStylesheets().add(DARK_THEME);
        }
    }

    private ImageView moon, sun;

    public static void toggleTheme(Scene scene, ToggleButton toggleButton) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(250), scene.getRoot());
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(event -> {
            scene.getStylesheets().clear();
            if (theme.equals("light")) {
                theme = "dark";

                ImageView moon = new ImageView(new Image("file:resources/icons/moon.png"));
                toggleButton.setGraphic(moon);

                moon.setFitWidth(30);
                moon.setFitHeight(30);

                scene.getStylesheets().add(DARK_THEME);
            } else if (theme.equals("dark")) {
                theme = "light";

                ImageView sun = new ImageView(new Image("file:resources/icons/sun.png"));
                toggleButton.setGraphic(sun);

                sun.setFitWidth(30);
                sun.setFitHeight(30);

                scene.getStylesheets().add(LIGHT_THEME);
            }

            FadeTransition fadeIn = new FadeTransition(Duration.millis(250), scene.getRoot());
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });

        fadeOut.play();
    }
}
