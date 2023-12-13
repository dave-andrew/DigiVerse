package net.slc.dv.view.login;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Getter;
import net.slc.dv.builder.HBoxBuilder;
import net.slc.dv.builder.VBoxBuilder;
import net.slc.dv.controller.AuthController;
import net.slc.dv.helper.ScreenManager;
import net.slc.dv.helper.toast.ToastBuilder;
import net.slc.dv.model.LoggedUser;
import net.slc.dv.resources.Icon;
import net.slc.dv.resources.Text;
import net.slc.dv.storage.IconStorage;
import net.slc.dv.storage.ImageStorage;
import net.slc.dv.storage.TextStorage;
import net.slc.dv.view.home.Home;
import net.slc.dv.view.register.RegisterView;

public class LoginView {

    private final AuthController authController;
    private final VBox registerVBox;
    private Scene scene;
    private Pane root;
    private BorderPane borderPane;
    private Label subTitle;
    private Label emailLbl, passwordLbl;
    private TextField emailTxt;
    private PasswordField passwordTxt;
    private Button loginBtn, registerLink;
    private ImageView loginImage;

    @Getter
    private static VBox outerContainer;

    private VBox vbox;

    private VBox emailVbox, passwordVbox, loginVbox, rememberMeVbox;
    private CheckBox rememberMe;
    private Label errorLbl;
    private int bannerCounter = 0;

    private ImageView forbidden;

    public LoginView(Stage stage) {
        this.authController = new AuthController();

        initialize();

        this.registerVBox = new RegisterView(stage, this.vbox, borderPane);
        this.registerVBox.getStyleClass().add("card");

        setLayout();
        actions(stage);
        stage.setScene(scene);
    }

