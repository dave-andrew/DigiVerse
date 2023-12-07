package net.slc.dv.view.classroom.detail.component;

import net.slc.dv.builder.ImageViewBuilder;
import net.slc.dv.builder.ButtonBuilder;
import net.slc.dv.builder.ImageViewBuilder;
import net.slc.dv.builder.VBoxBuilder;
import net.slc.dv.enums.Theme;
import net.slc.dv.resources.Icon;
import net.slc.dv.resources.IconStorage;
import net.slc.dv.controller.CommentController;
import net.slc.dv.controller.ForumController;
import net.slc.dv.helper.ImageManager;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import net.slc.dv.model.*;

import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

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
            forumContainer.setPadding(new Insets(15, 30, 15, 40));
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

		ImageView arrowDownImage = ImageViewBuilder.create()
				.bindImageProperty(IconStorage.getIcon(Icon.DOWN_ARROW))
				.setFitWidth(15)
				.setFitHeight(15)
				.build();


        Button dropDownBtn = new Button();
        dropDownBtn.setGraphic(arrowDownImage);
        dropDownBtn.setStyle("-fx-background-color: transparent;-fx-border-color: none; -fx-cursor: hand;");
        dropDownBtn.setPadding(new Insets(0));

        dropDownBtn.prefWidthProperty().bind(forumContainer.widthProperty().subtract(75));

        Button loadMoreComment = new Button("Load more");
        loadMoreComment.setStyle("-fx-background-color: transparent;-fx-border-color: none; -fx-cursor: hand;-fx-padding: 0;");

        loadMoreComment.setOnMouseClicked(e -> {
            fetchForumComment(commentContainer, forum);
        });

        dropDownBtn.setOnMouseClicked(e -> {
            if (forum.isToggle()) {
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
        List<ForumComment> forumCommentList = this.commentController.getForumComments(
                forum.getId(),
                forum.getCommentCounter()
        );

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

		ImageView profileImage = ImageViewBuilder.create()
				.bindImageProperty(IconStorage.getIcon(Icon.USER))
				.setFitWidth(30)
				.setFitHeight(30)
				.build();

        if (user.getProfile() != null) {
            profileImage = new ImageView(LoggedUser.getInstance().getProfileImage());
        }

        profileImage.setFitWidth(30);
        profileImage.setFitHeight(30);
        profileImage.getStyleClass().add("profile");

        ImageManager.makeCircular(profileImage, 15);

        TextField commentInput = getTextField(forum, commentContainer);

        HBox.setHgrow(commentInput, Priority.ALWAYS);

        Button commentButton = ButtonBuilder.create()
                .setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-cursor: hand;")
                .setPadding(0, 0, 0, 0)
                .setGraphic(ImageViewBuilder.create()
                        .setImage(new Image(Icon.SEND.getPath(Theme.LIGHT)))
                        .setFitWidth(20)
                        .setFitHeight(20)
                        .build())
                .setMinWidth(40)
                .build();

        VBox btnContainer = VBoxBuilder.create()
                .addChildren(commentButton)
                .setAlignment(Pos.CENTER_RIGHT)
                .build();

        commentButton.setOnMouseClicked(event -> {
            uploadComment(forum, commentContainer, commentInput);
        });

        commentSection.getChildren().addAll(profileImage, commentInput, btnContainer);
        commentSection.setAlignment(Pos.TOP_CENTER);

        return commentSection;
    }

    @NotNull
    private TextField getTextField(Forum forum, VBox commentContainer) {
        TextField commentInput = new TextField();
        commentInput.setPromptText("Write a comment...");
        commentInput.setStyle("-fx-font-size: 14px");
        commentInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                uploadComment(forum, commentContainer, commentInput);
            }
        });
        return commentInput;
    }

    private void uploadComment(Forum forum, VBox commentContainer, TextField commentInput) {
        ForumComment forumComment = commentController.createForumComment(commentInput.getText(),
                forum.getId(), LoggedUser.getInstance().getId());

        HBox commentItem = new CommentItem(forumComment);

        if (commentContainer.getChildren().isEmpty()) {
            commentContainer.getChildren().add(commentItem);
        } else {
            commentContainer.getChildren().add(commentContainer.getChildren().size() - 1, commentItem);
        }
        commentInput.clear();
    }

    private HBox Container(String type, String text, User user) {
        HBox container = new HBox(5);

		ImageView profileImage = ImageViewBuilder.create()
				.bindImageProperty(IconStorage.getIcon(Icon.USER))
				.setFitWidth(30)
				.setFitHeight(30)
				.build();

		if (user.getProfile() != null) {
			profileImage = new ImageView(user.getProfile());
            ImageManager.makeCircular(profileImage, 15);
		}

        profileImage.setFitWidth(30);
        profileImage.setFitHeight(30);
        profileImage.getStyleClass().add("profile");

        if (type.equals("post")) {
            LoggedUser loggedUser = LoggedUser.getInstance();

            ImageView loggedUserProfile;
            if(loggedUser.getProfile() != null){
                loggedUserProfile = new ImageView(loggedUser.getProfileImage());
            } else {
                loggedUserProfile = ImageViewBuilder.create()
                        .bindImageProperty(IconStorage.getIcon(Icon.USER))
                        .build();
            }


            loggedUserProfile.setFitWidth(30);
            loggedUserProfile.setFitHeight(30);
            loggedUserProfile.getStyleClass().add("profile");

            ImageManager.makeCircular(loggedUserProfile, 15);

            container.getChildren().add(loggedUserProfile);

            postInput = new TextField();
            postInput.setPromptText("What's on your mind, " + LoggedUser.getInstance().getUsername() + "?");

            Button postBtn = ButtonBuilder.create()
                    .setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-cursor: hand;")
                    .setPadding(0, 0, 0, 0)
                    .setGraphic(ImageViewBuilder.create()
                            .setImage(new Image(Icon.SEND.getPath(Theme.LIGHT)))
                            .setFitWidth(20)
                            .setFitHeight(20)
                            .build())
                    .setMinWidth(40)
                    .build();

            VBox btnContainer = VBoxBuilder.create()
                    .addChildren(postBtn)
                    .setAlignment(Pos.CENTER_RIGHT)
                    .build();

            container.getChildren().addAll(postInput, btnContainer);

            postInput.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    Forum forum = forumController.createForum(postInput.getText(), classroom.getClassId());
                    System.out.println(forum.getUser().getUsername());
                    VBox forumContainer = forumContainer(forum);
                    forumContainer.setPadding(new Insets(15, 30, 15, 40));
                    this.getChildren().add(1, forumContainer);
                    postInput.clear();
                }
            });

            postBtn.setOnMouseClicked(event -> {
                Forum forum = forumController.createForum(postInput.getText(), classroom.getClassId());
                VBox forumContainer = forumContainer(forum);
                forumContainer.setPadding(new Insets(15, 30, 15, 40));
                this.getChildren().add(1, forumContainer);
                postInput.clear();
            });

            HBox.setHgrow(postInput, Priority.ALWAYS);
            container.getStyleClass().add("border");
            container.setAlignment(Pos.BOTTOM_CENTER);
            container.setPadding(new Insets(15, 30, 15, 40));
        } else if (type.equals("display")) {
            container.getChildren().add(profileImage);

            VBox userContainer = new VBox();
            Label userName = new Label(user.getUsername());
            userName.setStyle("-fx-font-size: 16px");
            userName.setPadding(new Insets(0, 0, 7, 0));

            Label label = new Label("  " + text);
            label.setWrapText(true);
            label.setStyle("-fx-font-family: 'Nunito Regular'; -fx-font-size: 14px");
            label.setPadding(new Insets(5, 0, 5, 10));
            label.prefWidthProperty().bind(this.widthProperty().subtract(115));
            label.getStyleClass().add("reply-bg");

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
