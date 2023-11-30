package net.slc.dv.view.component.classtask;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import net.slc.dv.builder.ButtonBuilder;
import net.slc.dv.builder.ScrollPaneBuilder;
import net.slc.dv.builder.VBoxBuilder;
import net.slc.dv.view.component.classtask.question.QuestionContainer;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CreateQuestionTask {

    private List<QuestionContainer> questionContainers;
    private Button addQuestionBtn;
    private VBox container;
    private ScrollPane root;
    public CreateQuestionTask() {
        questionContainers = new ArrayList<>();

        questionContainers.add(new QuestionContainer());

        this.addQuestionBtn = ButtonBuilder.create("Add Question")
            .setOnAction(e -> {
                questionContainers.add(new QuestionContainer());

                VBoxBuilder.create(this.container)
                    .removeChildren(addQuestionBtn)
                    .addChildren(questionContainers.get(questionContainers.size() - 1).getRoot())
                    .addChildren(addQuestionBtn)
                    .build();
            })
            .build();

        this.container = VBoxBuilder.create()
            .addChildren(questionContainers.stream().map((questionContainer) -> questionContainer.getRoot()).toArray(Node[]::new))
            .addChildren(addQuestionBtn)
            .build();

        this.root = ScrollPaneBuilder.create()
            .setContent(container)
            .build();

    }
}
