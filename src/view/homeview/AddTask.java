package view.homeview;

import controller.TaskController;
import helper.DateManager;
import helper.ScreenManager;
import helper.ThemeManager;
import javafx.geometry.Insets;
import model.Forum;
import model.LoggedUser;
import model.Task;
import view.Profile;
import view.component.TimeSpinner;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Classroom;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AddTask extends BorderPane {

    private Classroom classroom;
    private Stage stage;
    private TaskController taskController;

    private Stage dialogStage;
    private Scene dialogScene;

    private BorderPane root;

    //    NAVBAR
    private HBox leftNav;
    private Image closeImg;
    private ImageView close;
    private Label title, errorLbl;
    private Button joinBtn;
    private Button closeBtn;

    //  CENTER FORM
    private TextField titleField;
    private TextArea descriptionField;
    private CheckBox scored;
    private DatePicker datePicker;
    private TimeSpinner timeSpinner;

    public AddTask(Stage stage, Classroom classroom) {
        this.classroom = classroom;
        this.stage = stage;
        this.taskController = new TaskController();
        init();

    }

    private void init() {
        this.dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(stage);
//        dialogStage.initStyle(StageStyle.TRANSPARENT);

        errorLbl = new Label();
        errorLbl.setStyle("-fx-text-fill: red;");

        this.root = new BorderPane();
        this.root.setRight(rightBar());
        this.root.setTop(navBar());
        this.root.setCenter(center());

        dialogScene = new Scene(root, ScreenManager.SCREEN_WIDTH, ScreenManager.SCREEN_HEIGHT);
        dialogStage.setScene(dialogScene);

        ThemeManager.getTheme(dialogScene);

        dialogStage.setTitle("Add Task");
        dialogStage.showAndWait();
    }

    private VBox center() {
        HBox center = new HBox();
        center.setAlignment(Pos.CENTER);

        VBox container = new VBox(center);
        container.setAlignment(Pos.CENTER);

        VBox content = new VBox(5);

        Label title = new Label("Task Title");
        this.titleField = new TextField();

        Label description = new Label("Task Description");
        this.descriptionField = new TextArea();
        VBox.setMargin(description, new Insets(30, 0, 0, 0));

        Label addFileBtn = new Label();
        VBox.setMargin(addFileBtn, new Insets(100, 0, 0, 0));

        container.getChildren().add(addFileBtn);

        HBox errorContainer = new HBox();
        errorContainer.setAlignment(Pos.CENTER);

        errorContainer.getChildren().add(errorLbl);

        content.getChildren().addAll(title, titleField, description, descriptionField, errorContainer);
        content.getStyleClass().add("card");

        center.getChildren().addAll(content);

        return container;
    }

    private HBox navBar() {
        HBox container = new HBox();

        leftNav = new HBox(20);

        closeImg = new Image("file:resources/icons/close.png");
        close = new ImageView(closeImg);
        close.setFitWidth(20);
        close.setPreserveRatio(true);

        closeBtn = new Button();
        closeBtn.setGraphic(close);
        closeBtn.getStyleClass().add("image-button");

        title = new Label("Create New Task");
        title.getStyleClass().add("title");
        title.setPadding(new Insets(10, 0, 10, 0));

        HBox.setHgrow(title, Priority.ALWAYS);

        leftNav.getChildren().addAll(closeBtn, title);
        leftNav.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(leftNav, Priority.ALWAYS);

        closeBtn.setOnAction(e -> {
            this.dialogStage.close();
        });


        container.getChildren().addAll(leftNav);
        container.getStyleClass().add("nav-bar");
        container.setAlignment(Pos.CENTER);

        return container;
    }

    private void submitForm() {
        String title = this.titleField.getText();
        String description = this.descriptionField.getText();
        LocalDate deadline = this.datePicker.getValue();
        LocalTime time = this.timeSpinner.getValue();
        boolean scored = this.scored.isSelected();

        String deadlineAt = DateManager.formatDate(deadline, time);

        if (title.isEmpty() || description.isEmpty()) {
            errorLbl.setText("Please fill all the fields");
            return;
        }

        this.taskController.createTask(title, description, deadlineAt, scored, classroom.getClassId());

        dialogStage.close();
    }

    private VBox rightBar() {
        VBox rightBar = new VBox();
        VBox dateTimeContainer = new VBox();
        Label dateTimeLabel = new Label("Deadline");
        this.datePicker = new DatePicker();

        datePicker.setValue(LocalDate.now());

        Profile.DateFormatter(datePicker);

        this.timeSpinner = new TimeSpinner();
        timeSpinner.getValueFactory().setValue(LocalTime.of(23, 59, 59));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");

        timeSpinner.valueProperty().addListener((obs, oldTime, newTime) -> {
            timeSpinner.getEditor().setText(newTime.format(formatter));
        });

        VBox dateTimeBox = new VBox();
        dateTimeBox.getChildren().addAll(dateTimeLabel, datePicker, timeSpinner);
        dateTimeBox.setSpacing(10);
        dateTimeBox.setAlignment(Pos.CENTER_LEFT);
        dateTimeBox.setPadding(new Insets(30, 40, 0, 40));

        dateTimeContainer.getChildren().addAll(dateTimeBox);

        Label scoreLabel = new Label("Scored");
        this.scored = new CheckBox();
        scored.setSelected(false);

        VBox scoreBox = new VBox();
        scoreBox.getChildren().addAll(scoreLabel, scored);
        scoreBox.setSpacing(10);
        scoreBox.setAlignment(Pos.CENTER_LEFT);
        scoreBox.setPadding(new Insets(30, 40, 0, 40));

        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        joinBtn = new Button("Create Task");
        joinBtn.getStyleClass().add("primary-button");
        joinBtn.setStyle("-fx-text-fill: white;");
        joinBtn.setPrefWidth(300);

        VBox.setMargin(joinBtn, new Insets(0, 0, 50, 0));

        joinBtn.setOnAction(e -> {
            submitForm();
        });


        rightBar.getChildren().addAll(dateTimeContainer, scoreBox, spacer, joinBtn);
        rightBar.setAlignment(Pos.CENTER);

        rightBar.getStyleClass().add("side-nav");

        return rightBar;
    }

}
