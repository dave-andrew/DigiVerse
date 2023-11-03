package view.component.classdetail.component;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Classroom;

public class LeftContent extends HBox {

    private String role;
    private Classroom classroom;

    VBox container;
    Label classCodeTitle, classCode;

    private void init() {
        container = new VBox();

        if(role.equals("Teacher")) {
            classCodeTitle = new Label("Class Code:");
            classCode = new Label(classroom.getClassCode());

            container.getChildren().addAll(classCodeTitle, classCode);

            this.getChildren().addAll(container);
        }

    }

    private void setLayout() {


    }

    public LeftContent(String role, Classroom classroom) {
        this.role = role;
        this.classroom = classroom;

        init();
        setLayout();

        this.getStyleClass().add("small-container");
    }

}