    private void initialize() {
        root = new Pane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #0000a1, #1f90ff);");

        Circle backerPuff1 = createBackerPuffs(ScreenManager.SCREEN_WIDTH / 2, 90, 100);
        Circle backerPuff2 = createBackerPuffs(ScreenManager.SCREEN_WIDTH / 2 + 15, 100, 70);
        Circle backerPuff3 = createBackerPuffs(ScreenManager.SCREEN_WIDTH / 2 + 10, 180, 40);
        Circle backerPuff4 = createBackerPuffs(ScreenManager.SCREEN_WIDTH / 2 + 40, 250, 60);
        Circle backerPuff5 = createBackerPuffs(ScreenManager.SCREEN_WIDTH / 2 + 30, 320, 30);
        Circle backerPuff6 = createBackerPuffs(ScreenManager.SCREEN_WIDTH / 2, 370, 50);
        Circle backerPuff7 = createBackerPuffs(ScreenManager.SCREEN_WIDTH / 2, 430, 20);
        Circle backerPuff8 = createBackerPuffs(ScreenManager.SCREEN_WIDTH / 2 + 20, 460, 40);
        Circle backerPuff9 = createBackerPuffs(ScreenManager.SCREEN_WIDTH / 2 + 30, 520, 60);
        Circle backerPuff10 = createBackerPuffs(ScreenManager.SCREEN_WIDTH / 2 + 60, 640, 80);
        Circle backerPuff11 = createBackerPuffs(ScreenManager.SCREEN_WIDTH / 2 + 20, 720, 40);
        Circle backerPuff12 = createBackerPuffs(ScreenManager.SCREEN_WIDTH / 2 + 20, 780, 60);
        Circle backerPuff13 = createBackerPuffs(ScreenManager.SCREEN_WIDTH / 2 + 30, 850, 90);

        root.getChildren()
                .addAll(
                        backerPuff1,
                        backerPuff2,
                        backerPuff3,
                        backerPuff4,
                        backerPuff5,
                        backerPuff6,
                        backerPuff7,
                        backerPuff8,
                        backerPuff9,
                        backerPuff10,
                        backerPuff11,
                        backerPuff12,
                        backerPuff13);

        Circle backPuff1 = createBackPuffs(ScreenManager.SCREEN_WIDTH / 2 - 40, 0, 100);
        Circle backPuff2 = createBackPuffs(ScreenManager.SCREEN_WIDTH / 2 + 25, 100, 70);
        Circle backPuff3 = createBackPuffs(ScreenManager.SCREEN_WIDTH / 2 + 40, 180, 40);
        Circle backPuff4 = createBackPuffs(ScreenManager.SCREEN_WIDTH / 2 + 60, 250, 60);
        Circle backPuff5 = createBackPuffs(ScreenManager.SCREEN_WIDTH / 2 + 50, 320, 30);
        Circle backPuff6 = createBackPuffs(ScreenManager.SCREEN_WIDTH / 2 + 40, 370, 50);
        Circle backPuff7 = createBackPuffs(ScreenManager.SCREEN_WIDTH / 2 + 30, 430, 20);
        Circle backPuff8 = createBackPuffs(ScreenManager.SCREEN_WIDTH / 2 + 20, 460, 40);
        Circle backPuff9 = createBackPuffs(ScreenManager.SCREEN_WIDTH / 2 + 70, 520, 60);
        Circle backPuff10 = createBackPuffs(ScreenManager.SCREEN_WIDTH / 2 + 60, 640, 80);
        Circle backPuff11 = createBackPuffs(ScreenManager.SCREEN_WIDTH / 2 + 60, 720, 40);
        Circle backPuff12 = createBackPuffs(ScreenManager.SCREEN_WIDTH / 2 + 50, 780, 60);
        Circle backPuff13 = createBackPuffs(ScreenManager.SCREEN_WIDTH / 2 + 40, 850, 90);

        root.getChildren()
                .addAll(
                        backPuff1,
                        backPuff2,
                        backPuff3,
                        backPuff4,
                        backPuff5,
                        backPuff6,
                        backPuff7,
                        backPuff8,
                        backPuff9,
                        backPuff10,
                        backPuff11,
                        backPuff12,
                        backPuff13);

        Circle puff1 = createPuffs(ScreenManager.SCREEN_WIDTH / 2, 0, 100);
        Circle puff2 = createPuffs(ScreenManager.SCREEN_WIDTH / 2 + 50, 100, 70);
        Circle puff3 = createPuffs(ScreenManager.SCREEN_WIDTH / 2 + 50, 180, 40);
        Circle puff4 = createPuffs(ScreenManager.SCREEN_WIDTH / 2 + 80, 250, 60);
        Circle puff5 = createPuffs(ScreenManager.SCREEN_WIDTH / 2 + 90, 320, 30);
        Circle puff6 = createPuffs(ScreenManager.SCREEN_WIDTH / 2 + 50, 370, 50);
        Circle puff7 = createPuffs(ScreenManager.SCREEN_WIDTH / 2 + 60, 430, 20);
        Circle puff8 = createPuffs(ScreenManager.SCREEN_WIDTH / 2 + 40, 460, 40);
        Circle puff9 = createPuffs(ScreenManager.SCREEN_WIDTH / 2 + 80, 520, 60);
        Circle puff10 = createPuffs(ScreenManager.SCREEN_WIDTH / 2 + 100, 640, 80);
        Circle puff11 = createPuffs(ScreenManager.SCREEN_WIDTH / 2 + 90, 720, 40);
        Circle puff12 = createPuffs(ScreenManager.SCREEN_WIDTH / 2 + 80, 780, 60);
        Circle puff13 = createPuffs(ScreenManager.SCREEN_WIDTH / 2 + 70, 850, 90);

        root.getChildren()
                .addAll(puff1, puff2, puff3, puff4, puff5, puff6, puff7, puff8, puff9, puff10, puff11, puff12, puff13);

        borderPane = new BorderPane();
        borderPane.setPrefSize(ScreenManager.SCREEN_WIDTH / 2, ScreenManager.SCREEN_HEIGHT);
        borderPane.setStyle("-fx-background-color: #f6f6f6;");
        borderPane.setTranslateX(ScreenManager.SCREEN_WIDTH / 2 + 70);

        root.getChildren().add(borderPane);

        loginForm();

        loginBanner();

        scene = new Scene(root, ScreenManager.SCREEN_WIDTH, ScreenManager.SCREEN_HEIGHT);
    }

