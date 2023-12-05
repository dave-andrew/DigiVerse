package net.slc.dv.view.test;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import net.slc.dv.builder.*;
import net.slc.dv.controller.AnswerController;
import net.slc.dv.controller.TaskController;
import net.slc.dv.enums.QuestionType;
import net.slc.dv.interfaces.QuestionBox;
import net.slc.dv.model.*;
import net.slc.dv.view.task.task.TaskBase;
import net.slc.dv.view.test.question.QuestionEssay;
import net.slc.dv.view.test.question.QuestionMultipleChoice;
import net.slc.dv.view.test.question.QuestionTrueFalse;
import org.jetbrains.annotations.Nullable;

public class DoTestView extends BorderPane {
	private final StackPane mainPane;
	private ScrollPane questionScroll;
	private Button saveButton;
	private Button submitButton;
	private final Task task;
	private final Classroom classroom;
	private final String userRole;
	private final AnswerController answerController;
	private final TaskController taskController;
	private List<Question> questions;
	private List<QuestionBox> questionBoxes;
	private AnswerHeader answerHeaders;
	private final List<AnswerDetail> answerDetails;
	private GridPane questionNumbers;

	public DoTestView(StackPane mainPane, Task task, Classroom classroom, String userRole) {
		this.mainPane = mainPane;
		this.task = task;
		this.classroom = classroom;
		this.userRole = userRole;
		this.answerController = new AnswerController();
		this.taskController = new TaskController();
		this.questions = taskController.fetchQuestion(task.getId());
		this.answerHeaders = answerController.fetchAnswerHeader(
				task.getId(), LoggedUser.getInstance().getId());
		this.answerDetails = answerController.fetchAnswerDetails(answerHeaders);
		this.questionBoxes = new ArrayList<>();

		this.initCenter();
		this.initRight();

		StackPaneBuilder.modify(mainPane).removeAllChildren().addChildren(this).build();
	}

	private void initCenter() {
		this.questionScroll = new ScrollPane();
		VBox questionContainer = new VBox();
		questionContainer.setSpacing(20);
		for (int i = 0; i < questions.size(); i++) {
			Question question = questions.get(i);

			@Nullable
			AnswerDetail answerDetail = answerDetails.stream()
					.filter(answer -> Objects.equals(answer.getQuestionId(), question.getQuestionID()))
					.findFirst()
					.orElse(null);

			if (question.getQuestionType().equals(QuestionType.MULTIPLE_CHOICE)) {
				QuestionMultipleChoice questionBox = new QuestionMultipleChoice(i + 1, question, answerDetail);
				questionContainer.getChildren().add(questionBox);
				questionBoxes.add(questionBox);
				questionBox.getStyleClass().add("card");
				continue;
			}
			if (question.getQuestionType().equals(QuestionType.TRUE_FALSE)) {
				QuestionTrueFalse questionBox = new QuestionTrueFalse(i + 1, question, answerDetail);
				questionContainer.getChildren().add(questionBox);
				questionBoxes.add(questionBox);
				questionBox.getStyleClass().add("card");
				continue;
			}
			if (question.getQuestionType().equals(QuestionType.ESSAY)) {
				QuestionEssay questionBox = new QuestionEssay(i + 1, question, answerDetail);
				questionContainer.getChildren().add(questionBox);
				questionBoxes.add(questionBox);
				questionBox.getStyleClass().add("card");
			}
		}

		VBox container = VBoxBuilder.create()
				.addChildren(questionContainer)
				.setPadding(40, 50, 40, 70)
				.setAlignment(Pos.CENTER)
				.build();

		questionScroll.setContent(container);
		questionScroll.setFitToWidth(true);
		questionScroll.setPannable(true);

		questionScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		questionScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

		this.setCenter(questionScroll);
	}

