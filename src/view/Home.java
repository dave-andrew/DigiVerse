package view;

import helper.ScreenManager;
import helper.ThemeManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.LoggedUser;
import view.component.classroom.ClassCard;


public class Home {

    private LoggedUser loggedUser;

    private Scene scene;
    private BorderPane borderPane;
    private GridPane classGrid;

    private HBox navBar, homeSideNav, calenderSideNav;

    private HBox leftNav, rightNav;

    private Image iconImg, plusImg;
    private ImageView icon, plus, userImg;

    private Label title;


    private StackPane mainPane;
    private ScrollPane scrollPane;

    private Button plusBtn, userBtn;

    private VBox sideBar;

    private ContextMenu plusMenu;
    private MenuItem createClass, joinClass;

    private HBox sideNavItem(String imagePath, String label) {
        HBox hbox = new HBox(30);

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
        classGrid = new GridPane();
        StackPane sp = new ClassCard("Hello", "World");
        StackPane sp2 = new ClassCard("Hello", "World");
        StackPane sp3 = new ClassCard("Hello", "World");
        StackPane sp4 = new ClassCard("Hello", "World");
        StackPane sp5 = new ClassCard("Hello", "World");
        StackPane sp6 = new ClassCard("Hello", "World");

        classGrid.add(sp, 0, 0);
        classGrid.add(sp2, 1, 0);
        classGrid.add(sp3, 2, 0);
        classGrid.add(sp4, 3, 0);
        classGrid.add(sp5, 0, 1);
        classGrid.add(sp6, 1, 1);

        classGrid.setPadding(new Insets(20));
        classGrid.setHgap(20);
        classGrid.setVgap(20);

        scrollPane.setContent(classGrid);
    }

    private void initialize() {

        loggedUser = LoggedUser.getInstance();

        borderPane = new BorderPane();
        mainPane = new StackPane();
        scrollPane = new ScrollPane();
        mainPane.getChildren().add(scrollPane);

        fetchClass();

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

        plusMenu = new ContextMenu();

        createClass = new MenuItem("Create Class");
        joinClass = new MenuItem("Join Class");

        plusMenu.getItems().addAll(createClass, joinClass);


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

        plusBtn = new Button();
        plusBtn.setGraphic(plus);
        plusBtn.getStyleClass().add("image-button");

        userBtn = new Button();
        userBtn.setGraphic(userImg);
        userBtn.getStyleClass().add("image-button");

        title = new Label("DigiVerse");
        title.getStyleClass().add("title");

        leftNav.getChildren().addAll(icon, title);
        leftNav.setAlignment(Pos.CENTER_LEFT);

        rightNav.getChildren().addAll(plusBtn, userBtn);
        rightNav.setAlignment(Pos.CENTER_RIGHT);

        sideBar = new VBox(10);

        homeSideNav = sideNavItem("file:resources/icons/home.png", "Home");

        calenderSideNav = sideNavItem("file:resources/icons/calendar.png", "Calendar");
    }

    private Scene setLayout() {
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        scrollPane.setPannable(true);

        final double SPEED = 0.5;
        scrollPane.getContent().setOnScroll(scrollEvent -> {
            double deltaY = scrollEvent.getDeltaY() * SPEED;
            scrollPane.setVvalue(scrollPane.getVvalue() - deltaY);
        });


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

    private void actions(Stage stage) {

        plusBtn.setOnMouseClicked(e -> {
            plusMenu.show(plusBtn, e.getScreenX(), e.getScreenY());
        });

        createClass.setOnAction(e -> {
            new CreateClass(stage);
        });

        joinClass.setOnAction(e -> {
            new JoinClass(stage);
        });

    }

    public Home(Stage stage) {

        initialize();
        scene = setLayout();
        actions(stage);

        stage.setScene(scene);
        stage.setTitle("DigiVerse - Home");
    }

}
