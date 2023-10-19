package view;

import helper.ThemeManager;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Home {

    private Scene scene;
    private BorderPane borderPane;

    private void initialize() {
        borderPane = new BorderPane();

        Label lbl = new Label("Home Page Right Now");
        borderPane.setCenter(lbl);

        scene = new Scene(borderPane, 1200, 700);
        ThemeManager.getTheme(scene);
    }

    public Home(Stage stage) {

        initialize();

        stage.setScene(scene);

    }

}
