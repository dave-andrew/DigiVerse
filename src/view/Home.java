package view;

import controller.AuthController;
import controller.ClassController;
import helper.ImageManager;
import helper.ScreenManager;
import helper.StageManager;
import helper.ThemeManager;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Classroom;
import model.LoggedUser;
import view.homeview.Calendar;
import view.homeview.ClassroomList;

import java.util.ArrayList;
import java.util.Objects;


public class Home {

    private LoggedUser loggedUser;

    private AuthController authController;
    private ClassController classController;

    public static ArrayList<Classroom> teacherClassList = new ArrayList<>();
    public static ArrayList<Classroom> studentClassList = new ArrayList<>();

    private Scene scene;
    private BorderPane borderPane;
    private GridPane classGrid;

    private HBox navBar, homeSideNav, calenderSideNav, logOutBtn;

    private HBox leftNav, rightNav;

    private Image iconImg, plusImg;
    private ImageView icon, plus, userImg, leftArrow;
    private Button iconBtn;
    private Image logoutImage;
    private ImageView logoutIcon;

    private Label title;


    private StackPane mainPane, sp;
    private ScrollPane scrollPane;

    private Button plusBtn, userBtn, toggleSide;

    private VBox sideBar, sideBarSpacer;

    private ContextMenu plusMenu;
    private MenuItem createClass, joinClass;

    private HBox sideNavItem(String imagePath, String label) {
        HBox hbox = new HBox(10);

        Image image = new Image(imagePath);
        ImageView icon = new ImageView(image);

        icon.setFitWidth(25);
        icon.setPreserveRatio(true);

        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-size: 16px;");

        hbox.getChildren().addAll(icon, lbl);
        hbox.setAlignment(Pos.CENTER_LEFT);
        return hbox;
    }

    private void fetchClass() {

        mainPane.getChildren().clear();
        classGrid = new ClassroomList(mainPane, leftNav, iconBtn);
        scrollPane.setContent(classGrid);

        mainPane.getChildren().add(scrollPane);
    }

