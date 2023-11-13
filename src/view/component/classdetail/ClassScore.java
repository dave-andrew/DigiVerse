package view.component.classdetail;

import controller.AnswerController;
import controller.MemberController;
import controller.TaskController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import model.Classroom;
import model.ClassroomMember;
import model.Task;
import view.component.classdetail.component.MemberItem;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ClassScore extends HBox {

    private MemberController memberController;
    private TaskController taskController;
    private AnswerController answerController;
    private VBox memberContainer, scoreContainer;
    private VBox memberList;
    private ScrollPane members;
    private Classroom classroom;

    public ClassScore(Classroom classroom) {
        this.classroom = classroom;
        init();

        setLayout();
    }

    private void init() {
        this.memberController = new MemberController();
        this.taskController = new TaskController();
        this.answerController = new AnswerController();

        this.members = new ScrollPane();
        this.members.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        this.members.setFitToWidth(true);
        this.members.setPannable(true);

        this.memberContainer = new VBox();
        this.memberContainer.setPrefWidth(500);


        this.scoreContainer = new VBox();

        this.memberList = new VBox();
        this.memberList.setPadding(new Insets(20));

        this.memberContainer.setPrefWidth(500);
        this.memberContainer.setStyle("-fx-border-color: transparent #e0e0e0 transparent transparent;");

        this.members.setContent(memberList);
        this.memberContainer.getChildren().add(members);

        this.getChildren().addAll(memberContainer, scoreContainer);
    }

    private void setLayout() {
        fetchMember();
    }

    private void fetchMember() {
        this.memberList.getChildren().clear();

        this.memberController.getClassMember(classroom.getClassId()).forEach(member -> {
            if(member.getRole().equals("Student")) {
                HBox scoreContainer = new HBox();
                Label score = new Label("Score: ");
                Label scoreValue = new Label("100");
                scoreValue.setPrefWidth(40);
                scoreValue.setAlignment(Pos.CENTER_RIGHT);
                scoreContainer.setAlignment(Pos.CENTER_RIGHT);

                scoreContainer.getChildren().addAll(score, scoreValue);

                MemberItem memberItem = new MemberItem(member);
                HBox spacer = new HBox();

                HBox.setHgrow(spacer, Priority.ALWAYS);

                memberItem.getChildren().add(spacer);
                memberItem.getChildren().add(scoreContainer);

                memberItem.getStyleClass().add("task-item");

                memberItem.setOnMouseClicked(e -> {
                    fetchAnswer(member);
                });

                this.memberList.getChildren().add(memberItem);
            }
        });
    }

    private void fetchAnswer(ClassroomMember member) {
        this.scoreContainer.getChildren().clear();

        double score = 0;
        int taskCount = 0;

        ArrayList<Task> taskList = taskController.getScoredClassroomTask(classroom.getClassId());

        for (Task task : taskList) {

            HBox scoreContent = new HBox();

            Label taskTitle = new Label(task.getTitle());


            Integer taskScore = this.answerController.getAnswerScore(task.getId(), member.getUser().getId());

            if(taskScore != null) {
                score += taskScore;
                taskCount++;
            }

            this.scoreContainer.getChildren().add(taskTitle);
        }

        score /= taskCount;

        System.out.println(score);
    }

}
