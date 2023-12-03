package net.slc.dv.view.task.add.component;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;
import net.slc.dv.builder.*;
import net.slc.dv.enums.QuestionType;
import net.slc.dv.interfaces.CreateQuestionBox;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CreateTrueFalseQuestion implements CreateQuestionBox {
	private final VBox root;
	private final TextArea questionField;
	private final List<TextField> answerFields;
	private final ComboBox<String> answerKey;

	public CreateTrueFalseQuestion() {
		Label questionLbl = LabelBuilder.create("Enter Question Here")
			.build();

        this.questionField = TextAreaBuilder.create()
                .setPromptText("Enter Question Here")
                .setWrapText(true)
                .setMaxHeight(200)
                .build();

        VBox questionContainer = VBoxBuilder.create()
                .addChildren(questionLbl, questionField)
                .setSpacing(10)
                .build();

		this.answerFields = new ArrayList<>();


        Label answerKeyLbl = LabelBuilder.create("Answer Key")
                .build();

		answerKey = ComboBoxBuilder.<String>create()
			.setItems("True", "False")
			.setValue("True")
			.build();

        HBox answerKey = HBoxBuilder.create()
                .addChildren(answerKeyLbl, this.answerKey)
                .setSpacing(10)
                .build();

        this.root = VBoxBuilder.create()
                .addChildren(questionContainer, answerKey)
                .setSpacing(30)
                .build();
    }

	@Override
	public QuestionType getQuestionType() {
		return QuestionType.TRUE_FALSE;
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
		return this.answerKey.getValue();
	}
}
