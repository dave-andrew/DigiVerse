package game.gamestate;

import helper.InputManager;
import helper.ScreenManager;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import view.OfflineGame;
import view.gameview.PauseMenu;
import view.gameview.SettingMenu;

public class GamePauseState extends GameBaseState {

    private PauseMenu pauseMenu;
    private SettingMenu settingMenu;

    public GamePauseState(OfflineGame game) {
        super(game);

        settingMenu = new SettingMenu(game);
        pauseMenu = new PauseMenu(game, settingMenu);
        settingMenu.setPauseMenu(pauseMenu);

        game.getPauseMenu().getChildren().addAll(pauseMenu);

        double screenWidth = ScreenManager.SCREEN_WIDTH;
        double screenHeight = ScreenManager.SCREEN_HEIGHT;

        double pauseMenuX = (screenWidth - game.getPauseMenu().getPrefWidth()) / 2;
        double pauseMenuY = (screenHeight - game.getPauseMenu().getPrefHeight()) / 2;

        game.getPauseMenu().setLayoutX(pauseMenuX);
        game.getPauseMenu().setLayoutY(pauseMenuY);
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
