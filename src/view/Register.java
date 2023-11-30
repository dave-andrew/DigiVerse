package view;

import controller.AuthController;
import helper.ScreenManager;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Register extends VBox {

    private final AuthController authController = new AuthController();
    private final VBox loginVBox;
    private final BorderPane root;
    private Label subTitle;
    private Label nameLbl, emailLbl, passwordLbl, confirmPasswordLbl, agelbl;
    private TextField nameTxt, emailTxt;
    private PasswordField passwordTxt, confirmPasswordTxt;
    private DatePicker dobPicker;
    private Button registerBtn;
    private ImageView registerImage;
    private VBox vbox;
    private VBox nameVbox, emailVbox, passwordVbox, confirmPasswordVbox, ageVbox, registerVbox;
    private Button loginLink;
    private Label errorLbl;

    public Register(Stage stage, VBox vbox, BorderPane borderPane) {
        this.loginVBox = vbox;
        this.root = borderPane;
        initialize();
        setLayout();
        actions(stage);

        stage.setTitle("DigiVerse - Register");
    }

    private void initialize() {
        subTitle = new Label("Be the change, sign in to make it happen!");
        nameLbl = new Label("Name:");
        emailLbl = new Label("Email:");
        passwordLbl = new Label("Password:");
        confirmPasswordLbl = new Label("Confirm Password:");
        agelbl = new Label("Age:");
        errorLbl = new Label();

        nameTxt = new TextField();
        emailTxt = new TextField();
        passwordTxt = new PasswordField();
        confirmPasswordTxt = new PasswordField();

        dobPicker = new DatePicker();

        nameVbox = new VBox();
        emailVbox = new VBox();
        passwordVbox = new VBox();
        confirmPasswordVbox = new VBox();
        ageVbox = new VBox();
        registerVbox = new VBox();

        registerBtn = new Button("Register");
        registerBtn.getStyleClass().add("primary-button");
        registerBtn.setStyle("-fx-text-fill: white;");

        registerBtn.setPrefWidth(260);
        registerBtn.setPrefHeight(40);

        registerBtn.setOnMouseEntered(e -> {
            registerBtn.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.5), 5, 0, 0, 0);-fx-text-fill: white;");

            Timeline timeline = new Timeline();

            KeyValue kv = new KeyValue(registerBtn.scaleXProperty(), 1.1);
            KeyValue kv2 = new KeyValue(registerBtn.scaleYProperty(), 1.1);

            KeyFrame kf = new KeyFrame(Duration.millis(100), kv, kv2);

            timeline.getKeyFrames().add(kf);
            timeline.play();
        });

        registerBtn.setOnMouseExited(e -> {
            registerBtn.setStyle("-fx-effect: null;-fx-text-fill: white;");

            Timeline timeline = new Timeline();

            KeyValue kv = new KeyValue(registerBtn.scaleXProperty(), 1);
            KeyValue kv2 = new KeyValue(registerBtn.scaleYProperty(), 1);

            KeyFrame kf = new KeyFrame(Duration.millis(100), kv, kv2);

            timeline.getKeyFrames().add(kf);
            timeline.play();
        });

        loginLink = new Button("Already have an account? Login here!");
        loginLink.getStyleClass().add("link-button");
        loginLink.setStyle("-fx-font-size: 14px");

        loginLink.setOnMouseEntered(e -> {
            loginLink.setStyle("-fx-font-size: 14px;-fx-text-fill: #1E90FF;");
        });

        loginLink.setOnMouseExited(e -> {
            loginLink.setStyle("-fx-font-size: 14px;-fx-text-fill: #000000;");
        });

        Image image = new Image("file:resources/image/auth_image.png");
        registerImage = new ImageView(image);

        vbox = new VBox(10);
    }

    private void setLayout() {

        Font font = Font.loadFont("file:resources/fonts/VT323-Regular.ttf", 30);
        subTitle.setFont(font);

        errorLbl.setStyle("-fx-text-fill: red;-fx-font-weight: bold;");

        nameVbox.getChildren().addAll(nameLbl, nameTxt);
        emailVbox.getChildren().addAll(emailLbl, emailTxt);
        passwordVbox.getChildren().addAll(passwordLbl, passwordTxt);
        confirmPasswordVbox.getChildren().addAll(confirmPasswordLbl, confirmPasswordTxt);
        ageVbox.getChildren().addAll(agelbl, dobPicker);
        registerVbox.getChildren().addAll(registerBtn, loginLink);
        registerVbox.setAlignment(Pos.CENTER);
        registerVbox.setPadding(new Insets(30, 0, 0, 0));

        VBox.setMargin(registerBtn, new Insets(0, 0, 10, 0));

        vbox.getChildren().addAll(subTitle, nameVbox, emailVbox, passwordVbox, confirmPasswordVbox, ageVbox, registerVbox, errorLbl);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(0, 0, 0, 50));

        StackPane imagePane = new StackPane();
        imagePane.setPrefSize(400, ScreenManager.SCREEN_HEIGHT);

        Rectangle rect = new Rectangle();
        rect.setHeight(ScreenManager.SCREEN_HEIGHT);
        rect.setWidth(600);
        rect.setArcHeight(20);
        rect.setArcWidth(20);

        registerImage.setFitHeight(ScreenManager.SCREEN_HEIGHT);
        registerImage.setClip(rect);

        imagePane.getChildren().add(registerImage);

        this.getChildren().addAll(vbox);
        this.setAlignment(Pos.CENTER);
    }

    private void actions(Stage stage) {
        loginLink.setOnAction(e -> {
            root.setLeft(loginVBox);
        });

        registerBtn.setOnAction(e -> {
            String username = nameTxt.getText();
            String email = emailTxt.getText();
            String password = passwordTxt.getText();
            String confirmPassword = confirmPasswordTxt.getText();
            String dob = String.valueOf(dobPicker.getValue());

            String output = authController.checkRegister(username, email, password, confirmPassword, dob);

            if (output.equals("Register Success!")) {
                errorLbl.setStyle("-fx-text-fill: green;-fx-font-weight: bold;");
                root.setLeft(loginVBox);
            }
            errorLbl.setText(output);
        });
    }

}
