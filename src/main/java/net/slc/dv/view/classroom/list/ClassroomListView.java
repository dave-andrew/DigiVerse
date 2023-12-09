package net.slc.dv.view.classroom.list;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import net.slc.dv.builder.LabelBuilder;
import net.slc.dv.controller.ClassController;
import net.slc.dv.controller.MemberController;
import javafx.geometry.Insets;
import net.slc.dv.enums.Role;
import net.slc.dv.model.Classroom;
import net.slc.dv.model.LoggedUser;
import net.slc.dv.resources.Text;
import net.slc.dv.storage.TextStorage;
import net.slc.dv.view.classroom.detail.ClassroomDetailView;
import net.slc.dv.view.classroom.list.component.ClassCard;
import net.slc.dv.view.home.Home;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ClassroomListView extends GridPane {

    private final StackPane mainPane;
    private Consumer<Classroom> setNavigation;
    private ClassController classController;

    public ClassroomListView(StackPane mainPane, Consumer<Classroom> setNavigation) {
        this.mainPane = mainPane;
        this.setNavigation = setNavigation;
        init();

        ArrayList<Classroom> classroomList = classController.getUserClassroom();

        if(classroomList.isEmpty()) {
            HBox hBox = startingBanner();

            mainPane.getChildren().add(hBox);
            return;
        }

        int columnCount = 0;
        int maxColumns = 4;

        for (Classroom classroom : classroomList) {
            StackPane sp = new ClassCard(classroom);

            sp.setOnMouseClicked(e -> {
                Role userRole = new MemberController().getRole(classroom.getClassId());
                new ClassroomDetailView(mainPane, classroom, userRole);

                this.setNavigation.accept(classroom);

                if (userRole.equals(Role.TEACHER)) {
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

    private HBox startingBanner() {
        HBox hBox = new HBox();

        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(30));

        VBox messageContainer = new VBox();
        messageContainer.setAlignment(Pos.CENTER_LEFT);
        messageContainer.setSpacing(40);



        Label dear = LabelBuilder.create(TextStorage.getText(Text.EMPTY_CLASSROOM_DEAR) + " " + LoggedUser.getInstance().getUsername() + ",")
                .setStyleClass("title")
                .build();

        Label p1 = new Label(TextStorage.getText(Text.EMPTY_CLASSROOM_DESCRIPTION_ONE));
        p1.setWrapText(true);

        Label p2 = new Label(TextStorage.getText(Text.EMPTY_CLASSROOM_DESCRIPTION_TWO));
        p2.setWrapText(true);

        Label p3 = new Label(TextStorage.getText(Text.EMPTY_CLASSROOM_DESCRIPTION_THREE));
        p3.setWrapText(true);

        Label p4 = new Label(TextStorage.getText(Text.EMPTY_CLASSROOM_DESCRIPTION_FOUR));
        p4.setWrapText(true);

        VBox pContainer = new VBox();

        Label p5 = new Label(TextStorage.getText(Text.EMPTY_CLASSROOM_SINCERELY) + ",");
        Label p6 = new Label("DigiVerse Team");
        p6.setStyle("-fx-font-size: 16px");

        pContainer.getChildren().addAll(p5, p6);
        pContainer.setAlignment(Pos.CENTER_RIGHT);

        messageContainer.getChildren().addAll(dear, p1, p2, p3, p4, pContainer);

        messageContainer.setPrefWidth(600);
        messageContainer.setPrefHeight(600);
        messageContainer.setAlignment(Pos.CENTER_LEFT);
        messageContainer.getStyleClass().add("card");

        VBox messageBox = new VBox();
        messageBox.setAlignment(Pos.CENTER);
        messageBox.getChildren().add(messageContainer);

        hBox.getChildren().add(messageBox);
        hBox.setAlignment(Pos.CENTER);

        return hBox;
    }
}
