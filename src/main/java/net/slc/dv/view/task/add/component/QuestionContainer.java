package net.slc.dv.view.task.add.component;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.Getter;
import net.slc.dv.builder.*;
import net.slc.dv.resources.Icon;
import net.slc.dv.resources.IconStorage;
import net.slc.dv.enums.QuestionType;
import net.slc.dv.interfaces.CreateQuestionBox;

import java.util.function.Consumer;

@Getter
public class QuestionContainer {
    protected Label errorLbl;
    protected VBox root;
    protected VBox questionContainer;
    protected HBox questionTypeContainer;
    protected ComboBox<String> questionSelect;
    @Getter
    protected CreateQuestionBox questionBox;

    public QuestionContainer(Consumer<QuestionContainer> consumer) {
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

        Label questionLbl = LabelBuilder
                .create("Question Type")
                .setStyle("-fx-font-size: 18px;")
                .build();

        ImageView closeIcon = ImageViewBuilder.create()
                .setImage(IconStorage.getIcon(Icon.BIN))
                .setFitWidth(20)
                .setFitHeight(20)
                .setPreserveRatio(true)
                .build();

        Button closeButton = ButtonBuilder.create()
                .setGraphic(closeIcon)
                .setStyle("-fx-background-color: transparent;-fx-border-color: red; -fx-border-width: 1px; -fx-border-radius:5px; -fx-cursor: hand;")
                .setOnAction(e -> consumer.accept(this))
                .build();

        HBox spacer = HBoxBuilder.create()
                .setHgrow(Priority.ALWAYS)
                .build();

        this.questionTypeContainer = HBoxBuilder.create()
                .addChildren(questionLbl, questionSelect, spacer, closeButton)
                .setAlignment(Pos.CENTER_LEFT)
                .setSpacing(10)
                .build();

    }

    private VBox getContent() {

        if (questionSelect.getValue().equals(QuestionType.toString(QuestionType.MULTIPLE_CHOICE))) {
            this.questionBox = new CreateMultipleChoiceQuestion();
            return ((CreateMultipleChoiceQuestion) this.questionBox);
        }

        if (questionSelect.getValue().equals(QuestionType.toString(QuestionType.TRUE_FALSE))) {
            this.questionBox = new CreateTrueFalseQuestion();
            return ((CreateTrueFalseQuestion) this.questionBox);
        }

        this.questionBox = new CreateEssayQuestion();
        return ((CreateEssayQuestion) this.questionBox);
    }

    public VBox getRootNode() {
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
