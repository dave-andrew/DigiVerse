package view.component.classdetail.component;

import controller.CommentController;
import controller.ForumController;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import model.*;

import java.util.Collections;
import java.util.List;

public class RightContent extends VBox {

    private TextField postInput;
    private ForumController forumController;
    private CommentController commentController;
    private Classroom classroom;

    private void init() {
        this.forumController = new ForumController();
        this.commentController = new CommentController();
    }

    private void setLayout() {
        this.getChildren().add(Container("post", "", LoggedUser.getInstance()));

        List<Forum> forumList = this.forumController.getClassroomForum(classroom.getClassId());

        for (Forum forum : forumList) {
            VBox forumContainer = forumContainer(forum);

            this.getChildren().add(forumContainer);
        }
    }

    public VBox forumContainer(Forum forum) {
        VBox forumContainer = new VBox();
        forumContainer.getChildren().add(Container("display", forum.getText(), forum.getUser()));

        Line line = new Line();
        line.setStroke(Color.valueOf("#E0E0E0"));
        line.endXProperty().bind(this.widthProperty().subtract(75));

        VBox.setMargin(line, new Insets(10, 0, 10, 0));

        forumContainer.getChildren().add(line);

        VBox commentContainer = new VBox();
        commentContainer.setPadding(new Insets(10, 10, 10, 10));

        forumContainer.getChildren().add(commentContainer);

        forumContainer.getChildren().add(commentSection(forum, commentContainer));

        Line line2 = new Line();
        line2.setStroke(Color.valueOf("#E0E0E0"));
        line2.endXProperty().bind(this.widthProperty().subtract(75));

        forumContainer.getChildren().add(line2);

        HBox dropDownComment = new HBox();
        dropDownComment.setPadding(new Insets(10, 10, 10, 10));

        Image arrowDown = new Image("file:resources/icons/down-arrow.png");
        ImageView arrowDownImage = new ImageView(arrowDown);

        arrowDownImage.setFitWidth(20);
        arrowDownImage.setFitHeight(20);

        Button dropDownBtn = new Button();
        dropDownBtn.setGraphic(arrowDownImage);
        dropDownBtn.setStyle("-fx-background-color: transparent;-fx-border-color: none");

        dropDownBtn.setOnMouseClicked(e -> {

        });

        forumContainer.getChildren().add(dropDownComment);

        forumContainer.getStyleClass().add("border");
        forumContainer.setPadding(new Insets(10, 10, 10, 10));


        return forumContainer;
    }

    public HBox commentSection(Forum forum, VBox commentContainer) {
        HBox commentSection = new HBox();
        commentSection.setPadding(new Insets(10, 10, 10, 10));
        commentSection.setAlignment(Pos.CENTER_LEFT);
        commentSection.setSpacing(10);

        Image profile = new Image("file:resources/icons/user.png");
        ImageView profileImage = new ImageView(profile);
        profileImage.setFitWidth(30);
        profileImage.setFitHeight(30);
        profileImage.getStyleClass().add("profile");

        TextField commentInput = new TextField();
        commentInput.setPromptText("Write a comment...");
        commentInput.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    ForumComment forumComment = commentController.createForumComment(commentInput.getText(), forum.getId(), LoggedUser.getInstance().getId());

                    HBox commentItem = new CommentItem(forumComment);

                    commentContainer.getChildren().add(commentItem);
                }
            }
        });

        HBox.setHgrow(commentInput, Priority.ALWAYS);

        commentSection.getChildren().addAll(profileImage, commentInput);

        return commentSection;
    }

    private HBox Container(String type, String text, User user) {
        HBox container = new HBox(5);

        if(LoggedUser.getInstance() == null || LoggedUser.getInstance().getProfileImage() == null) {
            Image profile = new Image("file:resources/icons/user.png");
            ImageView profileImage = new ImageView(profile);
            profileImage.setFitWidth(30);
            profileImage.setFitHeight(30);

            profileImage.getStyleClass().add("profile");
            container.getChildren().add(profileImage);
        }
        Image profile = user.getProfile();
        ImageView profileImage = new ImageView(profile);
        profileImage.getStyleClass().add("profile");

        if(type.equals("post")) {
            container.getChildren().add(profileImage);
            postInput = new TextField();
            postInput.setPromptText("What's on your mind " + LoggedUser.getInstance().getUsername() + "?");

            container.getChildren().add(postInput);

            postInput.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (event.getCode() == KeyCode.ENTER) {
                        Forum forum = forumController.createForum(postInput.getText(), classroom.getClassId());
                        getChildren().add(1, Container("display", forum.getText(), user));
                        postInput.clear();
                    }
                }
            });

            HBox.setHgrow(postInput, Priority.ALWAYS);
            container.getStyleClass().add("border");
            container.setPadding(new Insets(10, 10, 10, 10));

        } else if(type.equals("display")) {
            container.getChildren().add(profileImage);

            VBox userContainer = new VBox();
            Label userName = new Label(user.getUsername());
            userName.setStyle("-fx-font-size: 12px");

            Label label = new Label(text);

            userContainer.getChildren().addAll(userName, label);

            container.getChildren().add(userContainer);
        }

        container.setAlignment(Pos.TOP_LEFT);

        return container;
    }

    public RightContent(Classroom classroom) {
        this.classroom = classroom;
        init();
        setLayout();


        this.setSpacing(20);
    }

}
