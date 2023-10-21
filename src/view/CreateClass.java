package view;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class CreateClass {

    private Scene scene;

    private void initialize() {
        scene = new Scene(null);
    }

    public CreateClass(Stage stage) {
        initialize();


        stage.setScene(scene);
    }

}
