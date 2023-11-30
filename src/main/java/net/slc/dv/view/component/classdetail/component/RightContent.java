package net.slc.dv.view.component.classdetail.component;

import net.slc.dv.controller.CommentController;
import net.slc.dv.controller.ForumController;
import net.slc.dv.helper.ImageManager;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
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
import net.slc.dv.model.*;

import javafx.util.Duration;
import java.util.Collections;
import java.util.List;

public class RightContent extends VBox {

    private final Classroom classroom;
    private TextField postInput;
    private ForumController forumController;
    private CommentController commentController;
    private User user;

    private void init() {
        this.forumController = new ForumController();
        this.commentController = new CommentController();
        this.user = LoggedUser.getInstance();
    }

    private void setLayout() {
        this.getChildren().add(Container("post", "", user));

        List<Forum> forumList = this.forumController.getClassroomForum(classroom.getClassId());

        for (Forum forum : forumList) {

            VBox forumContainer = forumContainer(forum);

            this.getChildren().add(forumContainer);
        }
    }

    public VBox forumContainer(Forum forum) {
        VBox forumContainer = new VBox();

        HBox post = Container("display", forum.getText(), forum.getUser());

        forumContainer.getChildren().add(post);

        VBox line = new VBox();
        line.getChildren().add(post);
        line.getStyleClass().add("bottom-border");

        VBox.setMargin(line, new Insets(10, 0, 10, 0));

        forumContainer.getChildren().add(line);

        VBox commentContainer = new VBox();

        forumContainer.getChildren().add(commentContainer);

        HBox commentInput = commentInput(forum, commentContainer);
        commentInput.getStyleClass().add("bottom-border");

        forumContainer.getChildren().add(commentInput);

        HBox dropDownComment = new HBox();

        Image arrowDown = new Image("file:resources/icons/down-arrow.png");
        ImageView arrowDownImage = new ImageView(arrowDown);

        arrowDownImage.setFitWidth(8);
        arrowDownImage.setFitHeight(8);

        Button dropDownBtn = new Button();
        dropDownBtn.setGraphic(arrowDownImage);
        dropDownBtn.setStyle("-fx-background-color: transparent;-fx-border-color: none; -fx-cursor: hand;");
        dropDownBtn.setPadding(new Insets(0));

        dropDownBtn.prefWidthProperty().bind(forumContainer.widthProperty().subtract(75));

        Button loadMoreComment = new Button("Load more");
        loadMoreComment.setStyle("-fx-background-color: transparent;-fx-border-color: none; -fx-cursor: hand;");

        loadMoreComment.setOnMouseClicked(e -> {
            fetchForumComment(commentContainer, forum);
        });

        dropDownBtn.setOnMouseClicked(e -> {
            if(forum.isToggle()) {
                fetchForumComment(commentContainer, forum);

                forum.setToggle(false);
                commentContainer.getChildren().add(loadMoreComment);

                KeyFrame start = new KeyFrame(Duration.ZERO,
                        new KeyValue(dropDownBtn.rotateProperty(), 0)
                );

                KeyFrame end = new KeyFrame(Duration.seconds(0.5),
                        new KeyValue(dropDownBtn.rotateProperty(), 180)
                );

                Timeline timeline = new Timeline(start, end);
                timeline.play();
            } else {
                commentContainer.getChildren().clear();
                forum.setCommentCounter(0);
                forum.setToggle(true);

                KeyFrame start = new KeyFrame(Duration.ZERO,
                        new KeyValue(dropDownBtn.rotateProperty(), 180)
                );

                KeyFrame end = new KeyFrame(Duration.seconds(0.5),
                        new KeyValue(dropDownBtn.rotateProperty(), 0)
                );

                Timeline timeline = new Timeline(start, end);
                timeline.play();
            }
        });

        dropDownComment.getChildren().add(dropDownBtn);

        forumContainer.getChildren().add(dropDownComment);

        forumContainer.getStyleClass().add("border");
        forumContainer.setPadding(new Insets(15));

        return forumContainer;
    }

