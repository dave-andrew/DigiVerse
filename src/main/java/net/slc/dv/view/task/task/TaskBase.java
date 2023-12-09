package net.slc.dv.view.task.task;

import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import net.slc.dv.builder.StackPaneBuilder;
import net.slc.dv.enums.Role;
import net.slc.dv.model.Classroom;
import net.slc.dv.model.Task;
import net.slc.dv.resources.Text;
import net.slc.dv.storage.TextStorage;

public class TaskBase extends BorderPane {
    private final Role userRole;
    private final TaskDetailView taskDetail;
    private final AnswerDetailView answerDetail;
    private Button taskButton, answer;
    private ScrollPane taskDetailScrollPane;

    public TaskBase(StackPane mainPane, Task task, Classroom classroom, Role userRole) {
        this.userRole = userRole;
        this.taskDetail = new TaskDetailView(mainPane, task, classroom, userRole);
        this.answerDetail = new AnswerDetailView(task, classroom);

        init();

        StackPaneBuilder.modify(mainPane).removeAllChildren().addChildren(this).build();
    }

    private void init() {
        if (this.userRole.equals(Role.TEACHER)) {
            this.setTop(setTopNav());
            actions();
        }

        this.taskDetailScrollPane = new ScrollPane();
        taskDetailScrollPane.setContent(taskDetail);

        taskDetailScrollPane.setFitToWidth(true);

        this.setCenter(taskDetailScrollPane);
    }

    private HBox setTopNav() {

        HBox topNav = new HBox();

        taskButton = new Button(TextStorage.getText(Text.TASK));
        taskButton.getStyleClass().add("nav-button");
        taskButton.getStyleClass().add("active");

        answer = new Button(TextStorage.getText(Text.ANSWER));
        answer.getStyleClass().add("nav-button");

        topNav.getChildren().addAll(taskButton, answer);

        topNav.getStyleClass().add("nav-bar");

        return topNav;
    }

    private void actions() {

        taskButton.setOnMouseClicked(e -> {
            this.setCenter(taskDetailScrollPane);
            taskButton.getStyleClass().add("active");
            answer.getStyleClass().remove("active");
        });

        answer.setOnMouseClicked(e -> {
            this.setCenter(answerDetail);
            answer.getStyleClass().add("active");
            taskButton.getStyleClass().remove("active");
        });
    }
}
