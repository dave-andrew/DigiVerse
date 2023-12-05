package net.slc.dv.view.test.question;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;
import net.slc.dv.builder.*;
import net.slc.dv.enums.QuestionType;
import net.slc.dv.helper.DecimalTextFormatter;
import net.slc.dv.interfaces.QuestionBox;
import net.slc.dv.model.AnswerDetail;
import net.slc.dv.model.Question;
import org.jetbrains.annotations.Nullable;

@Getter
public class QuestionMultipleChoice extends VBox implements QuestionBox {
	private final Question question;
	private final AnswerDetail answerDetail;
	private final RadioButton[] answerButtons;
	private final ToggleGroup answerGroup;
	private String answer;
	private TextField scoreField;

	public QuestionMultipleChoice(int number, Question question, @Nullable AnswerDetail answerDetail) {
		this(number, question, answerDetail, false);
	}

	public QuestionMultipleChoice(int number, Question question, @Nullable AnswerDetail answerDetail, boolean isChecking) {
		this.question = question;
		this.answerDetail = answerDetail;

		Label numberLabel = LabelBuilder.create(number + ". " + question.getQuestionText())
				.setWrapText(true)
				.build();

		VBox questionContainer = VBoxBuilder.create()
				.addChildren(numberLabel)
				.setSpacing(10)
				.build();

		String[] answers = question.getQuestionChoice().split(";");

		this.answerButtons = new RadioButton[4];
		this.answerGroup = new ToggleGroup();

		String[] letters = { "A. ", "B. ", "C. ", "D. " };
		for (int i = 0; i < 4; i++) {
			answerButtons[i] = new RadioButton(letters[i] + answers[i]);
			answerButtons[i].setDisable(isChecking);
			answerButtons[i].setToggleGroup(answerGroup);
		}

		this.setActiveAnswer();

		answerGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				RadioButton selected = (RadioButton) newValue;
				selected.setSelected(true);
				this.answer = selected.getText().substring(3);
			}
		});

		GridPane answerGrid = GridPaneBuilder.create()
				.addChildren(answerButtons[0], 0, 0)
				.addChildren(answerButtons[1], 1, 0)
				.addChildren(answerButtons[2], 0, 1)
				.addChildren(answerButtons[3], 1, 1)
				.setHGap(5)
				.setVGap(5)
				.build();

		VBoxBuilder.modify(this)
				.addChildren(questionContainer, answerGrid)
				.setSpacing(10)
				.build();


		if (!isChecking) {
			return;
		}

		createFieldLabelPair();
	}

	private void createFieldLabelPair() {
		Label fieldLabel = LabelBuilder.create("Score: ")
				.build();

		this.scoreField = TextFieldBuilder.create()
				.setTextFormatter(new DecimalTextFormatter(0, 2, 0, 10))
				.setText(String.valueOf(answerDetail == null ? "0" : answerDetail.getAnswerScore().intValue()))
				.setDisable(true)
				.build();

		HBox fieldLabelPair = HBoxBuilder.create()
				.addChildren(fieldLabel, scoreField)
				.setSpacing(10)
				.build();

		VBoxBuilder.modify(this)
				.addChildren(fieldLabelPair)
				.build();
	}

	private void setActiveAnswer() {
		if (answerDetail == null) {
			return;
		}

		String answer = answerDetail.getAnswerText();

		for (RadioButton answerButton : answerButtons) {
			if (answerButton.getText().substring(3).equals(answer)) {
				answerButton.setSelected(true);
				answerGroup.selectToggle(answerButton);
				this.answer = answerButton.getText().substring(3);
				return;
			}
		}
	}

	@Override
	public QuestionType getQuestionType() {
		return QuestionType.MULTIPLE_CHOICE;
	}

	@Override
	public String getQuestionAnswer() {
		return this.answer;
	}

	@Override
	public String getQuestionId() {
		return this.question.getQuestionID();
	}

	@Nullable
	@Override
	public Double getAnswerScore() {
		if (this.answer == null) {
			return 0.0;
		}

		if (this.answer.equals(this.question.getQuestionAnswer())) {
			return 1.0;
		}

		if (this.answerDetail == null) {
			return null;
		}

		if (this.answerDetail.getAnswerScore() != null) {
			return this.answerDetail.getAnswerScore();
		}

		return null;
	}

	@Override
	public boolean isAnswered() {

		return this.answer != null;
	}
}
