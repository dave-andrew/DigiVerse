package net.slc.dv.view.task.task;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import net.slc.dv.builder.ButtonBuilder;
import net.slc.dv.builder.VBoxBuilder;
import net.slc.dv.constant.Icon;
import net.slc.dv.controller.AnswerController;
import net.slc.dv.controller.MemberController;
import net.slc.dv.controller.TaskController;
import net.slc.dv.enums.QuestionType;
import net.slc.dv.enums.TaskType;
import net.slc.dv.interfaces.QuestionBox;
import net.slc.dv.model.*;
import net.slc.dv.view.classroom.detail.component.MemberItem;
import net.slc.dv.view.test.question.QuestionEssay;
import net.slc.dv.view.test.question.QuestionMultipleChoice;
import net.slc.dv.view.test.question.QuestionTrueFalse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class AnswerDetailView extends HBox {

	private final MemberController memberController;
	private final AnswerController answerController;
	private final TaskController taskController;
	private List<QuestionBox> questionBoxes;
	private final Task task;
	private final Classroom classroom;
	private VBox memberAnswerContainer;
	private VBox memberList;
	private int idx = 1;

	public AnswerDetailView(Task task, Classroom classroom) {
		this.memberController = new MemberController();
		this.answerController = new AnswerController();
		this.taskController = new TaskController();
		this.questionBoxes = new ArrayList<>();
		this.task = task;
		this.classroom = classroom;
		init();
		setLayout();
	}

	private void init() {
		VBox memberContainer = new VBox();
		this.memberList = new VBox();
		this.memberList.setPadding(new Insets(20));

		memberContainer.setPrefWidth(500);
		memberContainer.setStyle("-fx-border-color: transparent #e0e0e0 transparent transparent;");

		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setContent(memberList);
		scrollPane.setFitToWidth(true);
		scrollPane.setPannable(true);
		scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);

		memberContainer.getChildren().add(scrollPane);

		this.memberAnswerContainer = new VBox();
		this.memberAnswerContainer.setPadding(new Insets(20));
		ScrollPane scrollAnswerContainer = new ScrollPane();

		scrollAnswerContainer.setPannable(true);
		scrollAnswerContainer.setContent(memberAnswerContainer);
		scrollAnswerContainer.setFitToWidth(true);

		Label fileTitle = new Label("Choose a student to see the answer!");
		this.memberAnswerContainer.getChildren().addAll(fileTitle);

		this.getChildren().addAll(memberContainer, scrollAnswerContainer);
	}

	private void setLayout() {
		fetchMember();
	}

	private void fetchMember() {
		memberController.getClassMember(classroom.getClassId()).forEach(member -> {
			if (member.getRole().equals("Student")) {
				MemberItem memberItem = new MemberItem(member, idx);
				memberList.getChildren().add(memberItem);

				memberItem.getStyleClass().add("task-item");

				memberItem.setOnMouseClicked(e -> {
					fetchAnswer(member);
				});
				idx++;
			}
		});
	}

	private void fetchAnswer(ClassroomMember member) {
		memberAnswerContainer.getChildren().clear();


		Label fileTitle = new Label("Student Answer");
		fileTitle.setStyle("-fx-font-size: 20px;");
		this.memberAnswerContainer.getChildren().addAll(fileTitle);
		VBox.setMargin(fileTitle, new Insets(0, 0, 20, 0));

		if (task.getTaskType().equals(TaskType.FILE)) {
			getFileList(member);
			return;
		}

		getQuestionAnswer(member);
	}

	private void getFileList(ClassroomMember member) {
		GridPane fileList = new GridPane();
		fileList.setHgap(10);
		fileList.setVgap(10);

		AtomicInteger rowIndex = new AtomicInteger();
		AtomicInteger colIndex = new AtomicInteger();

		ArrayList<File> files = answerController.getMemberFileAnswer(this.task.getId(), member.getUser().getId());

		files.forEach(answer -> {

			HBox fileItem = new HBox();

			if (answer.getName().endsWith(".pdf")) {
				Image image = new Image(Icon.PDF);
				ImageView icon = new ImageView(image);

				icon.setFitWidth(25);
				icon.setPreserveRatio(true);

				fileItem.getChildren().addAll(icon);

			} else if (answer.getName().endsWith(".png") || answer.getName().endsWith(".jpg") || answer.getName().endsWith(".jpeg")) {

				Image image = new Image(Icon.IMAGE);
				ImageView icon = new ImageView(image);

				icon.setFitWidth(25);
				icon.setPreserveRatio(true);

				fileItem.getChildren().addAll(icon);

			} else {
				Image image = new Image(Icon.FILE);
				ImageView icon = new ImageView(image);

				icon.setFitWidth(25);
				icon.setPreserveRatio(true);

				fileItem.getChildren().addAll(icon);

			}

			Label fileName = new Label(answer.getName());
			HBox.setMargin(fileName, new Insets(0, 0, 0, 10));

			fileItem.getChildren().addAll(fileName);
			fileItem.getStyleClass().add("card");
			fileItem.setOnMouseEntered(e -> {
				fileItem.setStyle("-fx-background-color: #f0f0f0;-fx-cursor: hand;");
			});

			fileItem.setOnMouseExited(e -> {
				fileItem.setStyle("-fx-background-color: #fff;");
			});

			fileList.add(fileItem, colIndex.get(), rowIndex.get());

			colIndex.getAndIncrement();

			if (colIndex.get() >= 3) {
				colIndex.set(0);
				rowIndex.getAndIncrement();
			}

			fileItem.setOnMouseClicked(e -> {
				answerController.downloadAnswer(answer);
			});
		});

		this.memberAnswerContainer.getChildren().addAll(fileList);
	}

	private void getQuestionAnswer(ClassroomMember member) {
		AnswerHeader answerHeader = answerController.fetchAnswerHeader(this.task.getId(), member.getUser().getId());

		if(answerHeader == null || answerHeader.getFinishedAt() == null) {
			return;
		}

		List<Question> questions = taskController.fetchQuestion(task.getId());
		List<AnswerDetail> answerDetails = answerController.getMemberQuestionAnswer(task.getId(), member.getUser().getId());

		// TODO DEP PRETTIFY PLZ
		Button saveButton = ButtonBuilder.create("Save")
				.setPrefWidth(100)
				.setStyleClass("primary-button")
				.setStyle("-fx-text-fill: white;")
				.setVMargin(30, 0, 0, 0)
				.setOnAction(e -> this.saveScore(member))
				.bindPrefWidth(memberAnswerContainer)
				.build();

		VBox answerContainer = VBoxBuilder.create()
				.setSpacing(30)
				.build();


		for (int i = 0; i < questions.size(); i++) {
			Question question = questions.get(i);
			AnswerDetail answerDetail = answerDetails
					.stream()
					.filter(answer -> Objects.equals(answer.getQuestionId(), question.getQuestionID()))
					.findFirst()
					.orElse(null);

			if (question.getQuestionType().equals(QuestionType.MULTIPLE_CHOICE)) {
				QuestionMultipleChoice questionBox = new QuestionMultipleChoice(i + 1, question, answerDetail, true);
				answerContainer.getChildren().add(questionBox);
				questionBox.getStyleClass().add("card");
				questionBoxes.add(questionBox);
				continue;
			}
			if (question.getQuestionType().equals(QuestionType.TRUE_FALSE)) {
				QuestionTrueFalse questionBox = new QuestionTrueFalse(i + 1, question, answerDetail, true);
				answerContainer.getChildren().add(questionBox);
				questionBox.getStyleClass().add("card");
				questionBoxes.add(questionBox);
				continue;
			}
			if (question.getQuestionType().equals(QuestionType.ESSAY)) {
				QuestionEssay questionBox = new QuestionEssay(i + 1, question, answerDetail, true);
				answerContainer.getChildren().add(questionBox);
				questionBox.getStyleClass().add("card");
				questionBoxes.add(questionBox);
			}
		}

		this.memberAnswerContainer.getChildren().addAll(answerContainer, saveButton);
	}

	private void saveScore(ClassroomMember member){
		List<AnswerDetail> answerDetails = new ArrayList<>();
		AnswerHeader answerHeader = this.answerController.fetchAnswerHeader(this.task.getId(), member.getUser().getId());

		assert answerHeader != null;

		this.questionBoxes.forEach(questionBox -> {
			AnswerDetail answerDetail = new AnswerDetail(
					answerHeader.getId(),
					questionBox.getQuestionId(),
					questionBox.getQuestionAnswer(),
					questionBox.isAnswered() ? questionBox.getAnswerScore() : 0.0
			);
			answerDetails.add(answerDetail);
		});

		answerController.scoreQuestionAnswer(answerDetails);

		AtomicBoolean isDone = new AtomicBoolean(true);
		this.questionBoxes.forEach(questionBox -> {
			if(questionBox.getAnswerScore() == null) {
				isDone.set(false);
			}
		});

		if(isDone.get()) {
			answerController.finishScoring(this.task.getId(), member.getUser().getId(), answerHeader.getId(), this.calculateScore());
		}
	}

	private Double calculateScore(){
		AtomicReference<Double> score = new AtomicReference<>(0.0);
		this.questionBoxes.forEach(questionBox -> {
			if(questionBox instanceof QuestionEssay) {
				assert questionBox.getAnswerScore() != null;
				score.updateAndGet(v -> v + (questionBox.getAnswerScore() / 10));
				return;
			}

			score.updateAndGet(v -> v + questionBox.getAnswerScore());
		});

		System.out.println("Hello:" + score.get());
		score.updateAndGet(v -> v / this.questionBoxes.size());


		System.out.println(score.get());
		return score.get();
	}
}
