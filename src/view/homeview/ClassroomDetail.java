package view.homeview;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import model.Classroom;
import view.component.classdetail.ClassDetailNav;

public class ClassroomDetail extends BorderPane {

    private Classroom classroom;
    private HBox topBar;

    private void init() {
        topBar = new ClassDetailNav("Teacher");

        topBar.getStyleClass().add("nav-bar");

        this.setTop(topBar);
    }

    public ClassroomDetail(Classroom classroom) {
        init();

        this.classroom = classroom;
    }
}
