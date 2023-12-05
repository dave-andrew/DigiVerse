package net.slc.dv.view.task.task;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import net.slc.dv.builder.*;
import net.slc.dv.constant.Icon;
import net.slc.dv.controller.AnswerController;
import net.slc.dv.controller.CommentController;
import net.slc.dv.enums.TaskType;
import net.slc.dv.helper.DateManager;
import net.slc.dv.helper.ImageManager;
import net.slc.dv.model.*;
import net.slc.dv.view.classroom.detail.component.CommentItem;
import net.slc.dv.view.task.upload.UploadFileModal;
import net.slc.dv.view.test.DoTestView;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TaskDetailView extends HBox {
	private final StackPane mainPane;
	private final AnswerController answerController;
	private final CommentController commentController;
	private final Task task;
	private final String userRole;
	private final Classroom classroom;
	private VBox mainContent, sideContent, fileContainer;
	private HBox innerMainContent;
	private Button actionButton, markAsDoneBtn;
	private Label submitStatus;
	private Button downloadBtn;
	private VBox commentListContainer;

	public TaskDetailView(StackPane mainPane, Task task, Classroom classroom, String userRole) {
		this.mainPane = mainPane;
		this.task = task;
		this.classroom = classroom;
		this.userRole = userRole;
		this.answerController = new AnswerController();
		this.commentController = new CommentController();
		init();
		setLayout();
		initSideContent();
		actions();
	}

	private void init() {
		this.mainContent = new VBox();
		this.innerMainContent = new HBox();

		this.setPadding(new Insets(35, 60, 35, 60));
		this.prefWidthProperty().bind(this.widthProperty());
	}

	private void setLayout() {
		ImageView imgView = ImageViewBuilder.create()
				.setImage(new Image(Icon.TASK))
				.setFitHeight(40)
				.setFitWidth(40)
				.build();

		StackPane imgStack = StackPaneBuilder.create()
				.addChildren(imgView)
				.setAlignment(Pos.TOP_LEFT)
				.setHMargin(0, 20, 0, 0)
				.setMaxHeight(40)
				.setStyleClass("circle-bg")
				.build();

		Label taskName = LabelBuilder.create(task.getTitle())
				.setStyleClass("title")
				.build();

		String postedByString = "Posted by: " + task.getUser().getUsername() + " \u2022 " + DateManager.ddMMMyy(task.getCreatedAt());
		Label postedBy = LabelBuilder.create(postedByString)
				.setVMargin(20, 0, 0, 0)
				.build();

		VBox detail = VBoxBuilder.create()
				.addChildren(taskName, postedBy)
				.setHgrow(Priority.ALWAYS)
				.build();

		this.commentListContainer = VBoxBuilder.create()
				.setSpacing(10)
				.setAlignment(Pos.CENTER_LEFT)
				.setHgrow(Priority.ALWAYS)
				.setPadding(20, 0, 0, 0)
				.build();

		AnswerHeader answerHeader = this.answerController.fetchAnswerHeader(task.getId(), LoggedUser.getInstance().getId());

		Label score = new Label();



		if(answerHeader != null) {
			score = LabelBuilder.create(answerHeader.getScore() == null ? "Score: -" : "Score: " + answerHeader.getScore() )
					.setHgrow(Priority.ALWAYS)
					.setAlignment(Pos.CENTER_LEFT)
					.build();
		}
		HBox spacer = HBoxBuilder.create()
				.setHgrow(Priority.ALWAYS)
				.build();


		Label deadline = LabelBuilder.create("Deadline: " + DateManager.ddMMMyy(task.getDeadlineAt()))
				.setHgrow(Priority.ALWAYS)
				.setAlignment(Pos.CENTER_RIGHT)
				.build();


		HBox scoreDeadlineBox = HBoxBuilder.create()
				.addChildren(score, spacer, deadline)
				.setAlignment(Pos.CENTER_LEFT)
				.setStyleClass("bottom-border")
				.build();

		detail.getChildren().add(scoreDeadlineBox);

		Line line = new Line();
		line.setStroke(Color.valueOf("#E0E0E0"));
		line.endXProperty().bind(detail.widthProperty());
		line.endXProperty().bind(innerMainContent.widthProperty().subtract(75));


		HBox taskDescContainer = new HBox();
		taskDescContainer.setAlignment(Pos.CENTER_LEFT);

		taskDescContainer.getStyleClass().add("bottom-border");
		taskDescContainer.setPadding(new Insets(20, 0, 20, 0));

		Label taskDesc = new Label(task.getDescription());
		HBox.setHgrow(taskDesc, Priority.ALWAYS);
		taskDesc.setPadding(new Insets(10, 0, 5, 0));
		taskDesc.setWrapText(true);

		taskDescContainer.getChildren().add(taskDesc);

		detail.getChildren().add(taskDescContainer);

		VBox userComment = new VBox();
		userComment.setAlignment(Pos.CENTER_LEFT);

		Label commentTitle = new Label("Add Comment:");
		commentTitle.getStyleClass().add("title");

		userComment.getChildren().add(commentTitle);

		if (userRole.equals("Student")) {
			HBox commentInputContainer = new HBox(10);
			commentInputContainer.setPadding(new Insets(10, 10, 10, 10));

			ImageView userImg;
			if (LoggedUser.getInstance().getProfileImage() == null) {
				userImg = new ImageView(new Image("file:resources/icons/user.png"));

				userImg.setFitWidth(30);
				userImg.setFitHeight(30);

			} else {
				userImg = new ImageView(LoggedUser.getInstance().getProfileImage());
			}

			ImageManager.makeCircular(userImg, 15);

			commentInputContainer.getChildren().add(userImg);

			TextField commentInput = new TextField();
			HBox.setHgrow(commentInput, Priority.ALWAYS);
			commentInput.setPromptText("Write your comment here...");

			commentInputContainer.getChildren().add(commentInput);

			commentInput.setOnKeyPressed(e -> {
				if (e.getCode().toString().equals("ENTER")) {
					TaskComment newTaskComment = this.commentController.createTaskComment(commentInput.getText(), task.getId(), LoggedUser.getInstance().getId());


					HBox commentItem = new CommentItem(newTaskComment);
					commentListContainer.getChildren().add(0, commentItem);

					commentInput.clear();
				}
			});

			userComment.getChildren().add(commentInputContainer);

			detail.getChildren().add(userComment);
		}

		if (this.userRole.equals("Teacher")) {
			ArrayList<TaskComment> taskCommentList = this.commentController.getTaskComments(task.getId());

			for (TaskComment taskComment : taskCommentList) {

				VBox commentContainer = new VBox();

				CommentItem commentItem = new CommentItem(taskComment);
				commentContainer.getChildren().add(commentItem);

				commentListContainer.getChildren().add(commentContainer);
			}
		} else if (this.userRole.equals("Student")) {
			ArrayList<TaskComment> taskCommentList = this.commentController.getStudentTaskComments(task.getId());

			for (TaskComment taskComment : taskCommentList) {

				VBox commentContainer = new VBox();

				CommentItem commentItem = new CommentItem(taskComment);
				commentContainer.getChildren().add(commentItem);

				commentListContainer.getChildren().add(commentContainer);
			}
		}

		detail.getChildren().add(commentListContainer);

		innerMainContent.getChildren().addAll(imgStack, detail);
		innerMainContent.setAlignment(Pos.TOP_LEFT);

		mainContent.getChildren().add(innerMainContent);
		mainContent.getStyleClass().add("card");
		mainContent.setAlignment(Pos.TOP_LEFT);
		HBox.setHgrow(mainContent, Priority.ALWAYS);
//        mainContent.setMaxWidth(800);

		this.sideContent = new VBox();
		HBox.setMargin(sideContent, new Insets(0, 0, 0, 50));

		this.getChildren().addAll(mainContent, sideContent);
	}

	private void initSideContent() {
		if (!this.userRole.equals("Student")) {
			return;
		}

		Label submitTitle = LabelBuilder.create("Task")
				.setStyleClass("title")
				.build();

		String status = this.getStatus();
		this.submitStatus = LabelBuilder.create(status)
				.setStyle("-fx-text-size: 14px; -fx-text-fill: #F44336;")
				.build();

		if (status.equals("Submitted")) {
			this.submitStatus.setStyle("-fx-text-fill: #4CAF50;");
		} else if (status.equals("Done")) {
			this.submitStatus.setStyle("-fx-text-fill: #4CAF50;");
		}

		HBox spacer = HBoxBuilder.create()
				.setHgrow(Priority.ALWAYS)
				.build();

		HBox submitStatusContainer = HBoxBuilder.create()
				.addChildren(submitTitle, spacer, submitStatus)
				.setAlignment(Pos.CENTER_LEFT)
				.build();

		this.actionButton = getActionButton(status);

		this.markAsDoneBtn = ButtonBuilder.create("Mark as Done")
				.setStyleClass("secondary-button")
				.setDisable(!(status.equals("Not Submitted") || status.equals("Not Attempted")))
				.setPrefSize(300, 40)
				.setVMargin(10, 0, 0, 0)
				.build();

		VBox submitContainer = VBoxBuilder.create()
				.addChildren(submitStatusContainer, actionButton, markAsDoneBtn)
				.setStyleClass("card")
				.setVgrow(Priority.NEVER)
				.build();

		VBoxBuilder.modify(this.sideContent)
				.addChildren(submitContainer)
				.setAlignment(Pos.TOP_CENTER)
				.build();



		Label constraintTitle = LabelBuilder.create("Constraints").build();

		VBox constraintBox = VBoxBuilder.create()
				.addChildren(constraintTitle)
				.setAlignment(Pos.CENTER_LEFT)
				.setVMargin(30, 0, 0, 0)
				.setStyleClass("card")
				.build();

		String[] constraints = new String[]{
				"1. Max file size 100Kb",
				"2. File type any."
		};

		for (String constraint : constraints) {
			Label constraintLabel = LabelBuilder.create(constraint)
					.setStyle("-fx-font-size: 14px;")
					.setMargin(5, 0, 0, 0)
					.build();

			constraintBox.getChildren().add(constraintLabel);
		}

		sideContent.getChildren().add(constraintBox);
		sideContent.setPrefWidth(330);
		this.fileContainer = new VBox();
		fileContainer.setAlignment(Pos.CENTER_LEFT);
		fileContainer.getStyleClass().add("card");

		Label fileTitle = new Label("Your Answer: ");
		fileContainer.getChildren().add(fileTitle);
		VBox.setMargin(fileContainer, new Insets(30, 0, 0, 0));

		this.downloadBtn = new Button("Download");
		downloadBtn.setStyle("-fx-text-fill: #fff;");
		downloadBtn.setPrefSize(300, 40);
		VBox.setMargin(downloadBtn, new Insets(10, 0, 0, 0));

		fileContainer.getChildren().add(downloadBtn);

		fetchAnswer();
	}

	private Button getActionButton(String status) {
		if (this.task.getTaskType() == TaskType.FILE) {
			return ButtonBuilder.create("+ Upload File")
					.setStyleClass("primary-button")
					.setStyle("-fx-text-fill: #fff;")
					.setPrefSize(300, 40)
					.setDisable(status.equals("Submitted") || this.checkIsDeadline())
					.setOnAction(e -> this.uploadFile())
					.setVMargin(30, 0, 0, 0)
					.setVMargin(30, 0, 0, 0)
					.build();
		}

		return ButtonBuilder.create("+ Do Test")
				.setStyleClass("primary-button")
				.setStyle("-fx-text-fill: #fff;")
				.setDisable(status.equals("Done") || this.checkIsDeadline())
				.setPrefSize(300, 40)
				.setOnAction(e -> this.doTest())
				.setVMargin(30, 0, 0, 0)
				.setVMargin(30, 0, 0, 0)
				.build();
	}

	private String getStatus() {
		if (this.task.getTaskType() == TaskType.FILE) {
			if (this.answerController.checkAnswer(this.task.getId(), LoggedUser.getInstance().getId())) {
				return "Submitted";
			}
			return "Not Submitted";
		}
		if(this.answerController.checkTest(this.task.getId(), LoggedUser.getInstance().getId())){
			return "Done";
		}

		return "Not Done";
	}


	private void uploadFile() {
		if (checkIsDeadline()) {
			return;
		}

		new UploadFileModal(task.getId(), fileContainer, sideContent, downloadBtn);
		fetchAnswer();
	}

	private void doTest() {
		if (checkIsDeadline()) {
			return;
		}

		new DoTestView(mainPane, task, classroom, userRole);
	}

	private ArrayList<File> fetchAnswer() {
		if (this.task.getTaskType() != TaskType.FILE) {
			return new ArrayList<>();
		}

		if (this.answerController.checkAnswer(this.task.getId(), LoggedUser.getInstance().getId())) {
			submitStatus.setText("Submitted");

			ArrayList<File> fileList = this.answerController.getMemberFileAnswer(this.task.getId(), LoggedUser.getInstance().getId());

			if (!sideContent.getChildren().contains(fileContainer)) {
				sideContent.getChildren().add(fileContainer);
			}

			if (!fileList.isEmpty()) {
				downloadBtn.getStyleClass().add("primary-button");
				downloadBtn.setOnMouseClicked(e -> this.answerController.downloadAllAnswer(fileList));
			}

			return fileList;
		}

		sideContent.getChildren().remove(fileContainer);
		return new ArrayList<>();
	}

	private void actions() {
		if(!userRole.equals("Student")){
			return;
		}

		this.markAsDoneBtn.setOnMouseClicked(e -> {

			if (checkIsDeadline()) {
				return;
			}

			ArrayList<File> fileList = fetchAnswer();

			if (submitStatus.getText().equals("Not Submitted")) {
				this.answerController.markAsDone(task.getId(), LoggedUser.getInstance().getId());
				this.submitStatus.setText("Submitted");
				this.submitStatus.setStyle("-fx-text-fill: #4CAF50;");
				this.sideContent.getChildren().add(fileContainer);
			} else if (submitStatus.getText().equals("Submitted") && fileList.isEmpty()) {
				this.answerController.markUndone(task.getId(), LoggedUser.getInstance().getId());
				this.submitStatus.setText("Not Submitted");
				this.submitStatus.setStyle("-fx-text-fill: #F44336;");
				this.sideContent.getChildren().remove(fileContainer);
			} else if(submitStatus.getText().equals("Done")){
				this.answerController.markUndone(task.getId(), LoggedUser.getInstance().getId());
				this.submitStatus.setText("Not Done");
				this.submitStatus.setStyle("-fx-text-fill: #4CAF50;");
			} else {
				this.answerController.markAsDone(task.getId(), LoggedUser.getInstance().getId());
				this.submitStatus.setText("Done");
				this.submitStatus.setStyle("-fx-text-fill: #F44336;");
			}
		});
	}

	private boolean checkIsDeadline() {
		String nowString = DateManager.getNow();
		String deadlineString = task.getDeadlineAt();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			Date now = dateFormat.parse(nowString);
			Date deadline = dateFormat.parse(deadlineString);

			if (now.compareTo(deadline) > 0) {
				return true;
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

}
