package game.gamestate;

import helper.InputManager;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import view.OfflineGame;

public class GameLevelUpState extends GameBaseState {

    private ImageView guiView;

    public GameLevelUpState(OfflineGame game) {
        super(game);
        this.guiView = new ImageView(new Image("file:resources/game/gui/level-up.png"));
    }

    @Override
    public void onEnterState() {

    }

    @Override
    public void onUpdate(long now) {
        if(InputManager.getPressedKeys().contains(KeyCode.SPACE)) {
//            game.getRoot().getChildren().remove();
            game.cleanUp();
            game.changeState(game.startState);
        }
    }
}
