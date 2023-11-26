package view.homeview;

import controller.AuthController;
import controller.ClassController;
import controller.MemberController;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import model.Classroom;
import view.Home;
import view.component.classroom.ClassCard;

import java.util.ArrayList;

public class ClassroomList extends GridPane {

    private ClassController classController;
    private int index = 0;
    private void init() {
        classController = new ClassController();
    }

    private HBox leftNav;
    private StackPane mainPane;
    private Button iconBtn;
    public ClassroomList(StackPane mainPane, HBox leftNav, Button iconBtn) {
        this.leftNav = leftNav;
        this.mainPane = mainPane;
        this.iconBtn = iconBtn;
        init();

        ArrayList<Classroom> classroomList = classController.getUserClassroom();

        for (Classroom classroom : classroomList) {
            StackPane sp = new ClassCard(classroom);

            sp.setOnMouseClicked(e -> {
                String userRole = new MemberController().getRole(classroom.getClassId());
                BorderPane classDetail = new ClassroomDetail(classroom, userRole, mainPane);

                setLeftNav(classroom);

                mainPane.getChildren().clear();
                mainPane.getChildren().add(classDetail);

                if(userRole.equals("Teacher")) {
                    Home.teacherClassList.add(classroom);
                } else {
                    Home.studentClassList.add(classroom);
                }
            });

            this.add(sp, index % 4, index / 4);
            index++;
        }

        this.prefWidthProperty().bind(mainPane.widthProperty().subtract(10));
        this.setPadding(new Insets(20));
        this.setHgap(20);
        this.setVgap(20);
    }

    private void setLeftNav(Classroom classroom) {
        Image image = new Image("file:resources/icons/right-arrow.png");
        ImageView icon = new ImageView(image);

        icon.setFitWidth(15);
        icon.setFitHeight(15);

        Label lbl = new Label(classroom.getClassName());
        lbl.setStyle("-fx-font-size: 16px;");

        lbl.setOnMouseEntered(e -> {
            lbl.setStyle("-fx-underline: true;-fx-cursor: hand;");
        });

        lbl.setOnMouseExited(e -> {
            lbl.setStyle("-fx-underline: false;");
        });

        lbl.setOnMouseClicked(e -> {
            String userRole = new MemberController().getRole(classroom.getClassId());
            BorderPane classDetail = new ClassroomDetail(classroom, userRole, mainPane);

            setLeftNav(classroom);

            mainPane.getChildren().clear();
            mainPane.getChildren().add(classDetail);
        });

        this.leftNav.getChildren().clear();

        this.leftNav.getChildren().addAll(iconBtn, icon, lbl);
    }

}
