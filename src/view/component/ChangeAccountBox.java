package view.component;

import controller.AuthController;
import helper.ImageManager;
import helper.StageManager;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.LoggedUser;
import view.Login;

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

        Label userInfoLbl = new Label("Logged As:");
        userInfoLbl.setStyle("-fx-font-size: 20px;");

        Image image = new Image("file:resources/icons/user.png");
        ImageView userImg = new ImageView(image);
        userImg.setFitWidth(40);
        userImg.setFitHeight(40);

        if (loggedUser != null) {
            userNameLbl = new Label(loggedUser.getUsername());
            userEmailLbl = new Label(loggedUser.getEmail());
            if (loggedUser.getProfile() != null) {
                userImg.setImage(loggedUser.getProfile());
            }
        }

        ImageManager.makeCircular(userImg, 20);

        changeAccountBtn = new Button("Change Account");
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

            new Login(StageManager.getInstance());
            dialogStage.close();
        });
    }

}
