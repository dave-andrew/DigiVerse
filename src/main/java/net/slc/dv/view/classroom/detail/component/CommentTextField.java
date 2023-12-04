package net.slc.dv.view.classroom.detail.component;

import net.slc.dv.helper.ImageManager;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import net.slc.dv.model.Comment;
import net.slc.dv.model.LoggedUser;

public class CommentTextField extends HBox {

    private final TextField replyField;

    public CommentTextField(Comment comment) {
        ImageView profileImg;

        if (LoggedUser.getInstance().getProfile() == null) {
            profileImg = new ImageView(new Image("file:resources/icons/user.png"));
        } else {
            profileImg = new ImageView(LoggedUser.getInstance().getProfile());
        }

        profileImg.setFitWidth(30);
        profileImg.setFitHeight(30);

        ImageManager.makeCircular(profileImg, 15);

        this.replyField = new TextField();
        replyField.prefWidthProperty().bind(this.widthProperty().subtract(50));

        replyField.setPromptText("Reply to " + comment.getUser().getUsername() + "...");

        this.getChildren().addAll(profileImg, replyField);
        this.setSpacing(10);
    }

    public TextField getReplyField() {
        return replyField;
    }

}
