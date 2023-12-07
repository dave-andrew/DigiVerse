package net.slc.dv.view.offlinegame.component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import net.slc.dv.view.offlinegame.OfflineGameView;

public class PauseMenu extends VBox {

    public PauseMenu(OfflineGameView game, VBox settingMenu) {
        StackPane stackPane = new StackPane();
        Button resumeButton = new Button("Resume");

        Button settingButton = new Button("Settings");
        Button exitButton = new Button("Quit");

        Label pauseLabel = new Label("PAUSE MENU");
        pauseLabel.setStyle("-fx-font-size: 40px;-fx-font-family: 'Minecraft'");

        resumeButton.setPrefWidth(250);

        settingButton.setPrefWidth(250);

        exitButton.setPrefWidth(250);

        resumeButton.setOnMouseClicked(e -> {
            game.getRoot().getChildren().remove(game.getPauseMenu());
            game.changeState(game.getPlayState());
        });

        settingButton.setOnMouseClicked(e -> {
            game.getPauseMenu().getChildren().remove(this);
            game.getPauseMenu().getChildren().add(settingMenu);
        });

        exitButton.setOnMouseClicked(e -> {
            System.exit(0);
        });

        VBox pauseMenu = new VBox(40);
        pauseMenu.getChildren().addAll(pauseLabel, resumeButton, settingButton, exitButton);
        pauseMenu.setAlignment(Pos.CENTER);
        pauseMenu.setStyle("-fx-background-color: transparent;");
        pauseMenu.setPadding(new Insets(20, 20, 20, 20));

        Image image = new Image("file:resources/game/gui/setting-menu.png");
        ImageView imageView = new ImageView(image);

        imageView.setFitWidth(500);
        imageView.setFitHeight(600);

        stackPane.getChildren().add(imageView);

        stackPane.getChildren().add(pauseMenu);

        this.getChildren().addAll(stackPane);
        this.setSpacing(40);
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-background-color: transparent;");
    }

}
