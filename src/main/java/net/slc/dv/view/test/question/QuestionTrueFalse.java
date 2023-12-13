package net.slc.dv.view.test.question;

import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import net.slc.dv.builder.*;
import net.slc.dv.enums.QuestionType;
import net.slc.dv.helper.DecimalTextFormatter;
import net.slc.dv.interfaces.QuestionBox;
import net.slc.dv.model.AnswerDetail;
import net.slc.dv.model.Question;
import net.slc.dv.resources.Text;
import net.slc.dv.storage.TextStorage;

public class QuestionTrueFalse extends VBox implements QuestionBox {
	private final Question question;
	private final AnswerDetail answerDetail;
	private String answer;
	private final RadioButton[] answerButtons;
	private final ToggleGroup answerGroup;
	private TextField scoreField;

	public QuestionTrueFalse(int number, Question question, AnswerDetail answerDetail) {
		this(number, question, answerDetail, false);
	}

	public QuestionTrueFalse(int number, Question question, AnswerDetail answerDetail, boolean isChecking) {
		this.question = question;
		this.answerDetail = answerDetail;

		Label numberLabel = LabelBuilder.create(number + ". " + question.getQuestionText())
				.setWrapText(true)
				.build();

		VBox questionContainer = VBoxBuilder.create()
				.addChildren(numberLabel)
				.setSpacing(10)
				.build();


		this.answerButtons = new RadioButton[4];
		this.answerGroup = new ToggleGroup();

		String[] letters = { TextStorage.getText(Text.TRUE), TextStorage.getText(Text.FALSE) };
		for (int i = 0; i < letters.length; i++) {
			answerButtons[i] = new RadioButton(letters[i]);
			answerButtons[i].setDisable(isChecking);
			answerButtons[i].setToggleGroup(answerGroup);
		}

		answerGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				RadioButton selected = (RadioButton) newValue;
				this.answer = selected.getText();
			}
		});

		this.setActiveAnswer();

		GridPane answerGrid = GridPaneBuilder.create()
				.addChildren(answerButtons[0], 0, 0)
				.addChildren(answerButtons[1], 1, 0)
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

		VBoxBuilder.modify(this)
				.addChildren(createFieldLabelPair())
				.build();

	}

	private HBox createFieldLabelPair() {
		Label fieldLabel = LabelBuilder.create(TextStorage.getText(Text.SCORE) + ": ")
				.build();

		this.scoreField = TextFieldBuilder.create()
				.setTextFormatter(new DecimalTextFormatter(0, 2, 0, 10))
				.setText(String.valueOf(answerDetail != null ? answerDetail.getAnswerScore().intValue() : 0))
				.setDisable(true)
				.build();

		return HBoxBuilder.create()
				.addChildren(fieldLabel, scoreField)
				.setSpacing(10)
				.build();
	}

	private void setActiveAnswer() {
		if(answerDetail == null) {
			return;
		}

		String answer = answerDetail.getAnswerText();

		if(answer.equals(TextStorage.getText(Text.TRUE))){
			this.answerGroup.selectToggle(this.answerButtons[0]);
			answerButtons[0].setSelected(true);
			this.answer = TextStorage.getText(Text.TRUE);
		} else {
			this.answerGroup.selectToggle(this.answerButtons[0]);
			answerButtons[1].setSelected(true);
			this.answer = TextStorage.getText(Text.TRUE);
		}
	}

	@Override
	public QuestionType getQuestionType() {
		return QuestionType.TRUE_FALSE;
	}

	@Override
	public String getQuestionAnswer() {
		return this.answer;
	}

	@Override
	public String getQuestionId() {
		return this.question.getQuestionID();
	}

	@Override
	public Double getAnswerScore() {
		if(this.answer == null) {
			return 0.0;
		}

		if(this.answer.equalsIgnoreCase(this.question.getQuestionAnswer())) {
			return 1.0;
		}

		if(this.answerDetail == null) {
			return null;
		}

		if(this.answerDetail.getAnswerScore() != null) {
			return this.answerDetail.getAnswerScore();
		}

		return null;
	}

	@Override
	public boolean isAnswered() {
		return this.answer != null;
	}
}