    private void initialize() {

        loggedUser = LoggedUser.getInstance();

        borderPane = new BorderPane();
        mainPane = new StackPane();
//        mainPane.prefWidthProperty().bind(borderPane.widthProperty().subtract(210));

        navBar = new HBox();
        leftNav = new HBox(15);
        rightNav = new HBox(25);

        scrollPane = new ScrollPane();
        mainPane.getChildren().add(scrollPane);

        iconImg = new Image("file:resources/icons/logo.png");
        icon = new ImageView(iconImg);
        iconBtn = new Button();
        iconBtn.setGraphic(icon);
        icon.setFitHeight(40);
        icon.setPreserveRatio(true);
        iconBtn.setStyle("-fx-cursor: hand;-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0;");

        classGrid = new ClassroomList(mainPane, leftNav, iconBtn);
        scrollPane.setContent(classGrid);

        plusImg = new Image("file:resources/icons/plus.png");
        plus = new ImageView(plusImg);
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


        if (loggedUser != null) {
            Image userImage = loggedUser.getProfile();
            userImg = new ImageView(Objects.requireNonNullElseGet(userImage, () -> new Image("file:resources/icons/user.png")));
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

        this.logoutImage = new Image("file:resources/icons/logout.png");
        this.logoutIcon = new ImageView(logoutImage);
        this.logoutIcon.setFitWidth(20);
        this.logoutIcon.setFitHeight(20);

        sideBar = new VBox();

        homeSideNav = sideNavItem("file:resources/icons/home.png", "Home");
        homeSideNav.getStyleClass().addAll("side-nav-item", "active");
        homeSideNav.setPrefWidth(250);

        calenderSideNav = sideNavItem("file:resources/icons/calendar.png", "Calendar");
        calenderSideNav.getStyleClass().add("side-nav-item");
        calenderSideNav.setPrefWidth(250);
    }

    private Scene setLayout() {
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        scrollPane.setPannable(true);

        sideBar.getChildren().addAll(homeSideNav, calenderSideNav);
        sideBar.getStyleClass().add("side-nav");

//        TODO: Add class with role as teacher and student

        this.sideBarSpacer = new VBox();
        VBox.setVgrow(sideBarSpacer, Priority.ALWAYS);

        sideBar.getChildren().add(sideBarSpacer);

        this.logOutBtn = new HBox();
        logOutBtn.getStyleClass().add("side-nav-item");
//        logOutBtn.setAlignment(Pos.CENTER);

        Label logOutLbl = new Label("Log Out");
        logOutLbl.setStyle("-fx-font-size: 16px;-fx-text-fill: #d70000;");

        HBox.setMargin(logOutLbl, new Insets(0, 0, 0, 10));

        logOutBtn.getChildren().addAll(logoutIcon, logOutLbl);

        logOutBtn.setOnMouseClicked(e -> {
            logout();
        });

        VBox.setMargin(logOutBtn, new Insets(0, 0, 40, 0));

        sideBar.getChildren().add(logOutBtn);

        navBar.getChildren().addAll(leftNav, rightNav);
        navBar.getStyleClass().add("nav-bar");

        HBox.setHgrow(leftNav, Priority.ALWAYS);
        HBox.setHgrow(rightNav, Priority.NEVER);

        this.sp = new StackPane();
        sp.getChildren().add(sideBar);

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

    private void actions(Stage stage) {

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

        homeSideNav.setOnMouseClicked(e -> {
            calenderSideNav.getStyleClass().remove("active");
            homeSideNav.getStyleClass().remove("active");
            homeSideNav.getStyleClass().add("active");
            mainPane.getChildren().clear();
            leftNav.getChildren().clear();
            leftNav.getChildren().add(iconBtn);
            fetchClass();
        });

        calenderSideNav.setOnMouseClicked(e -> {
            calenderSideNav.getStyleClass().remove("active");
            homeSideNav.getStyleClass().remove("active");
            calenderSideNav.getStyleClass().add("active");
            mainPane.getChildren().clear();
            leftNav.getChildren().clear();
            leftNav.getChildren().add(iconBtn);

            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

            scrollPane.prefWidthProperty().bind(mainPane.widthProperty().subtract(10));

            VBox calendar = new Calendar(mainPane, leftNav, iconBtn);
            calendar.setAlignment(Pos.TOP_CENTER);

            calendar.getStyleClass().add("card");
            calendar.setStyle("-fx-effect: null");

            calendar.setPadding(new Insets(0, 0, 200, 0));

//            VBox.setMargin(calendar, new Insets(50));

            scrollPane.setContent(calendar);
            scrollPane.fitToWidthProperty().set(true);
            scrollPane.setPadding(new Insets(20));

            mainPane.getChildren().add(scrollPane);
        });

        iconBtn.setOnMouseClicked(e -> {
            calenderSideNav.getStyleClass().remove("active");
            homeSideNav.getStyleClass().remove("active");
            homeSideNav.getStyleClass().add("active");
            mainPane.getChildren().clear();
            leftNav.getChildren().clear();
            leftNav.getChildren().add(iconBtn);
            fetchClass();
        });

        userBtn.setOnMouseClicked(e -> {
            calenderSideNav.getStyleClass().remove("active");
            homeSideNav.getStyleClass().remove("active");
            homeSideNav.getStyleClass().remove("active");
            mainPane.getChildren().clear();
            leftNav.getChildren().clear();
            leftNav.getChildren().add(iconBtn);

            profilePage();
        });

        toggleSide.setOnMouseClicked(e -> {

            if(toggleSideBar) {
                closeSidebar();
                sideBar.getChildren().remove(homeSideNav);
                sideBar.getChildren().remove(calenderSideNav);
                sideBar.getChildren().remove(sideBarSpacer);
                sideBar.getChildren().remove(logOutBtn);
            } else {
                openSidebar();
            }

            toggleSideBar = !toggleSideBar;
        });

    }

    private boolean toggleSideBar = true;

    void closeSidebar() {
        sp.setTranslateX(0);

        KeyValue keyValue = new KeyValue(sp.prefWidthProperty(), 0, Interpolator.EASE_BOTH);
        KeyValue rotateArrow = new KeyValue(leftArrow.rotateProperty(), 180, Interpolator.EASE_BOTH);

        KeyFrame start = new KeyFrame(Duration.ZERO,
                new KeyValue(sp.prefWidthProperty(), sp.getWidth(), Interpolator.LINEAR));

        KeyFrame end = new KeyFrame(Duration.seconds(0.5),
                keyValue,
                rotateArrow
        );

        Timeline timeline = new Timeline(start, end);
        timeline.play();
    }


    void openSidebar() {

        double currentTranslateX = sp.getTranslateX();

        KeyValue keyValue = new KeyValue(sp.prefWidthProperty(), 240, Interpolator.EASE_BOTH);
        KeyValue rotateArrow = new KeyValue(leftArrow.rotateProperty(), 0, Interpolator.EASE_BOTH);


        KeyFrame start = new KeyFrame(Duration.ZERO,
                new KeyValue(sp.translateXProperty(), currentTranslateX, Interpolator.LINEAR));

        KeyFrame end = new KeyFrame(Duration.seconds(0.5),
                keyValue,
                rotateArrow
        );

        Timeline timeline = new Timeline(start, end);
        timeline.play();

        timeline.onFinishedProperty().set(e -> {
            sideBar.getChildren().add(homeSideNav);
            sideBar.getChildren().add(calenderSideNav);
            sideBar.getChildren().add(sideBarSpacer);
            sideBar.getChildren().add(logOutBtn);
        });
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

    public void logout() {
        LoggedUser.getInstance().logout();

        this.authController.removeAuth();

        new LoginView(StageManager.getInstance());
    }

    public Home(Stage stage) {
        this.authController = new AuthController();
        this.classController = new ClassController();

        initialize();
        scene = setLayout();
        actions(stage);

        stage.setScene(scene);
        stage.setTitle("DigiVerse");
    }

}
