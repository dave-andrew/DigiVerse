package net.slc.dv.game.gamestate;

import net.slc.dv.helper.InputManager;
import net.slc.dv.helper.ScreenManager;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import net.slc.dv.view.OfflineGame;

public class GameLevelUpState extends GameBaseState {

    private final ImageView guiView;

    public GameLevelUpState(OfflineGame game) {
        super(game);
        this.guiView = new ImageView(new Image("file:resources/game/gui/level-up.png"));

        this.guiView.setScaleX(0.75);
        this.guiView.setScaleY(0.75);

        this.guiView.setX(ScreenManager.SCREEN_WIDTH / 2 - this.guiView.getImage().getWidth() / 2);
        this.guiView.setY(ScreenManager.SCREEN_HEIGHT / 2 - this.guiView.getImage().getHeight() / 2);
    }

    @Override
    public void onEnterState() {
        game.addLevel();
        game.getRoot().getChildren().add(guiView);

        game.setEnemySpawnRate(game.getEnemySpawnRate() + 0.05);
        game.setBaseEnemyHealth(game.getBaseEnemyHealth() + 1);
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
