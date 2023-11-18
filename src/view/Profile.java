package view;

import controller.UserController;
import helper.StageManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private LoggedUser loggedUser;
    private HBox editContainer;
    private UserController userController;

    public Profile(ImageView profileNav) {
        this.loggedUser = LoggedUser.getInstance();
        this.userController = new UserController();
        this.profileNav = profileNav;
        init();
        actions();
    }

    private void init() {

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
    }

    private VBox setEditProfile() {
        VBox container = new VBox(20);

        Image editIcon = new Image("file:resources/icons/edit-profile.png");
        ImageView editIconView = new ImageView(editIcon);

        editIconView.setFitWidth(25);
        editIconView.setPreserveRatio(true);

        Button editBtn = new Button("Edit Profile");
        editBtn.getStyleClass().add("primary-button");
        editBtn.setPrefWidth(200);

        container.getChildren().addAll(editIconView, editBtn);
        container.setAlignment(Pos.TOP_CENTER);
        container.getStyleClass().add("small-container");

        return container;
    }

    private VBox setChangePassword() {
        VBox container = new VBox(20);

        Image passwordIcon = new Image("file:resources/icons/change-password.png");
        ImageView passwordIconView = new ImageView(passwordIcon);

        passwordIconView.setFitWidth(25);
        passwordIconView.setPreserveRatio(true);

        Button passwordBtn = new Button("Change Password");
        passwordBtn.getStyleClass().add("primary-button");
        passwordBtn.setPrefWidth(200);

        container.getChildren().addAll(passwordIconView, passwordBtn);
        container.setAlignment(Pos.TOP_CENTER);
        container.getStyleClass().add("small-container");

        return container;
    }

}
