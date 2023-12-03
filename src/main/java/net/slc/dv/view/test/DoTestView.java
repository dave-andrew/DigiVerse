package net.slc.dv.view.test;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import net.slc.dv.builder.*;
import net.slc.dv.controller.TaskController;
import net.slc.dv.enums.QuestionType;
import net.slc.dv.enums.TaskType;
import net.slc.dv.interfaces.QuestionBox;
import net.slc.dv.model.Question;
import net.slc.dv.model.Task;
import net.slc.dv.view.test.question.QuestionEssay;
import net.slc.dv.view.test.question.QuestionMultipleChoice;
import net.slc.dv.view.test.question.QuestionTrueFalse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DoTestView extends HBox {
	private final StackPane mainPane;
	private ScrollPane questionScroll;
	private Button saveButton;
	private Button submitButton;
	private final Task task;
	private final TaskController taskController;
	private List<Question> questions;
	private List<QuestionBox> questionBoxes;

	public DoTestView(StackPane mainPane, Task task) {
		this.mainPane = mainPane;
		this.task = task;
		this.taskController = new TaskController();
		this.questions = taskController.fetchQuestion(task.getId());
		this.questionBoxes = new ArrayList<>();

		this.initCenter();
		this.initRight();

		StackPaneBuilder.modify(mainPane)
				.removeAllChildren()
				.addChildren(this)
				.build();
	}

	private void initCenter() {
		this.questionScroll = new ScrollPane();
		VBox questionContainer = new VBox();
		for (int i = 0; i < questions.size(); i++) {
			Question question = questions.get(i);

			System.out.println("nya" + question.getQuestionType());
			if (question.getQuestionType().equals(QuestionType.MULTIPLE_CHOICE)) {
				QuestionMultipleChoice questionBox = new QuestionMultipleChoice(i + 1, question);
				questionContainer.getChildren().add(questionBox);
				questionBoxes.add(questionBox);
				continue;
			}
			if (question.getQuestionType().equals(QuestionType.TRUE_FALSE)) {
				QuestionTrueFalse questionBox = new QuestionTrueFalse(i + 1, question);
				questionContainer.getChildren().add(questionBox);
				questionBoxes.add(questionBox);
				continue;
			}
			if (question.getQuestionType().equals(QuestionType.ESSAY)) {
				QuestionEssay questionBox = new QuestionEssay(i + 1, question);
				questionContainer.getChildren().add(questionBox);
				questionBoxes.add(questionBox);
			}
		}

		questionScroll.setContent(questionContainer);

		HBoxBuilder.modify(this)
				.addChildren(questionScroll)
				.build();
	}

	private void initRight() {
		Label submitTitle = LabelBuilder.create("Submit Task")
				.setStyleClass("title")
				.build();

		Label submitStatus = LabelBuilder.create("Not Submitted")
				.setStyleClass("title")
				.build();

		HBox spacer = HBoxBuilder.create()
				.setHgrow(Priority.ALWAYS)
				.build();

		HBox submitStatusContainer = HBoxBuilder.create()
				.addChildren(submitTitle, spacer, submitStatus)
				.setAlignment(Pos.CENTER_LEFT)
				.build();


		GridPane questionNumbers = this.getQuestionNumbers();

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
				.setVgrow(Priority.NEVER)
				.build();

		HBoxBuilder.modify(this)
				.addChildren(submitContainer)
				.build();
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
					.setOnAction(e -> this.changeQuestion(finalI))
					.build();

			if (questionBoxes.get(i).getQuestionAnswer() != null) {
				questionNumber.getStyleClass().add("answered");
				//TODO: DEP change this
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

	private void saveAnswer() {
//		for (QuestionBox questionBox : questionBoxes) {
//			if (questionBox.getQuestionAnswer() != null) {
//				taskController.saveAnswer(questionBox.getQuestionAnswer());
//			}
//		}
	}

	private void submitAnswer() {
//		for (QuestionBox questionBox : questionBoxes) {
//			if (questionBox.getQuestionAnswer() != null) {
//				taskController.saveAnswer(questionBox.getQuestionAnswer());
//			}
//		}
//		taskController.submitAnswer(task.getId());
	}
}
