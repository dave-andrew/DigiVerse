package net.slc.dv.view.classroom.detail.component;

import net.slc.dv.builder.ImageViewBuilder;
import net.slc.dv.resources.Icon;
import net.slc.dv.storage.IconStorage;
import net.slc.dv.helper.ImageManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import net.slc.dv.model.ClassroomMember;
import net.slc.dv.model.User;

public class ClassMemberItem extends HBox {
    private ImageView profile;

    public ClassMemberItem(ClassroomMember classMember, int idx) {
        User user = classMember.getUser();

        if (user.getBlobProfile() != null) {
            profile = ImageViewBuilder.create()
                    .setImage(user.getProfile())
                    .build();
        } else {
            profile = ImageViewBuilder.create()
                    .bindImageProperty(IconStorage.getIcon(Icon.USER))
                    .build();
        }

        VBox userBox = new VBox();
        userBox.setAlignment(Pos.CENTER_LEFT);
        userBox.setPadding(new Insets(0, 0, 0, 10));

        Label index = new Label(idx + ".    ");

        Label userNameLbl = new Label(user.getUsername());

        userBox.getChildren().addAll(userNameLbl);

        this.profile.setFitWidth(40);
        this.profile.setFitHeight(40);

        ImageManager.makeCircular(profile, 20);

        this.getChildren().addAll(index, profile);
        this.getChildren().add(userBox);
        this.setAlignment(Pos.CENTER_LEFT);

    }

}
