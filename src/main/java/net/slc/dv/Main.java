package net.slc.dv;

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
import net.slc.dv.controller.AuthController;
import net.slc.dv.database.connection.ConnectionChecker;
import net.slc.dv.enums.ToastType;
import net.slc.dv.helper.ScreenManager;
import net.slc.dv.helper.StageManager;
import net.slc.dv.helper.ThemeManager;
import net.slc.dv.helper.toast.ToastBuilder;
import net.slc.dv.model.LoggedUser;
import net.slc.dv.view.Home;
import net.slc.dv.view.LoginView;
import net.slc.dv.view.OfflineGame;


public class Main extends Application {

    private AuthController authController;
    private ConnectionChecker connectionChecker;
    private Scene scene;
    private String currentScene;

    public static void main(String[] args) {
        launch(args);
    }

    private Scene initialize() {
        authController = new AuthController();

        BorderPane borderPane = new BorderPane();

        VBox loading = new VBox();
        loading.setAlignment(Pos.CENTER);

        ProgressBar progressBar = new ProgressBar();
        progressBar.setPrefWidth(300);

        simulateLoading(progressBar);

        loading.getChildren().add(progressBar);

        borderPane.setCenter(loading);

        scene = new Scene(borderPane, ScreenManager.SCREEN_WIDTH, ScreenManager.SCREEN_HEIGHT);
        ThemeManager.getTheme(scene);

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
            this.connectionChecker = new ConnectionChecker();

            this.connectionChecker.getIsConnected().addListener(this::sceneHandler);
        });

        service.start();
    }

    private void sceneHandler(boolean value) {
        String message = authController.checkAuth();
        Stage stage = StageManager.getInstance();

        if (!value) {
            if (this.currentScene == null) {
                this.currentScene = "offline";
                new OfflineGame(stage);
                return;
            }

            ToastBuilder.buildNormal(ToastType.BUTTON).setText("You are offline!").build();
            this.currentScene = "offline";
            new OfflineGame(stage);
            return;
        }

        if (this.currentScene != null && this.currentScene.equals("offline")) {
            ToastBuilder.buildButton(ToastType.BUTTON)
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
        if (message.equals("true")) {
            ToastBuilder.buildNormal(ToastType.NORMAL).setText("Welcome back, " + LoggedUser.getInstance().getUsername() + "!").build();
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
        Stage primaryStage = StageManager.getInstance();
        scene = initialize();
        primaryStage.setScene(scene);

        primaryStage.setTitle("DigiVerse");
        primaryStage.getIcons().add(new Image("file:resources/icons/app_logo.png"));
        stage.initStyle(StageStyle.UTILITY);
        primaryStage.show();
    }
}
