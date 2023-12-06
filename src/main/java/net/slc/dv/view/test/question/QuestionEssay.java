package net.slc.dv.view.test.question;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import net.slc.dv.builder.*;
import net.slc.dv.enums.QuestionType;
import net.slc.dv.helper.DecimalTextFormatter;
import net.slc.dv.interfaces.QuestionBox;
import net.slc.dv.model.AnswerDetail;
import net.slc.dv.model.Question;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Text;

public class QuestionEssay extends VBox implements QuestionBox {
	private final Question question;
	private final TextArea answerField;
	private final AnswerDetail answerDetail;
	private final boolean isChecking;
	private TextField scoreField;

	public QuestionEssay(int number, Question question, @Nullable AnswerDetail answerDetail) {
		this(number, question, answerDetail, false);
	}

	public QuestionEssay(int number, Question question, @Nullable AnswerDetail answerDetail, boolean isChecking) {
		this.question = question;
		this.answerDetail = answerDetail;
		this.isChecking = isChecking;

		Label numberLabel = LabelBuilder.create(number + ". " + question.getQuestionText())
				.setWrapText(true)
				.setPadding(0, 0, 20, 0)
				.build();

		VBox questionContainer = VBoxBuilder.create()
				.addChildren(numberLabel)
				.setSpacing(10)
				.build();

		this.answerField = TextAreaBuilder.create()
				.setPromptText("Enter Answer Here")
				.setWrapText(true)
				.setText(answerDetail == null ? "" : answerDetail.getAnswerText())
				.setDisable(isChecking)
				.setMaxHeight(200)
				.build();

		this.setActiveAnswer();

		VBoxBuilder.modify(this)
				.addChildren(questionContainer, answerField)
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
		Label fieldLabel = LabelBuilder.create("Score: ")
				.build();

		this.scoreField = TextFieldBuilder.create()
				.setPromptText("0")
				.setTextFormatter(new DecimalTextFormatter(0, 1, 0, 10))
				.setText(answerDetail == null ? "0" : String.valueOf(answerDetail.getAnswerScore().intValue()))
				.setDisable(!(this.isChecking && this.answerDetail != null))
				.build();

		Label slashLabel = LabelBuilder.create("/ 10")
				.build();

		return HBoxBuilder.create()
				.addChildren(fieldLabel, scoreField, slashLabel)
				.setSpacing(10)
				.build();
	}


	private void setActiveAnswer() {
		if (answerDetail == null) {
			return;
		}

		this.answerField.setText(answerDetail.getAnswerText());
	}

	@Override
	public QuestionType getQuestionType() {
		return QuestionType.MULTIPLE_CHOICE;
	}

	@Nullable
	@Override
	public String getQuestionAnswer() {
		return this.answerField.getText().isEmpty() ? null : this.answerField.getText();
	}

	@Override
	public String getQuestionId() {
		return question.getQuestionID();
	}

	@Override
	@Nullable
	public Double getAnswerScore() {
		return this.scoreField == null ? null : this.scoreField.getText().isEmpty() ? null : Double.valueOf(this.scoreField.getText());
	}

	@Override
	public boolean isAnswered() {
		return !this.answerField.getText().isEmpty();
	}
}
