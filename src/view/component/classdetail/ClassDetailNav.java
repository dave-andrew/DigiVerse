package view.component.classdetail;

import helper.ScreenManager;
import helper.StageManager;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import model.LoggedUser;
import view.homeview.ClassroomDetail;

public class ClassDetailNav extends HBox {
    private Button forum, task, member, score;

    private ClassroomDetail parent;
    private ClassForum classForum;
    private ClassMember classMember;
    private ClassScore classScore;
    private ClassTask classTask;

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

        this.classForum = new ClassForum(parent.getClassroom());
        this.classMember = new ClassMember(parent.getClassroom());
        this.classScore = new ClassScore(parent.getClassroom());
        this.classTask = new ClassTask(parent.getClassroom());

        this.parent.setCenter(classForum);
    }

    void clearStyle() {
        this.parent.getChildren().remove(classForum);
        this.parent.getChildren().remove(classMember);
        this.parent.getChildren().remove(classScore);
        this.parent.getChildren().remove(classTask);

        forum.getStyleClass().remove("active");
        task.getStyleClass().remove("active");
        member.getStyleClass().remove("active");
        score.getStyleClass().remove("active");
    }

    private void actions() {
        forum.setOnAction(e -> {
            clearStyle();
            this.parent.setCenter(classForum);
            forum.getStyleClass().add("active");
        });

        task.setOnAction(e -> {
            clearStyle();
            this.parent.setCenter(classTask);
            task.getStyleClass().add("active");
        });

        member.setOnAction(e -> {
            clearStyle();
            this.parent.setCenter(classMember);
            member.getStyleClass().add("active");
        });

        score.setOnAction(e -> {
            clearStyle();
            this.parent.setCenter(classScore);
            score.getStyleClass().add("active");
        });
    }
    public ClassDetailNav(String role, ClassroomDetail parent) {
        this.parent = parent;

        init();
        actions();

        if(role.equals("Student")){
            this.getChildren().addAll(forum, task, member);
        } else if(role.equals("Teacher")){
            this.getChildren().addAll(forum, task, member, score);
        }
    }

}
