import controller.AuthController;
import helper.ThemeManager;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import view.Home;
import view.Login;

public class Main extends Application {

    private AuthController authController;

    private Scene scene;
    private Button button;
    private BorderPane borderPane;

    private Scene initialize() {
        authController = new AuthController();

        borderPane = new BorderPane();
        scene = new Scene(borderPane, 1200, 700);
        ThemeManager.getTheme(scene);

        return scene;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        scene = initialize();
        primaryStage.setScene(scene);

        PauseTransition delay = new PauseTransition(Duration.seconds(2));

        delay.setOnFinished(event -> {
            if(authController.checkAuth()){
                Platform.runLater(() -> new Home(primaryStage));
            } else {
                Platform.runLater(() -> new Login(primaryStage));
            }
        });

        delay.play();

        primaryStage.setTitle("DVibes");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
