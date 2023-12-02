package net.slc.dv.view.home;

import java.util.ArrayList;
import java.util.Objects;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.slc.dv.builder.*;
import net.slc.dv.helper.ImageManager;
import net.slc.dv.helper.ScreenManager;
import net.slc.dv.helper.ThemeManager;
import net.slc.dv.model.Classroom;
import net.slc.dv.model.LoggedUser;
import net.slc.dv.view.CreateClass;
import net.slc.dv.view.JoinClass;
import net.slc.dv.view.home.component.Calendar;
import net.slc.dv.view.home.component.Profile;
import net.slc.dv.view.home.component.sideNavbar.SideNavbar;
import net.slc.dv.view.homeview.ClassroomList;

public class Home {

    public static ArrayList<Classroom> teacherClassList = new ArrayList<>();
    public static ArrayList<Classroom> studentClassList = new ArrayList<>();
    private Scene scene;
    private BorderPane borderPane;
    private GridPane classGrid;

    private HBox navBar, logOutBtn;
    private HBox leftNav, rightNav;
    private ImageView userImg;
    private ImageView leftArrow;
    private Button iconBtn;

    private StackPane mainPane, sp;
    private ScrollPane scrollPane;

    private Button plusBtn, userBtn, toggleSide;
    private SideNavbar sideNavbar;

    private ContextMenu plusMenu;
    private MenuItem createClass, joinClass;
    private boolean toggleSideBar = true;

    public Home(Stage stage) {

        initialize();
        scene = setLayout();
        actions(stage);

        stage.setScene(scene);
        stage.setTitle("DigiVerse");
    }

    private void fetchClass() {

        mainPane.getChildren().clear();
        classGrid = new ClassroomList(mainPane, leftNav, iconBtn);
        scrollPane.setContent(classGrid);

        mainPane.getChildren().add(scrollPane);
    }

    private void initialize() { // semua komponen yang di ho

        borderPane = new BorderPane();
        mainPane = new StackPane();

        //        mainPane.prefWidthProperty().bind(borderPane.widthProperty().subtract(210));

        navBar = new HBox();
        leftNav = new HBox(15);
        rightNav = new HBox(25);

        scrollPane = new ScrollPane();
        mainPane.getChildren().add(scrollPane);

        ImageView icon = ImageViewBuilder.create()
                .setImage(new Image("file:resources/icons/logo.png"))
                .setFitHeight(40)
                .setPreserveRatio(true)
                .build();

        iconBtn = ButtonBuilder.create()
                .setGraphic(icon)
                .setStyle(
                        "-fx-cursor: hand;-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0;")
                .build();

        classGrid = new ClassroomList(mainPane, leftNav, iconBtn);
        scrollPane.setContent(classGrid);

        Image plusImg = new Image("file:resources/icons/plus.png");
        ImageView plus = new ImageView(plusImg);
        plus.setFitWidth(20);
        plus.setFitHeight(20);
        plus.setPreserveRatio(true);

        plusMenu = new ContextMenu();
        plusMenu.getStyleClass().add("context-menu");

        createClass = new MenuItem("Create Class");
        createClass.getStyleClass().add("item");
        joinClass = new MenuItem("Join Class");
        joinClass.getStyleClass().add("item");

        plusMenu.getItems().addAll(createClass, joinClass);

        LoggedUser loggedUser = LoggedUser.getInstance();
        if (loggedUser != null) {
            Image userImage = loggedUser.getProfile();
            userImg = new ImageView(
                    Objects.requireNonNullElseGet(userImage, () -> new Image("file:resources/icons/user.png")));
        } else {
            userImg = new ImageView(new Image("file:resources/icons/user.png"));
        }
        userImg.setFitWidth(40);
        userImg.setFitHeight(40);
        userImg.setPreserveRatio(true);

        plusBtn = new Button();
        plusBtn.setGraphic(plus);
        plusBtn.getStyleClass().add("image-button");

        ImageManager.makeCircular(userImg, 20);

        userBtn = new Button();
        userBtn.setGraphic(userImg);
        userBtn.getStyleClass().add("image-button");

        leftNav.getChildren().add(iconBtn);
        leftNav.setAlignment(Pos.CENTER_LEFT);

        ToggleButton themeSwitchButton = new ToggleButton();
        themeSwitchButton.setOnAction(e -> {
            ThemeManager.toggleTheme(scene, themeSwitchButton);
        });

        ImageView sun = new ImageView(new Image("file:resources/icons/sun.png"));
        themeSwitchButton.setGraphic(sun);

        sun.setFitWidth(30);
        sun.setFitHeight(30);

        themeSwitchButton.getStyleClass().add("image-button");

        rightNav.getChildren().addAll(themeSwitchButton, plusBtn, userBtn);
        rightNav.setAlignment(Pos.CENTER_RIGHT);

        sideNavbar = new SideNavbar();
    }

