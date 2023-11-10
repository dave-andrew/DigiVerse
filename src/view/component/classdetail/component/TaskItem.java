package view.component.classdetail.component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;
import javafx.scene.layout.Region; // Import Region for setting spacing
import model.Task;

import javax.swing.*;

public class TaskItem extends HBox {

    public TaskItem(Task task) {
        StackPane stackPane = new StackPane();

        Image image = new Image("file:resources/icons/task.png");
        ImageView imageView = new ImageView(image);
        stackPane.getChildren().add(imageView);

        stackPane.getStyleClass().add("circle-bg");

        imageView.setFitWidth(20);
        imageView.setFitHeight(20);

        HBox.setMargin(stackPane, new Insets(0, 20, 0, 0));

        this.getChildren().add(stackPane);

        Label title = new Label(task.getTitle());
        title.setAlignment(Pos.CENTER);

        this.getChildren().add(title);

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        this.getChildren().add(spacer);

        Label deadline = new Label(task.getDeadlineAt());
        deadline.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(deadline, Priority.NEVER);
        deadline.setStyle("-fx-font-size: 13px; -fx-text-fill: #9E9E9E;");
        this.getChildren().add(deadline);

        this.getStyleClass().add("task-item");

        this.setAlignment(Pos.CENTER_LEFT);
    }
}
