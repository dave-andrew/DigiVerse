package net.slc.dv.view.homeview;

import javafx.geometry.Insets;
import builder.*;
import controller.TaskController;
import helper.DateManager;
import helper.ScreenManager;
import helper.ThemeManager;
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
import net.slc.dv.builder.ButtonBuilder;
import net.slc.dv.controller.TaskController;
import net.slc.dv.helper.DateManager;
import net.slc.dv.helper.ScreenManager;
import net.slc.dv.helper.ThemeManager;
import net.slc.dv.model.Classroom;
import net.slc.dv.view.Profile;
import net.slc.dv.view.component.TimeSpinner;
import model.Classroom;
import view.component.classtask.CreateFileTask;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AddTask extends BorderPane {

	enum CenterType {
		QUESTION, FILE
	}
	private Classroom classroom;
	private Stage stage;
	private TaskController taskController;

	private Stage dialogStage;
	private Scene dialogScene;

	private BorderPane root;

	//    NAVBAR
	private HBox leftNav;
	private ImageView close;
	private Label title;
	private Button joinBtn;
	private Button closeBtn;

	//  CENTER FORM
	private CheckBox scored;
	private DatePicker datePicker;
	private TimeSpinner timeSpinner;
	private CenterType centerType;
	private CreateFileTask createFileTask;

	public AddTask(Stage stage, Classroom classroom) {
		this.classroom = classroom;
		this.stage = stage;
		this.taskController = new TaskController();
		this.centerType = CenterType.FILE;
		this.createFileTask = new CreateFileTask();
		init();

	}

	private void init() {
		this.dialogStage = new Stage();
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		dialogStage.initOwner(stage);
//        dialogStage.initStyle(StageStyle.TRANSPARENT);

		this.root = new BorderPane();
		this.root.setRight(rightBar());
		this.root.setTop(navBar());
		this.changeCenter();

		dialogScene = new Scene(root, ScreenManager.SCREEN_WIDTH, ScreenManager.SCREEN_HEIGHT);
		dialogStage.setScene(dialogScene);

		ThemeManager.getTheme(dialogScene);

		dialogStage.setTitle("Add Task");
		dialogStage.showAndWait();
	}

	private void changeCenter() {
		if(this.centerType == CenterType.FILE) {
			this.root.setCenter(this.createFileTask.getRoot());
		}
		else {
			this.root.setCenter(centerQuestion());
		}
	}

	private VBox centerQuestion() {
		return new VBox();
	}


	private HBox navBar() {
		close = ImageViewBuilder.create()
				.setImage(new Image("file:resources/icons/close.png"))
				.setFitWidth(20)
				.setPreserveRatio(true)
				.build();

		closeBtn = ButtonBuilder.create()
				.setGraphic(close)
				.setStyleClass("image-button")
				.setOnAction(e -> dialogStage.close())
				.build();

		title = LabelBuilder.create("Create New Task")
				.setStyleClass("title")
				.setPadding(10, 0, 10, 0)
				.setHgrow(Priority.ALWAYS)
				.build();

		leftNav = HBoxBuilder.create()
				.addChildren(closeBtn, title)
				.setAlignment(Pos.CENTER_LEFT)
				.setHgrow(Priority.ALWAYS)
				.setSpacing(20)
				.build();

		return HBoxBuilder.create()
				.addChildren(leftNav)
				.setStyleClass("nav-bar")
				.setAlignment(Pos.CENTER)
				.build();
	}

	private void submitForm() {
		String title = this.createFileTask.getTitleField().getText();
		String description = this.createFileTask.getDescriptionField().getText();
		LocalDate deadline = this.datePicker.getValue();
		LocalTime time = this.timeSpinner.getValue();
		boolean scored = this.scored.isSelected();

		String deadlineAt = DateManager.formatDate(deadline, time);

		if (title.isEmpty() || description.isEmpty()) {
			this.createFileTask.getErrorLbl().setText("Please fill all the fields");
			return;
		}

		this.taskController.createTask(title, description, deadlineAt, scored, classroom.getClassId());

		dialogStage.close();
	}

	private VBox rightBar() {
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

		VBox dateTimeBox = VBoxBuilder.create()
				.addChildren(dateTimeLabel, datePicker, timeSpinner)
				.setSpacing(10)
				.setAlignment(Pos.CENTER_LEFT)
				.setPadding(30, 40, 0, 40)
				.build();

		VBox dateTimeContainer = VBoxBuilder.create()
				.addChildren(dateTimeBox)
				.build();

		Label scoreLabel = new Label("Scored");
		this.scored = new CheckBox();
		scored.setSelected(false);

		VBox scoreBox = VBoxBuilder.create()
				.addChildren(scoreLabel, scored)
				.setSpacing(10)
				.setAlignment(Pos.CENTER_LEFT)
				.setPadding(30, 40, 0, 40)
				.build();

		VBox spacer = VBoxBuilder.create()
				.setVgrow(Priority.ALWAYS)
				.build();

		joinBtn = ButtonBuilder.create("Create Task")
				.setStyleClass("primary-button")
				.setStyle("-fx-text-fill: white;")
				.setPrefWidth(300)
				.setOnAction(e -> this.submitForm())
				.setVMargin(0, 0, 50, 0)
				.build();

		Button questionBtn = ButtonBuilder.create("Create Question")
				.setStyleClass("primary-button")
				.setStyle("-fx-text-fill: white;")
				.setPrefWidth(300)
				.setOnAction(e -> {
					if(this.centerType == CenterType.FILE) {
						this.centerType = CenterType.QUESTION;
					}
					else {
						this.centerType = CenterType.FILE;
					}

					this.changeCenter();
				})
				.build();

		return VBoxBuilder.create()
				.addChildren(questionBtn, dateTimeContainer, scoreBox, spacer, joinBtn)
				.setAlignment(Pos.CENTER)
				.setStyleClass("side-nav")
				.build();
	}

}
