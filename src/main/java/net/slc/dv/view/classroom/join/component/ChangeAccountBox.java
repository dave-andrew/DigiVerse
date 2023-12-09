package net.slc.dv.view.classroom.join.component;

import net.slc.dv.builder.ImageViewBuilder;
import net.slc.dv.resources.Icon;
import net.slc.dv.resources.Text;
import net.slc.dv.storage.IconStorage;
import net.slc.dv.controller.AuthController;
import net.slc.dv.helper.ImageManager;
import net.slc.dv.helper.StageManager;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.slc.dv.model.LoggedUser;
import net.slc.dv.storage.TextStorage;
import net.slc.dv.view.login.LoginView;

public class ChangeAccountBox extends VBox {

    private final Stage dialogStage;
    private AuthController authController;
    private Label userNameLbl;
    private Label userEmailLbl;
    private Button changeAccountBtn;

    public ChangeAccountBox(Stage dialogStage) {
        this.dialogStage = dialogStage;
        initialize();
        actions();

        this.setPrefWidth(800);
    }

    private void initialize() {
        LoggedUser loggedUser = LoggedUser.getInstance();
        authController = new AuthController();

        this.setSpacing(10);

        VBox userBox = new VBox();
        HBox userHbox = new HBox(10);

        Label userInfoLbl = new Label(TextStorage.getText(Text.LOGGED_AS) + ":");
        userInfoLbl.setStyle("-fx-font-size: 20px;");

        ImageView userImg = new ImageView();

        if (loggedUser != null) {
            userNameLbl = new Label(loggedUser.getUsername());
            userEmailLbl = new Label(loggedUser.getEmail());
            if (loggedUser.getProfile() != null) {
                ImageViewBuilder.modify(userImg)
                        .setImage(loggedUser.getProfile())
                        .setFitHeight(40)
                        .setFitWidth(40)
                        .build();
            }
            else {
                ImageViewBuilder.modify(userImg)
                        .bindImageProperty(IconStorage.getIcon(Icon.USER))
                        .setFitHeight(40)
                        .setFitWidth(40)
                        .build();
            }
        }

        ImageManager.makeCircular(userImg, 20);

        changeAccountBtn = new Button(TextStorage.getText(Text.CHANGE_ACCOUNT));
        changeAccountBtn.getStyleClass().add("secondary-button");

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        userBox.getChildren().addAll(userNameLbl, userEmailLbl);
        userHbox.getChildren().addAll(userImg, userBox, spacer, changeAccountBtn);
        userHbox.setAlignment(Pos.CENTER_LEFT);

        HBox titleBox = new HBox();
        titleBox.getChildren().add(userInfoLbl);
        titleBox.getStyleClass().add("bottom-border");

        this.getChildren().addAll(titleBox, userHbox);
    }

    private void actions() {
        changeAccountBtn.setOnAction(e -> {
            LoggedUser.getInstance().logout();

            this.authController.removeAuth();

            new LoginView(StageManager.getInstance());
            dialogStage.close();
        });
    }

}
