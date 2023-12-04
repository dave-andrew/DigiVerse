package net.slc.dv.view.task.add.component;

import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
public class CreateMultipleChoiceQuestion extends VBox implements CreateQuestionBox {
	private final TextArea questionField;
	private final List<TextField> answerFields;
	private final ComboBox<String> answerKey;

	public CreateMultipleChoiceQuestion() {
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

		Label answerLbl = LabelBuilder.create("Enter Answers Here")
			.build();

		this.answerFields = new ArrayList<>();

		List<HBox> answerFields = List.of(
			createFieldLabelPair("A", "Enter Choice Here"),
			createFieldLabelPair("B", "Enter Choice Here"),
			createFieldLabelPair("C", "Enter Choice Here"),
			createFieldLabelPair("D", "Enter Choice Here"));

		GridPane answerGrid = GridPaneBuilder.create()
			.addChildren(answerFields.get(0), 0, 0)
			.addChildren(answerFields.get(1), 1, 0)
			.addChildren(answerFields.get(2), 0, 1)
			.addChildren(answerFields.get(3), 1, 1)
			.setHGap(20)
			.setVGap(5)
			.build();

        VBox answerContainer = VBoxBuilder.create()
                .addChildren(answerLbl, answerGrid)
                .setSpacing(10)
                .build();

        Label answerKeyLbl = LabelBuilder.create("Answer Key")
                .build();

        answerKey = ComboBoxBuilder.<String>create()
                .setItems("A", "B", "C", "D")
                .setValue("A")
                .build();

        HBox answerKey = HBoxBuilder.create()
                .addChildren(answerKeyLbl, this.answerKey)
                .setSpacing(10)
                .setAlignment(Pos.CENTER_LEFT)
                .build();

        VBoxBuilder.modify(this)
                .addChildren(questionContainer, answerContainer, answerKey)
                .setSpacing(30)
                .build();
    }

	private HBox createFieldLabelPair(String label, String promptText) {
		Label lbl = LabelBuilder.create(label)
			.build();

		TextField field = TextFieldBuilder.create()
			.setPromptText(promptText)
			.build();

		answerFields.add(field);

        return HBoxBuilder.create()
                .addChildren(lbl, field)
                .setAlignment(Pos.CENTER_LEFT)
                .setSpacing(10)
                .build();
    }

	@Override
	public QuestionType getQuestionType() {
		return QuestionType.MULTIPLE_CHOICE;
	}

	@Override
	public String getQuestionText() {
		return this.questionField.getText();
	}

	@Override
	public String getQuestionAnswer() {
		StringBuilder questionAnswer = new StringBuilder();

		for (int i = 0; i < answerFields.size(); i++) {
			if (answerFields.get(i).getText().isEmpty()) {
				questionAnswer.append(" ");
			} else {
				questionAnswer.append(answerFields.get(i).getText());
			}
			if (i != answerFields.size() - 1) {
				questionAnswer.append(";");
			}
		}

		return questionAnswer.toString();
	}

	@Override
	public String getQuestionKey() {
		return this.answerKey.getValue();
	}
}
