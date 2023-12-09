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
import net.slc.dv.resources.Text;
import net.slc.dv.storage.TextStorage;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CreateTrueFalseQuestion extends VBox implements CreateQuestionBox {
	private final TextArea questionField;
	private final List<TextField> answerFields;
	private final ComboBox<String> answerKey;

	public CreateTrueFalseQuestion() {
		Label questionLbl = LabelBuilder.create(TextStorage.getText(Text.ENTER_QUESTION_HERE))
			.build();

        this.questionField = TextAreaBuilder.create()
                .setPromptText(TextStorage.getText(Text.ENTER_QUESTION_HERE))
                .setWrapText(true)
                .setMaxHeight(200)
                .build();

        VBox questionContainer = VBoxBuilder.create()
                .addChildren(questionLbl, questionField)
                .setSpacing(10)
                .build();

		this.answerFields = new ArrayList<>();


        Label answerKeyLbl = LabelBuilder.create(TextStorage.getText(Text.ANSWER_KEY))
                .build();

		answerKey = ComboBoxBuilder.<String>create()
			.setItems(TextStorage.getText(Text.TRUE), TextStorage.getText(Text.FALSE))
			.setValue(TextStorage.getText(Text.TASK))
			.build();

        HBox answerKey = HBoxBuilder.create()
                .addChildren(answerKeyLbl, this.answerKey)
                .setSpacing(10)
                .build();

        VBoxBuilder.modify(this)
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

	@Nullable
	@Override
	public String getQuestionAnswer() {
		return null;
	}

	@Override
	public String getQuestionKey() {
		if(this.answerKey.getValue().equals(TextStorage.getText(Text.TRUE)))
			return "true";
		else if(this.answerKey.getValue().equals(TextStorage.getText(Text.FALSE)))
			return "false";
		else
			return null;
	}
}
