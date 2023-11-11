import controller.AuthController;
import helper.ScreenManager;
import helper.StageManager;
import helper.ThemeManager;
import helper.Toast;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import model.LoggedUser;
import view.Home;
import view.Login;
import view.OfflineGame;

public class Main extends Application {

    private AuthController authController;

    private Scene scene;
    private Button button;
    private BorderPane borderPane;

    private Scene initialize() {
        authController = new AuthController();

        borderPane = new BorderPane();
        scene = new Scene(borderPane, ScreenManager.SCREEN_WIDTH, ScreenManager.SCREEN_HEIGHT);
        ThemeManager.getTheme(scene);

        return scene;
    }

    @Override
    public void start(Stage stage) throws Exception {
        Stage primaryStage = StageManager.getInstance();
        scene = initialize();
        primaryStage.setScene(scene);

        PauseTransition delay = new PauseTransition(Duration.seconds(2));

        delay.setOnFinished(event -> {
            String message = authController.checkAuth();
            if(message.equals("true")){
                Toast.makeText(primaryStage, "Welcome back, "+ LoggedUser.getInstance().getUsername() +"!", 2000, 500, 500);
                Platform.runLater(() -> new Home(primaryStage));
            } else if(message.equals("false")) {
                Platform.runLater(() -> new Login(primaryStage));
            } else {
                Platform.runLater(() -> new OfflineGame(primaryStage));
            }
        });

        delay.play();

        primaryStage.setTitle("DigiVerse");
        primaryStage.setResizable(false);
        stage.initStyle(StageStyle.UTILITY);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
