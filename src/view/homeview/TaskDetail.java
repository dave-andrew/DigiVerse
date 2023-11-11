package view.homeview;

import helper.StageManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import model.Task;
import view.component.classtask.UploadFileModal;

public class TaskDetail extends HBox {

    private VBox mainContent, sideContent;
    private HBox innerMainContent;
    private Task task;

    private Button submitBtn;

    public TaskDetail(Task task) {
        this.task = task;
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

        Label postedBy = new Label("Posted by: " + task.getUser().getUsername() + " • " + task.getCreatedAt());
        VBox.setMargin(postedBy, new Insets(20, 0, 0, 0));

        VBox detail = new VBox();
        detail.getChildren().addAll(taskName, postedBy);

        Label score;
        if (task.isScored()) {
            score = new Label("Score: 100");
        } else {
            score = new Label("Score: -");
        }

        Label deadline = new Label("Deadline: " + task.getDeadlineAt());

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

        Line line = new Line();
        line.setStroke(Color.valueOf("#E0E0E0"));
        line.endXProperty().bind(detail.widthProperty());
        line.endXProperty().bind(innerMainContent.widthProperty().subtract(50));

        VBox.setMargin(line, new Insets(20, 0, 20, 0));

        detail.getChildren().add(line);

        Label taskDesc = new Label(task.getDescription());
        detail.getChildren().add(taskDesc);

        Line line2 = new Line();
        line2.setStroke(Color.valueOf("#E0E0E0"));
        line2.endXProperty().bind(detail.widthProperty());
        line2.endXProperty().bind(innerMainContent.widthProperty().subtract(50));

        VBox.setMargin(line2, new Insets(20, 0, 20, 0));
        detail.getChildren().add(line2);

        innerMainContent.getChildren().addAll(imgStack, detail);
        innerMainContent.setAlignment(Pos.TOP_LEFT);
//        innerMainContent.setStyle("-fx-background-color: #F5f5f5");

        scoreDeadlineBox.prefWidthProperty().bind(innerMainContent.widthProperty());

        mainContent.getChildren().add(innerMainContent);
        mainContent.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(mainContent, Priority.ALWAYS);

        HBox.setMargin(sideContent, new Insets(0, 0, 0, 50));

        this.getChildren().addAll(mainContent, sideContent);
        this.prefWidthProperty().bind(this.widthProperty());
    }

    private void setSideContent() {
        VBox submitContainer = new VBox();
        VBox.setVgrow(submitContainer, Priority.NEVER);

        Label submitTitle = new Label("Submit Task");
        submitTitle.getStyleClass().add("title");

        Label submitStatus = new Label("Not Submitted");

        HBox submitStatusContainer = new HBox();
        submitStatusContainer.setAlignment(Pos.CENTER_LEFT);

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        submitStatusContainer.getChildren().addAll(submitTitle, spacer, submitStatus);

        this.submitBtn = new Button("+ Upload File");
        submitBtn.getStyleClass().add("primary-button");
        submitBtn.setPrefSize(300, 40);
        VBox.setMargin(submitBtn, new Insets(30, 0, 0, 0));

        Button markAsDoneBtn = new Button("Mark as Done");
        markAsDoneBtn.getStyleClass().add("secondary-button");
        markAsDoneBtn.setPrefSize(300, 40);
        VBox.setMargin(markAsDoneBtn, new Insets(10, 0, 0, 0));

        submitContainer.getChildren().addAll(submitStatusContainer, submitBtn, markAsDoneBtn);

        VBox spacerVert = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        sideContent.getChildren().addAll(submitContainer, spacerVert);
        sideContent.setAlignment(Pos.TOP_CENTER);

        submitContainer.getStyleClass().add("card");
    }

    private void actions() {
        this.submitBtn.setOnAction(e -> {
            new UploadFileModal(task.getId());
        });
    }
}
