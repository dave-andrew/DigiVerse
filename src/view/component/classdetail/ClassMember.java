package view.component.classdetail;

import controller.ClassController;
import controller.MemberController;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;
import model.Classroom;
import view.component.classdetail.component.ClassMemberItem;

import java.util.concurrent.atomic.AtomicBoolean;

public class ClassMember extends ClassBase {

    private MemberController memberController;
    private SplitPane outerContainer;
    private VBox teacherContainer, studentContainer;
    private Label teacherTitle, studentTitle;

    public ClassMember(Classroom classroom) {
        super(classroom);

        init();
        fetchClassMember();
    }

    @Override
    public void init() {
        this.memberController = new MemberController();


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

    private void fetchClassMember() {
        AtomicBoolean hasStudents = new AtomicBoolean(false);

        memberController.getClassMember(classroom.getClassId()).forEach(classroomMember -> {
            if (classroomMember.getRole().equals("Teacher")) {
                teacherContainer.getChildren().add(new ClassMemberItem(classroomMember));
            } else {
                studentContainer.getChildren().add(new ClassMemberItem(classroomMember));
                hasStudents.set(true);
            }
        });

        if (!hasStudents.get()) {
            Label noStudentsLabel = new Label("No students yet to teach!");
            studentContainer.getChildren().add(noStudentsLabel);
        }
    }

}
