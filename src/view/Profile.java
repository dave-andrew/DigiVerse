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
import javafx.scene.shape.Circle;
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
    private HBox editContainer, buttonContainer, changeButtonContainer;
    private UserController userController;
    private Label errorLbl;

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

        name = new Label("Username : " + loggedUser.getUsername());
        name.getStyleClass().add("title");

        email = new Label("Email : " + loggedUser.getEmail());
        email.getStyleClass().add("title");

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dobString = loggedUser.getDob();

        LocalDate dateOfBirth = LocalDate.parse(dobString, dateFormatter);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        String formattedDate = dateOfBirth.format(formatter);

        birthday = new Label("Birthday : " + formattedDate);
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

            String message = this.userController.updateProfile(nameField.getText(), emailField.getText(), String.valueOf(birthdayField.getValue()));

            if(message.equals("Success")) {
                this.name.setText("Username : " + nameField.getText());
                this.email.setText("Email : " + emailField.getText());

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
                String formattedDate = birthdayField.getValue().format(formatter);

                this.birthday.setText("Birthday : " + formattedDate);

                loggedUser.setUsername(nameField.getText());
                loggedUser.setEmail(emailField.getText());
                loggedUser.setAge(String.valueOf(birthdayField.getValue()));

                this.getChildren().removeAll(nameField, emailField, birthdayField, errorLbl, buttonContainer);
                this.getChildren().addAll(name, email, birthday, editContainer);
                return;
            }

            errorLbl.setText(message);
        });

        this.cancelBtn.setOnMouseClicked(e -> {
            this.getChildren().removeAll(nameField, emailField, birthdayField, errorLbl, buttonContainer);
            this.getChildren().addAll(name, email, birthday, editContainer);
        });

        this.updatePasswordBtn.setOnMouseClicked(e -> {
            String message = this.userController.updatePassword(oldPasswordField.getText(), newPasswordField.getText(), confirmPasswordField.getText());

            if(message.equals("Success")) {
                this.getChildren().removeAll(oldPasswordField, newPasswordField, confirmPasswordField, errorLbl, changeButtonContainer);
                this.getChildren().addAll(name, email, birthday, editContainer);
                return;
            }

            errorLbl.setText(message);
        });

        this.cancelPasswordBtn.setOnMouseClicked(e -> {
            this.getChildren().removeAll(oldPasswordField, newPasswordField, confirmPasswordField, errorLbl, changeButtonContainer);
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

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String dobString = loggedUser.getDob();

            LocalDate dateOfBirth = LocalDate.parse(dobString, formatter);
            birthdayField = new DatePicker(dateOfBirth);

            DateFormatter(birthdayField);

            this.nameField.setMaxWidth(500);
            this.emailField.setMaxWidth(500);
            this.birthdayField.setMaxWidth(500);

            this.getChildren().removeAll(name, email, birthday, editContainer);
            this.errorLbl.setText("");
            this.getChildren().addAll(nameField, emailField, birthdayField, errorLbl, buttonContainer);
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

            this.getChildren().removeAll(name, email, birthday, editContainer);
            this.getChildren().addAll(oldPasswordField, newPasswordField, confirmPasswordField, errorLbl, changeButtonContainer);
        });

        return passwordContainer;
    }

}