    private void loginBanner() {
        VBox loginBannerContainer = new VBox();
        loginBannerContainer.setAlignment(Pos.CENTER);
        loginBannerContainer.setPrefWidth(ScreenManager.SCREEN_WIDTH / 2);
        loginBannerContainer.prefHeightProperty().bind(borderPane.heightProperty());

        //        Label welcomeToLabel = new Label("Welcome to");
        //        welcomeToLabel.setStyle("-fx-text-fill: white;-fx-font-size: 25px;");
        //        welcomeToLabel.setPadding(new Insets(0, 0, 100, 0));

        ImageView appIconView = new ImageView(IconStorage.getIcon(Icon.LOGO).getValue());
        appIconView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 0);");

        appIconView.setFitHeight(100);
        appIconView.setPreserveRatio(true);

        Button appIconButton = new Button();
        appIconButton.setGraphic(appIconView);
        appIconButton.setStyle("-fx-background-color: transparent;");

        Label appDescription = new Label(TextStorage.getText(Text.CANDLE_SHINE_WHEN_BURNED));
        appDescription.setMaxWidth(400);
        appDescription.setWrapText(true);
        appDescription.setAlignment(Pos.CENTER);
        appDescription.setTextAlignment(TextAlignment.CENTER);
        appDescription.setStyle("-fx-text-fill: white;-fx-font-size: 24px");
        appDescription.setPadding(new Insets(150, 0, 0, 0));

        loginBannerContainer.getChildren().addAll(appIconButton, appDescription);

