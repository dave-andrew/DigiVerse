package view;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Login {

    private BorderPane borderPane;

    private Label lbl;

    public Login(Stage stage) {
        borderPane = new BorderPane();
        lbl = new Label("Login");
        borderPane.setCenter(lbl);
        stage.setTitle("DVibes - Login");
        stage.setScene(new Scene(borderPane, 1200, 700));
    }
}
