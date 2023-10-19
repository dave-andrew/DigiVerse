package view;

import javafx.scene.Scene;
import javafx.stage.Stage;

public interface PageView {

    void initialize();

    Scene setLayout();

    void actions(Stage stage);

}
