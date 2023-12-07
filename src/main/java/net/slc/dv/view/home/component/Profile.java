package net.slc.dv.view.home.component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import net.slc.dv.builder.ButtonBuilder;
import net.slc.dv.builder.HBoxBuilder;
import net.slc.dv.builder.ImageViewBuilder;
import net.slc.dv.builder.VBoxBuilder;
import net.slc.dv.controller.MemberController;
import net.slc.dv.controller.TaskController;
import net.slc.dv.controller.UserController;
import net.slc.dv.helper.ImageManager;
import net.slc.dv.helper.StageManager;
import net.slc.dv.model.Classroom;
import net.slc.dv.model.LoggedUser;
import net.slc.dv.model.Task;
import net.slc.dv.resources.Icon;
import net.slc.dv.resources.IconStorage;
import net.slc.dv.view.classroom.detail.component.TaskItem;
import net.slc.dv.view.task.task.TaskBase;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Profile extends VBox {

    private final TaskController taskController;
    private final UserController userController;
    private final ImageView profileNav;
    private final LoggedUser loggedUser;
    private final StackPane mainPane;
    private ImageView profile;
    private Label name, email, birthday;
    private TextField nameField;
    private TextField emailField;
    private DatePicker birthdayField;
    private PasswordField oldPasswordField, newPasswordField, confirmPasswordField;
    private Label errorLbl;
    private Label errorLbl2;
    private VBox profileContainer, profileContent;
    private Button updateProfileBtn, cancelBtn, cancelPasswordBtn, updatePasswordBtn;
    private VBox taskContainer;
    private Button pendingTask, finishedTask;
    private VBox updateProfileContainer, updatePasswordContainer;
    private Consumer<Classroom> setNavigation;

    public Profile(ImageView profileNav, StackPane mainPane, Consumer<Classroom> setNavigation) {
        this.mainPane = mainPane;
        this.profileNav = profileNav;
        this.setNavigation = setNavigation;

        this.loggedUser = LoggedUser.getInstance();
        this.userController = new UserController();
        this.taskController = new TaskController();

        init();
        actions();

        setUpProfileContent();
    }

    public static void DateFormatter(DatePicker birthdayField) {
        birthdayField.setConverter(new StringConverter<>() {
            final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");

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

    private void init() {
        Rectangle banner = new Rectangle();
        banner.widthProperty().bind(this.widthProperty().subtract(40));
        banner.setHeight(225);

        double cornerRadiusTop = 20.0;

        banner.setArcWidth(cornerRadiusTop);
        banner.setArcHeight(cornerRadiusTop);

        VBox.setMargin(banner, new Insets(20, 0, 0, 0));

        StackPane bannerStack = new StackPane();
        bannerStack.prefWidthProperty().bind(mainPane.widthProperty());
        bannerStack.getChildren().add(banner);
        bannerStack.setAlignment(Pos.CENTER);
        bannerStack.setPadding(new Insets(20, 0, 0, 0));

        ColorPicker colorPicker = new ColorPicker();
        bannerStack.getChildren().add(colorPicker);

        colorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            banner.setFill(newValue);
        });

        HBox colorPickerContainer = HBoxBuilder.create()
                .addChildren(colorPicker)
                .setAlignment(Pos.BOTTOM_RIGHT)
                .setPadding(0, 50, 20, 0)
                .build();

        bannerStack.getChildren().add(colorPickerContainer);

        VBox bannerContainer = new VBox();
        bannerContainer.getChildren().add(bannerStack);

        bannerContainer.setAlignment(Pos.CENTER);

        this.getChildren().add(bannerContainer);

        this.updateProfileContainer = new VBox(20);
        this.updateProfileContainer.setAlignment(Pos.TOP_CENTER);
        this.updateProfileContainer.getStyleClass().add("card");
        this.updateProfileContainer.setMaxWidth(600);
        VBox.setMargin(updateProfileContainer, new Insets(0, 0, 150, 0));

        this.updatePasswordContainer = new VBox(20);
        this.updatePasswordContainer.setAlignment(Pos.TOP_CENTER);
        this.updatePasswordContainer.getStyleClass().add("card");
        this.updatePasswordContainer.setMaxWidth(600);
        VBox.setMargin(updatePasswordContainer, new Insets(0, 0, 150, 0));

        this.errorLbl = new Label();
        this.errorLbl.setStyle("-fx-text-fill: red;");

        this.errorLbl2 = new Label();
        this.errorLbl2.setStyle("-fx-text-fill: red;");

        this.updateProfileBtn = new Button("Update Profile");
        this.updateProfileBtn.getStyleClass().add("primary-button");
        this.updateProfileBtn.setStyle("-fx-text-fill: white;");

        this.cancelBtn = new Button("Cancel");
        this.cancelBtn.getStyleClass().add("secondary-button");

        HBox buttonContainer = new HBox(20);
        buttonContainer.getChildren().addAll(cancelBtn, updateProfileBtn);
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

        HBox changeButtonContainer = new HBox(20);

        this.updatePasswordBtn = new Button("Update Password");
        this.updatePasswordBtn.getStyleClass().add("primary-button");
        this.updatePasswordBtn.setStyle("-fx-text-fill: white;");

        this.cancelPasswordBtn = new Button("Cancel");
        this.cancelPasswordBtn.getStyleClass().add("secondary-button");

        changeButtonContainer.getChildren().addAll(cancelPasswordBtn, updatePasswordBtn);
        changeButtonContainer.setAlignment(Pos.TOP_CENTER);

        if (loggedUser.getProfileImage() == null) {
            profile = ImageViewBuilder.create()
                    .bindImageProperty(IconStorage.getIcon(Icon.USER))
                    .build();
        } else {
            profile = new ImageView(loggedUser.getProfileImage());
        }

        HBox profileLayout = new HBox(30);
        profileLayout.setAlignment(Pos.BOTTOM_LEFT);

        ImageManager.makeCircular(profileLayout, profile, 100);

        name = new Label(loggedUser.getUsername());
        name.setStyle("-fx-font-size: 40px");

        HBox emailContainer = new HBox(5);
        emailContainer.setAlignment(Pos.CENTER_LEFT);

        ImageView emailIcon = ImageViewBuilder.create()
                .bindImageProperty(IconStorage.getIcon(Icon.EMAIL))
                .setFitHeight(15)
                .setFitWidth(15)
                .build();

        email = new Label(loggedUser.getEmail());
        email.setStyle("-fx-font-size: 14px");

        emailContainer.getChildren().addAll(emailIcon, email);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dobString = loggedUser.getDob();

        LocalDate dateOfBirth = LocalDate.parse(dobString, dateFormatter);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        String formattedDate = dateOfBirth.format(formatter);

        HBox birthdayContainer = new HBox(5);
        birthdayContainer.setAlignment(Pos.CENTER_LEFT);

        ImageView cakeIcon = ImageViewBuilder.create()
                .bindImageProperty(IconStorage.getIcon(Icon.CAKE))
                .setFitWidth(15)
                .setFitHeight(15)
                .build();

        birthday = new Label(formattedDate);
        birthday.setStyle("-fx-font-size: 14px");

        birthdayContainer.getChildren().addAll(cakeIcon, birthday);

        this.profileContainer = new VBox(20);

        profileContainer.setAlignment(Pos.CENTER);


        VBox nameBirthday = new VBox(5);

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox editContainer = new HBox(10);
        editContainer.setAlignment(Pos.CENTER);

        HBox nameWithButtonContainer = new HBox();
        nameWithButtonContainer.getChildren().addAll(name, spacer, editContainer);
        nameWithButtonContainer.setAlignment(Pos.TOP_CENTER);

        nameBirthday.getChildren().addAll(nameWithButtonContainer, birthdayContainer, emailContainer);

        nameBirthday.prefWidthProperty().bind(this.widthProperty());
        nameBirthday.setAlignment(Pos.BOTTOM_CENTER);

        nameBirthday.setPadding(new Insets(0, 0, -20, 0));

        profileLayout.getChildren().addAll(nameBirthday);

        profileContainer.getChildren().addAll(profileLayout);

        VBox.setMargin(profileLayout, new Insets(-125, 0, 0, 0));
        VBox.setMargin(profileContainer, new Insets(0, 75, 0, 75));

        this.getChildren().addAll(profileContainer);
        VBox.setMargin(this, new Insets(50, 0, 0, 0));


        HBox edit = setEditProfile();
        HBox changePassword = setChangePassword();

        VBox.setMargin(editContainer, new Insets(100, 0, 0, 0));

        editContainer.getChildren().addAll(edit, changePassword);
        editContainer.setAlignment(Pos.TOP_CENTER);

        Line line = new Line();
        line.setStartX(0);

        line.endXProperty().bind(profileContainer.widthProperty().subtract(20));
        VBox.setMargin(line, new Insets(30, 0, 0, 0));

        line.setStroke(Color.valueOf("#2c3e50"));
        line.setStrokeWidth(1);

        profileContainer.getChildren().add(line);

        this.updateProfileContainer.getChildren().addAll(nameField, emailField, birthdayField, errorLbl, buttonContainer);
        this.updatePasswordContainer.getChildren().addAll(oldPasswordField, newPasswordField, confirmPasswordField, errorLbl2, changeButtonContainer);

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

            if (selectedFile != null) {
                Image image = new Image("file:" + selectedFile.getAbsolutePath());
                profile.setImage(image);
                this.profileNav.setImage(image);

                this.userController.updateProfileImage(selectedFile);

                loggedUser.setProfileImage(image);
            }
        });

        this.updateProfileBtn.setOnMouseClicked(e -> {

            String message = this.userController.updateProfile(nameField.getText(), emailField.getText(), String.valueOf(birthdayField.getValue()));

            if (message.equals("Success")) {
                this.name.setText(nameField.getText());
                this.email.setText(emailField.getText());

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
                String formattedDate = birthdayField.getValue().format(formatter);

                this.birthday.setText(formattedDate);

                loggedUser.setUsername(nameField.getText());
                loggedUser.setEmail(emailField.getText());
                loggedUser.setDob(String.valueOf(birthdayField.getValue()));

                profileContainer.getChildren().removeAll(updateProfileContainer);

                profileContainer.getChildren().add(profileContent);
                return;
            }

            errorLbl.setText(message);
        });

        this.cancelBtn.setOnMouseClicked(e -> {
            profileContainer.getChildren().removeAll(updateProfileContainer);
            profileContainer.getChildren().add(profileContent);
        });

        this.updatePasswordBtn.setOnMouseClicked(e -> {
            String message = this.userController.updatePassword(oldPasswordField.getText(), newPasswordField.getText(), confirmPasswordField.getText());

            if (message.equals("Success")) {
                profileContainer.getChildren().removeAll(updatePasswordContainer);
                profileContainer.getChildren().add(profileContent);
                return;
            }

            errorLbl2.setText(message);
        });

        this.cancelPasswordBtn.setOnMouseClicked(e -> {
            profileContainer.getChildren().removeAll(updatePasswordContainer);
            profileContainer.getChildren().add(profileContent);
        });
    }

    private HBox setEditProfile() {
        HBox editProfileContainer = new HBox(5);

        ImageView editIconView = ImageViewBuilder.create()
                .bindImageProperty(IconStorage.getIcon(Icon.USER))
                .build();

        editIconView.setFitWidth(25);
        editIconView.setPreserveRatio(true);
        HBox.setMargin(editIconView, new Insets(0, 0, 0, 20));

        Button editBtn = ButtonBuilder.create("Edit Profile")
                .setStyle("-fx-font-size: 14px")
                .setGraphic(editIconView)
                .build();

        editProfileContainer.getChildren().addAll(editBtn);
        editProfileContainer.setAlignment(Pos.CENTER_RIGHT);

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


        editBtn.setOnMouseClicked(e -> {
            this.errorLbl.setText("");
            profileContainer.getChildren().remove(profileContent);
            profileContainer.getChildren().removeAll(updateProfileContainer, updatePasswordContainer);
            profileContainer.getChildren().addAll(updateProfileContainer);
        });

        VBox.setMargin(editProfileContainer, new Insets(0, 0, 400, 0));
        editBtn.getStyleClass().add("border-button");

        return editProfileContainer;
    }

    private HBox setChangePassword() {
        HBox passwordContainer = new HBox(5);

        ImageView passwordIconView = ImageViewBuilder.create()
                .bindImageProperty(IconStorage.getIcon(Icon.CHANGE_PASSWORD))
                .setFitHeight(25)
                .setPreserveRatio(true)
                .build();


        HBox.setMargin(passwordIconView, new Insets(0, 0, 0, 20));

        Button passwordBtn = ButtonBuilder.create("Change Password")
                .setGraphic(passwordIconView)
                .setStyle("-fx-font-size: 14px")
                .build();


        passwordBtn.setOnMouseClicked(e -> {
            this.errorLbl2.setText("");
            profileContainer.getChildren().remove(profileContent);
            profileContainer.getChildren().removeAll(updateProfileContainer, updatePasswordContainer);
            profileContainer.getChildren().addAll(updatePasswordContainer);
        });

        VBox.setMargin(passwordContainer, new Insets(0, 0, 400, 0));
        passwordBtn.getStyleClass().add("border-button");

        passwordContainer.getChildren().addAll(passwordBtn);
        passwordContainer.setAlignment(Pos.CENTER_RIGHT);

        return passwordContainer;
    }

    public void setUpProfileContent() {

        this.profileContent = new VBox(20);
        this.profileContent.setAlignment(Pos.TOP_CENTER);
        this.profileContent.getStyleClass().add("card");

        profileContent.setPadding(new Insets(20));
        profileContent.setMaxWidth(600);
        VBox.setMargin(profileContent, new Insets(0, 0, 400, 0));

        HBox tab = new HBox();
        tab.setAlignment(Pos.CENTER);

        this.pendingTask = new Button("Pending Task");
        pendingTask.getStyleClass().addAll("nav-button", "active");

        this.finishedTask = new Button("Finished Task");
        finishedTask.getStyleClass().add("nav-button");

        tab.getChildren().addAll(pendingTask, finishedTask);

        profileContent.getChildren().add(tab);

        taskContainer = new VBox();
        taskContainer.setAlignment(Pos.TOP_CENTER);

        fetchPendingTask();

        pendingTask.setOnMouseClicked(e -> fetchPendingTask());

        finishedTask.setOnMouseClicked(e -> fetchFinishTask());

        this.profileContent.getChildren().add(taskContainer);

        profileContainer.getChildren().add(this.profileContent);
    }

    public void fetchPendingTask() {
        this.taskContainer.getChildren().clear();
        this.finishedTask.getStyleClass().remove("active");
        this.pendingTask.getStyleClass().remove("active");
        this.pendingTask.getStyleClass().add("active");

        ArrayList<Task> tasks = this.taskController.fetchUserPendingTask(loggedUser.getId());

        taskLayout(tasks);
    }

    private void taskLayout(ArrayList<Task> tasks) {
        if(tasks.isEmpty()) {
            Label noTask = new Label("Chill dulu gak sih?");
            noTask.setStyle("-fx-font-size: 20px");
            this.taskContainer.getChildren().add(noTask);
            return;
        }

        for (Task task : tasks) {
            TaskItem taskItem = new TaskItem(task);
            this.taskContainer.getChildren().add(taskItem);

            taskItem.setOnMouseClicked(e -> {
                String userRole = new MemberController().getRole(task.getClassroom().getClassId());
                new TaskBase(mainPane, task, task.getClassroom(), userRole);

                this.setNavigation.accept(task.getClassroom());
            });
        }
    }


    public void fetchFinishTask() {
        this.taskContainer.getChildren().clear();

        this.pendingTask.getStyleClass().remove("active");
        this.finishedTask.getStyleClass().remove("active");
        this.finishedTask.getStyleClass().add("active");

        ArrayList<Task> tasks = this.taskController.fetchUserFinishedTask(loggedUser.getId());

        taskLayout(tasks);

    }

}
