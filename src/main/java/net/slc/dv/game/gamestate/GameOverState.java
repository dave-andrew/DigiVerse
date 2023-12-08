package net.slc.dv.game.gamestate;

import net.slc.dv.helper.InputManager;
import net.slc.dv.helper.ScreenManager;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import net.slc.dv.view.offlinegame.OfflineGameView;


public class GameOverState extends GameBaseState {

    private final ImageView guiView;

    public GameOverState(OfflineGameView game) {
        super(game);

        this.guiView = new ImageView(new Image("file:resources/game/gui/game-over.png"));

        this.guiView.setScaleX(0.75);
        this.guiView.setScaleY(0.75);

        this.guiView.setX(ScreenManager.SCREEN_WIDTH / 2 - this.guiView.getImage().getWidth() / 2);
        this.guiView.setY(ScreenManager.SCREEN_HEIGHT / 2 - this.guiView.getImage().getHeight() / 2);
    }

    @Override
    public void onEnterState() {

        game.resetLevel();
        game.setEnemySpawnRate(0.01);
        game.setBaseEnemyHealth(1);

        game.setBatchTimer(game.getInitialTimer());
        game.getTimerLabel().setText("Time: " + game.getINITIAL_TIMER_VALUE() + "s");

        game.getRoot().getChildren().add(guiView);

        game.setDeadPause(false);

        OfflineGameView.getMediaPlayer().stop();
    }

    @Override
    public void onUpdate(long now) {
        if(InputManager.getPressedKeys().contains(KeyCode.SPACE)) {
            game.getRoot().getChildren().remove(guiView);
            game.cleanUp();
            game.changeState(game.getPlayState());
        }
    }
}
