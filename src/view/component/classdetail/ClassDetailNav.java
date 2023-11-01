package view.component.classdetail;

import helper.ScreenManager;
import helper.StageManager;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import model.LoggedUser;

public class ClassDetailNav extends HBox {
    private Button forum, task, member, score;

    void init(){
        forum = new Button("Forum");
        forum.getStyleClass().add("nav-button");
        forum.getStyleClass().add("active");

        task = new Button("Task");
        task.getStyleClass().add("nav-button");

        member = new Button("Member");
        member.getStyleClass().add("nav-button");

        score = new Button("Score");
        score.getStyleClass().add("nav-button");
    }

    void clearStyle() {
        forum.getStyleClass().remove("active");
        task.getStyleClass().remove("active");
        member.getStyleClass().remove("active");
        score.getStyleClass().remove("active");
    }

    private void actions() {
        forum.setOnAction(e -> {
            clearStyle();
            forum.getStyleClass().add("active");
        });

        task.setOnAction(e -> {
            clearStyle();
            task.getStyleClass().add("active");
        });

        member.setOnAction(e -> {
            clearStyle();
            member.getStyleClass().add("active");
        });

        score.setOnAction(e -> {
            clearStyle();
            score.getStyleClass().add("active");
        });
    }
    public ClassDetailNav(String role) {
        init();
        actions();

        if(role.equals("Student")){
            this.getChildren().addAll(forum, task, member);
        } else if(role.equals("Teacher")){
            this.getChildren().addAll(forum, task, member, score);
        }
    }

}
