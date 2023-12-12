package net.slc.dv.view.register;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.slc.dv.builder.HBoxBuilder;
import net.slc.dv.builder.VBoxBuilder;
import net.slc.dv.resources.Text;
import net.slc.dv.storage.ImageStorage;
import net.slc.dv.controller.AuthController;
import net.slc.dv.helper.ScreenManager;
import net.slc.dv.storage.TextStorage;
import net.slc.dv.view.login.LoginView;

public class RegisterView extends VBox {

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

    public RegisterView(Stage stage, VBox vbox, BorderPane borderPane) {
        this.loginVBox = vbox;
        this.root = borderPane;
        initialize();
        setLayout();
        actions(stage);

        stage.setTitle("DigiVerse - " + TextStorage.getText(Text.REGISTER));
    }

    private void initialize() {
        subTitle = new Label(TextStorage.getText(Text.BE_THE_CHANGE));
        nameLbl = new Label( TextStorage.getText(Text.NAME) + ":");
        emailLbl = new Label(TextStorage.getText(Text.EMAIL) + ":");
        passwordLbl = new Label(TextStorage.getText(Text.PASSWORD) + ":");
        confirmPasswordLbl = new Label(TextStorage.getText(Text.CONFIRM_PASSWORD) + ":");
        agelbl = new Label(TextStorage.getText(Text.DATE_OF_BIRTH) + ":");
        errorLbl = new Label();

        nameTxt = new TextField();
        emailTxt = new TextField();
        passwordTxt = new PasswordField();
        confirmPasswordTxt = new PasswordField();

        dobPicker = new DatePicker();
        dobPicker.prefWidthProperty().bind(nameTxt.widthProperty());

        nameVbox = new VBox();
        emailVbox = new VBox();
        passwordVbox = new VBox();
        confirmPasswordVbox = new VBox();
        ageVbox = new VBox();
        registerVbox = new VBox();

        nameVbox.setPrefWidth(350);
        emailVbox.setPrefWidth(350);
        passwordVbox.setPrefWidth(350);
        confirmPasswordVbox.setPrefWidth(350);
        ageVbox.setPrefWidth(350);

        registerBtn = new Button(TextStorage.getText(Text.REGISTER));
        registerBtn.getStyleClass().add("primary-button");
        registerBtn.setStyle("-fx-text-fill: white;");
        registerBtn.setPrefWidth(350);
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

        loginLink = new Button(TextStorage.getText(Text.ALREADY_HAVE_AN_ACCOUNT));
        loginLink.getStyleClass().add("link-button");
        loginLink.setStyle("-fx-font-size: 14px");

        loginLink.setOnMouseEntered(e -> {
            loginLink.setStyle("-fx-font-size: 14px;-fx-text-fill: #1E90FF;");
        });

        loginLink.setOnMouseExited(e -> {
            loginLink.setStyle("-fx-font-size: 14px;-fx-text-fill: #000000;");
        });

        Image image = new Image(ImageStorage.AUTH_IMAGE);
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

        HBox nameHBox = HBoxBuilder.create()
                .addChildren(nameVbox)
                .setAlignment(Pos.CENTER)
                .build();

        HBox emailHBox = HBoxBuilder.create()
                .addChildren(emailVbox)
                .setAlignment(Pos.CENTER)
                .build();

        HBox passwordHBox = HBoxBuilder.create()
                .addChildren(passwordVbox)
                .setAlignment(Pos.CENTER)
                .build();

        HBox confirmPasswordHBox = HBoxBuilder.create()
                .addChildren(confirmPasswordVbox)
                .setAlignment(Pos.CENTER)
                .build();

        HBox ageHBox = HBoxBuilder.create()
                .addChildren(ageVbox)
                .setAlignment(Pos.CENTER)
                .build();

        vbox.getChildren().addAll(subTitle, nameHBox, emailHBox, passwordHBox, confirmPasswordHBox, ageHBox, registerVbox, errorLbl);
        vbox.setAlignment(Pos.CENTER);
//        vbox.setPadding(new Insets(0, 0, 0, 60));

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
        this.setMaxWidth(600);
        this.setAlignment(Pos.CENTER);
    }

    private void actions(Stage stage) {
        loginLink.setOnAction(e -> {
            VBoxBuilder.modify(LoginView.getOuterContainer())
                    .removeAll()
                    .addChildren(loginVBox)
                    .setPrefWidth(550)
                    .build();

            root.getChildren().clear();
            root.setCenter(LoginView.getOuterContainer());
        });

        registerBtn.setOnAction(e -> {
            String username = nameTxt.getText();
            String email = emailTxt.getText();
            String password = passwordTxt.getText();
            String confirmPassword = confirmPasswordTxt.getText();
            String dob = String.valueOf(dobPicker.getValue());

            String output = authController.checkRegister(username, email, password, confirmPassword, dob);

            if (output.equals(TextStorage.getText(Text.REGISTER_SUCCESS))) {
                errorLbl.setStyle("-fx-text-fill: green;-fx-font-weight: bold;");

                VBoxBuilder.modify(LoginView.getOuterContainer())
                        .removeAll()
                        .addChildren(loginVBox)
                        .build();

                nameTxt.setText("");
                emailTxt.setText("");
                passwordTxt.setText("");
                confirmPasswordTxt.setText("");
                dobPicker.setValue(null);

                root.getChildren().clear();
                root.setCenter(LoginView.getOuterContainer());
                return;
            }
            errorLbl.setText(output);
        });
    }

}
