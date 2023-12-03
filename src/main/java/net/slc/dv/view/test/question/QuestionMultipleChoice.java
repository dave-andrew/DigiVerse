package net.slc.dv.view.test.question;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import net.slc.dv.builder.*;
import net.slc.dv.enums.QuestionType;
import net.slc.dv.interfaces.QuestionBox;
import net.slc.dv.model.Question;

@Getter
public class QuestionMultipleChoice extends VBox implements QuestionBox {
    private final Question question;
    private String answerKey;

    public QuestionMultipleChoice(int number, Question question) {
        this.question = question;

        Label numberLabel = LabelBuilder.create(number + ".").build();

        Label questionText = LabelBuilder.create(question.getQuestionText()).build();

        VBox questionContainer = VBoxBuilder.create()
                .addChildren(numberLabel, questionText)
                .setSpacing(10)
                .build();

        String[] answers = question.getQuestionChoice().split(";");

        RadioButton[] answerButtons = new RadioButton[4];
        ToggleGroup answerGroup = new ToggleGroup();

        String[] letters = {"A. ", "B. ", "C. ", "D. "};
        for (int i = 0; i < 4; i++) {
            answerButtons[i] = new RadioButton(letters[i] + answers[i]);
            answerButtons[i].setToggleGroup(answerGroup);
        }

        answerGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                RadioButton selected = (RadioButton) newValue;
                this.answerKey = selected.getText().split(" ")[1];
            }
        });

        GridPane answerGrid = GridPaneBuilder.create()
                .addChildren(answerButtons[0], 0, 0)
                .addChildren(answerButtons[1], 1, 0)
                .addChildren(answerButtons[2], 0, 1)
                .addChildren(answerButtons[3], 1, 1)
                .setHGap(5)
                .setVGap(5)
                .build();

        VBoxBuilder.modify(this)
                .addChildren(questionContainer, answerGrid)
                .setSpacing(10)
                .build();
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.MULTIPLE_CHOICE;
    }

    @Override
    public String getQuestionAnswer() {
        return this.answerKey;
    }

    @Override
    public String getQuestionKey() {
        return this.question.getQuestionAnswer();
    }
}
