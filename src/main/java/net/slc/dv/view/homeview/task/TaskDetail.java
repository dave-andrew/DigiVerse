package net.slc.dv.view.homeview.task;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import net.slc.dv.controller.AnswerController;
import net.slc.dv.controller.CommentController;
import net.slc.dv.helper.DateManager;
import net.slc.dv.helper.ImageManager;
import net.slc.dv.helper.toast.ToastBuilder;
import net.slc.dv.model.LoggedUser;
import net.slc.dv.model.Task;
import net.slc.dv.model.TaskComment;
import net.slc.dv.view.component.classdetail.component.CommentItem;
import net.slc.dv.view.component.classtask.UploadFileModal;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TaskDetail extends HBox {

    private final AnswerController answerController;
    private final CommentController commentController;
    private final Task task;
    private final String userRole;
    private VBox mainContent, sideContent, fileContainer;
    private HBox innerMainContent;
    private Button submitBtn, markAsDoneBtn;
    private Label submitStatus;
    private Button downloadBtn;

    private VBox commentListContainer;

    public TaskDetail(Task task, String userRole) {
        this.task = task;
        this.userRole = userRole;
        this.answerController = new AnswerController();
        this.commentController = new CommentController();
        init();
        setLayout();
        setSideContent();
        actions();
    }

    private void init() {
        this.mainContent = new VBox();
        this.sideContent = new VBox();
        this.innerMainContent = new HBox();

        this.setPadding(new Insets(35, 60, 35, 60));
        this.prefWidthProperty().bind(this.widthProperty());
    }

    private void setLayout() {
        StackPane imgStack = new StackPane();
        imgStack.setAlignment(Pos.TOP_LEFT);
        HBox.setMargin(imgStack, new Insets(0, 20, 0, 0));

        Image img = new Image("file:resources/icons/task.png");
        ImageView imgView = new ImageView(img);
        imgView.setFitWidth(40);
        imgView.setFitHeight(40);

        imgStack.setMaxHeight(40);

        imgStack.getChildren().add(imgView);

        imgStack.getStyleClass().add("circle-bg");

        Label taskName = new Label(task.getTitle());
        taskName.getStyleClass().add("title");

        String createDate = DateManager.ddMMMyy(task.getCreatedAt());

        Label postedBy = new Label("Posted by: " + task.getUser().getUsername() + " \u2022 " + createDate);
        VBox.setMargin(postedBy, new Insets(20, 0, 0, 0));

        VBox detail = new VBox();
        detail.getChildren().addAll(taskName, postedBy);
        HBox.setHgrow(detail, Priority.ALWAYS);

        this.commentListContainer = new VBox(10);
        this.commentListContainer.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(commentListContainer, Priority.ALWAYS);
        this.commentListContainer.setPadding(new Insets(20, 0, 0, 0));

        Label score;
        if (task.isScored()) {
            score = new Label("Score: 100");
        } else {
            score = new Label("Score: -");
        }

        String formattedDate = DateManager.ddMMMyy(task.getDeadlineAt());

        Label deadline = new Label("Deadline: " + formattedDate);

        HBox scoreDeadlineBox = new HBox();
        scoreDeadlineBox.setAlignment(Pos.CENTER_LEFT);

        HBox.setHgrow(score, Priority.ALWAYS);
        score.setAlignment(Pos.CENTER_LEFT);

        HBox.setHgrow(deadline, Priority.NEVER);
        deadline.setAlignment(Pos.CENTER_RIGHT);

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        scoreDeadlineBox.getChildren().addAll(score, spacer, deadline);

        detail.getChildren().add(scoreDeadlineBox);

        scoreDeadlineBox.getStyleClass().add("bottom-border");

        Line line = new Line();
        line.setStroke(Color.valueOf("#E0E0E0"));
        line.endXProperty().bind(detail.widthProperty());
        line.endXProperty().bind(innerMainContent.widthProperty().subtract(75));

//        detail.getStyleClass().add("bottom-border");

//        VBox.setMargin(line, new Insets(20, 0, 20, 0));

//        detail.getChildren().add(line);

        HBox taskDescContainer = new HBox();
        taskDescContainer.setAlignment(Pos.CENTER_LEFT);

        taskDescContainer.getStyleClass().add("bottom-border");
        taskDescContainer.setPadding(new Insets(20, 0, 20, 0));

        Label taskDesc = new Label(task.getDescription());
        HBox.setHgrow(taskDesc, Priority.ALWAYS);
        taskDesc.setPadding(new Insets(10, 0, 5, 0));
        taskDesc.setWrapText(true);

        taskDescContainer.getChildren().add(taskDesc);

        detail.getChildren().add(taskDescContainer);

        VBox userComment = new VBox();
        userComment.setAlignment(Pos.CENTER_LEFT);

        Label commentTitle = new Label("Add Comment:");
        commentTitle.getStyleClass().add("title");

        userComment.getChildren().add(commentTitle);

        if (userRole.equals("Student")) {
            HBox commentInputContainer = new HBox(10);
            commentInputContainer.setPadding(new Insets(10, 10, 10, 10));

            ImageView userImg;
            if (LoggedUser.getInstance().getProfileImage() == null) {
                userImg = new ImageView(new Image("file:resources/icons/user.png"));

                userImg.setFitWidth(30);
                userImg.setFitHeight(30);

            } else {
                userImg = new ImageView(LoggedUser.getInstance().getProfileImage());
            }

            ImageManager.makeCircular(userImg, 15);

            commentInputContainer.getChildren().add(userImg);

            TextField commentInput = new TextField();
            HBox.setHgrow(commentInput, Priority.ALWAYS);
            commentInput.setPromptText("Write your comment here...");

            commentInputContainer.getChildren().add(commentInput);

            commentInput.setOnKeyPressed(e -> {
                if (e.getCode().toString().equals("ENTER")) {
                    TaskComment newTaskComment = this.commentController.createTaskComment(commentInput.getText(), task.getId(), LoggedUser.getInstance().getId());


                    HBox commentItem = new CommentItem(newTaskComment);
                    commentListContainer.getChildren().add(0, commentItem);

                    commentInput.clear();
                }
            });

            userComment.getChildren().add(commentInputContainer);

            detail.getChildren().add(userComment);
        }

        if (this.userRole.equals("Teacher")) {
            ArrayList<TaskComment> taskCommentList = this.commentController.getTaskComments(task.getId());

            for (TaskComment taskComment : taskCommentList) {

                VBox commentContainer = new VBox();

                CommentItem commentItem = new CommentItem(taskComment);
                commentContainer.getChildren().add(commentItem);

                commentListContainer.getChildren().add(commentContainer);
            }
        } else if (this.userRole.equals("Student")) {
            ArrayList<TaskComment> taskCommentList = this.commentController.getStudentTaskComments(task.getId());

            for (TaskComment taskComment : taskCommentList) {

                VBox commentContainer = new VBox();

                CommentItem commentItem = new CommentItem(taskComment);
                commentContainer.getChildren().add(commentItem);

                commentListContainer.getChildren().add(commentContainer);
            }
        }

        detail.getChildren().add(commentListContainer);

        innerMainContent.getChildren().addAll(imgStack, detail);
        innerMainContent.setAlignment(Pos.TOP_LEFT);

        mainContent.getChildren().add(innerMainContent);
        mainContent.getStyleClass().add("card");
        mainContent.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(mainContent, Priority.ALWAYS);
//        mainContent.setMaxWidth(800);

        HBox.setMargin(sideContent, new Insets(0, 0, 0, 50));

        this.getChildren().addAll(mainContent, sideContent);
    }

    private void setSideContent() {
        VBox submitContainer = new VBox();
        VBox.setVgrow(submitContainer, Priority.NEVER);

        Label submitTitle = new Label("Submit Task");
        submitTitle.getStyleClass().add("title");

        this.submitStatus = new Label("Not Submitted");

        HBox submitStatusContainer = new HBox();
        submitStatusContainer.setAlignment(Pos.CENTER_LEFT);

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        submitStatusContainer.getChildren().addAll(submitTitle, spacer, submitStatus);

        this.submitBtn = new Button("+ Upload File");
        submitBtn.getStyleClass().add("primary-button");
        submitBtn.setStyle("-fx-text-fill: #fff;");
        submitBtn.setPrefSize(300, 40);
        VBox.setMargin(submitBtn, new Insets(30, 0, 0, 0));

        this.markAsDoneBtn = new Button("Mark as Done");
        markAsDoneBtn.getStyleClass().add("secondary-button");
        markAsDoneBtn.setPrefSize(300, 40);
        VBox.setMargin(markAsDoneBtn, new Insets(10, 0, 0, 0));

        submitContainer.getChildren().addAll(submitStatusContainer, submitBtn, markAsDoneBtn);

        if (this.userRole.equals("Student")) {

            sideContent.getChildren().addAll(submitContainer);
            sideContent.setAlignment(Pos.TOP_CENTER);


            VBox constraintBox = new VBox();
            constraintBox.setAlignment(Pos.CENTER_LEFT);
            constraintBox.getStyleClass().add("card");

            Label constraintTitle = new Label("Constraints: ");
            constraintBox.getChildren().add(constraintTitle);

            Label constraint1 = new Label("1. Max file size 100Kb");
            constraint1.setStyle("-fx-font-size: 14px;");
            VBox.setMargin(constraint1, new Insets(5, 0, 0, 0));

            Label constraint2 = new Label("2. File type any.");
            constraint2.setStyle("-fx-font-size: 14px;");

            constraintBox.getChildren().addAll(constraint1, constraint2);

            VBox.setMargin(constraintBox, new Insets(30, 0, 0, 0));

            sideContent.getChildren().add(constraintBox);
            sideContent.setPrefWidth(330);
        }

        submitContainer.getStyleClass().add("card");

        this.fileContainer = new VBox();
        fileContainer.setAlignment(Pos.CENTER_LEFT);
        fileContainer.getStyleClass().add("card");

        Label fileTitle = new Label("Your Answer: ");
        fileContainer.getChildren().add(fileTitle);
        VBox.setMargin(fileContainer, new Insets(30, 0, 0, 0));

        this.downloadBtn = new Button("Download");
        downloadBtn.setStyle("-fx-text-fill: #fff;");
        downloadBtn.setPrefSize(300, 40);
        VBox.setMargin(downloadBtn, new Insets(10, 0, 0, 0));

        fileContainer.getChildren().add(downloadBtn);

        fetchAnswer();
    }

    private ArrayList<File> fetchAnswer() {
        if (this.answerController.checkAnswer(this.task.getId(), LoggedUser.getInstance().getId())) {
            submitStatus.setText("Submitted");

            ArrayList<File> fileList = this.answerController.getMemberAnswer(this.task.getId(), LoggedUser.getInstance().getId());

            if (!sideContent.getChildren().contains(fileContainer)) {
                sideContent.getChildren().add(fileContainer);
            }

            if (!fileList.isEmpty()) {
                downloadBtn.getStyleClass().add("primary-button");
                downloadBtn.setOnMouseClicked(e -> {
                    this.answerController.downloadAllAnswer(fileList);
                });
            }

            return fileList;
        }

        sideContent.getChildren().remove(fileContainer);
        return new ArrayList<>();
    }

    private void actions() {
        this.submitBtn.setOnAction(e -> {

            if (!checkDeadline()) {
                return;
            }

            new UploadFileModal(task.getId(), fileContainer, sideContent, downloadBtn);
            fetchAnswer();
        });

        this.markAsDoneBtn.setOnMouseClicked(e -> {

            if (!checkDeadline()) {
                return;
            }

            ArrayList<File> fileList = fetchAnswer();

            if (submitStatus.getText().equals("Not Submitted")) {
                this.answerController.markAsDone(task.getId(), LoggedUser.getInstance().getId());
                this.submitStatus.setText("Submitted");
                this.sideContent.getChildren().add(fileContainer);
            } else if (submitStatus.getText().equals("Submitted") && fileList.isEmpty()) {
                this.answerController.markUndone(task.getId(), LoggedUser.getInstance().getId());
                this.submitStatus.setText("Not Submitted");
                this.sideContent.getChildren().remove(fileContainer);
            }
        });
    }

    private boolean checkDeadline() {
        String nowString = DateManager.getNow();
        String deadlineString = task.getDeadlineAt();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date now = dateFormat.parse(nowString);
            Date deadline = dateFormat.parse(deadlineString);

            if (now.compareTo(deadline) > 0) {
                ToastBuilder.buildNormal().setText("Deadline has passed\nYou can't submit this task anymore").build();
                return false;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

}
