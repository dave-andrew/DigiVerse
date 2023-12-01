package net.slc.dv.view.component.classtask;

import javafx.geometry.Insets;
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
        ImageView imageView = ImageViewBuilder.create()
            .setImage(new Image("file:resources/icons/plus-white.png"))
            .setFitWidth(20)
            .setFitHeight(20)
            .setPreserveRatio(true)
            .build();

        questionContainers = new ArrayList<>();

        questionContainers.add(new QuestionContainer());

        this.container = VBoxBuilder.create()
            .addChildren(questionContainers.stream().map(QuestionContainer::getRoot).toArray(Node[]::new))
            .setAlignment(Pos.CENTER)
            .setPadding(40, 80, 100, 80)
            .setSpacing(30)
            .build();

        this.addQuestionBtn = ButtonBuilder
                .create()
                .setOnAction(e -> addQuestion())
                .setStyleClass("primary-button")
                .setGraphic(imageView)
                .bindPrefWidth(container)
                .build();

        VBoxBuilder.create(this.container)
                .addChildren(addQuestionBtn)
                .build();

        this.root = ScrollPaneBuilder.create()
            .setContent(container)
            .setPannable(true)
            .setFitToWidth(true)
            .build();

    }

    private void addQuestion() {
        questionContainers.add(new QuestionContainer());

        VBoxBuilder.create(this.container)
                .removeChildren(addQuestionBtn)
                .addChildren(questionContainers.get(questionContainers.size() - 1).getRoot())
                .addChildren(addQuestionBtn)
                .build();
    }
}
