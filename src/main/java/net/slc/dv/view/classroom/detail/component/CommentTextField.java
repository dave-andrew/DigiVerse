package net.slc.dv.view.classroom.detail.component;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import lombok.Getter;
import net.slc.dv.builder.ButtonBuilder;
import net.slc.dv.builder.ImageViewBuilder;
import net.slc.dv.builder.VBoxBuilder;
import net.slc.dv.enums.Theme;
import net.slc.dv.builder.ImageViewBuilder;
import net.slc.dv.resources.Icon;
import net.slc.dv.resources.IconStorage;
import net.slc.dv.helper.ImageManager;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import net.slc.dv.model.Comment;
import net.slc.dv.model.LoggedUser;

@Getter
public class CommentTextField extends HBox {

    private final TextField replyField;
    private final Button postBtn;

    public CommentTextField(Comment comment) {
        ImageView profileImg;

        if (LoggedUser.getInstance().getProfile() == null) {
            profileImg = ImageViewBuilder.create()
                    .bindImageProperty(IconStorage.getIcon(Icon.USER))
                    .setFitWidth(30)
                    .setFitHeight(30)
                    .build();
        } else {
            profileImg = new ImageView(LoggedUser.getInstance().getProfile());
        }

        profileImg.setFitWidth(30);
        profileImg.setFitHeight(30);

        ImageManager.makeCircular(profileImg, 15);

        this.replyField = new TextField();
        replyField.prefWidthProperty().bind(this.widthProperty().subtract(50));

        replyField.setPromptText("Reply to " + comment.getUser().getUsername() + "...");

        this.postBtn = ButtonBuilder.create()
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
                .setAlignment(Pos.CENTER)
                .build();

        this.getChildren().addAll(profileImg, replyField, btnContainer);
        this.setSpacing(10);
    }


}
