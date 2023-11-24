package view;

import controller.UserController;
import helper.ImageManager;
import helper.StageManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import model.LoggedUser;
import model.User;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Profile extends VBox {

    private ImageView profile, profileNav;
    private Label name, email, birthday;
    private TextField nameField;
    private TextField emailField;
    private DatePicker birthdayField;
    private PasswordField oldPasswordField, newPasswordField, confirmPasswordField;
    private LoggedUser loggedUser;
    private HBox editContainer, buttonContainer, changeButtonContainer, profileLayout, birthdayContainer;
    private UserController userController;
    private Label errorLbl;
    private VBox profileContainer;

    private VBox passwordContainer, editProfileContainer, nameBirthday;

    private Button updateProfileBtn, cancelBtn, cancelPasswordBtn, updatePasswordBtn;

    public Profile(ImageView profileNav) {
        this.loggedUser = LoggedUser.getInstance();
        this.userController = new UserController();
        this.profileNav = profileNav;
        init();
        actions();
    }

    private void init() {

        Rectangle banner = new Rectangle();
        banner.setArcHeight(20);
        banner.setArcWidth(20);
        banner.widthProperty().bind(this.widthProperty().subtract(40));
        banner.setHeight(225);
        banner.setFill(Color.valueOf("#2c3e50"));

        VBox.setMargin(banner, new Insets(20, 0, 0, 0));

        VBox bannerContainer = new VBox();
        bannerContainer.getChildren().add(banner);

        bannerContainer.setAlignment(Pos.CENTER);

        this.getChildren().add(bannerContainer);

        this.errorLbl = new Label();
        this.errorLbl.setStyle("-fx-text-fill: red;");

        this.updateProfileBtn = new Button("Update Profile");
        this.updateProfileBtn.getStyleClass().add("primary-button");

        this.cancelBtn = new Button("Cancel");
        this.cancelBtn.getStyleClass().add("secondary-button");

        this.buttonContainer = new HBox(20);
        this.buttonContainer.getChildren().addAll(cancelBtn, updateProfileBtn);
        buttonContainer.setAlignment(Pos.TOP_CENTER);

        this.oldPasswordField = new PasswordField();
        this.newPasswordField = new PasswordField();
        this.confirmPasswordField = new PasswordField();

        this.oldPasswordField.setPromptText("Old Password");
        this.newPasswordField.setPromptText("New Password");
        this.confirmPasswordField.setPromptText("Confirm Password");

        this.oldPasswordField.setMaxWidth(500);
        this.newPasswordField.setMaxWidth(500);
        this.confirmPasswordField.setMaxWidth(500);

        this.changeButtonContainer = new HBox(20);

        this.updatePasswordBtn = new Button("Update Password");
        this.updatePasswordBtn.getStyleClass().add("primary-button");

        this.cancelPasswordBtn = new Button("Cancel");
        this.cancelPasswordBtn.getStyleClass().add("secondary-button");

        this.changeButtonContainer.getChildren().addAll(cancelPasswordBtn, updatePasswordBtn);
        changeButtonContainer.setAlignment(Pos.TOP_CENTER);

        if (loggedUser.getProfileImage() == null) {
            profile = new ImageView(new Image("file:resources/icons/user.png"));
        } else {
            profile = new ImageView(loggedUser.getProfileImage());
        }

        ImageManager.makeCircular(profile, 75);

        name = new Label(loggedUser.getUsername());
        name.setStyle("-fx-font-size: 35px");

        email = new Label("Email : " + loggedUser.getEmail());
        email.getStyleClass().add("title");

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dobString = loggedUser.getDob();

        LocalDate dateOfBirth = LocalDate.parse(dobString, dateFormatter);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        String formattedDate = dateOfBirth.format(formatter);

        this.birthdayContainer = new HBox();
        birthdayContainer.setAlignment(Pos.CENTER_LEFT);

        ImageView cakeIcon = new ImageView(new Image("file:resources/icons/cake.png"));

        cakeIcon.setFitWidth(15);
        cakeIcon.setFitHeight(15);

        birthday = new Label(formattedDate);
        birthday.setStyle("-fx-font-size: 14px");

        birthdayContainer.getChildren().addAll(cakeIcon, birthday);

        this.profileContainer = new VBox(20);

        this.profileLayout = new HBox(30);
        profileLayout.setAlignment(Pos.BOTTOM_LEFT);

        this.nameBirthday = new VBox();
        nameBirthday.getChildren().addAll(name, birthdayContainer);
        nameBirthday.setAlignment(Pos.BOTTOM_LEFT);

        nameBirthday.setPadding(new Insets(0, 0, 5, 0));

        profileLayout.getChildren().addAll(profile, nameBirthday);

        profileContainer.getChildren().addAll(profileLayout, email);

        VBox.setMargin(profileLayout, new Insets(-100, 0, 0, 0));

        VBox.setMargin(profileContainer, new Insets(0, 100, 0, 100));

        this.getChildren().addAll(profileContainer);
        VBox.setMargin(this, new Insets(50, 0, 0, 0));


        VBox edit = setEditProfile();
        VBox changePassword = setChangePassword();

        this.editContainer = new HBox(50);
        this.editContainer.getChildren().addAll(edit, changePassword);
        editContainer.setAlignment(Pos.TOP_CENTER);

        VBox.setMargin(editContainer, new Insets(100, 0, 0, 0));

        this.getChildren().add(editContainer);

        this.prefWidthProperty().bind(this.widthProperty());
        this.setAlignment(Pos.TOP_LEFT);
        this.setSpacing(20);
    }

    private void actions() {
        this.profile.setOnMouseClicked(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Profile Picture");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
            );

            File selectedFile = fileChooser.showOpenDialog(StageManager.getInstance());

            if(selectedFile != null) {
                Image image = new Image("file:" + selectedFile.getAbsolutePath());
                profile.setImage(image);
                this.profileNav.setImage(image);

                this.userController.updateProfileImage(selectedFile);

                loggedUser.setProfileImage(image);
            }
        });

        this.updateProfileBtn.setOnMouseClicked(e -> {

            String message = this.userController.updateProfile(nameField.getText(), emailField.getText(), String.valueOf(birthdayField.getValue()));

            if(message.equals("Success")) {
                this.name.setText(nameField.getText());
                this.email.setText("Email : " + emailField.getText());

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
                String formattedDate = birthdayField.getValue().format(formatter);

                this.birthday.setText(formattedDate);

                loggedUser.setUsername(nameField.getText());
                loggedUser.setEmail(emailField.getText());
                loggedUser.setAge(String.valueOf(birthdayField.getValue()));

                birthdayContainer.getChildren().add(birthday);

                profileContainer.getChildren().removeAll(nameField, emailField, birthdayField, errorLbl, buttonContainer);
                profileContainer.getChildren().addAll(name, email, editContainer);
                return;
            }

            errorLbl.setText(message);
        });

        this.cancelBtn.setOnMouseClicked(e -> {
            profileContainer.getChildren().removeAll(nameField, emailField, birthdayField, errorLbl, buttonContainer);

            profileLayout.getChildren().add(nameBirthday);

            profileContainer.getChildren().addAll(email, editContainer);
        });

        this.updatePasswordBtn.setOnMouseClicked(e -> {
            String message = this.userController.updatePassword(oldPasswordField.getText(), newPasswordField.getText(), confirmPasswordField.getText());

            if(message.equals("Success")) {
                profileContainer.getChildren().removeAll(oldPasswordField, newPasswordField, confirmPasswordField, errorLbl, changeButtonContainer);

                profileLayout.getChildren().add(nameBirthday);

                profileContainer.getChildren().addAll(email, editContainer);
                return;
            }

            errorLbl.setText(message);
        });

        this.cancelPasswordBtn.setOnMouseClicked(e -> {
            profileContainer.getChildren().removeAll(oldPasswordField, newPasswordField, confirmPasswordField, errorLbl, changeButtonContainer);
            profileContainer.getChildren().addAll(email, editContainer);
        });
    }

    private VBox setEditProfile() {
        this.editProfileContainer = new VBox(20);

        Image editIcon = new Image("file:resources/icons/edit-profile.png");
        ImageView editIconView = new ImageView(editIcon);

        editIconView.setFitWidth(25);
        editIconView.setPreserveRatio(true);

        Button editBtn = new Button("Edit Profile");
        editBtn.getStyleClass().add("primary-button");
        editBtn.setPrefWidth(200);

        editProfileContainer.getChildren().addAll(editIconView, editBtn);
        editProfileContainer.setAlignment(Pos.TOP_CENTER);
        editProfileContainer.getStyleClass().add("small-container");

        editBtn.setOnMouseClicked(e -> {
            this.nameField = new TextField(loggedUser.getUsername());
            this.emailField = new TextField(loggedUser.getEmail());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String dobString = loggedUser.getDob();

            LocalDate dateOfBirth = LocalDate.parse(dobString, formatter);
            birthdayField = new DatePicker(dateOfBirth);

            DateFormatter(birthdayField);

            this.nameField.setMaxWidth(500);
            this.emailField.setMaxWidth(500);
            this.birthdayField.setMaxWidth(500);

            profileLayout.getChildren().remove(nameBirthday);
            profileContainer.getChildren().removeAll(email, editContainer);
            this.errorLbl.setText("");
            profileContainer.getChildren().addAll(nameField, emailField, birthdayField, errorLbl, buttonContainer);
        });

        return editProfileContainer;
    }

    public static void DateFormatter(DatePicker birthdayField) {
        birthdayField.setConverter(new StringConverter<>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");

            @Override
            public String toString(LocalDate date) {
                return date != null ? dateFormatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return string != null && !string.isEmpty() ? LocalDate.parse(string, dateFormatter) : null;
            }
        });
    }

    private VBox setChangePassword() {
        this.passwordContainer = new VBox(20);

        Image passwordIcon = new Image("file:resources/icons/change-password.png");
        ImageView passwordIconView = new ImageView(passwordIcon);

        passwordIconView.setFitWidth(25);
        passwordIconView.setPreserveRatio(true);

        Button passwordBtn = new Button("Change Password");
        passwordBtn.getStyleClass().add("primary-button");
        passwordBtn.setPrefWidth(200);

        passwordContainer.getChildren().addAll(passwordIconView, passwordBtn);
        passwordContainer.setAlignment(Pos.TOP_CENTER);
        passwordContainer.getStyleClass().add("small-container");

        passwordBtn.setOnMouseClicked(e -> {
            this.errorLbl.setText("");
            profileContainer.getChildren().removeAll(email, editContainer);
            profileContainer.getChildren().addAll(oldPasswordField, newPasswordField, confirmPasswordField, errorLbl, changeButtonContainer);
        });

        return passwordContainer;
    }

}
