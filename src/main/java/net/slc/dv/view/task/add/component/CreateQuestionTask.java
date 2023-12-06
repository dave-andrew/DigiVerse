package net.slc.dv.view.task.add.component;

import java.util.ArrayList;
import java.util.List;
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
import net.slc.dv.resources.Icon;
import net.slc.dv.resources.IconStorage;
import net.slc.dv.interfaces.CreateQuestionBox;
import net.slc.dv.model.Question;

@Getter
public class CreateQuestionTask extends ScrollPane {
	private final List<QuestionContainer> questionContainers;
	private final Button addQuestionBtn;
	private final VBox container;
	private CreateGeneralTask generalTask;

	public CreateQuestionTask() {
		this.generalTask = new CreateGeneralTask();
		ImageView imageView = ImageViewBuilder.create()
				.bindImageProperty(IconStorage.getIcon(Icon.PLUS_WHITE))
				.setFitWidth(20)
				.setFitHeight(20)
				.setPreserveRatio(true)
				.build();

        questionContainers = new ArrayList<>();

        questionContainers.add(new QuestionContainer(this::removeQuestion));

        this.container = VBoxBuilder.create()
                .addChildren(generalTask)
                .addChildren(questionContainers.stream()
                        .map(QuestionContainer::getRootNode)
                        .toArray(Node[]::new))
                .setAlignment(Pos.CENTER)
                .setPadding(40, 187, 100, 187)
                .setSpacing(30)
                .build();

        this.addQuestionBtn = ButtonBuilder.create()
                .setOnAction(e -> addNewQuestion())
                .setStyleClass("primary-button")
                .setGraphic(imageView)
                .bindPrefWidth(container)
                .build();

        VBoxBuilder.modify(this.container).addChildren(addQuestionBtn).build();

        ScrollPaneBuilder.modify(this)
                .setContent(container)
                .setPannable(true)
                .setFitToWidth(true)
                .build();
    }

    private void addNewQuestion() {
        questionContainers.add(new QuestionContainer(this::removeQuestion));

        VBoxBuilder.modify(this.container)
                .removeChildren(addQuestionBtn)
                .addChildren(
                        questionContainers.get(questionContainers.size() - 1).getRootNode())
                .addChildren(addQuestionBtn)
                .build();
    }

    private void removeQuestion(QuestionContainer question) {
        questionContainers.remove(question);

        VBoxBuilder.modify(this.container).removeAll(question.getRoot()).build();
    }

    public String getTaskTitle() {
        return generalTask.getTitleField().getText();
    }

    public String getTaskDescription() {
        return generalTask.getDescriptionField().getText();
    }

    public List<Question> getQuestions() {
        List<QuestionContainer> questionContainers = this.questionContainers;
        List<Question> questionList = new ArrayList<>();
        questionContainers.forEach(questionContainer -> {
            CreateQuestionBox questionBox = questionContainer.getQuestionBox();
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
