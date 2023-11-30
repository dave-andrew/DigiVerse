package net.slc.dv.view.component.classdetail.component;

import net.slc.dv.controller.CommentController;
import net.slc.dv.helper.ImageManager;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import net.slc.dv.model.Comment;
import net.slc.dv.model.ForumComment;
import net.slc.dv.model.TaskComment;

import java.util.ArrayList;

public class CommentItem extends HBox {

    private final Comment comment;

    private final CommentController commentController;

    private Label replyBtn;
    private CommentTextField commentTextField;
    private VBox replyInputContainer, replyContainer;

    private boolean toggle = true;
    private boolean toggleReply = true;

    private Label loadReplyBtn;

    public CommentItem(Comment comment) {
        this.comment = comment;
        this.commentController = new CommentController();
        init();
        setLayout();
    }

    private void init() {
    }

    private void setLayout() {
        Image profile = new Image("file:resources/icons/user.png");
        ImageView profileImage = new ImageView(profile);

        if(comment.getUser().getProfile() != null) {
            profileImage = new ImageView(comment.getUser().getProfile());
        }

        profileImage.setFitWidth(30);
        profileImage.setFitHeight(30);

        ImageManager.makeCircular(profileImage, 15);

        profileImage.getStyleClass().add("profile");

        VBox textContainer = new VBox(5);
        textContainer.prefWidthProperty().bind(this.widthProperty().subtract(50));

        Label username = new Label(comment.getUser().getUsername());
        username.setStyle("-fx-font-size: 16px");

        Label commentText = new Label(comment.getText());
        commentText.setWrapText(true);

        commentText.setStyle("-fx-font-family: 'Nunito Regular'; -fx-font-size: 14px");

        HBox btnContainer = new HBox(10);

        this.replyBtn = new Label("Reply");
        replyBtn.getStyleClass().add("reply-button");
        replyBtn.setOpacity(0.5);

        this.loadReplyBtn = new Label("View Reply");
        loadReplyBtn.getStyleClass().add("reply-button");
        loadReplyBtn.setOpacity(0.5);

        btnContainer.getChildren().addAll(replyBtn, loadReplyBtn);

        VBox replyContentContainer = new VBox();
        replyContentContainer.setPadding(new Insets(5, 0, 5, 10));

        replyContentContainer.getChildren().addAll(commentText);
        replyContentContainer.getStyleClass().add("reply-bg");

        textContainer.getChildren().addAll(username, replyContentContainer, btnContainer);

        this.replyInputContainer = new VBox();

        textContainer.getChildren().add(replyInputContainer);

        this.replyContainer = new VBox();

        textContainer.getChildren().add(replyContainer);

        this.setSpacing(10);
        this.getChildren().addAll(profileImage, textContainer);

        actions();
    }

    private void actions() {
        this.replyBtn.setOnMouseClicked(e -> {

            if(toggle) {

                this.commentTextField = new CommentTextField(comment);

                commentTextField.setOnKeyPressed(e2 -> {
                    if(e2.getCode().toString().equals("ENTER")) {
                        TaskComment replyComment = commentController.replyComment(commentTextField.getReplyField().getText(), comment.getId());

                        replyContainer.getChildren().add(new CommentItem(replyComment));

                        commentTextField.getReplyField().clear();
                    }
                });

                replyInputContainer.getChildren().add(commentTextField);

            } else {

                replyInputContainer.getChildren().clear();

            }
            toggle = !toggle;
        });

        this.loadReplyBtn.setOnMouseClicked(e -> {

            if(toggleReply) {
                fetchReply();
                if(!replyContainer.getChildren().isEmpty()) {
                    replyContainer.getStyleClass().add("left-border");
                }
            } else {
                replyContainer.getChildren().clear();
                replyContainer.getStyleClass().remove("left-border");
            }
            toggleReply = !toggleReply;
        });
    }

    public void fetchReply() {
        ArrayList<TaskComment> replyList = commentController.getReplyTaskComment(comment.getId());

        for (TaskComment taskComment : replyList) {
            replyContainer.getChildren().add(new CommentItem(taskComment));
        }
    }

}
