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
    private String userRole;
    private HBox topBar;

    private void init() {
        if(!this.userRole.isEmpty()) {
            topBar = new ClassDetailNav(this.userRole, this);

            topBar.getStyleClass().add("nav-bar");


            this.setTop(topBar);
        }
    }

    public ClassroomDetail(Classroom classroom, String userRole) {
        this.classroom = classroom;
        this.userRole = userRole;

        init();
    }

    public Classroom getClassroom() {
        return classroom;
    }
}
