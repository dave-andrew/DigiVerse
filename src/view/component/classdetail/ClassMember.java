package view.component.classdetail;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import model.Classroom;

public class ClassMember extends ClassBase {

    private SplitPane outerContainer;
    private VBox teacherContainer, studentContainer;
    private Label teacherTitle, studentTitle;

    public ClassMember(Classroom classroom) {
        super(classroom);

        init();
    }

    @Override
    public void init() {
        SplitPane container = new SplitPane();
        container.setStyle("-fx-background-color: transparent;");

        outerContainer = new SplitPane();
        outerContainer.setStyle("-fx-background-color: transparent;");
        outerContainer.setPadding(new Insets(20));

        VBox teacherBox = new VBox(10);
        VBox studentBox = new VBox(10);

        teacherContainer = new VBox(10);
        studentContainer = new VBox(10);

        teacherContainer.setPadding(new Insets(7, 20, 7, 20));
        studentContainer.setPadding(new Insets(7, 20, 7, 20));

        teacherTitle = new Label("Teacher");
        teacherTitle.getStyleClass().add("title");

        studentTitle = new Label("Student");
        studentTitle.getStyleClass().add("title");

        teacherContainer.getChildren().add(teacherTitle);
        teacherContainer.getStyleClass().add("border");

        teacherContainer.getChildren().add(new Label("Teacher 1"));

        studentContainer.getChildren().add(studentTitle);
        studentContainer.getStyleClass().add("border");

        teacherBox.getChildren().add(teacherContainer);
        studentBox.getChildren().add(studentContainer);

        outerContainer.getItems().addAll(teacherBox, studentBox);

        outerContainer.setDividerPositions(0.5);

        container.getItems().add(outerContainer);
        container.prefWidthProperty().bind(this.widthProperty());

        container.setPadding(new Insets(10, 40, 10, 40));

        this.setContent(container);
    }

}
