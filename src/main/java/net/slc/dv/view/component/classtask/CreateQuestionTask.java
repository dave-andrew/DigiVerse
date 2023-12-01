package net.slc.dv.view.component.classtask;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
            .addChildren(questionContainers.stream().map(QuestionContainer::getRoot).toArray(Node[]::new))
            .addChildren(addQuestionBtn)
            .setAlignment(Pos.CENTER)
            .setPadding(40, 80, 0, 80)
            .setSpacing(30)
            .build();

        this.root = ScrollPaneBuilder.create()
            .setContent(container)
            .setPannable(true)
            .setFitToWidth(true)
            .build();

    }
}
