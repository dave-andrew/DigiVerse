package view.component.classdetail;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import model.Classroom;

public class ClassTask extends ClassBase {
    public ClassTask(Classroom classroom) {
        super(classroom);
    }

    @Override
    public void init() {
        this.setContent(new Label("Task"));
    }
}
