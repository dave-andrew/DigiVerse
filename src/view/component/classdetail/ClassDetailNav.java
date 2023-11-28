package view.component.classdetail;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import view.homeview.ClassroomDetail;

public class ClassDetailNav extends HBox {
    private final String userRole;
    private final ClassroomDetail parent;

    private Button forum, task, member, score;
    private ClassForum classForum;
    private ClassMember classMember;
    private ClassScore classScore;
    private ClassTask classTask;

    public ClassDetailNav(String role, ClassroomDetail parent) {
        this.parent = parent;
        this.userRole = role;

        init();
        actions();

        this.getChildren().addAll(forum, task, member, score);

        if (role.equals("Student")) {
            this.score.setVisible(false);
        }
    }

    void init() {
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
        this.classTask = new ClassTask(parent.getClassroom(), parent.getMainPane(), userRole);

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
        forum.setOnAction(e -> setActive(classForum));
        task.setOnAction(e -> setActive(classTask));
        member.setOnAction(e -> setActive(classMember));
        score.setOnAction(e -> setActive(classScore));
    }

    private void setActive(ClassBase classBase) {
        clearStyle();
        this.parent.setCenter(classBase);
        forum.getStyleClass().add("active");
    }

    private void setActive(Pane classBase) {
        clearStyle();
        this.parent.setCenter(classBase);
        forum.getStyleClass().add("active");
    }

}
