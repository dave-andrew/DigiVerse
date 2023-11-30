package net.slc.dv.view.homeview.task;

import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import net.slc.dv.model.Classroom;
import net.slc.dv.model.Task;

public class TaskBase extends BorderPane {

    private Task task;
    private Classroom classroom;
    private String userRole;

    private Button taskButton, answer;

    private TaskDetail taskDetail;
    private AnswerDetail answerDetail;
    private ScrollPane taskDetailScrollPane;

    public TaskBase(Task task, Classroom classroom, String userRole) {
        this.task = task;
        this.userRole = userRole;
        this.classroom = classroom;

        this.taskDetail = new TaskDetail(task, userRole);
        this.answerDetail = new AnswerDetail(task, classroom);

        init();
    }

    private void init() {
        if (this.userRole.equals("Teacher")) {
            this.setTop(setTopNav());
            actions();
        }

        this.taskDetailScrollPane = new ScrollPane();
        this.taskDetailScrollPane.setContent(taskDetail);

        this.taskDetailScrollPane.setFitToWidth(true);

        this.setCenter(this.taskDetailScrollPane);
    }

    private HBox setTopNav() {

        HBox topNav = new HBox();

        taskButton = new Button("Task");
        taskButton.getStyleClass().add("nav-button");
        taskButton.getStyleClass().add("active");

        answer = new Button("Answer");
        answer.getStyleClass().add("nav-button");

        topNav.getChildren().addAll(taskButton, answer);

        topNav.getStyleClass().add("nav-bar");

        return topNav;
    }

    private void actions() {

        taskButton.setOnMouseClicked(e -> {
            this.setCenter(taskDetail);
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
