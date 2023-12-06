package net.slc.dv.view.classroom.list;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import net.slc.dv.builder.LabelBuilder;
import net.slc.dv.controller.ClassController;
import net.slc.dv.controller.MemberController;
import javafx.geometry.Insets;
import net.slc.dv.model.Classroom;
import net.slc.dv.model.LoggedUser;
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
                String userRole = new MemberController().getRole(classroom.getClassId());
                new ClassroomDetailView(mainPane, classroom, userRole);

                this.setNavigation.accept(classroom);

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

    private HBox startingBanner() {
        HBox hBox = new HBox();

        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(30));

        VBox messageContainer = new VBox();
        messageContainer.setAlignment(Pos.CENTER_LEFT);
        messageContainer.setSpacing(40);



        Label dear = LabelBuilder.create("Dear " + LoggedUser.getInstance().getUsername() + ",")
                .setStyleClass("title")
                .build();

        Label p1 = new Label("Welcome to an exciting journey of learning and discovery with DigiVerse! I am thrilled to have each and every one of you in our class. This promises to be a rewarding and enriching experience, filled with opportunities for growth, collaboration, and success.");
        p1.setWrapText(true);

        Label p2 = new Label("In this application, we value curiosity, creativity, and the pursuit of knowledge. Together, we will explore new concepts, engage in meaningful discussions, and support one another on our academic endeavors.");
        p2.setWrapText(true);

        Label p3 = new Label("We look forward to getting to know each of you and learning with you this year.");
        p3.setWrapText(true);

        Label p4 = new Label("Let's make this a fantastic academic journey together!");
        p4.setWrapText(true);

        VBox pContainer = new VBox();

        Label p5 = new Label("Sincerely,");
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
