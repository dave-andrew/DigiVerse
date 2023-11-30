package net.slc.dv.view;

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
import net.slc.dv.controller.MemberController;
import net.slc.dv.controller.TaskController;
import net.slc.dv.controller.UserController;
import net.slc.dv.helper.ImageManager;
import net.slc.dv.helper.StageManager;
import net.slc.dv.model.Classroom;
import net.slc.dv.model.LoggedUser;
import net.slc.dv.model.Task;
import net.slc.dv.view.component.classdetail.component.TaskItem;
import net.slc.dv.view.homeview.ClassroomDetail;
import net.slc.dv.view.homeview.task.TaskBase;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Profile extends VBox {

    private final TaskController taskController;
    private final UserController userController;
    private final ImageView profileNav;
    private final LoggedUser loggedUser;
    private final StackPane mainPane;
    private final HBox leftNav;
    private final Button iconBtn;

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

    public Profile(ImageView profileNav, HBox leftNav, Button iconBtn, StackPane mainPane) {
        this.loggedUser = LoggedUser.getInstance();
        this.userController = new UserController();
        this.taskController = new TaskController();

        this.mainPane = mainPane;
        this.leftNav = leftNav;
        this.iconBtn = iconBtn;

        this.profileNav = profileNav;
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
        banner.setStyle("-fx-fill: linear-gradient(to top, violet, indigo, blue, green, yellow, orange, red);");

        double cornerRadiusTop = 20.0;

        banner.setArcWidth(cornerRadiusTop);
        banner.setArcHeight(cornerRadiusTop);

        VBox.setMargin(banner, new Insets(20, 0, 0, 0));

        VBox bannerContainer = new VBox();
        bannerContainer.getChildren().add(banner);

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
            profile = new ImageView(new Image("file:resources/icons/user.png"));
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

        ImageView emailIcon = new ImageView(new Image("file:resources/icons/email.png"));

        emailIcon.setFitWidth(15);
        emailIcon.setFitHeight(15);

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

        ImageView cakeIcon = new ImageView(new Image("file:resources/icons/cake.png"));

        cakeIcon.setFitWidth(15);
        cakeIcon.setFitHeight(15);

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
                loggedUser.setAge(String.valueOf(birthdayField.getValue()));

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

        Image editIcon = new Image("file:resources/icons/edit-profile.png");
        ImageView editIconView = new ImageView(editIcon);

        editIconView.setFitWidth(25);
        editIconView.setPreserveRatio(true);
        HBox.setMargin(editIconView, new Insets(0, 0, 0, 20));

        Label editBtn = new Label("Edit Profile");
        editBtn.setPrefWidth(100);
        editBtn.setStyle("-fx-font-size: 14px");

        editProfileContainer.getChildren().addAll(editIconView, editBtn);
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


        editProfileContainer.setOnMouseClicked(e -> {
            this.errorLbl.setText("");
            profileContainer.getChildren().remove(profileContent);
            profileContainer.getChildren().removeAll(updateProfileContainer, updatePasswordContainer);
            profileContainer.getChildren().addAll(updateProfileContainer);
        });

        VBox.setMargin(editProfileContainer, new Insets(0, 0, 400, 0));
        editProfileContainer.getStyleClass().add("border-button");

        return editProfileContainer;
    }

    private HBox setChangePassword() {
        HBox passwordContainer = new HBox(5);

        Image passwordIcon = new Image("file:resources/icons/change-password.png");
        ImageView passwordIconView = new ImageView(passwordIcon);

        passwordIconView.setFitWidth(25);
        passwordIconView.setPreserveRatio(true);

        HBox.setMargin(passwordIconView, new Insets(0, 0, 0, 20));

        Label passwordBtn = new Label("Change Password");
        passwordBtn.setPrefWidth(150);
        passwordBtn.setStyle("-fx-font-size: 14px");

        passwordContainer.getChildren().addAll(passwordIconView, passwordBtn);
        passwordContainer.setAlignment(Pos.CENTER_RIGHT);
        passwordContainer.getStyleClass().add("border-button");

        passwordContainer.setOnMouseClicked(e -> {
            this.errorLbl2.setText("");
            profileContainer.getChildren().remove(profileContent);
            profileContainer.getChildren().removeAll(updateProfileContainer, updatePasswordContainer);
            profileContainer.getChildren().addAll(updatePasswordContainer);
        });

        VBox.setMargin(passwordContainer, new Insets(0, 0, 400, 0));

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

        for (Task task : tasks) {
            TaskItem taskItem = new TaskItem(task);
            this.taskContainer.getChildren().add(taskItem);

            taskItem.setOnMouseClicked(e -> {
                this.mainPane.getChildren().clear();

                String userRole = new MemberController().getRole(task.getClassroom().getClassId());
                this.mainPane.getChildren().add(new TaskBase(task, task.getClassroom(), userRole));

                setLeftNav(task.getClassroom());
            });
        }
    }

    private void setLeftNav(Classroom classroom) {
        Image image = new Image("file:resources/icons/right-arrow.png");
        ImageView icon = new ImageView(image);

        icon.setFitWidth(25);
        icon.setPreserveRatio(true);

        Label lbl = new Label(classroom.getClassName());
        lbl.setStyle("-fx-font-size: 16px;");

        lbl.setOnMouseEntered(e -> {
            lbl.setStyle("-fx-underline: true;-fx-cursor: hand;");
        });

        lbl.setOnMouseExited(e -> {
            lbl.setStyle("-fx-underline: false;");
        });

        lbl.setOnMouseClicked(e -> {
            String userRole = new MemberController().getRole(classroom.getClassId());
            BorderPane classDetail = new ClassroomDetail(classroom, userRole, mainPane);

            setLeftNav(classroom);

            mainPane.getChildren().clear();
            mainPane.getChildren().add(classDetail);
        });

        this.leftNav.getChildren().clear();

        this.leftNav.getChildren().addAll(iconBtn, icon, lbl);
    }

    public void fetchFinishTask() {
        this.taskContainer.getChildren().clear();

        this.pendingTask.getStyleClass().remove("active");
        this.finishedTask.getStyleClass().remove("active");
        this.finishedTask.getStyleClass().add("active");

        ArrayList<Task> tasks = this.taskController.fetchUserFinishedTask(loggedUser.getId());

        for (Task task : tasks) {
            TaskItem taskItem = new TaskItem(task);
            this.taskContainer.getChildren().add(taskItem);
        }

    }

}
