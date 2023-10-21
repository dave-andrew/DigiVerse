package view;

import helper.ScreenManager;
import helper.ThemeManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.LoggedUser;
import view.component.JoinClassNav;

public class CreateClass {

    private LoggedUser loggedUser;

    private Scene scene;
    private BorderPane borderPane;

    private VBox mainVbox;
    private HBox topBar;

    private VBox userInfoBox, userBox;
    private HBox userHbox;
    private Label userInfoLbl, userNameLbl, userEmailLbl;
    private ImageView userImg;
    private Button changeAccountBtn;

    private void initialize(Stage stage) {
        loggedUser = LoggedUser.getInstance();

        borderPane = new BorderPane();
        mainVbox = new VBox(20);
        topBar = new JoinClassNav(stage);
        userInfoBox = new VBox(10);
        userBox = new VBox();

        userInfoLbl = new Label("Logged As:");

        Image image = new Image("file:resources/icons/user.png");
        userImg = new ImageView(image);
        userImg.setFitWidth(40);
        userImg.setPreserveRatio(true);

        if(loggedUser != null) {
            userNameLbl = new Label(loggedUser.getUsername());
            userEmailLbl = new Label(loggedUser.getEmail());
        }

        userBox.getChildren().addAll(userNameLbl, userEmailLbl);

        userHbox = new HBox(10);

        changeAccountBtn = new Button("Change");
        changeAccountBtn.getStyleClass().add("secondary-button");

        userHbox.getChildren().addAll(userImg, userBox, changeAccountBtn);
        userHbox.setAlignment(Pos.TOP_CENTER);

        userInfoBox.getChildren().addAll(userInfoLbl, userHbox);
        userInfoBox.setAlignment(Pos.TOP_CENTER);
        userInfoBox.getStyleClass().add("container");
    }

    private Scene setLayout() {
        mainVbox.setPadding(new Insets(20, 20, 20, 20));

        mainVbox.getChildren().add(userInfoBox);
        mainVbox.setAlignment(Pos.TOP_CENTER);

        borderPane.setTop(topBar);
        borderPane.setCenter(mainVbox);

        scene = new Scene(borderPane, ScreenManager.SCREEN_WIDTH, ScreenManager.SCREEN_HEIGHT);
        ThemeManager.getTheme(scene);
        return scene;
    }

    public CreateClass(Stage stage) {

        initialize(stage);

        scene = setLayout();

        stage.setScene(scene);
    }

}
