package net.slc.dv.view.classroom.detail.component;

import net.slc.dv.builder.ImageViewBuilder;
import net.slc.dv.helper.DateManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import net.slc.dv.model.Task;
import net.slc.dv.resources.Icon;
import net.slc.dv.resources.IconStorage;

public class TaskItem extends HBox {

	public TaskItem(Task task) {
		StackPane stackPane = new StackPane();

		ImageView imageView = ImageViewBuilder.create()
				.bindImageProperty(IconStorage.getIcon(Icon.TASK))
				.setFitHeight(20)
				.setFitWidth(20)
				.build();

		stackPane.getChildren().add(imageView);

		stackPane.getStyleClass().add("circle-bg");


		HBox.setMargin(stackPane, new Insets(0, 20, 0, 0));

		this.getChildren().add(stackPane);

		Label title = new Label(task.getTitle());
		title.setAlignment(Pos.CENTER);

		this.getChildren().add(title);

		HBox spacer = new HBox();
		HBox.setHgrow(spacer, Priority.ALWAYS);

		this.getChildren().add(spacer);

		String formattedDate = DateManager.ddMMMyy(task.getDeadlineAt());

		Label deadline = new Label(formattedDate);
		deadline.setAlignment(Pos.CENTER_RIGHT);
		HBox.setHgrow(deadline, Priority.NEVER);
		deadline.setStyle("-fx-font-size: 13px; -fx-text-fill: #9E9E9E;");
		this.getChildren().add(deadline);

		this.getStyleClass().add("task-item");

		this.setAlignment(Pos.CENTER_LEFT);
	}
}
