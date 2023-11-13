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
import java.util.HashMap;
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
                Label scoreValue = new Label();
                scoreValue.setPrefWidth(60);
                scoreValue.setAlignment(Pos.CENTER_RIGHT);
                scoreContainer.setAlignment(Pos.CENTER_RIGHT);

                scoreContainer.getChildren().addAll(score, scoreValue);

                MemberItem memberItem = new MemberItem(member);
                HBox spacer = new HBox();

                HBox.setHgrow(spacer, Priority.ALWAYS);

                memberItem.getChildren().add(spacer);
                memberItem.getChildren().add(scoreContainer);

                memberItem.getStyleClass().add("task-item");

                fetchAnswer(member, scoreValue, memberItem);

                this.memberList.getChildren().add(memberItem);
            }
        });
    }

    private void fetchAnswer(ClassroomMember member, Label memberScore, MemberItem memberItem) {
        this.scoreContainer.getChildren().clear();

        double score = 0;
        int taskCount = 0;

        ArrayList<Task> taskList = taskController.getScoredClassroomTask(classroom.getClassId());
        HashMap<String, Integer> taskHash = new HashMap<>();

        for (Task task : taskList) {

            Integer taskScore = this.answerController.getAnswerScore(task.getId(), member.getUser().getId());

            taskHash.put(task.getTitle(), taskScore);

            if(taskScore != null) {
                score += taskScore;
                taskCount++;
            }
        }

        score /= taskCount;

        memberScore.setText(String.format("%.2f", score));

        double averageScore = score;
        memberItem.setOnMouseClicked(e -> {
            displayMemberScore(taskHash, averageScore);
        });

    }

    private void displayMemberScore(HashMap<String, Integer> taskHash, double averageScore) {
        Label scoreTitle = new Label("Student Average Score : ");
        Label scoreValue = new Label(String.format("%.2f", averageScore));

        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);
        container.getStyleClass().add("card");

        scoreValue.getStyleClass().add("title");

        container.getChildren().addAll(scoreTitle, scoreValue);

        this.scoreContainer.getChildren().addAll(container);

        for (String taskTitle : taskHash.keySet()) {
            Label taskTitleLbl = new Label(taskTitle);
            Label taskScoreLbl = new Label("Score: " + taskHash.get(taskTitle).toString());

            HBox taskScoreContainer = new HBox();
            taskScoreContainer.setAlignment(Pos.CENTER_RIGHT);

            taskScoreContainer.getChildren().addAll(taskTitleLbl, taskScoreLbl);

            this.scoreContainer.getChildren().add(taskScoreContainer);
        }
    }

}
