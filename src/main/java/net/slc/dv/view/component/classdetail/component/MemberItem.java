package net.slc.dv.view.component.classdetail.component;

import net.slc.dv.helper.ImageManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import net.slc.dv.model.ClassroomMember;

public class MemberItem extends HBox {

    public MemberItem(ClassroomMember member, int idx) {
        Image profileImg;
        if(member.getUser().getBlobProfile() != null) {
            profileImg = member.getUser().getProfile();
        } else {
            profileImg = new Image("file:resources/icons/user.png");
        }

        ImageView profile = new ImageView(profileImg);
        profile.setFitWidth(35);
        profile.setFitHeight(35);

        ImageManager.makeCircular(profile, 17.5);

        HBox userBox = new HBox();
        userBox.setAlignment(Pos.CENTER_LEFT);
        userBox.setPadding(new Insets(0, 0, 0, 10));

        Label userNameLbl = new Label(member.getUser().getUsername());
        userNameLbl.setStyle("-fx-font-size: 18px;");

        userBox.getChildren().addAll(userNameLbl);

        Label index = new Label(idx + ".  ");

        this.getChildren().addAll(index, profile, userBox);
        this.setAlignment(Pos.CENTER_LEFT);
    }

}
