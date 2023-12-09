package net.slc.dv.view.classroom.detail.component;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import net.slc.dv.enums.Role;
import net.slc.dv.resources.Text;
import net.slc.dv.storage.TextStorage;
import net.slc.dv.view.classroom.detail.ClassroomDetailView;

public class ClassDetailNav extends HBox {
    private final Role userRole;
    private final ClassroomDetailView parent;

    private Button forum, task, member, score;
    private ClassForum classForum;
    private ClassMember classMember;
    private ClassScore classScore;
    private ClassTask classTask;

    public ClassDetailNav(Role role, ClassroomDetailView parent) {
        this.parent = parent;
        this.userRole = role;

        init();
        actions();

        this.getChildren().addAll(forum, task, member, score);

        if (role.equals(Role.STUDENT)) {
            this.score.setVisible(false);
        }
    }

    void init() {
        forum = new Button(TextStorage.getText(Text.FORUM));
        forum.getStyleClass().add("nav-button");
        forum.getStyleClass().add("active");

        task = new Button(TextStorage.getText(Text.TASK));
        task.getStyleClass().add("nav-button");

        member = new Button(TextStorage.getText(Text.MEMBER));
        member.getStyleClass().add("nav-button");

        score = new Button(TextStorage.getText(Text.SCORE));
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
        forum.setOnAction(e -> forumActive(classForum));
        task.setOnAction(e -> taskActive(classTask));
        member.setOnAction(e -> memberActive(classMember));
        score.setOnAction(e -> scoreActive(classScore));
    }

    private void forumActive(ClassBase classBase) {
        clearStyle();
        this.parent.setCenter(classBase);
        forum.getStyleClass().add("active");
    }

    private void taskActive(ClassBase classBase) {
        clearStyle();
        this.parent.setCenter(classBase);
        task.getStyleClass().add("active");
    }

    private void memberActive(ClassBase classBase) {
        clearStyle();
        this.parent.setCenter(classBase);
        member.getStyleClass().add("active");
    }

    private void scoreActive(Pane classBase) {
        clearStyle();
        this.parent.setCenter(classBase);
        score.getStyleClass().add("active");
    }

}
