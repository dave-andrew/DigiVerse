// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.

import helper.ThemeManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import view.Register;

public class Main extends Application {

    private Scene scene;
    private Button button;
    private BorderPane borderPane;

    private Scene initialize() {
        button = new Button();
        button.setText("Click me!");
//        Label lbl = new Label("Testing");
        borderPane = new BorderPane();
        borderPane.setCenter(button);
        scene = new Scene(borderPane, 1200, 700);
        ThemeManager.getTheme(scene);

        return scene;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        scene = initialize();
        primaryStage.setScene(scene);

        button.setOnAction(e -> {
            new Register(primaryStage);
        });

        primaryStage.setTitle("DVibes");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}