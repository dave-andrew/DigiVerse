package view.component.classdetail;

import controller.TaskController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Classroom;
import model.Task;
import view.homeview.AddTask;
import view.homeview.task.TaskBase;
import view.component.classdetail.component.TaskItem;

import java.util.ArrayList;

public class ClassTask extends ClassBase {

    private TaskController taskController;
    private StackPane mainPane;
    private String userRole;

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
        if(this.userRole.equals("Teacher")) {
            this.addTaskBtn = new Button("Add Task");
            this.addTaskBtn.getStyleClass().add("primary-button");

            this.taskContainer.getChildren().add(addTaskBtn);
            VBox.setMargin(addTaskBtn, new Insets(0, 0, 40, 0));
        }

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
        if(this.userRole.equals("Teacher")) {
            this.addTaskBtn.setOnAction(e -> {
                Scene scene = this.getScene();
                Stage stage = (Stage) scene.getWindow();

                new AddTask(stage, this.classroom);
            });
        }
    }

    private void fetchTask() {
        this.taskListContainer.getChildren().clear();

        Label title = new Label("Task List:");
        this.taskListContainer.getChildren().add(title);

        VBox.setMargin(title, new Insets(0, 0, 20, 0));

        title.getStyleClass().add("title");

        ArrayList<Task> tasks = this.taskController.getClassroomTask(this.classroom.getClassId());

        if(tasks.isEmpty()) {
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
                mainPane.getChildren().add(new TaskBase(task, userRole));
            });
        }
    }
}
