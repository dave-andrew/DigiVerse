import controller.AuthController;
import helper.ScreenManager;
import helper.StageManager;
import helper.ThemeManager;
import helper.Toast;
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
import model.LoggedUser;
import view.Home;
import view.LoginView;
import view.OfflineGame;

public class Main extends Application {

    private AuthController authController;

    private Scene scene;
    private BorderPane borderPane;

    private Scene initialize() {
        authController = new AuthController();

        borderPane = new BorderPane();

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
            String message = authController.checkAuth();
            if (message.equals("true")) {
                Toast.makeText(StageManager.getInstance(), "Welcome back, " + LoggedUser.getInstance().getUsername() + "!", 2000, 500, 500);
                new Home(StageManager.getInstance());
            } else if (message.equals("false")) {
                new LoginView(StageManager.getInstance());
            } else {
                new OfflineGame(StageManager.getInstance());
            }
        });

        service.start();
    }


    @Override
    public void start(Stage stage) throws Exception {
        Stage primaryStage = StageManager.getInstance();
        scene = initialize();
        primaryStage.setScene(scene);

        primaryStage.setTitle("DigiVerse");
        primaryStage.getIcons().add(new Image("file:resources/icons/app_logo.png"));
        stage.initStyle(StageStyle.UTILITY);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
