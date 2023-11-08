package game.gamestate;

import helper.ImageManager;
import helper.InputManager;
import helper.ScreenManager;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import view.OfflineGame;

public class GameStartState extends GameBaseState{

    private ImageView guiView;

    public GameStartState(OfflineGame game) {
        super(game);
    }

    @Override
    public void onEnterState() {
        Image gui = ImageManager.importGUI("start-banner");
        guiView = new ImageView(gui);
        this.game.getRoot().getChildren().add(guiView);

        guiView.setX(ScreenManager.SCREEN_WIDTH / 2 - ((gui.getWidth()) / 2));
        guiView.setY(ScreenManager.SCREEN_HEIGHT / 2 - ((gui.getHeight()) / 2));

        guiView.setScaleX(3);
        guiView.setScaleY(3);


    }

    @Override
    public void onUpdate(long now) {
        if(InputManager.getPressedKeys().contains(KeyCode.SPACE)) {
            this.game.getRoot().getChildren().remove(guiView);
            this.game.changeState(this.game.playState);
        }
    }
}
