package view.gameview;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import view.OfflineGame;

public class SettingMenu extends VBox {

    private final Label fps60Label;
    private final double originalMusicVolume;
    private final double originalTargetFPS;
    private VBox pauseMenu;
    private Label fps144Label;

    public SettingMenu(OfflineGame game) {
        Slider musicSlider = new Slider();
        Slider SFXSlider = new Slider();

        Label settingsLabel = new Label("Settings");
        settingsLabel.getStyleClass().add("title");

        this.getChildren().add(settingsLabel);

        Button applyButton = new Button("Apply");
        Button backButton = new Button("Back");

        Label musicLabel = new Label("Music Volume");
        Label SFXLabel = new Label("SFX Volume");
        Label resolutionLabel = new Label("Resolution");

        fps60Label = new Label("60 FPS");
        fps60Label.getStyleClass().add("fps-card");

        fps60Label.setOnMouseClicked(e -> {
            fps60Label.getStyleClass().add("act");
            fps144Label.getStyleClass().remove("act");
            game.setTargetFPS(60);
        });

        fps144Label = new Label("144 FPS");
        fps144Label.getStyleClass().addAll("fps-card", "act");

        fps144Label.setOnMouseClicked(e -> {
            fps144Label.getStyleClass().add("act");
            fps60Label.getStyleClass().remove("act");
            game.setTargetFPS(144);
        });

        HBox fpsContainer = new HBox(20);
        fpsContainer.getChildren().addAll(fps60Label, fps144Label);

        VBox resolutionContainer = new VBox();
        resolutionContainer.getChildren().addAll(resolutionLabel, fpsContainer);

        VBox musicContainer = new VBox();
        musicContainer.getChildren().addAll(musicLabel, musicSlider);

        VBox SFXContainer = new VBox();
        SFXContainer.getChildren().addAll(SFXLabel, SFXSlider);

        musicSlider.setMin(0);
        musicSlider.setMax(100);

        musicSlider.setValue(game.getMediaPlayer().getVolume() * 100);

        SFXSlider.setMin(0);
        SFXSlider.setMax(100);

        musicSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double newVolume = newValue.doubleValue() / 100.0;
            game.getMediaPlayer().setVolume(newVolume);
        });

        applyButton.setPrefWidth(250);
        applyButton.getStyleClass().add("primary-button");

        originalMusicVolume = game.getMediaPlayer().getVolume();
        originalTargetFPS = game.getTargetFPS();

        applyButton.setOnAction(e -> {
            game.getPauseMenu().getChildren().remove(this);
            game.getPauseMenu().getChildren().add(pauseMenu);
        });

        backButton.setPrefWidth(250);
        backButton.getStyleClass().add("secondary-button");

        backButton.setOnAction(e -> {
            game.getMediaPlayer().setVolume(originalMusicVolume);
            game.setTargetFPS(originalTargetFPS);

            game.getPauseMenu().getChildren().remove(this);
            game.getPauseMenu().getChildren().add(pauseMenu);
        });

        pauseMenu = new VBox();
        pauseMenu.setSpacing(40);

        HBox buttonContainer = new HBox(20);
        buttonContainer.getChildren().addAll(backButton, applyButton);

        this.getChildren().addAll(musicContainer, SFXContainer, resolutionContainer, buttonContainer);
        this.setSpacing(40);
        this.setAlignment(Pos.CENTER);
    }

    public void setPauseMenu(VBox pauseMenu) {
        this.pauseMenu = pauseMenu;
    }
}
