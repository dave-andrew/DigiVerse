package game.gamestate;

import helper.InputManager;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import view.OfflineGame;

public class GamePauseState extends GameBaseState {

    private Button resumeButton, settingButton, exitButton;

    public GamePauseState(OfflineGame game) {
        super(game);

        resumeButton = new Button("Resume");
        settingButton = new Button("Setting");
        exitButton = new Button("Exit");

        resumeButton.setOnAction(e -> {
            game.getRoot().getChildren().remove(game.getPauseMenu());
            game.changeState(game.playState);
        });

        settingButton.setOnAction(e -> {
            game.getRoot().getChildren().remove(game.getPauseMenu());
        });

        exitButton.setOnAction(e -> {
            System.exit(0);
        });

        game.getPauseMenu().getChildren().addAll(resumeButton, settingButton, exitButton);

    }

    @Override
    public void onEnterState() {

        game.getRoot().getChildren().add(game.getPauseMenu());

    }

    @Override
    public void onUpdate(long now) {
        this.game.updateGame(now);
    }
}
