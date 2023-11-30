package net.slc.dv.view.homeview;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import net.slc.dv.model.Classroom;
import net.slc.dv.view.component.classdetail.ClassDetailNav;

public class ClassroomDetail extends BorderPane {

    private final Classroom classroom;
    private final String userRole;
    private StackPane mainPane;

    public ClassroomDetail(Classroom classroom, String userRole, StackPane mainPane) {
        this.classroom = classroom;
        this.userRole = userRole;
        this.mainPane = mainPane;

        init();
    }

    private void init() {
        if (!this.userRole.isEmpty()) {
            HBox topBar = new ClassDetailNav(this.userRole, this);
            topBar.getStyleClass().add("nav-bar");
            this.setTop(topBar);
        }
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public StackPane getMainPane() {
        return mainPane;
    }

    public void setMainPane(StackPane mainPane) {
        this.mainPane = mainPane;
    }
}
