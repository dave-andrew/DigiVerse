package view.homeview;

import javafx.scene.layout.BorderPane;
import model.Classroom;

public class ClassroomDetail extends BorderPane {

    private Classroom classroom;

    public ClassroomDetail(Classroom classroom) {
        this.classroom = classroom;
    }

}
