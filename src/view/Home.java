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
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.LoggedUser;
import view.component.ClassCard;
import view.component.GridClass;


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
    private ScrollPane scrollPane;


    private VBox sideBar;

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
    GridPane classGrid;

    private void fetchClass() {
        classGrid = new GridPane();
        StackPane sp = new ClassCard("Hello", "World");
        StackPane sp2 = new ClassCard("Hello", "World");
        StackPane sp3 = new ClassCard("Hello", "World");
        StackPane sp4 = new ClassCard("Hello", "World");
        StackPane sp5 = new ClassCard("Hello", "World");
        StackPane sp6 = new ClassCard("Hello", "World");

        classGrid.add(sp, 0, 1);
        classGrid.add(sp2, 0, 2);
        classGrid.add(sp3, 0, 3);
        classGrid.add(sp4, 0, 4);
        classGrid.add(sp5, 0, 5);
        classGrid.add(sp6, 0, 6);

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


//        Label test = new Label("Hello World");
//        mainPane.getChildren().add(test);

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
        plus.setOnMousePressed(e -> {
            new CreateClass(stage);
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
