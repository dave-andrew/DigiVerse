package net.slc.dv.view.classroom.detail.component;

import net.slc.dv.resources.Icon;
import net.slc.dv.resources.IconStorage;
import net.slc.dv.helper.ImageManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import net.slc.dv.model.ClassroomMember;
import net.slc.dv.model.User;

public class ClassMemberItem extends HBox {

    private final Image profileImg;
    private final ImageView profile;

    public ClassMemberItem(ClassroomMember classMember, int idx) {
        User user = classMember.getUser();

        if (user.getBlobProfile() != null) {
            System.out.println("Ada profile");
            profileImg = user.getProfile();
        } else {
            profileImg = IconStorage.getIcon(Icon.USER);
        }

        VBox userBox = new VBox();
        userBox.setAlignment(Pos.CENTER_LEFT);
        userBox.setPadding(new Insets(0, 0, 0, 10));

        Label index = new Label(idx + ".    ");

        Label userNameLbl = new Label(user.getUsername());

        userBox.getChildren().addAll(userNameLbl);

        this.profile = new ImageView(profileImg);
        this.profile.setFitWidth(40);
        this.profile.setFitHeight(40);

        ImageManager.makeCircular(profile, 20);

        this.getChildren().addAll(index, profile);
        this.getChildren().add(userBox);
        this.setAlignment(Pos.CENTER_LEFT);

    }

}
