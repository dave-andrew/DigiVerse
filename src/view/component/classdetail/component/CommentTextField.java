package view.component.classdetail.component;

import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import model.Comment;

public class CommentTextField extends HBox {

    public CommentTextField(Comment comment) {
        ImageView profileImg;

        if (comment.getUser().getProfile() == null) {
            profileImg = new ImageView(new Image("file:resources/icons/user.png"));
        } else {
            profileImg = new ImageView(comment.getUser().getProfile());
        }

        profileImg.setFitWidth(30);
        profileImg.setFitHeight(30);

        TextField replyField = new TextField();
        replyField.prefWidthProperty().bind(this.widthProperty().subtract(50));

        replyField.setPromptText("Reply to " + comment.getUser().getUsername() + "...");

        this.getChildren().addAll(profileImg, replyField);
        this.setSpacing(10);
    }

}
