package view;

import helper.ScreenManager;
import helper.ThemeManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.LoggedUser;
import view.component.ChangeAccountBox;
import view.component.classroom.CreateClassForm;
import view.component.classroom.CreateClassNav;

public class CreateClass {
    private Scene scene;
    private BorderPane borderPane;

    private VBox mainVbox;
    private HBox topBar;

    private VBox userInfoBox;
    private VBox classFormBox;

    public CreateClass(Stage stage) {
        initialize(stage);
        setLayout();
        showAndWait();
    }

    private void initialize(Stage ownerStage) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(ownerStage);
//        dialogStage.initStyle(StageStyle.TRANSPARENT);

        borderPane = new BorderPane();
        mainVbox = new VBox(20);
        topBar = new CreateClassNav(dialogStage);

        userInfoBox = new ChangeAccountBox(dialogStage);
        userInfoBox.setAlignment(Pos.TOP_CENTER);
        userInfoBox.getStyleClass().add("container");

        classFormBox = new CreateClassForm(dialogStage);
        classFormBox.getStyleClass().add("container");

        dialogStage.setScene(new Scene(borderPane, ScreenManager.SCREEN_WIDTH, ScreenManager.SCREEN_HEIGHT));
        ThemeManager.getTheme(dialogStage.getScene());
    }

    private void setLayout() {
        mainVbox.setPadding(new Insets(20, 20, 20, 20));

        mainVbox.getChildren().addAll(userInfoBox, classFormBox);
        mainVbox.setAlignment(Pos.TOP_CENTER);

        borderPane.setTop(topBar);
        borderPane.setCenter(mainVbox);
    }

    private void showAndWait() {
        Stage dialogStage = (Stage) borderPane.getScene().getWindow();
        dialogStage.setTitle("Create Class");
        dialogStage.showAndWait();
    }
}
