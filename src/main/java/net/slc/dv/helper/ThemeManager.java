package net.slc.dv.helper;

import javafx.animation.FadeTransition;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import net.slc.dv.enums.PowerUp;
import net.slc.dv.enums.Theme;
import net.slc.dv.resources.IconStorage;

public class ThemeManager {
    private static ThemeManager instance;
    public static final String LIGHT_THEME = "file:resources/light_theme.css";
    public static final String DARK_THEME = "file:resources/dark_theme.css";
    private static Theme theme = Theme.LIGHT;

    public void getTheme(Scene scene) {
        scene.getStylesheets().clear();
        if (theme.equals(Theme.LIGHT)) {
            scene.getStylesheets().add(LIGHT_THEME);
        } else if (theme.equals(Theme.DARK)) {
            scene.getStylesheets().add(DARK_THEME);
        }
    }

    public Theme getTheme() {
        return theme;
    }

    public static ThemeManager getInstance() {
        if(instance == null) {
            instance = new ThemeManager();
        }
        return instance;
    }


    public void toggleTheme(Scene scene, ToggleButton toggleButton) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(250), scene.getRoot());
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(event -> {
            scene.getStylesheets().clear();
            if (theme.equals(Theme.LIGHT)) {
                theme = Theme.DARK;

                IconStorage.changeTheme(Theme.DARK);
                scene.getStylesheets().add(DARK_THEME);
            } else if (theme.equals(Theme.DARK)) {
                theme = Theme.LIGHT;

                IconStorage.changeTheme(Theme.LIGHT);
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
