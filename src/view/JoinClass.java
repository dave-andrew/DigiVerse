package view;

import helper.ScreenManager;
import helper.ThemeManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.component.ChangeAccountBox;
import view.component.classroom.GroupCodeForm;
import view.component.classroom.JoinClassNav;

public class JoinClass {

    private Scene scene;
    private BorderPane borderPane;

    private VBox mainVbox;
    private HBox topBar;

    private VBox userInfoBox;
    private VBox classFormBox;
    private VBox joinInfo;
    private Label joinInfoSub, lbl1, lbl2;
    private VBox joinInfoList;

    private void initialize(Stage stage) {

        borderPane = new BorderPane();
        mainVbox = new VBox(20);
        topBar = new JoinClassNav(stage);

        userInfoBox = new ChangeAccountBox();
        userInfoBox.setAlignment(Pos.TOP_CENTER);
        userInfoBox.getStyleClass().add("container");

        classFormBox = new GroupCodeForm();
        classFormBox.getStyleClass().add("container");

        joinInfo = new VBox(5);
        joinInfo.setPadding(new Insets(20));

        joinInfoSub = new Label("To join using group class code:");

        joinInfoList = new VBox(2);

        lbl1 = new Label("• Use authorized account");
        lbl2 = new Label("• Use a maximum of 10 letters of group code.");

        joinInfoList.getChildren().addAll(lbl1, lbl2);

        joinInfo.getChildren().addAll(joinInfoSub, joinInfoList);
        joinInfo.getStyleClass().add("container");

    }

    private Scene setLayout() {
        mainVbox.setPadding(new Insets(20, 20, 20, 20));

        mainVbox.getChildren().addAll(userInfoBox, classFormBox, joinInfo);
        mainVbox.setAlignment(Pos.TOP_CENTER);

        borderPane.setTop(topBar);
        borderPane.setCenter(mainVbox);

        scene = new Scene(borderPane, ScreenManager.SCREEN_WIDTH, ScreenManager.SCREEN_HEIGHT);
        ThemeManager.getTheme(scene);
        return scene;
    }

    public JoinClass(Stage stage) {

        initialize(stage);

        scene = setLayout();

        stage.setScene(scene);
    }
}
