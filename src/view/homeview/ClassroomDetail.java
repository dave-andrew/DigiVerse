package view.homeview;

import javafx.scene.layout.*;
import model.Classroom;
import view.component.classdetail.ClassDetailNav;

public class ClassroomDetail extends BorderPane {

    private Classroom classroom;
    private String userRole;
    private StackPane mainPane;
    private HBox topBar;

    private void init() {
        if(!this.userRole.isEmpty()) {
            topBar = new ClassDetailNav(this.userRole, this);

            topBar.getStyleClass().add("nav-bar");

            this.setTop(topBar);
        }
    }

    public ClassroomDetail(Classroom classroom, String userRole, StackPane mainPane) {
        this.classroom = classroom;
        this.userRole = userRole;
        this.mainPane = mainPane;

        init();
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
