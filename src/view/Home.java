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
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.LoggedUser;


public class Home {

    private LoggedUser loggedUser;

    private Scene scene;
    private BorderPane borderPane;

    private HBox navBar, homeSideNav, calenderSideNav;

    private HBox leftNav, rightNav;

    private Image iconImg, plusImg;
    private ImageView icon, plus, userImg;

    private Label title;


    private StackPane mainPane;


    private VBox sideBar;

    private HBox sideNavItem(String imagePath, String label) {
        HBox hbox = new HBox(30);

        Image image = new Image(imagePath);
        ImageView icon = new ImageView(image);

        icon.setFitWidth(17);
        icon.setPreserveRatio(true);

        Label lbl = new Label(label);

        hbox.getChildren().addAll(icon, lbl);
        hbox.setAlignment(Pos.CENTER_LEFT);
        return hbox;
    }


    private void initialize() {
        loggedUser = LoggedUser.getInstance();

        borderPane = new BorderPane();
        mainPane = new StackPane();

        Label test = new Label("Hello World");
        mainPane.getChildren().add(test);

        Label right = new Label("Ini Right");
        mainPane.getChildren().add(right);
        StackPane.setAlignment(right, Pos.TOP_RIGHT);

        navBar = new HBox();
        leftNav = new HBox(15);
        rightNav = new HBox(25);

        iconImg = new Image("file:resources/icons/calendar.png");
        icon = new ImageView(iconImg);
        icon.setFitWidth(40);
        icon.setFitHeight(40);
        icon.setPreserveRatio(false);

        plusImg = new Image("file:resources/icons/plus.png");
        plus = new ImageView(plusImg);
        plus.setFitWidth(20);
        plus.setFitHeight(20);
        plus.setPreserveRatio(true);

        if (loggedUser != null) {
            Image userImage = loggedUser.getProfileImage();
            if (userImage == null) {
                userImg = new ImageView(new Image("file:resources/icons/user.png"));
            } else {
                userImg = new ImageView(userImage);
            }
        } else {
            userImg = new ImageView(new Image("file:resources/icons/user.png"));
        }
        userImg.setFitWidth(40);
        userImg.setFitHeight(40);
        userImg.setPreserveRatio(true);

        title = new Label("DigiVerse");
        title.getStyleClass().add("title");

        leftNav.getChildren().addAll(icon, title);
        leftNav.setAlignment(Pos.CENTER_LEFT);

        rightNav.getChildren().addAll(plus, userImg);
        rightNav.setAlignment(Pos.CENTER_RIGHT);

        sideBar = new VBox(10);

        homeSideNav = sideNavItem("file:resources/icons/home.png", "Home");

        calenderSideNav = sideNavItem("file:resources/icons/calendar.png", "Calendar");
    }

    private Scene setLayout() {

        sideBar.getChildren().addAll(homeSideNav, calenderSideNav);
        sideBar.getStyleClass().add("side-nav");

        navBar.getChildren().addAll(leftNav, rightNav);
        navBar.getStyleClass().add("nav-bar");

        HBox.setHgrow(leftNav, Priority.ALWAYS);
        HBox.setHgrow(rightNav, Priority.NEVER);

        borderPane.setTop(navBar);
        borderPane.setLeft(sideBar);
        borderPane.setCenter(mainPane);

        scene = new Scene(borderPane, ScreenManager.SCREEN_WIDTH, ScreenManager.SCREEN_HEIGHT);
        ThemeManager.getTheme(scene);

        return scene;
    }

    private void actions() {

    }

    public Home(Stage stage) {

        initialize();

        scene = setLayout();

        actions();

        stage.setScene(scene);
        stage.setTitle("DigiVerse - Home");
    }

}
