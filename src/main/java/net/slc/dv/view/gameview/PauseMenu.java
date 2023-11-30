package net.slc.dv.view.gameview;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import net.slc.dv.view.OfflineGame;

public class PauseMenu extends VBox {

    private Button resumeButton, settingButton, exitButton;
    private Label pauseLabel;

    public PauseMenu(OfflineGame game, VBox settingMenu) {

        StackPane stackPane = new StackPane();

        resumeButton = new Button("Resume");
        settingButton = new Button("Setting");
        exitButton = new Button("Exit");

        pauseLabel = new Label("Pause Menu");
        pauseLabel.getStyleClass().add("title");

        resumeButton.setPrefWidth(250);
        resumeButton.getStyleClass().add("primary-button");
        resumeButton.setStyle("-fx-text-fill: white;");

        settingButton.setPrefWidth(250);
        settingButton.getStyleClass().add("primary-button");
        settingButton.setStyle("-fx-text-fill: white;");

        exitButton.setPrefWidth(250);
        exitButton.getStyleClass().add("primary-button");
        exitButton.setStyle("-fx-text-fill: white;");

        resumeButton.setOnAction(e -> {
            game.getRoot().getChildren().remove(game.getPauseMenu());
            game.changeState(game.playState);
        });

        settingButton.setOnAction(e -> {
            game.getPauseMenu().getChildren().remove(this);
            game.getPauseMenu().getChildren().add(settingMenu);
        });

        exitButton.setOnAction(e -> {
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
        imageView.setFitHeight(400);

        stackPane.getChildren().add(imageView);

        stackPane.getChildren().add(pauseMenu);

        this.getChildren().addAll(stackPane);
        this.setSpacing(40);
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-background-color: transparent;");
    }

}
