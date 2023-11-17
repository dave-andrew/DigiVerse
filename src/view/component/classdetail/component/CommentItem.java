package view.component.classdetail.component;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import model.Comment;
import model.ForumComment;

public class CommentItem extends HBox {

    private Comment comment;

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

        Label commentText = new Label(comment.getText());
        commentText.prefWidthProperty().bind(this.widthProperty().subtract(50));
        commentText.setWrapText(true);

        this.setSpacing(10);
        this.getChildren().addAll(profileImage, commentText);
    }

}
