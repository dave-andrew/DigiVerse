package view.component.classdetail;

import controller.TaskController;
import helper.StageManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import model.Classroom;
import model.Task;
import view.component.classdetail.component.TaskItem;
import view.homeview.AddTask;
import view.homeview.task.TaskBase;

import java.util.ArrayList;

public class ClassTask extends ClassBase {

    private final StackPane mainPane;
    private final String userRole;

    private TaskController taskController;
    private HBox container;
    private VBox taskContainer, taskListContainer;

    private Button addTaskBtn;

    public ClassTask(Classroom classroom, StackPane mainPane, String userRole) {
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
    }

    private void actions() {
        if (this.userRole.equals("Teacher")) {
            this.addTaskBtn.setOnMouseClicked(e -> {

                new AddTask(StageManager.getInstance(), this.classroom);
                fetchTask();
            });
        }
    }

    private void fetchTask() {
        this.taskListContainer.getChildren().clear();


        Label title = new Label("Task List:");

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox titleContainer = new HBox(title, spacer);
        titleContainer.setAlignment(Pos.TOP_CENTER);

        if (this.userRole.equals("Teacher")) {
            this.addTaskBtn = new Button("+ Create Task");
            this.addTaskBtn.getStyleClass().add("primary-button");
            this.addTaskBtn.setStyle("-fx-text-fill: white");

            titleContainer.getChildren().add(addTaskBtn);
        }

        this.taskListContainer.getChildren().add(titleContainer);
        VBox.setMargin(titleContainer, new Insets(40, 0, 20, 0));

        title.getStyleClass().add("title");

        ArrayList<Task> tasks = this.taskController.getClassroomTask(this.classroom.getClassId());

        if (tasks.isEmpty()) {
            Label empty = new Label("No task yet! Chill dulu gak sih?");
            empty.getStyleClass().add("empty");

            HBox centerBox = new HBox(empty);
            centerBox.setAlignment(Pos.CENTER);

            this.taskListContainer.getChildren().add(centerBox);
            return;
        }

        for (Task task : tasks) {
            TaskItem taskItem = new TaskItem(task);
            this.taskListContainer.getChildren().add(taskItem);

            taskItem.setOnMouseClicked(e -> {
                mainPane.getChildren().clear();
                mainPane.getChildren().add(new TaskBase(task, classroom, userRole));
            });
        }
    }
}
