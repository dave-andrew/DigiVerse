package view;

import helper.ScreenManager;
import helper.ThemeManager;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Home {

    private Scene scene;
    private BorderPane borderPane;

    private HBox navBar;


    private VBox sideBar;



    private void initialize() {
        borderPane = new BorderPane();

        navBar = new HBox();

        sideBar = new VBox();
    }

    private Scene setLayout() {

        Label lbl = new Label("Home Page Right Now");
        borderPane.setCenter(lbl);

        scene = new Scene(borderPane, ScreenManager.SCREEN_WIDTH, ScreenManager.SCREEN_HEIGHT);
        ThemeManager.getTheme(scene);

        return scene;
    }

    public Home(Stage stage) {

        initialize();

        scene = setLayout();

        stage.setScene(scene);
        stage.setTitle("DigiVerse - Home");

    }

}
