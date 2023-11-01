package view.homeview;

import controller.AuthController;
import controller.ClassController;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import model.Classroom;
import view.component.classroom.ClassCard;

import java.util.ArrayList;

public class ClassroomList extends GridPane {

    private ClassController classController;
    private int index = 0;
    private void init() {
        classController = new ClassController();
    }

    public ClassroomList(StackPane mainPane) {

        init();

        ArrayList<Classroom> classroomList = classController.getUserClassroom();

        for (Classroom classroom : classroomList) {
            StackPane sp = new ClassCard(classroom.getClassName(), classroom.getClassDesc());

            sp.setOnMouseClicked(e -> {
                BorderPane classDetail = new ClassroomDetail(classroom);

                mainPane.getChildren().clear();
                mainPane.getChildren().add(classDetail);
            });

            this.add(sp, index % 4, index / 4);
            index++;
        }

        this.setPadding(new Insets(20));
        this.setHgap(20);
        this.setVgap(20);
    }

}