	private void initRight() {
		Label submitTitle =
				LabelBuilder.create("Submit Task").setStyleClass("title").build();

		Label submitStatus =
				LabelBuilder.create("Not Submitted").setStyleClass("title").build();

		HBox spacer = HBoxBuilder.create().setHgrow(Priority.ALWAYS).build();

		HBox submitStatusContainer = HBoxBuilder.create()
				.addChildren(submitTitle, spacer, submitStatus)
				.setAlignment(Pos.CENTER_LEFT)
				.build();

		this.questionNumbers = this.getQuestionNumbers();
		this.questionNumbers.setPadding(new Insets(20, 0, 0, 0));

		this.saveButton = ButtonBuilder.create("Save")
				.setStyleClass("primary-button")
				.setStyle("-fx-text-fill: #fff;")
				.setPrefSize(300, 40)
				.setOnAction(e -> this.saveAnswer())
				.setVMargin(30, 0, 0, 0)
				.build();

		this.submitButton = ButtonBuilder.create("Submit")
				.setStyleClass("primary-button")
				.setStyle("-fx-text-fill: #fff;")
				.setPrefSize(300, 40)
				.setOnAction(e -> this.submitAnswer())
				.setVMargin(30, 0, 0, 0)
				.build();

		VBox submitContainer = VBoxBuilder.create()
				.addChildren(submitStatusContainer, questionNumbers, saveButton, submitButton)
				.setStyleClass("card")
				.setVgrow(Priority.NEVER)
				.build();

		VBox rightContentContainer = VBoxBuilder.create()
				.addChildren(submitContainer)
				.setPadding(40, 60, 30, 10)
				.build();

		ScrollPane rightScroll = ScrollPaneBuilder.create()
				.setContent(rightContentContainer)
				.setPannable(true)
				.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER)
				.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER)
				.build();

		this.setRight(rightScroll);
	}

	private GridPane getQuestionNumbers() {
		GridPane questionNumbers = new GridPane();
		questionNumbers.setHgap(10);
		questionNumbers.setVgap(10);

		AtomicInteger rowIndex = new AtomicInteger();
		AtomicInteger colIndex = new AtomicInteger();

		for (int i = 0; i < questions.size(); i++) {
			Question question = questions.get(i);

			int finalI = i;
			Button questionNumber = ButtonBuilder.create(String.valueOf(i + 1))
					.setPrefSize(50, 50)
					.setStyleClass("test-button")
					.setOnAction(e -> this.changeQuestion(finalI))
					.build();

			if (questionBoxes.get(i).getQuestionAnswer() != null) {
				questionNumber.getStyleClass().add("submitted");
			}

			questionNumbers.add(questionNumber, colIndex.get(), rowIndex.get());

			if (colIndex.get() == 4) {
				colIndex.set(0);
				rowIndex.getAndIncrement();
				continue;
			}

			colIndex.getAndIncrement();
		}

		return questionNumbers;
	}

	private void changeQuestion(int index) {
		this.questionScroll.setVvalue((double) index / this.questions.size());
	}

	private AnswerHeader saveAnswer() {
		this.answerHeaders = answerController.fetchAnswerHeader(
				task.getId(), LoggedUser.getInstance().getId());
		ArrayList<AnswerDetail> answerDetails = new ArrayList<>();
		for (QuestionBox questionBox : questionBoxes) {

			if (!questionBox.isAnswered()) {
				this.questionNumbers
						.getChildren()
						.get(questionBoxes.indexOf(questionBox))
						.getStyleClass()
						.remove("submitted");
				continue;
			}

			AnswerDetail answerDetail = new AnswerDetail(
					questionBox.getQuestionId(), questionBox.getQuestionAnswer(), questionBox.getAnswerScore());


			this.questionNumbers
					.getChildren()
					.get(questionBoxes.indexOf(questionBox))
					.getStyleClass()
					.add("submitted");

			answerDetails.add(answerDetail);
		}

		return answerController.saveAnswer(
				answerHeaders, task.getId(), LoggedUser.getInstance().getId(), answerDetails);
	}

	private void submitAnswer() {
		AnswerHeader answerHeader = this.saveAnswer();
		answerController.submitTest(answerHeader);

		new TaskBase(mainPane, task, classroom, userRole);
	}
}
