package view;

import controller.AuthController;
import helper.ScreenManager;
import helper.ThemeManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Register {

    private AuthController authController = new AuthController();

    private Scene scene;
    private BorderPane borderPane;
    private Label subTitle;
    private Label nameLbl, emailLbl, passwordLbl, confirmPasswordLbl, agelbl;
    private TextField nameTxt, emailTxt;
    private PasswordField passwordTxt, confirmPasswordTxt;
    private Spinner<Integer> ageSpinner;
    private Button registerBtn;

    private Image image;
    private ImageView registerImage;

    private VBox vbox;

    private VBox nameVbox, emailVbox, passwordVbox, confirmPasswordVbox, ageVbox, registerVbox;

    private Button loginLink;

    private void initialize() {
        borderPane = new BorderPane();
        subTitle = new Label("Be the change, sign in to make it happen!");
        nameLbl = new Label("Name:");
        emailLbl = new Label("Email:");
        passwordLbl = new Label("Password:");
        confirmPasswordLbl = new Label("Confirm Password:");
        agelbl = new Label("Age:");

        nameTxt = new TextField();
        emailTxt = new TextField();
        passwordTxt = new PasswordField();
        confirmPasswordTxt = new PasswordField();

        ageSpinner = new Spinner<>();

        nameVbox = new VBox();
        emailVbox = new VBox();
        passwordVbox = new VBox();
        confirmPasswordVbox = new VBox();
        ageVbox = new VBox();
        registerVbox = new VBox();

        registerBtn = new Button("Register");
        registerBtn.getStyleClass().add("primary-button");
        loginLink = new Button("Already have an account? Login here!");
        loginLink.getStyleClass().add("link-button");

        image = new Image("file:resources/image/auth_image.png");

        registerImage = new ImageView(image);

        vbox = new VBox(10);
    }

    private Scene setLayout() {

        Font font = Font.loadFont("file:resources/fonts/LindenHill-Italic.ttf", 40);
        subTitle.setFont(font);

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 18);
        ageSpinner.setValueFactory(valueFactory);

        nameVbox.getChildren().addAll(nameLbl, nameTxt);
        emailVbox.getChildren().addAll(emailLbl, emailTxt);
        passwordVbox.getChildren().addAll(passwordLbl, passwordTxt);
        confirmPasswordVbox.getChildren().addAll(confirmPasswordLbl, confirmPasswordTxt);
        ageVbox.getChildren().addAll(agelbl, ageSpinner);
        registerVbox.getChildren().addAll(registerBtn, loginLink);
        registerVbox.setAlignment(Pos.CENTER);
        registerVbox.setPadding(new Insets(30, 0, 0, 0));

        vbox.getChildren().addAll(subTitle, nameVbox, emailVbox, passwordVbox, confirmPasswordVbox, ageVbox, registerVbox);
        vbox.setAlignment(Pos.CENTER_RIGHT);
        vbox.setPadding(new Insets(-130, 100, 0, 0));
        borderPane.setRight(vbox);

        StackPane imagePane = new StackPane();
        imagePane.setPrefSize(400, ScreenManager.SCREEN_HEIGHT);

        registerImage.setFitWidth(450);
        registerImage.setFitHeight(ScreenManager.SCREEN_HEIGHT);

        imagePane.getChildren().add(registerImage);

        borderPane.setLeft(imagePane);

        BorderPane.setAlignment(vbox, Pos.CENTER_RIGHT);

        scene = new Scene(borderPane, 1200, 700);
        ThemeManager.getTheme(scene);
        return scene;
    }

    private void actions(Stage stage) {
        loginLink.setOnAction(e -> {
            new Login(stage);
        });

        registerBtn.setOnAction(e -> {
            String username = nameTxt.getText();
            String email = emailTxt.getText();
            String password = passwordTxt.getText();
            String confirmPassword = confirmPasswordTxt.getText();
            int age = ageSpinner.getValue();

            String output = authController.checkRegister(username, email, password, confirmPassword, age);
            System.out.println(output);
        });
    }

    public Register(Stage stage) {
        initialize();

        scene = setLayout();

        actions(stage);

        ThemeManager.getTheme(scene);
        stage.setScene(scene);
        stage.setTitle("DVibes - Register");

    }

}
