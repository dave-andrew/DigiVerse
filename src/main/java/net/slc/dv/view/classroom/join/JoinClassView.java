package net.slc.dv.view.classroom.join;

import net.slc.dv.controller.ClassController;
import net.slc.dv.helper.ScreenManager;
import net.slc.dv.helper.ThemeManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.slc.dv.view.classroom.join.component.GroupCodeForm;
import net.slc.dv.view.classroom.join.component.JoinClassNav;
import net.slc.dv.view.classroom.join.component.ChangeAccountBox;

public class JoinClassView {

    private final Stage dialogStage;

    private ClassController classController;
    private Scene scene;
    private BorderPane borderPane;
    private VBox mainVbox;
    private JoinClassNav topBar;
    private VBox userInfoBox;
    private GroupCodeForm classFormBox;
    private VBox joinInfo;
    private Label errorLbl;

    public JoinClassView(Stage ownerStage) {
        this.dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(ownerStage);
//        dialogStage.initStyle(StageStyle.TRANSPARENT);

        initialize(ownerStage);
        setLayout();
        setActions();

        dialogStage.setScene(scene);

        dialogStage.setTitle("Join Class");
        dialogStage.showAndWait();
    }

    private void initialize(Stage stage) {
        classController = new ClassController();

        borderPane = new BorderPane();
        mainVbox = new VBox(20);
        topBar = new JoinClassNav();

        errorLbl = new Label();
        errorLbl.setStyle("-fx-text-fill: red;");

        userInfoBox = new ChangeAccountBox(dialogStage);
        userInfoBox.setAlignment(Pos.CENTER_LEFT);
        userInfoBox.getStyleClass().add("card");
        userInfoBox.setMaxWidth(800);

        classFormBox = new GroupCodeForm(errorLbl);
        classFormBox.getStyleClass().add("card");
        classFormBox.setMaxWidth(800);

        joinInfo = new VBox(5);
        joinInfo.setPadding(new Insets(20));

        Label joinInfoSub = new Label("To join using group class code:");

        VBox joinInfoList = new VBox(2);

        Label lbl1 = new Label("• Use an authorized account");
        Label lbl2 = new Label("• Use a maximum of 10 letters of group code.");

        joinInfoSub.setStyle("-fx-font-size: 17px;");
        lbl1.setStyle("-fx-font-size: 14px; -fx-font-family: 'Nunito Light'");
        lbl2.setStyle("-fx-font-size: 14px; -fx-font-family: 'Nunito Light'");

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

            if (message.equals("Class Joined!")) {
                dialogStage.close();
            }

            errorLbl.setText(message);

        });
    }
}
