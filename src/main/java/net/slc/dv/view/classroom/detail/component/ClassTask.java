package net.slc.dv.view.classroom.detail.component;

import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import net.slc.dv.controller.TaskController;
import net.slc.dv.enums.Role;
import net.slc.dv.helper.StageManager;
import net.slc.dv.model.Classroom;
import net.slc.dv.model.Task;
import net.slc.dv.resources.Text;
import net.slc.dv.storage.TextStorage;
import net.slc.dv.view.task.add.AddTaskView;
import net.slc.dv.view.task.task.TaskBase;

public class ClassTask extends ClassBase {

    private final StackPane mainPane;
    private final Role userRole;

    private TaskController taskController;
    private HBox container, titleContainer;
    private VBox taskContainer, taskListContainer;

    private Button addTaskBtn;

    public ClassTask(Classroom classroom, StackPane mainPane, Role userRole) {
        super(classroom);
        this.mainPane = mainPane;
        this.userRole = userRole;

        initTask();
        actions();
    }

    @Override
    public void init() {
        this.container = new HBox();
        this.taskController = new TaskController();

        this.taskContainer = new VBox();
        this.taskContainer.setAlignment(Pos.TOP_CENTER);
    }

    public void initTask() {
        this.taskListContainer = new VBox();
        this.taskListContainer.setMaxWidth(700);

        this.taskListContainer.setAlignment(Pos.TOP_LEFT);

        this.taskContainer.getChildren().add(taskListContainer);

        this.container.getChildren().add(taskContainer);

        this.taskContainer.prefWidthProperty().bind(this.widthProperty());

        fetchTask();

        this.setContent(container);
        this.setFitToWidth(true);
        this.setPannable(true);
    }

    private void actions() {
        this.addTaskBtn.setOnMouseClicked(e -> {
            new AddTaskView(StageManager.getInstance(), this.classroom);
            fetch();
        });
    }

    private void fetchTask() {
        this.taskListContainer.getChildren().clear();

        Label title = new Label(TextStorage.getText(Text.TASK_LIST) + ":");

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        this.titleContainer = new HBox(title, spacer);
        titleContainer.setAlignment(Pos.TOP_CENTER);

        this.addTaskBtn = new Button("+ " + TextStorage.getText(Text.CREATE_TASK));
        this.addTaskBtn.getStyleClass().add("primary-button");
        this.addTaskBtn.setStyle("-fx-text-fill: white");

        titleContainer.getChildren().add(addTaskBtn);

        this.addTaskBtn.setVisible(this.userRole.equals(Role.TEACHER));

//        this.taskListContainer.getChildren().add(titleContainer);
        VBox.setMargin(titleContainer, new Insets(40, 0, 20, 0));

        title.getStyleClass().add("title");

        fetch();
    }

    private void fetch() {
        this.taskListContainer.getChildren().clear();
        this.taskListContainer.getChildren().add(titleContainer);

        ArrayList<Task> tasks = this.taskController.getClassroomTask(this.classroom.getClassId());

        if (tasks.isEmpty()) {
            Label empty = new Label(TextStorage.getText(Text.LETS_CHILL));
            empty.getStyleClass().add("title");

            HBox centerBox = new HBox(empty);
            centerBox.setAlignment(Pos.CENTER);

            this.taskListContainer.getChildren().add(centerBox);
            return;
        }

        for (Task task : tasks) {
            TaskItem taskItem = new TaskItem(task);
            this.taskListContainer.getChildren().add(taskItem);

            taskItem.setOnMouseClicked(e -> {
                new TaskBase(mainPane, task, classroom, userRole);
            });
        }
    }
}
