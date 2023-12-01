package net.slc.dv.view.component.classtask;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import net.slc.dv.builder.ButtonBuilder;
import net.slc.dv.builder.ScrollPaneBuilder;
import net.slc.dv.builder.VBoxBuilder;
import net.slc.dv.interfaces.QuestionBox;
import net.slc.dv.model.Question;
import net.slc.dv.view.component.classtask.question.QuestionContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class CreateQuestionTask {
	private final List<QuestionContainer> questionContainers;
	private final Button addQuestionBtn;
	private final VBox container;
	private final ScrollPane root;

	public CreateQuestionTask() {
		questionContainers = new ArrayList<>();

		questionContainers.add(new QuestionContainer(this::removeQuestion));

		this.addQuestionBtn = ButtonBuilder.create("Add Question")
				.setOnAction(arg -> addNewQuestion())
				.build();

		this.container = VBoxBuilder.create()
				.addChildren(questionContainers.stream().map(QuestionContainer::getRoot).toArray(Node[]::new))
				.addChildren(addQuestionBtn)
				.build();

		this.root = ScrollPaneBuilder.create()
				.setContent(container)
				.build();
	}

	private void addNewQuestion() {
		questionContainers.add(new QuestionContainer(this::removeQuestion));

		VBoxBuilder.create(this.container)
				.removeChildren(addQuestionBtn)
				.addChildren(questionContainers.get(questionContainers.size() - 1).getRoot())
				.addChildren(addQuestionBtn)
				.build();
	}

	private void removeQuestion(QuestionContainer question) {
		questionContainers.remove(question);

		VBoxBuilder.create(this.container)
				.removeAll()
				.addChildren(questionContainers.stream().map(QuestionContainer::getRoot).toArray(Node[]::new))
				.addChildren(addQuestionBtn)
				.build();
	}

	public List<Question> getQuestions(){
		List<QuestionContainer> questionContainers = this.questionContainers;
		List<Question> questionList = new ArrayList<>();
		questionContainers.forEach(questionContainer -> {
			QuestionBox questionBox = questionContainer.getQuestionBox();
			Question question = new Question(
					questionBox.getQuestionType(),
					questionBox.getQuestionText(),
					questionBox.getQuestionAnswer(),
					questionBox.getQuestionKey());

			questionList.add(question);
		});


		return questionList;
	}
}
