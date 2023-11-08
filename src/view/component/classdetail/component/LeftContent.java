package view.component.classdetail.component;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import model.Classroom;

public class LeftContent extends HBox {

    private String role;
    private Classroom classroom;

    private VBox container;
    private Label classCodeTitle, classCode;

    public LeftContent(String role, Classroom classroom) {
        this.role = role;
        this.classroom = classroom;

        container = new VBox();

        if (role.equals("Teacher")) {
            classCodeTitle = new Label("Class Code:");
            classCode = new Label(classroom.getClassCode());

            container.getChildren().addAll(classCodeTitle, classCode);
        }

        VBox.setVgrow(container, Priority.NEVER);

        this.getChildren().add(container);

        HBox.setHgrow(this, Priority.ALWAYS);

        container.getStyleClass().add("small-container");
    }
}
