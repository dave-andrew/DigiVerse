package net.slc.dv.game.gamestate;

import javafx.scene.layout.BorderPane;
import net.slc.dv.helper.ScreenManager;
import net.slc.dv.view.offlinegame.OfflineGameView;
import net.slc.dv.view.offlinegame.component.PauseMenu;
import net.slc.dv.view.offlinegame.component.SettingMenu;

public class GamePauseState extends GameBaseState {

    public GamePauseState(OfflineGameView game) {
        super(game);

        SettingMenu settingMenu = new SettingMenu(game);
        PauseMenu pauseMenu = new PauseMenu(game, settingMenu);
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

        game.getMediaPlayer().pause();
        game.getRoot().getChildren().add(game.getPauseMenu());
    }

    @Override
    public void onUpdate(long now) {
        this.game.updateGame(now);
    }
}
