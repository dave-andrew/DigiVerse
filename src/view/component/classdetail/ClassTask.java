package view.component.classdetail;

import controller.TaskController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Classroom;
import model.Task;
import view.AddTask;

public class ClassTask extends ClassBase {

    private TaskController taskController;

    private HBox container;
    private VBox taskContainer, taskListContainer;

    private Button addTaskBtn;

    public ClassTask(Classroom classroom) {
        super(classroom);

        actions();
    }

    @Override
    public void init() {
        this.container = new HBox();
        this.taskController = new TaskController();

        this.taskContainer = new VBox();
        this.taskContainer.setAlignment(Pos.TOP_CENTER);

        this.addTaskBtn = new Button("+ Add Task");
        this.addTaskBtn.getStyleClass().add("primary-button");

        this.taskListContainer = new VBox();
        this.taskListContainer.setMaxWidth(700);
        VBox.setMargin(addTaskBtn, new Insets(0, 0, 40, 0));

        this.taskListContainer.setAlignment(Pos.TOP_LEFT);

        this.taskContainer.getChildren().addAll(addTaskBtn, taskListContainer);

        this.container.getChildren().add(taskContainer);

        this.taskContainer.prefWidthProperty().bind(this.widthProperty());

        fetchTask();

        this.setContent(container);
    }

    private void actions() {
        this.addTaskBtn.setOnAction(e -> {
            Scene scene = this.getScene();
            Stage stage = (Stage) scene.getWindow();

            new AddTask(stage, this.classroom);
        });
    }

    private void fetchTask() {
        this.taskListContainer.getChildren().clear();

        Label title = new Label("Task List");
        this.taskListContainer.getChildren().add(title);

        title.getStyleClass().add("title");
        for (Task task : this.taskController.getClassroomTask(classroom.getClassId())) {
            System.out.println(task.getTitle());
//            this.taskListContainer.getChildren().add();
        }
    }
}
