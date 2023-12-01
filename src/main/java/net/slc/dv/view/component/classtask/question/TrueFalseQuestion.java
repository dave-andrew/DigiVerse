package net.slc.dv.view.component.classtask.question;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;
import net.slc.dv.builder.*;
import net.slc.dv.enums.QuestionType;
import net.slc.dv.interfaces.Question;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TrueFalseQuestion implements Question {
    private final VBox root;
    private final TextArea questionField;
    private final List<TextField> answerFields;
    private final ComboBox<String> answerKey;

    public TrueFalseQuestion() {
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

        this.answerFields = new ArrayList<>();

        Label answerLbl = LabelBuilder.create("Enter Answers Here")
                .build();

        List<HBox> answerFields = List.of(
                createFieldLabelPair("True", "Enter Choice Here"),
                createFieldLabelPair("False", "Enter Choice Here"));

        GridPane answerGrid = GridPaneBuilder.create()
                .addChildren(answerFields.get(0), 0, 0)
                .addChildren(answerFields.get(1), 1, 0)
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
                .setItems("True", "False")
                .setValue("True")
                .build();

        HBox answerKey = HBoxBuilder.create()
                .addChildren(answerKeyLbl, this.answerKey)
                .setSpacing(10)
                .build();

        this.root = VBoxBuilder.create()
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
        return QuestionType.TRUE_FALSE;
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