        root.getChildren().add(loginBannerContainer);
    }

    private void loginForm() {
        subTitle = new Label(TextStorage.getText(Text.HOP_IN_AND_LETS_INITIATE_GREATNESS));

        registerLink = new Button(TextStorage.getText(Text.DONT_HAVE_AN_ACCOUNT));
        registerLink.getStyleClass().add("link-button");
        registerLink.setStyle("-fx-font-size: 14px");

        registerLink.setOnMouseEntered(e -> registerLink.setStyle("-fx-font-size: 14px; -fx-text-fill: #1f90ff;"));

        registerLink.setOnMouseExited(e -> registerLink.setStyle("-fx-font-size: 14px; -fx-text-fill: black;"));

        loginBtn = new Button(TextStorage.getText(Text.START_JOURNEY));
        loginBtn.getStyleClass().add("primary-button");
        loginBtn.setStyle("-fx-text-fill: white;");
        loginBtn.setPrefWidth(350);
        loginBtn.setPrefHeight(40);

        loginBtn.setOnMouseEntered(e -> {
            loginBtn.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.5), 5, 0, 0, 0);-fx-text-fill: "
                    + "white;");

            Timeline timeline = new Timeline();

            KeyValue kv = new KeyValue(loginBtn.scaleXProperty(), 1.1);
            KeyValue kv2 = new KeyValue(loginBtn.scaleYProperty(), 1.1);

            KeyFrame kf = new KeyFrame(Duration.millis(100), kv, kv2);

            timeline.getKeyFrames().add(kf);
            timeline.play();
        });

        loginBtn.setOnMouseExited(e -> {
            loginBtn.setStyle("-fx-effect: null;-fx-text-fill: white;");

            Timeline timeline = new Timeline();

            KeyValue kv = new KeyValue(loginBtn.scaleXProperty(), 1);
            KeyValue kv2 = new KeyValue(loginBtn.scaleYProperty(), 1);

            KeyFrame kf = new KeyFrame(Duration.millis(100), kv, kv2);

            timeline.getKeyFrames().add(kf);
            timeline.play();
        });

        emailVbox = new VBox();
        passwordVbox = new VBox();
        loginVbox = new VBox();
        rememberMeVbox = new VBox();
        rememberMeVbox.setStyle("-fx-cursor: hand;");

        emailLbl = new Label(TextStorage.getText(Text.EMAIL) + ":");
        passwordLbl = new Label(TextStorage.getText(Text.PASSWORD) + ":");

        emailTxt = new TextField();
        passwordTxt = new PasswordField();

        rememberMe = new CheckBox(TextStorage.getText(Text.REMEMBER_ME));
        rememberMe.setScaleX(0.85);
        rememberMe.setScaleY(0.85);

        Image image = new Image(ImageStorage.AUTH_IMAGE_TWO);

        loginImage = new ImageView(image);

        vbox = new VBox(10);

        errorLbl = new Label();
    }

    private Circle createPuffs(double x, double y, double radius) {
        Circle puff = new Circle(x, y, radius);
        puff.setStyle("-fx-fill: #f6f6f6;");
        return puff;
    }

    private Circle createBackPuffs(double x, double y, double radius) {
        Circle puff = new Circle(x, y, radius);
        puff.setStyle("-fx-fill: #33cfff;");
        return puff;
    }

    private Circle createBackerPuffs(double x, double y, double radius) {
        Circle puff = new Circle(x, y, radius);
        puff.setStyle("-fx-fill: #006fff;");
        return puff;
    }

    private void setLayout() {
        Font font = Font.loadFont("file:resources/fonts/VT323-Regular.ttf", 36);
        subTitle.setFont(font);
        subTitle.setStyle("-fx-text-fill: black;");
        subTitle.setWrapText(true);
        subTitle.setTextAlignment(TextAlignment.CENTER);

        errorLbl.setStyle("-fx-text-fill: red;");

        emailVbox.getChildren().addAll(emailLbl, emailTxt);
        passwordVbox.getChildren().addAll(passwordLbl, passwordTxt);
        loginVbox.getChildren().addAll(loginBtn, registerLink);
        loginVbox.setAlignment(Pos.CENTER);

        emailVbox.setPrefWidth(350);
        passwordVbox.setPrefWidth(350);

        rememberMeVbox.getChildren().add(rememberMe);
        rememberMeVbox.setAlignment(Pos.CENTER_LEFT);
        rememberMeVbox.setPrefWidth(350);

        rememberMeVbox.setPadding(new Insets(30, 0, 0, 0));

        loginVbox.setPadding(new Insets(0, 0, 10, 0));

        HBox emailHBox = HBoxBuilder.create()
                .addChildren(emailVbox)
                .setAlignment(Pos.CENTER)
                .build();

        HBox passwordHBox = HBoxBuilder.create()
                .addChildren(passwordVbox)
                .setAlignment(Pos.CENTER)
                .build();

        HBox rememberMeHBox = HBoxBuilder.create()
                .addChildren(rememberMeVbox)
                .setAlignment(Pos.CENTER)
                .build();

        vbox.getChildren().addAll(subTitle, emailHBox, passwordHBox, rememberMeHBox, loginVbox, errorLbl);
        vbox.setAlignment(Pos.CENTER);

        vbox.getStyleClass().add("card");

        vbox.setMaxWidth(600);
        vbox.setPadding(new Insets(0, 0, 0, 100));

        outerContainer = new VBox();
        outerContainer.getChildren().addAll(vbox);
        outerContainer.setAlignment(Pos.CENTER);
        outerContainer.setPadding(new Insets(0, 50, 0, 0));

        borderPane.getChildren().clear();

        borderPane.setCenter(outerContainer);

        loginImage.setFitHeight(ScreenManager.SCREEN_HEIGHT);

        scene.getStylesheets().add("file:resources/light_theme.css");
        //        ThemeManager.getTheme(scene);
    }

    private void actions(Stage stage) {
        loginBtn.setOnAction(e -> {
            loginLogic(stage);
        });

        passwordTxt.setOnKeyPressed(e -> {
            if (e.getCode().toString().equals("ENTER")) {
                loginLogic(stage);
            }
        });

        registerLink.setOnAction(e -> {
            VBoxBuilder.modify(outerContainer)
                    .removeAll()
                    .addChildren(registerVBox)
                    .build();

            borderPane.getChildren().clear();
            borderPane.setCenter(outerContainer);
        });
    }

    private void loginLogic(Stage stage) {
        String email = emailTxt.getText();
        String password = passwordTxt.getText();
        boolean remember = rememberMe.isSelected();

        String message = authController.checkLogin(email, password, remember);

        if (message.equals(TextStorage.getText(Text.LOGIN_SUCCESS))) {
            errorLbl.setStyle("-fx-text-fill: green;");
            ToastBuilder.buildNormal()
                    .setText(TextStorage.getText(Text.WELCOME_BACK) + ", "
                            + LoggedUser.getInstance().getUsername() + "!")
                    .build();
            new Home(stage);
        }

        errorLbl.setText(message);
    }
}
