package view.component.classdetail;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import model.Classroom;

public class ClassMember extends ClassBase {
    public ClassMember(Classroom classroom) {
        super(classroom);

        init();
    }

    @Override
    public void init() {
        this.setContent(new Label("Member"));
    }

}
