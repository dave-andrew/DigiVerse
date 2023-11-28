package view;

import controller.AuthController;
import helper.ScreenManager;
import helper.ThemeManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Login {

    private final AuthController authController = new AuthController();

    private Scene scene;
    private BorderPane borderPane;
    private Label subTitle;
    private Label emailLbl, passwordLbl;
    private TextField emailTxt;
    private PasswordField passwordTxt;
    private Button loginBtn, registerLink;

    private ImageView loginImage;

    private VBox vbox;

    private VBox emailVbox, passwordVbox, loginVbox, rememberMeVbox;

    private CheckBox rememberMe;

    private Label errorLbl;

    public Login(Stage stage) {
        initialize();

        scene = setLayout();

        actions(stage);

        stage.setScene(scene);
        stage.setTitle("DigiVerse - Login");
    }

    private void initialize() {
        borderPane = new BorderPane();

        subTitle = new Label("Hop in and let's initiate greatness!");

        registerLink = new Button("Don't have an account? Register here!");
        registerLink.getStyleClass().add("link-button");

        loginBtn = new Button("Start Journey");
        loginBtn.getStyleClass().add("primary-button");

        emailVbox = new VBox();
        passwordVbox = new VBox();
        loginVbox = new VBox();
        rememberMeVbox = new VBox();

        emailLbl = new Label("Email:");
        passwordLbl = new Label("Password:");

        emailTxt = new TextField();
        passwordTxt = new PasswordField();

        rememberMe = new CheckBox("Remember me");
        rememberMe.setScaleX(0.6);
        rememberMe.setScaleY(0.6);

        Image image = new Image("file:resources/image/auth_image2.png");

        loginImage = new ImageView(image);

        vbox = new VBox(10);

        errorLbl = new Label();
    }

    private Scene setLayout() {

        Font font = Font.loadFont("file:resources/fonts/LindenHill-Italic.ttf", 40);
        subTitle.setFont(font);

        errorLbl.setStyle("-fx-text-fill: red;");

        emailVbox.getChildren().addAll(emailLbl, emailTxt);
        passwordVbox.getChildren().addAll(passwordLbl, passwordTxt);
        loginVbox.getChildren().addAll(loginBtn, registerLink);
        loginVbox.setAlignment(Pos.CENTER);

        rememberMeVbox.getChildren().add(rememberMe);
        rememberMeVbox.setAlignment(Pos.CENTER);

        rememberMeVbox.setPadding(new Insets(30, 0, 0, 0));

        loginVbox.setPadding(new Insets(0, 0, 10, 0));

        vbox.getChildren().addAll(subTitle, emailVbox, passwordVbox, rememberMeVbox, loginVbox, errorLbl);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(-130, 100, 0, 0));
        borderPane.setRight(vbox);

        StackPane imagePane = new StackPane();
        imagePane.setPrefSize(400, ScreenManager.SCREEN_HEIGHT);

        loginImage.setFitWidth(450);
        loginImage.setFitHeight(ScreenManager.SCREEN_HEIGHT);

        imagePane.getChildren().add(loginImage);

        borderPane.setLeft(imagePane);

        scene = new Scene(borderPane, ScreenManager.SCREEN_WIDTH, ScreenManager.SCREEN_HEIGHT);
        ThemeManager.getTheme(scene);

        return scene;
    }

    private void actions(Stage stage) {
        loginBtn.setOnAction(e -> {
            String email = emailTxt.getText();
            String password = passwordTxt.getText();
            boolean remember = rememberMe.isSelected();

            String message = authController.checkLogin(email, password, remember);

            if (message.equals("Login Success!")) {
                errorLbl.setStyle("-fx-text-fill: green;");
                new Home(stage);
            }

            errorLbl.setText(message);
        });

        registerLink.setOnAction(e -> {
            new Register(stage);
        });
    }
}
