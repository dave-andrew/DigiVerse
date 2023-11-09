package view.component.classdetail;

import javafx.scene.control.ScrollPane;
import model.Classroom;

public abstract class ClassBase extends ScrollPane {

    protected Classroom classroom;

    public ClassBase(Classroom classroom) {
        this.classroom = classroom;

        init();
    }

    public abstract void init();

}
