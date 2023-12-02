package net.slc.dv.view.component.classtask;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import lombok.Getter;
import net.slc.dv.builder.ButtonBuilder;
import net.slc.dv.builder.ImageViewBuilder;
import net.slc.dv.builder.ScrollPaneBuilder;
import net.slc.dv.builder.VBoxBuilder;
import net.slc.dv.interfaces.QuestionBox;
import net.slc.dv.model.Question;
import net.slc.dv.view.component.classtask.question.QuestionContainer;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CreateQuestionTask {
	private final List<QuestionContainer> questionContainers;
	private final Button addQuestionBtn;
	private final VBox container;
	private final ScrollPane root;

	public CreateQuestionTask() {
		ImageView imageView = ImageViewBuilder.create()
				.setImage(new Image("file:resources/icons/plus-white.png"))
				.setFitWidth(20)
				.setFitHeight(20)
				.setPreserveRatio(true)
				.build();

		questionContainers = new ArrayList<>();

		questionContainers.add(new QuestionContainer(this::removeQuestion));

		this.container = VBoxBuilder.create()
				.addChildren(questionContainers.stream().map(QuestionContainer::getRoot).toArray(Node[]::new))
				.setAlignment(Pos.CENTER)
				.setPadding(40, 80, 100, 80)
				.setSpacing(30)
				.build();

		this.addQuestionBtn = ButtonBuilder
				.create()
				.setOnAction(e -> addNewQuestion())
				.setStyleClass("primary-button")
				.setGraphic(imageView)
				.bindPrefWidth(container)
				.build();

		VBoxBuilder.modify(this.container)
				.addChildren(addQuestionBtn)
				.build();

		this.root = ScrollPaneBuilder.create()
				.setContent(container)
				.setPannable(true)
				.setFitToWidth(true)
				.build();
	}

	private void addNewQuestion() {
		questionContainers.add(new QuestionContainer(this::removeQuestion));

		VBoxBuilder.modify(this.container)
				.removeChildren(addQuestionBtn)
				.addChildren(questionContainers.get(questionContainers.size() - 1).getRoot())
				.addChildren(addQuestionBtn)
				.build();
	}

	private void removeQuestion(QuestionContainer question) {
		questionContainers.remove(question);

		VBoxBuilder.modify(this.container)
				.removeAll()
				.addChildren(questionContainers.stream().map(QuestionContainer::getRoot).toArray(Node[]::new))
				.addChildren(addQuestionBtn)
				.build();
	}

	public List<Question> getQuestions() {
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
