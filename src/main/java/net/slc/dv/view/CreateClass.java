package net.slc.dv.view;

import net.slc.dv.helper.ScreenManager;
import net.slc.dv.helper.ThemeManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.slc.dv.view.component.ChangeAccountBox;
import net.slc.dv.view.component.classroom.CreateClassForm;
import net.slc.dv.view.component.classroom.CreateClassNav;

public class CreateClass {
    private Scene scene;
    private BorderPane borderPane;

    private VBox mainVbox;
    private HBox topBar;

    private VBox userInfoBox;
    private VBox classFormBox;

    private Label errorLbl;

    public CreateClass(Stage stage) {
        initialize(stage);
        setLayout();
        showAndWait();
    }

    private void initialize(Stage ownerStage) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(ownerStage);

        borderPane = new BorderPane();
        mainVbox = new VBox(20);
        topBar = new CreateClassNav();

        userInfoBox = new ChangeAccountBox(dialogStage);
        userInfoBox.setAlignment(Pos.CENTER_LEFT);
        userInfoBox.getStyleClass().add("card");
        userInfoBox.setMaxWidth(800);

        errorLbl = new Label();
        errorLbl.setStyle("-fx-text-fill: red;");

        classFormBox = new CreateClassForm(dialogStage, errorLbl);
        classFormBox.getStyleClass().add("card");
        classFormBox.setMaxWidth(800);

        dialogStage.setScene(new Scene(borderPane, ScreenManager.SCREEN_WIDTH, ScreenManager.SCREEN_HEIGHT));
        ThemeManager.getTheme(dialogStage.getScene());
    }

    private void setLayout() {
        mainVbox.setPadding(new Insets(20, 20, 20, 20));

        mainVbox.getChildren().addAll(userInfoBox, classFormBox);
        mainVbox.setAlignment(Pos.TOP_CENTER);

        borderPane.setTop(topBar);
        borderPane.setCenter(mainVbox);

        borderPane.setMaxWidth(800);
    }

    private void showAndWait() {
        Stage dialogStage = (Stage) borderPane.getScene().getWindow();
        dialogStage.setTitle("Create Class");
        dialogStage.showAndWait();
    }
}
