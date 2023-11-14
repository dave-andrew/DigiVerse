package view.component.classdetail;

import controller.AnswerController;
import controller.MemberController;
import controller.TaskController;
import helper.StageManager;
import helper.Toast;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private ScrollPane members, taskScores;
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
        this.members.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        this.members.setFitToWidth(true);
        this.members.setPannable(true);

        this.memberContainer = new VBox();
        this.memberContainer.setPrefWidth(450);

        this.scoreContainer = new VBox();
        this.scoreContainer.setAlignment(Pos.TOP_CENTER);

        this.taskScores = new ScrollPane();
        this.taskScores.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        this.taskScores.setFitToWidth(true);
        this.taskScores.setPannable(true);

        this.memberList = new VBox();
        this.memberList.setPadding(new Insets(20));
        this.memberContainer.setStyle("-fx-border-color: transparent #e0e0e0 transparent transparent;");

        this.members.setContent(memberList);
        this.memberContainer.getChildren().add(members);

        this.members.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);

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
                scoreContainer.setAlignment(Pos.CENTER_RIGHT);


                Label scoreValue = new Label("0");

                scoreContainer.getChildren().addAll(score, scoreValue);

                MemberItem memberItem = new MemberItem(member);
                HBox spacer = new HBox();

                HBox.setHgrow(spacer, Priority.ALWAYS);

                memberItem.getChildren().add(spacer);
                memberItem.getChildren().add(scoreContainer);

                memberItem.getStyleClass().add("task-item");

                fetchAnswer(member, memberItem, scoreContainer, scoreValue);

                this.memberList.getChildren().add(memberItem);
            }
        });
    }

    private void fetchAnswer(ClassroomMember member, MemberItem memberItem, HBox scoreContainer, Label scoreSideValue) {

        double score = 0;
        int taskCount = 0;

        ArrayList<Task> taskList = taskController.getScoredClassroomTask(classroom.getClassId());
        HashMap<String, Integer> taskHash = new HashMap<>();

        for (Task task : taskList) {

            Integer taskScore = this.answerController.getAnswerScore(task.getId(), member.getUser().getId());

            taskHash.put(task.getTitle(), taskScore);

            if (taskScore != null) {
                score += taskScore;
                taskCount++;
            }
        }

        if (taskCount != 0) {
            score /= taskCount;
        }

        scoreContainer.getChildren().remove(scoreContainer.getChildren().size() - 1);

        Label scoreValue = new Label(String.format("%.2f", score));
        scoreValue.setPrefWidth(60);
        scoreValue.setAlignment(Pos.CENTER_RIGHT);

        scoreContainer.getChildren().add(scoreValue);
        scoreSideValue.setText(String.format("%.2f", score));

        double averageScore = score;
        memberItem.setOnMouseClicked(e -> {
            this.selectedMember = member;
            displayMemberScore(taskHash, averageScore, memberItem, scoreContainer);
        });
    }

    private ClassroomMember selectedMember;

    private void displayMemberScore(HashMap<String, Integer> taskHash, double averageScore, MemberItem memberItem, HBox scoreContainer) {
        this.scoreContainer.getChildren().clear();

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);

        this.scoreContainer.getChildren().add(scrollPane);

        Label scoreTitle = new Label("Student Average Score : ");
        Label scoreValue = new Label(String.format("%.2f", averageScore));

        VBox scoreInnerContainer = new VBox();
        scoreInnerContainer.setAlignment(Pos.CENTER_LEFT);
        scoreInnerContainer.setPadding(new Insets(20));
        scoreInnerContainer.setSpacing(20);
        scoreInnerContainer.setPrefWidth(870);

        scrollPane.setContent(scoreInnerContainer);

        VBox averageScoreContainer = new VBox();
        averageScoreContainer.setAlignment(Pos.CENTER);
        averageScoreContainer.getStyleClass().add("card");

        scoreValue.getStyleClass().add("title");

        averageScoreContainer.getChildren().addAll(scoreTitle, scoreValue);

        scoreInnerContainer.getChildren().addAll(averageScoreContainer);

        for (String taskTitle : taskHash.keySet()) {
            Label taskTitleLbl = new Label(taskTitle);

            HBox taskScoreContainer = new HBox();
            taskScoreContainer.setAlignment(Pos.CENTER_LEFT);

            taskScoreContainer.getChildren().addAll(taskTitleLbl);

            HBox container = new HBox();
            container.setAlignment(Pos.CENTER_LEFT);

            Label taskScoreLbl = new Label("Score: ");
            container.getChildren().add(taskScoreLbl);

            if(taskHash.get(taskTitle) != null) {
                Label scoreLbl = new Label(String.valueOf(taskHash.get(taskTitle)));
                scoreLbl.setPrefWidth(60);
                scoreLbl.setAlignment(Pos.CENTER_RIGHT);
                container.getChildren().add(scoreLbl);

                HBox spacer = new HBox();
                spacer.setPrefWidth(50);

                container.getChildren().add(spacer);

            } else {
                TextField taskScoreInput = new TextField();
                taskScoreInput.setText(Integer.toString(0));
                taskScoreInput.setAlignment(Pos.CENTER_RIGHT);

                taskScoreInput.setOnMouseClicked(e -> {
                    taskScoreInput.clear();
                });

                taskScoreInput.focusedProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue && taskScoreInput.getText().isEmpty()) {
                        taskScoreInput.setText("0");
                    }
                });

                taskScoreInput.setPrefWidth(60);

                container.getChildren().add(taskScoreInput);

                Image image = new Image("file:resources/icons/save.png");
                ImageView saveIcon = new ImageView(image);

                saveIcon.setFitWidth(20);
                saveIcon.setFitHeight(20);

                Button saveBtn = new Button();
                saveBtn.setGraphic(saveIcon);

                HBox.setMargin(saveBtn, new Insets(0, 0, 0, 10));

                saveBtn.setStyle("-fx-cursor: hand;-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0;");

                container.getChildren().add(saveBtn);

                saveBtn.setOnMouseClicked(e -> {
                    if(this.answerController.scoreAnswer(taskTitle, selectedMember.getUser().getId(), taskScoreInput.getText())) {
                        Toast.makeText(StageManager.getInstance(), "Score saved!", 2000, 500, 500);
                        container.getChildren().removeAll(taskScoreInput, saveBtn);
                        Label scoreInput = new Label(taskScoreInput.getText());
                        scoreInput.setPrefWidth(60);
                        HBox.setMargin(scoreInput, new Insets(0, 50, 0, 0));
                        scoreInput.setAlignment(Pos.CENTER_RIGHT);

                        container.getChildren().add(scoreInput);

                        fetchAnswer(selectedMember, memberItem, scoreContainer, scoreValue);
                    }
                });
            }

            HBox spacer = new HBox();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            taskScoreContainer.getChildren().add(spacer);

            taskScoreContainer.getChildren().add(container);
            taskScoreContainer.setMaxWidth(400);

            scoreInnerContainer.getChildren().add(taskScoreContainer);
        }
    }

}
