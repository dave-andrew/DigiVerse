package view.homeview;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import model.Classroom;
import view.component.classdetail.ClassDetailNav;
import view.component.classdetail.ClassForum;

public class ClassroomDetail extends BorderPane {

    private Classroom classroom;
    private HBox topBar;

    private void init() {
        topBar = new ClassDetailNav("Teacher", this);

        topBar.getStyleClass().add("nav-bar");


        this.setTop(topBar);
    }

    public ClassroomDetail(Classroom classroom) {
        this.classroom = classroom;

        init();
    }

    public Classroom getClassroom() {
        return classroom;
    }
}
