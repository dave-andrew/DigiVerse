package net.slc.dv.view.task.add.component;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import lombok.Getter;
import net.slc.dv.builder.LabelBuilder;
import net.slc.dv.builder.TextAreaBuilder;
import net.slc.dv.builder.VBoxBuilder;
import net.slc.dv.enums.QuestionType;
import net.slc.dv.interfaces.CreateQuestionBox;

@Getter
public class CreateEssayQuestion extends VBox implements CreateQuestionBox {
	private final TextArea questionField;

	public CreateEssayQuestion() {
		Label questionLbl = LabelBuilder.create("Enter Question Here")
				.build();

		this.questionField = TextAreaBuilder.create()
				.setPromptText("Enter Question Here")
				.build();

		VBox questionContainer = VBoxBuilder.create()
				.addChildren(questionLbl, questionField)
				.setSpacing(10)
				.build();

		VBoxBuilder.modify(this)
				.addChildren(questionContainer)
				.setSpacing(5)
				.build();
	}

	@Override
	public QuestionType getQuestionType() {
		return QuestionType.ESSAY;
	}

	@Override
	public String getQuestionText() {
		return this.questionField.getText();
	}

	@Override
	public String getQuestionAnswer() {
		return null;
	}

	@Override
	public String getQuestionKey() {
		return null;
	}
}
