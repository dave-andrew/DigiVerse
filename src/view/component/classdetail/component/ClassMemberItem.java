package view.component.classdetail.component;

import helper.ImageManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.ClassroomMember;
import model.User;

public class ClassMemberItem extends HBox {

    private Image profileImg;
    private ImageView profile;
    public ClassMemberItem(ClassroomMember classMember) {
        User user = classMember.getUser();

        if(user.getBlobProfile() != null) {
            profileImg = ImageManager.convertBlobImage(user.getBlobProfile());
        } else {
            profileImg = new Image("file:resources/icons/user.png");
        }

        VBox userBox = new VBox();
        userBox.setAlignment(Pos.CENTER_LEFT);
        userBox.setPadding(new Insets(0, 0, 0, 10));

        Label userNameLbl = new Label(user.getUsername());

        userBox.getChildren().addAll(userNameLbl);

        this.profile = new ImageView(profileImg);
        this.profile.setFitWidth(40);
        this.profile.setFitHeight(40);

        ImageManager.makeCircular(profile, 20);

        this.getChildren().add(profile);
        this.getChildren().add(userBox);

    }

}
