package net.slc.dv.view.homeview;

import net.slc.dv.controller.ClassController;
import net.slc.dv.controller.MemberController;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import net.slc.dv.model.Classroom;
import net.slc.dv.view.home.Home;
import net.slc.dv.view.component.classroom.ClassCard;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ClassroomList extends GridPane {

    private final StackPane mainPane;
    private Consumer<String> setNavigation;
    private ClassController classController;

    public ClassroomList(StackPane mainPane, Consumer<String> setNavigation) {
        this.mainPane = mainPane;
        this.setNavigation = setNavigation;
        init();

        ArrayList<Classroom> classroomList = classController.getUserClassroom();

        int columnCount = 0;
        int maxColumns = 4;

        for (Classroom classroom : classroomList) {
            StackPane sp = new ClassCard(classroom);

            sp.setOnMouseClicked(e -> {
                String userRole = new MemberController().getRole(classroom.getClassId());
                BorderPane classDetail = new ClassroomDetail(classroom, userRole, mainPane);

                this.setNavigation.accept(classroom.getClassName());

                mainPane.getChildren().clear();
                mainPane.getChildren().add(classDetail);

                if (userRole.equals("Teacher")) {
                    Home.teacherClassList.add(classroom);
                } else {
                    Home.studentClassList.add(classroom);
                }
            });

            this.add(sp, columnCount % maxColumns, columnCount / maxColumns);

            columnCount++;

            if (columnCount % maxColumns == 0) {
                this.addRow(columnCount / maxColumns);
            }
        }

        this.prefWidthProperty().bind(mainPane.widthProperty().subtract(10));
        this.setPadding(new Insets(20));
        this.setHgap(20);
        this.setVgap(20);
    }

    private void init() {
        classController = new ClassController();
    }


}
