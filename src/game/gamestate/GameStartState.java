package game.gamestate;

import helper.ImageManager;
import helper.InputManager;
import helper.ScreenManager;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import view.OfflineGame;

public class GameStartState extends GameBaseState{

    private ImageView guiView;
    private Scene scene;

    public GameStartState(OfflineGame game) {
        super(game);
    }

    @Override
    public void onEnterState() {

        scene = game.getScene();

        Image gui = ImageManager.importGUI("start-banner");
        guiView = new ImageView(gui);
        this.game.getRoot().getChildren().add(guiView);

        double screenWidth = ScreenManager.SCREEN_WIDTH;
        double screenHeight = ScreenManager.SCREEN_HEIGHT;

        guiView.setScaleX(3);
        guiView.setScaleY(3);

        guiView.setX(screenWidth / 2 - gui.getWidth() / 2);
        guiView.setY(screenHeight / 2 - gui.getHeight() / 2);
    }

    @Override
    public void onUpdate(long now) {
        if(InputManager.getPressedKeys().contains(KeyCode.SPACE)) {
            this.game.getRoot().getChildren().remove(guiView);
            this.game.changeState(this.game.playState);
        }
    }
}
