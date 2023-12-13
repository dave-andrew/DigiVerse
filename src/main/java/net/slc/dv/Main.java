package net.slc.dv;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import net.slc.dv.builder.ButtonBuilder;
import net.slc.dv.builder.ImageViewBuilder;
import net.slc.dv.enums.Theme;
import net.slc.dv.resources.Icon;
import net.slc.dv.storage.IconStorage;
import net.slc.dv.controller.AuthController;
import net.slc.dv.database.connection.ConnectionChecker;
import net.slc.dv.helper.ScreenManager;
import net.slc.dv.helper.StageManager;
import net.slc.dv.helper.ThemeManager;
import net.slc.dv.helper.toast.ToastBuilder;
import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.slc.dv.model.LoggedUser;
import net.slc.dv.storage.ImageStorage;
import net.slc.dv.storage.TextStorage;
import net.slc.dv.view.home.Home;
import net.slc.dv.view.login.LoginView;
import net.slc.dv.view.offlinegame.OfflineGameView;

import java.io.File;

public class Main extends Application {

    private AuthController authController;
    private ConnectionChecker connectionChecker;
    private Scene scene;
    private String currentScene;
    private ImageView forbidden;
    private MediaPlayer mediaPlayer;

    private Scene initialize() {
        authController = new AuthController();

        File file = new File("resources/U2FsdGVkX196RSKgQrFGVdYWHBhy0jfg/U2FsdGVkX196RSKgQrFGVdYWHBhy0jfg.wav");
        Media media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(0.3);

        BorderPane borderPane = new BorderPane();

        VBox loading = new VBox(30);
        loading.setAlignment(Pos.CENTER);

        ProgressBar progressBar = new ProgressBar();
        progressBar.setPrefWidth(300);

        simulateLoading(progressBar);

        ImageView logo = ImageViewBuilder.create()
                .setImage(new Image(Icon.LOGO.getPath(Theme.LIGHT)))
                .build();

        this.forbidden = new ImageView();
        this.forbidden.setImage(new Image(ImageStorage.FORBIDDEN));
        this.forbidden.setFitHeight(ScreenManager.SCREEN_HEIGHT);
        this.forbidden.setFitWidth(1280);
        this.forbidden.setX(ScreenManager.SCREEN_WIDTH / 2 - forbidden.getFitWidth() / 2);

        Button button = ButtonBuilder.create()
                .setGraphic(logo)
                .setStyle("-fx-background-color: transparent;")
                .build();

        loading.getChildren().addAll(button, progressBar);

        button.setOnMouseEntered(e -> {
//            this.mediaPlayer.play();
//            borderPane.setCenter(forbidden);
        });

        borderPane.setCenter(loading);

        scene = new Scene(borderPane, ScreenManager.SCREEN_WIDTH, ScreenManager.SCREEN_HEIGHT);
        ThemeManager.getInstance().getTheme(scene);

        return scene;
    }

    private void simulateLoading(ProgressBar progressBar) {
        Service<Void> service = new Service<>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() throws Exception {
                        for (int i = 0; i <= 100; i++) {
                            updateProgress(i, 100);
                            Thread.sleep(20);
                        }
                        return null;
                    }
                };
            }
        };

        progressBar.progressProperty().bind(service.progressProperty());

        service.setOnSucceeded(e -> {
            this.mediaPlayer.stop();
            this.connectionChecker = new ConnectionChecker();

            this.connectionChecker.getIsConnected().addListener(this::sceneHandler);
        });

        service.start();
    }

    private void sceneHandler(boolean value) {
        Stage stage = StageManager.getInstance();

        if (!value) {
            if (this.currentScene == null) {
                this.currentScene = "offline";
                new OfflineGameView(stage);
                return;
            }

            ToastBuilder.buildNormal().setText("You are offline!").build();
            this.currentScene = "offline";
            new OfflineGameView(stage);
            return;
        }

        String message = authController.checkAuth();

        if (this.currentScene != null && this.currentScene.equals("offline")) {
            ToastBuilder.buildButton()
                    .setButtonText("Redirect")
                    .setButtonAction(toast -> this.redirectConnected(message))
                    .setOnClickClose(true)
                    .setText("You are online!")
                    .setFadeOutDelay(1000000)
                    .build();
            return;
        }

        this.redirectConnected(message);
    }

    private void redirectConnected(String message) {

        if (OfflineGameView.getMediaPlayer() != null) {
            OfflineGameView.getMediaPlayer().stop();
            OfflineGameView.getMediaPlayer().dispose();
        }

        if (message.equals("true")) {
            ToastBuilder.buildNormal().setText("Welcome back, " + LoggedUser.getInstance().getUsername() + "!").build();
            this.currentScene = "home";
            new Home(StageManager.getInstance());
            return;
        }

        if (message.equals("false")) {
            this.currentScene = "login";
            new LoginView(StageManager.getInstance());
        }
    }

    @Override
    public void start(Stage stage) {
        IconStorage.init();
        TextStorage.init();
        Stage primaryStage = StageManager.getInstance();

        scene = initialize();
        primaryStage.setScene(scene);

        primaryStage.setTitle("DigiVerse");
        primaryStage.getIcons().add(IconStorage.getIcon(Icon.APP_LOGO).getValue());
        stage.initStyle(StageStyle.UTILITY);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