    private void fetchForumComment(VBox commentContainer, Forum forum) {
        List<ForumComment> forumCommentList = this.commentController.getForumComments(forum.getId(), forum.getCommentCounter());

        VBox commentFetched = new VBox();

        for (ForumComment forumComment : forumCommentList) {
            HBox commentItem = new CommentItem(forumComment);
            commentFetched.getChildren().add(0, commentItem);
        }

        KeyValue keyValue = new KeyValue(commentFetched.opacityProperty(), 0, Interpolator.EASE_OUT);

        KeyFrame start = new KeyFrame(Duration.ZERO, keyValue);

        KeyFrame end = new KeyFrame(Duration.seconds(0.5),
                new KeyValue(commentFetched.opacityProperty(), 1)
        );


        Timeline timeline = new Timeline(start, end);

        timeline.play();

        commentContainer.getChildren().add(0, commentFetched);

        forum.setCommentCounter(forum.getCommentCounter() + 1);
    }

    public HBox commentInput(Forum forum, VBox commentContainer) {
        HBox commentSection = new HBox();
        commentSection.setPadding(new Insets(10, 10, 10, 10));
        commentSection.setSpacing(10);

        Image profile = new Image("file:resources/icons/user.png");
        ImageView profileImage = new ImageView(profile);

        if(user.getProfile() != null) {
            profileImage = new ImageView(LoggedUser.getInstance().getProfileImage());
        }

        profileImage.setFitWidth(30);
        profileImage.setFitHeight(30);
        profileImage.getStyleClass().add("profile");

        ImageManager.makeCircular(profileImage, 15);

        TextField commentInput = new TextField();
        commentInput.setPromptText("Write a comment...");
        commentInput.setStyle("-fx-font-size: 14px");
        commentInput.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    ForumComment forumComment = commentController.createForumComment(commentInput.getText(), forum.getId(), LoggedUser.getInstance().getId());

                    HBox commentItem = new CommentItem(forumComment);

                    if(commentContainer.getChildren().isEmpty()) {
                        commentContainer.getChildren().add(commentItem);
                    } else {
                        commentContainer.getChildren().add(commentContainer.getChildren().size()-1, commentItem);
                    }
                    commentInput.clear();
                }
            }
        });

        HBox.setHgrow(commentInput, Priority.ALWAYS);

        commentSection.getChildren().addAll(profileImage, commentInput);
        commentSection.setAlignment(Pos.TOP_CENTER);

        return commentSection;
    }

    private HBox Container(String type, String text, User user) {
        HBox container = new HBox(5);

        Image profile = new Image("file:resources/icons/user.png");
        ImageView profileImage = new ImageView(profile);

        if(user.getProfile() != null) {
            profile = user.getProfile();
            profileImage = new ImageView(profile);
        }

        profileImage.setFitWidth(30);
        profileImage.setFitHeight(30);
        profileImage.getStyleClass().add("profile");

        if(type.equals("post")) {
            ImageView loggedUserProfile = new ImageView(LoggedUser.getInstance().getProfileImage());
            loggedUserProfile.setFitWidth(30);
            loggedUserProfile.setFitHeight(30);
            loggedUserProfile.getStyleClass().add("profile");

            ImageManager.makeCircular(loggedUserProfile, 15);

            container.getChildren().add(loggedUserProfile);

            postInput = new TextField();
            postInput.setPromptText("What's on your mind, " + LoggedUser.getInstance().getUsername() + "?");

            container.getChildren().add(postInput);

            postInput.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    Forum forum = forumController.createForum(postInput.getText(), classroom.getClassId());
                    getChildren().add(1, Container("display", forum.getText(), user));
                    postInput.clear();
                }
            });

            HBox.setHgrow(postInput, Priority.ALWAYS);
            container.getStyleClass().add("border");
            container.setPadding(new Insets(15));

        } else if(type.equals("display")) {
            container.getChildren().add(profileImage);

            VBox userContainer = new VBox();
            Label userName = new Label(user.getUsername());
            userName.setStyle("-fx-font-size: 16px");

            Label label = new Label(text);
            label.setWrapText(true);
            label.setStyle("-fx-font-family: 'Nunito Regular'; -fx-font-size: 14px");

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
