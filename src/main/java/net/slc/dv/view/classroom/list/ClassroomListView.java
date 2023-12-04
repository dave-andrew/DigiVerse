package net.slc.dv.view.classroom.list;

import net.slc.dv.controller.ClassController;
import net.slc.dv.controller.MemberController;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import net.slc.dv.model.Classroom;
import net.slc.dv.view.classroom.detail.ClassroomDetailView;
import net.slc.dv.view.classroom.list.component.ClassCard;
import net.slc.dv.view.home.Home;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ClassroomListView extends GridPane {

    private final StackPane mainPane;
    private Consumer<String> setNavigation;
    private ClassController classController;

    public ClassroomListView(StackPane mainPane, Consumer<String> setNavigation) {
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
                BorderPane classDetail = new ClassroomDetailView(mainPane, classroom, userRole);

                this.setNavigation.accept(classroom.getClassName());

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
