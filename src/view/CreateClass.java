package view;

import helper.ScreenManager;
import helper.ThemeManager;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CreateClass {

    private Scene scene;

    private ScrollPane scrollPane;
    private VBox mainVbox;


    private void initialize() {
        scrollPane = new ScrollPane();
    }

    private Scene setLayout() {
        mainVbox = new VBox(20);
        mainVbox.setPadding(new Insets(20, 20, 20, 20));

        scrollPane.setContent(mainVbox);
        scrollPane.setFitToWidth(true);

        scene = new Scene(scrollPane, ScreenManager.SCREEN_WIDTH, ScreenManager.SCREEN_HEIGHT);
        ThemeManager.getTheme(scene);
        return scene;
    }

    public CreateClass(Stage stage) {
        initialize();
        scene = setLayout();

        stage.setScene(scene);
    }

}
