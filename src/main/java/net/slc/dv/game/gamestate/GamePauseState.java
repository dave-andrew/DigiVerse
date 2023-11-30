package net.slc.dv.game.gamestate;

import net.slc.dv.helper.ScreenManager;
import net.slc.dv.view.OfflineGame;
import net.slc.dv.view.gameview.PauseMenu;
import net.slc.dv.view.gameview.SettingMenu;

public class GamePauseState extends GameBaseState {

    public GamePauseState(OfflineGame game) {
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
