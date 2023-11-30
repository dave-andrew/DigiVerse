package net.slc.dv.view.component.classdetail;

import javafx.scene.control.ScrollPane;
import net.slc.dv.model.Classroom;

public abstract class ClassBase extends ScrollPane {

    protected Classroom classroom;

    public ClassBase(Classroom classroom) {
        this.classroom = classroom;

        init();
    }

    public abstract void init();

}
