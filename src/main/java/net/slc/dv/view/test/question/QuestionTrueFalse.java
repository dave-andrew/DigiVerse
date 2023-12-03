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
import net.slc.dv.model.Question;

public class QuestionTrueFalse extends VBox implements QuestionBox {
	private final Question question;
	private String answerKey;

	public QuestionTrueFalse(int number, Question question) {
		this.question = question;

		Label numberLabel = LabelBuilder.create(number + ".")
				.build();

		Label questionText = LabelBuilder.create(question.getQuestionText())
				.build();


		VBox questionContainer = VBoxBuilder.create()
				.addChildren(numberLabel, questionText)
				.setSpacing(10)
				.build();


		RadioButton[] answerButtons = new RadioButton[4];
		ToggleGroup answerGroup = new ToggleGroup();

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


	@Override
	public QuestionType getQuestionType() {
		return QuestionType.MULTIPLE_CHOICE;
	}


	@Override
	public String getQuestionAnswer() {
		return this.answerKey;
	}

	@Override
	public String getQuestionKey() {
		return this.question.getQuestionAnswer();
	}
}
