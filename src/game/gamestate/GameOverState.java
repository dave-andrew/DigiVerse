package game.gamestate;

import helper.InputManager;
import helper.ScreenManager;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import view.OfflineGame;


public class GameOverState extends GameBaseState {

    private ImageView guiView;

    public GameOverState(OfflineGame game) {
        super(game);

        this.guiView = new ImageView(new Image("file:resources/game/gui/game-over.png"));

        this.guiView.setScaleX(0.75);
        this.guiView.setScaleY(0.75);

        this.guiView.setX(ScreenManager.SCREEN_WIDTH / 2 - this.guiView.getImage().getWidth() / 2);
        this.guiView.setY(ScreenManager.SCREEN_HEIGHT / 2 - this.guiView.getImage().getHeight() / 2);
    }

    @Override
    public void onEnterState() {
        game.getRoot().getChildren().add(guiView);
    }

    @Override
    public void onUpdate(long now) {
        if(InputManager.getPressedKeys().contains(KeyCode.SPACE)) {
            game.getRoot().getChildren().remove(guiView);
            game.cleanUp();
            game.changeState(game.startState);
        }
    }
}
