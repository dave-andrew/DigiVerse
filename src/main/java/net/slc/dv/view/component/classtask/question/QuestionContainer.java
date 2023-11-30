package net.slc.dv.view.component.classtask.question;

import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;
import net.slc.dv.builder.*;
import net.slc.dv.enums.QuestionType;

import java.util.List;

@Getter
public class QuestionContainer {
	protected Label errorLbl;
	protected VBox root;
	protected VBox questionContainer;
	protected HBox questionTypeContainer;
	protected ComboBox<String> questionSelect;
	public QuestionContainer() {
		this.errorLbl = LabelBuilder.create()
				.setStyle("-fx-text-fill: red;")
				.build();

		this.questionSelect = ComboBoxBuilder.<String>create()
				.setItems(
					QuestionType.toString(QuestionType.MULTIPLE_CHOICE),
					QuestionType.toString(QuestionType.TRUE_FALSE),
					QuestionType.toString(QuestionType.ESSAY))
				.setValue(QuestionType.toString(QuestionType.MULTIPLE_CHOICE))
				.setOnAction(e -> {
					this.questionContainer.getChildren().remove(1);
					this.questionContainer.getChildren().add(getContent());
				})
				.build();

		Label questionLbl = LabelBuilder.create("Question Type")
				.build();

		this.questionTypeContainer = HBoxBuilder.create()
				.addChildren(questionLbl, questionSelect)
				.setSpacing(5)
				.build();

	}

	private VBox getContent(){
		if(questionSelect.getValue().equals(QuestionType.toString(QuestionType.MULTIPLE_CHOICE))) {
			return new MultipleChoiceQuestion().getRoot();
		}

		if(questionSelect.getValue().equals(QuestionType.toString(QuestionType.TRUE_FALSE))) {
			return new TrueFalseQuestion().getRoot();
		}

		return new EssayQuestion().getRoot();
	}

	public VBox getRoot() {
		this.questionContainer = VBoxBuilder.create()
				.addChildren(questionTypeContainer, getContent())
				.setSpacing(5)
				.build();

		this.root = VBoxBuilder.create()
				.addChildren(questionContainer, errorLbl)
				.setSpacing(5)
				.setStyleClass("card")
				.build();

		return root;
	}
}
