package view.component.classdetail.component;

import helper.ImageManager;
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
            profileImg = new Image("file:resources/images/profile.png");
        }

        VBox userBox = new VBox();

        Label userNameLbl = new Label(user.getUsername());
        Label userEmailLbl = new Label(user.getEmail());

        userBox.getChildren().addAll(userNameLbl, userEmailLbl);

        this.profile = new ImageView(profileImg);
        this.getChildren().add(profile);
        this.getChildren().add(userBox);

    }

}