    private Scene setLayout() { // masuk2in nya
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        scrollPane.setPannable(true);

        //        TODO: Add class with role as teacher and student

        navBar.getChildren().addAll(leftNav, rightNav);
        navBar.getStyleClass().add("nav-bar");

        HBox.setHgrow(leftNav, Priority.ALWAYS);
        HBox.setHgrow(rightNav, Priority.NEVER);

        this.sp = new StackPane();
        sp.getChildren().add(sideNavbar);

        Image image = new Image("file:resources/icons/left-arrow.png");
        leftArrow = new ImageView(image);

        leftArrow.setFitWidth(25);
        leftArrow.setFitHeight(25);

        this.toggleSide = new Button();
        toggleSide.setGraphic(leftArrow);
        toggleSide.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0;");

        sp.getChildren().add(toggleSide);

        sp.setAlignment(Pos.CENTER_RIGHT);

        borderPane.setCenter(mainPane);
        borderPane.setLeft(sp);
        borderPane.setTop(navBar);

        //        borderPane.getStyleClass().add("bg-secondary");

        scene = new Scene(borderPane, ScreenManager.SCREEN_WIDTH, ScreenManager.SCREEN_HEIGHT);
        ThemeManager.getTheme(scene);

        return scene;
    }

    private void actions(Stage stage) { // event nya

        plusBtn.setOnMouseClicked(e -> {
            plusMenu.show(plusBtn, e.getScreenX() - 150, e.getScreenY());
        });

        createClass.setOnAction(e -> {
            new CreateClass(stage);
            fetchClass();
        });

        joinClass.setOnAction(e -> {
            new JoinClass(stage);
            fetchClass();
        });

        this.sideNavbar.getHomeButton().setOnMouseClicked(e -> {
            this.sideNavbar.setActive(this.sideNavbar.getHomeButton());
            mainPane.getChildren().clear();
            leftNav.getChildren().clear();
            leftNav.getChildren().add(iconBtn);
            fetchClass();
        });
        //
        this.sideNavbar.getCalendarButton().setOnMouseClicked(e -> {
            this.sideNavbar.setActive(this.sideNavbar.getCalendarButton());
            mainPane.getChildren().clear();
            leftNav.getChildren().clear();
            leftNav.getChildren().add(iconBtn);

            mainPane.getChildren().add(new Calendar(mainPane, leftNav, iconBtn));
        });
        //
        //        iconBtn.setOnMouseClicked(e -> {
        //            calenderSideNav.getStyleClass().remove("active");
        //            homeSideNav.getStyleClass().remove("active");
        //            homeSideNav.getStyleClass().add("active");
        //            mainPane.getChildren().clear();
        //            leftNav.getChildren().clear();
        //            leftNav.getChildren().add(iconBtn);
        //            fetchClass();
        //        });
        //
        //        userBtn.setOnMouseClicked(e -> {
        //            calenderSideNav.getStyleClass().remove("active");
        //            homeSideNav.getStyleClass().remove("active");
        //            homeSideNav.getStyleClass().remove("active");
        //            mainPane.getChildren().clear();
        //            leftNav.getChildren().clear();
        //            leftNav.getChildren().add(iconBtn);
        //
        //            profilePage();
        //        });
        //
        //        toggleSide.setOnMouseClicked(e -> {
        //            if (toggleSideBar) {
        //                closeSidebar();
        //                sideBar.getChildren().remove(homeSideNav);
        //                sideBar.getChildren().remove(calenderSideNav);
        //                sideBar.getChildren().remove(sideBarSpacer);
        //                sideBar.getChildren().remove(logOutBtn);
        //            } else {
        //                openSidebar();
        //            }
        //
        //            toggleSideBar = !toggleSideBar;
        //        });
    }

    void closeSidebar() { //
        sp.setTranslateX(0);

        KeyValue keyValue = new KeyValue(sp.prefWidthProperty(), 0, Interpolator.EASE_BOTH);
        KeyValue rotateArrow = new KeyValue(leftArrow.rotateProperty(), 180, Interpolator.EASE_BOTH);

        KeyFrame start =
                new KeyFrame(Duration.ZERO, new KeyValue(sp.prefWidthProperty(), sp.getWidth(), Interpolator.LINEAR));

        KeyFrame end = new KeyFrame(Duration.seconds(0.5), keyValue, rotateArrow);

        Timeline timeline = new Timeline(start, end);
        timeline.play();
    }

    void openSidebar() {

        double currentTranslateX = sp.getTranslateX();

        KeyValue keyValue = new KeyValue(sp.prefWidthProperty(), 240, Interpolator.EASE_BOTH);
        KeyValue rotateArrow = new KeyValue(leftArrow.rotateProperty(), 0, Interpolator.EASE_BOTH);

        KeyFrame start = new KeyFrame(
                Duration.ZERO, new KeyValue(sp.translateXProperty(), currentTranslateX, Interpolator.LINEAR));

        KeyFrame end = new KeyFrame(Duration.seconds(0.5), keyValue, rotateArrow);

        Timeline timeline = new Timeline(start, end);
        timeline.play();

        //        timeline.onFinishedProperty().set(e -> {
        //            sideBar.getChildren().add(homeSideNav);
        //            sideBar.getChildren().add(calenderSideNav);
        //            sideBar.getChildren().add(sideBarSpacer);
        //            sideBar.getChildren().add(logOutBtn);
        //        });
    }

    public void profilePage() {
        VBox profile = new Profile(userImg, leftNav, iconBtn, mainPane);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(profile);

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        scrollPane.prefWidthProperty().bind(mainPane.widthProperty());
        profile.prefWidthProperty().bind(scrollPane.widthProperty());

        mainPane.getChildren().add(scrollPane);
    }
}
