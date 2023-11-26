package view.component.classroom;

import controller.TaskController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import model.Classroom;
import model.Task;

import java.util.ArrayList;

public class ClassCard extends StackPane {

    private TaskController taskController;

    public ClassCard(Classroom classroom) {

        this.taskController = new TaskController();

        this.setPrefSize(300, 250);
        this.getStyleClass().add("class-card");

        VBox cardContent = new VBox(10);

        Label classNameLbl = new Label(classroom.getClassName());
        classNameLbl.getStyleClass().add("bold-text");

        Label classCodeLbl = new Label(classroom.getClassDesc());
        classCodeLbl.setWrapText(true);
        cardContent.getStyleClass().add("blue-bg");

        classNameLbl.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");
        classCodeLbl.setStyle("-fx-text-fill: white; -fx-font-size: 12px;");

        cardContent.setMaxHeight(100);
        cardContent.setPadding(new Insets(20));

        cardContent.getChildren().addAll(classNameLbl, classCodeLbl);

        VBox spacer = new VBox();
        spacer.setPadding(new Insets(10));

        ArrayList<Task> pendingTask = this.taskController.fetchClassroomPendingTask(classroom.getClassId());

        for (Task task : pendingTask) {
            Label taskCard = new Label(" • " + task.getTitle());
            taskCard.setWrapText(true);
            taskCard.setStyle("-fx-text-fill: black; -fx-font-size: 12px;");
            spacer.getChildren().add(taskCard);
        }

        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox pendingTaskContainer = new VBox(10);
        pendingTaskContainer.setPadding(new Insets(0));
        pendingTaskContainer.getChildren().addAll(cardContent, spacer);

        this.getChildren().addAll(pendingTaskContainer);
        setAlignment(cardContent, Pos.TOP_CENTER);
    }

}
