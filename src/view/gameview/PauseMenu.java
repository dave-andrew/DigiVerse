package view.gameview;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import view.OfflineGame;

public class PauseMenu extends VBox {

    public PauseMenu(OfflineGame game, VBox settingMenu) {

        Button resumeButton = new Button("Resume");
        Button settingButton = new Button("Setting");
        Button exitButton = new Button("Exit");

        Label pauseLabel = new Label("Pause Menu");
        pauseLabel.getStyleClass().add("title");

        this.getChildren().add(pauseLabel);

        resumeButton.setPrefWidth(250);
        resumeButton.getStyleClass().add("primary-button");

        settingButton.setPrefWidth(250);
        settingButton.getStyleClass().add("primary-button");

        exitButton.setPrefWidth(250);
        exitButton.getStyleClass().add("primary-button");

        resumeButton.setOnAction(e -> {
            game.getRoot().getChildren().remove(game.getPauseMenu());
            game.changeState(game.getPlayState());
        });

        settingButton.setOnAction(e -> {
            game.getPauseMenu().getChildren().remove(this);
            game.getPauseMenu().getChildren().add(settingMenu);
        });

        exitButton.setOnAction(e -> {
            System.exit(0);
        });

        this.getChildren().addAll(resumeButton, settingButton, exitButton);
        this.setSpacing(40);
        this.setAlignment(Pos.CENTER);
    }

}
