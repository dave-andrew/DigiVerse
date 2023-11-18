package view;

import controller.UserController;
import helper.StageManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import model.LoggedUser;
import model.User;

import java.io.File;

public class Profile extends VBox {

    private ImageView profile, profileNav;
    private Label name, email, birthday;
    private TextField nameField, emailField, birthdayField;
    private PasswordField oldPasswordField, newPasswordField, confirmPasswordField;
    private LoggedUser loggedUser;
    private HBox editContainer, buttonContainer, changeButtonContainer;
    private UserController userController;

    private VBox passwordContainer, editProfileContainer;

    private Button updateProfileBtn, cancelBtn, cancelPasswordBtn, updatePasswordBtn;

    public Profile(ImageView profileNav) {
        this.loggedUser = LoggedUser.getInstance();
        this.userController = new UserController();
        this.profileNav = profileNav;
        init();
        actions();
    }

    private void init() {

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

        if(loggedUser.getProfileImage() == null) {
            profile = new ImageView(new Image("file:resources/icons/user.png"));
        } else {
            profile = new ImageView(loggedUser.getProfileImage());
        }

        profile.setFitHeight(100);
        profile.setFitWidth(100);

        name = new Label(loggedUser.getUsername());
        name.getStyleClass().add("title");

        email = new Label(loggedUser.getEmail());
        email.getStyleClass().add("title");

        birthday = new Label(String.valueOf(loggedUser.getAge()));
        birthday.getStyleClass().add("title");

        this.getChildren().addAll(profile, name, email, birthday);

        VBox edit = setEditProfile();
        VBox changePassword = setChangePassword();

        this.editContainer = new HBox(50);
        this.editContainer.getChildren().addAll(edit, changePassword);
        editContainer.setAlignment(Pos.TOP_CENTER);

        VBox.setMargin(editContainer, new Insets(100, 0, 0, 0));

        this.getChildren().add(editContainer);

        this.prefWidthProperty().bind(this.widthProperty());
        this.setAlignment(Pos.TOP_CENTER);
        this.setSpacing(20);
        this.setPadding(new Insets(100, 0, 0, 0));
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

            if(this.userController.updateProfile(nameField.getText(), emailField.getText(), Integer.parseInt(birthdayField.getText()))) {
                this.name.setText(nameField.getText());
                this.email.setText(emailField.getText());
                this.birthday.setText(String.valueOf(birthdayField.getText()));

                loggedUser.setUsername(nameField.getText());
                loggedUser.setEmail(emailField.getText());
                loggedUser.setAge(Integer.parseInt(birthdayField.getText()));

                this.getChildren().removeAll(nameField, emailField, birthdayField, buttonContainer);
                this.getChildren().addAll(name, email, birthday, editContainer);
            }
        });

        this.cancelBtn.setOnMouseClicked(e -> {
            this.getChildren().removeAll(nameField, emailField, birthdayField, buttonContainer);
            this.getChildren().addAll(name, email, birthday, editContainer);
        });

        this.updatePasswordBtn.setOnMouseClicked(e -> {
            if(this.userController.updatePassword(oldPasswordField.getText(), newPasswordField.getText(), confirmPasswordField.getText())) {
                this.getChildren().removeAll(oldPasswordField, newPasswordField, confirmPasswordField, changeButtonContainer);
                this.getChildren().addAll(name, email, birthday, editContainer);
            }
        });

        this.cancelPasswordBtn.setOnMouseClicked(e -> {
            this.getChildren().removeAll(oldPasswordField, newPasswordField, confirmPasswordField, changeButtonContainer);
            this.getChildren().addAll(name, email, birthday, editContainer);
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
            this.birthdayField = new TextField(String.valueOf(loggedUser.getAge()));

            this.nameField.setMaxWidth(500);
            this.emailField.setMaxWidth(500);
            this.birthdayField.setMaxWidth(500);

            this.getChildren().removeAll(name, email, birthday, editContainer);

            this.getChildren().addAll(nameField, emailField, birthdayField, buttonContainer);
        });

        return editProfileContainer;
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
            this.getChildren().removeAll(name, email, birthday, editContainer);
            this.getChildren().addAll(oldPasswordField, newPasswordField, confirmPasswordField, changeButtonContainer);
        });

        return passwordContainer;
    }

}
