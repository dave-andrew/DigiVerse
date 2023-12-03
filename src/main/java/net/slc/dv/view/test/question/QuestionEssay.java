package net.slc.dv.view.test.question;

import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import net.slc.dv.builder.GridPaneBuilder;
import net.slc.dv.builder.LabelBuilder;
import net.slc.dv.builder.TextAreaBuilder;
import net.slc.dv.builder.VBoxBuilder;
import net.slc.dv.enums.QuestionType;
import net.slc.dv.interfaces.QuestionBox;
import net.slc.dv.model.AnswerDetail;
import net.slc.dv.model.Question;

public class QuestionEssay extends VBox implements QuestionBox {
	private final Question question;
	private TextArea answerField;
	private AnswerDetail answerDetail;

	public QuestionEssay(int number, Question question, AnswerDetail answerDetail) {
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


		this.answerField = TextAreaBuilder.create()
				.setPromptText("Enter Answer Here")
				.setWrapText(true)
				.setMaxHeight(200)
				.build();

		this.setActiveAnswer();

		VBoxBuilder.modify(this)
				.addChildren(questionContainer, answerField)
				.setSpacing(10)
				.build();

	}

	private void setActiveAnswer() {
		if(answerDetail == null) {
			return;
		}

		this.answerField.setText(answerDetail.getAnswerText());
	}
	@Override
	public QuestionType getQuestionType() {
		return QuestionType.MULTIPLE_CHOICE;
	}


	@Override
	public String getQuestionAnswer() {
		return this.answerField.getText().isEmpty() ? null : this.answerField.getText();
	}

	@Override
	public String getQuestionKey() {
		return this.question.getQuestionAnswer();
	}

	@Override
	public String getQuestionId() {
		return question.getQuestionID();
	}
}
