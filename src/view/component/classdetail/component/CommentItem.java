package view.component.classdetail.component;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Comment;
import model.ForumComment;

public class CommentItem extends HBox {

    private Comment comment;

    private Label replyBtn;
    private HBox commentTextField;

    private boolean toggle = true;

    public CommentItem(Comment comment) {
        this.comment = comment;
        init();
        setLayout();
    }

    private void init() {
    }

    private void setLayout() {
        Image profile = new Image("file:resources/icons/user.png");
        ImageView profileImage = new ImageView(profile);

        profileImage.setFitWidth(30);
        profileImage.setFitHeight(30);

        profileImage.getStyleClass().add("profile");

        VBox textContainer = new VBox(5);
        textContainer.prefWidthProperty().bind(this.widthProperty().subtract(50));

        Label username = new Label(comment.getUser().getUsername());
        username.setStyle("-fx-font-size: 16px");

        Label commentText = new Label(comment.getText());
        commentText.setWrapText(true);

        this.replyBtn = new Label("Reply");
        replyBtn.getStyleClass().add("reply-button");

        textContainer.getChildren().addAll(username, commentText, replyBtn);

        VBox replyInputContainer = new VBox();

        this.replyBtn.setOnMouseClicked(e -> {

            if(toggle) {

                this.commentTextField = new CommentTextField(comment);

                replyInputContainer.getChildren().add(commentTextField);

            } else {

                replyInputContainer.getChildren().clear();

            }
            toggle = !toggle;
        });

        textContainer.getChildren().add(replyInputContainer);

        VBox replyContainer = new VBox();

        textContainer.getChildren().add(replyContainer);

        this.setSpacing(10);
        this.getChildren().addAll(profileImage, textContainer);
    }

    public Label getReply() {
        return replyBtn;
    }

}
