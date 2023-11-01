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
        topBar = new ClassDetailNav("Teacher");

        topBar.getStyleClass().add("nav-bar");

        ScrollPane test = new ClassForum();

        this.setTop(topBar);
        this.setCenter(test);
    }

    public ClassroomDetail(Classroom classroom) {
        init();

        this.classroom = classroom;
    }
}
