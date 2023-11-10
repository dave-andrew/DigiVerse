package view;

import helper.ScreenManager;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Classroom;

public class AddTask extends BorderPane {

    private Classroom classroom;
    private Stage stage;

    private Stage dialogStage;
    private Scene dialogScene;

    private BorderPane root;

    public AddTask(Classroom classroom, Stage stage) {
        this.classroom = classroom;
        this.stage = stage;
        init();
    }

    private void init() {
        this.dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(stage);
//        dialogStage.initStyle(StageStyle.TRANSPARENT);

        this.root = new BorderPane();
        this.root.setTop(navBar());
        this.root.setRight(rightBar());

        dialogScene = new Scene(root, ScreenManager.SCREEN_WIDTH, ScreenManager.SCREEN_HEIGHT);
        dialogStage.setScene(dialogScene);

        dialogStage.setTitle("Add Task");
        dialogStage.showAndWait();
    }

    private HBox navBar() {
        HBox navBar = new HBox();
        navBar.getStyleClass().add("nav-bar");

        Button backBtn = new Button("Back");
        backBtn.getStyleClass().add("back-button");

        Label title = new Label("Add Task");
        title.getStyleClass().add("title");

        navBar.getChildren().addAll(backBtn, title);

        backBtn.setOnAction(e -> {
            this.dialogStage.close();
        });

        return navBar;
    }

    private VBox rightBar() {
        VBox rightBar = new VBox();
        rightBar.getStyleClass().add("right-bar");

        Button addBtn = new Button("Add");
        addBtn.getStyleClass().add("primary-button");

        rightBar.getChildren().add(addBtn);

        return rightBar;
    }
}
