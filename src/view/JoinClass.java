package view;

import controller.ClassController;
import helper.ScreenManager;
import helper.ThemeManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import view.component.ChangeAccountBox;
import view.component.classroom.GroupCodeForm;
import view.component.classroom.JoinClassNav;

public class JoinClass {

    private ClassController classController;

    private Scene scene;
    private BorderPane borderPane;

    private VBox mainVbox;
    private JoinClassNav topBar;

    private VBox userInfoBox;
    private GroupCodeForm classFormBox;
    private VBox joinInfo;
    private Label joinInfoSub, lbl1, lbl2, errorLbl;
    private VBox joinInfoList;
    private Stage dialogStage;

    public JoinClass(Stage ownerStage) {
        initialize(ownerStage);
        setLayout();
        setActions();

        this.dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(ownerStage);
//        dialogStage.initStyle(StageStyle.TRANSPARENT);
        dialogStage.setScene(scene);

        dialogStage.setTitle("Join Class");
        dialogStage.showAndWait();
    }

    private void initialize(Stage stage) {
        classController = new ClassController();

        borderPane = new BorderPane();
        mainVbox = new VBox(20);
        topBar = new JoinClassNav(stage);

        errorLbl = new Label();
        errorLbl.setStyle("-fx-text-fill: red;");

        userInfoBox = new ChangeAccountBox(dialogStage);
        userInfoBox.setAlignment(Pos.TOP_CENTER);
        userInfoBox.getStyleClass().add("card");
        userInfoBox.setMaxWidth(800);

        classFormBox = new GroupCodeForm(errorLbl);
        classFormBox.getStyleClass().add("card");
        classFormBox.setMaxWidth(800);

        joinInfo = new VBox(5);
        joinInfo.setPadding(new Insets(20));

        joinInfoSub = new Label("To join using group class code:");

        joinInfoList = new VBox(2);

        lbl1 = new Label("• Use an authorized account");
        lbl2 = new Label("• Use a maximum of 10 letters of group code.");

        joinInfoList.getChildren().addAll(lbl1, lbl2);

        joinInfo.getChildren().addAll(joinInfoSub, joinInfoList);
        joinInfo.getStyleClass().add("card");
        joinInfo.setMaxWidth(800);
    }

    private void setLayout() {
        mainVbox.setPadding(new Insets(20, 20, 20, 20));

        mainVbox.getChildren().addAll(userInfoBox, classFormBox, joinInfo);
        mainVbox.setAlignment(Pos.TOP_CENTER);

        borderPane.setTop(topBar);
        borderPane.setCenter(mainVbox);

        scene = new Scene(borderPane, ScreenManager.SCREEN_WIDTH, ScreenManager.SCREEN_HEIGHT);
        ThemeManager.getTheme(scene);
    }

    private void setActions() {
        topBar.getJoinBtn().setOnMouseClicked(e -> {
            String message = classController.checkJoinClass(classFormBox.getGroupCode());

            if(message.equals("Class Joined!")) {
                dialogStage.close();
            }

            errorLbl.setText(message);

        });
    }
}
