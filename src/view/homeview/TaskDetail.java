package view.homeview;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import model.Task;

public class TaskDetail extends HBox {

    private VBox mainContent, sideContent;
    private HBox innerMainContent;
    private Task task;

    public TaskDetail(Task task) {
        this.task = task;
        init();
        setLayout();
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

        Label postedBy = new Label("Posted by :  " + task.getUser().getUsername() + " • " + task.getCreatedAt());

        VBox detail = new VBox();
        detail.getChildren().addAll(taskName, postedBy);

        Label score;
        if (task.isScored()) {
            score = new Label("Score: 100");
        } else {
            score = new Label("Score: -");
        }
        detail.getChildren().add(score);

        Label taskDesc = new Label(task.getDescription());
        detail.getChildren().add(taskDesc);

        innerMainContent.getChildren().addAll(imgStack, detail);
        innerMainContent.setAlignment(Pos.TOP_CENTER);

        mainContent.getChildren().add(innerMainContent);

        this.getChildren().addAll(mainContent, sideContent);
    }
}
