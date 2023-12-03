package net.slc.dv.view.test.question;

import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import net.slc.dv.builder.GridPaneBuilder;
import net.slc.dv.builder.LabelBuilder;
import net.slc.dv.builder.VBoxBuilder;
import net.slc.dv.enums.QuestionType;
import net.slc.dv.interfaces.QuestionBox;
import net.slc.dv.model.AnswerDetail;
import net.slc.dv.model.Question;

public class QuestionTrueFalse extends VBox implements QuestionBox {
	private final Question question;
	private final AnswerDetail answerDetail;
	private String answerKey;
	private final RadioButton[] answerButtons;
	private final ToggleGroup answerGroup;

	public QuestionTrueFalse(int number, Question question, AnswerDetail answerDetail) {
		this.question = question;
		this.answerDetail = answerDetail;

		Label numberLabel = LabelBuilder.create(number + ".")
				.build();

		Label questionText = LabelBuilder.create(question.getQuestionText())
				.build();


		VBox questionContainer = VBoxBuilder.create()
				.addChildren(numberLabel, questionText)
				.setSpacing(10)
				.build();


		this.answerButtons = new RadioButton[4];
		this.answerGroup = new ToggleGroup();

		String[] letters = { "True", "False"};
		for (int i = 0; i < letters.length; i++) {
			answerButtons[i] = new RadioButton(letters[i]);
			answerButtons[i].setToggleGroup(answerGroup);
		}

		answerGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				RadioButton selected = (RadioButton) newValue;
				this.answerKey = selected.getText();
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

	}

	private void setActiveAnswer() {
		if(answerDetail == null) {
			return;
		}


		String answer = answerDetail.getAnswerText();

		System.out.println(answer);
		if(answer.equals("True")) {
			System.out.println("masuk sini");
			this.answerGroup.selectToggle(this.answerButtons[0]);
			answerButtons[0].setSelected(true);
			this.answerKey = "True";
		} else {
			this.answerGroup.selectToggle(this.answerButtons[0]);
			answerButtons[1].setSelected(true);
			this.answerKey = "False";
		}
	}

	@Override
	public QuestionType getQuestionType() {
		return QuestionType.TRUE_FALSE;
	}


	@Override
	public String getQuestionAnswer() {
		return this.answerKey;
	}

	@Override
	public String getQuestionKey() {
		return this.question.getQuestionAnswer();
	}

	@Override
	public String getQuestionId() {
		return this.question.getQuestionID();
	}
}
