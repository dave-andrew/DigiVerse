package net.slc.dv.view.classroom.detail.component;

import net.slc.dv.builder.ImageViewBuilder;
import net.slc.dv.helper.ImageManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import net.slc.dv.model.ClassroomMember;
import net.slc.dv.resources.Icon;
import net.slc.dv.resources.IconStorage;

public class MemberItem extends HBox {

    public MemberItem(ClassroomMember member, int idx) {
        ImageView profileImg = new ImageView();
        if(member.getUser().getBlobProfile() != null) {
            ImageViewBuilder.modify(profileImg)
                    .setImage(member.getUser().getProfile())
                    .setFitHeight(35)
                    .setFitWidth(35)
                    .build();
        } else {
            ImageViewBuilder.modify(profileImg)
                    .bindImageProperty(IconStorage.getIcon(Icon.USER))
                    .setFitHeight(35)
                    .setFitWidth(35)
                    .build();
        }

        ImageManager.makeCircular(profileImg, 17.5);

        HBox userBox = new HBox();
        userBox.setAlignment(Pos.CENTER_LEFT);
        userBox.setPadding(new Insets(0, 0, 0, 10));

        Label userNameLbl = new Label(member.getUser().getUsername());
        userNameLbl.setStyle("-fx-font-size: 18px;");

        userBox.getChildren().addAll(userNameLbl);

        Label index = new Label(idx + ".  ");

        this.getChildren().addAll(index, profileImg, userBox);
        this.setAlignment(Pos.CENTER_LEFT);
    }

}
