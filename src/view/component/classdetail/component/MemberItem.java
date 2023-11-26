package view.component.classdetail.component;

import helper.ImageManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import model.ClassroomMember;

public class MemberItem extends HBox {

    private Image profileImg;
    private ImageView profile;

    public MemberItem(ClassroomMember member, int idx) {
        if(member.getUser().getBlobProfile() != null) {
            profileImg = member.getUser().getProfile();
        } else {
            profileImg = new Image("file:resources/icons/user.png");
        }

        this.profile = new ImageView(profileImg);
        this.profile.setFitWidth(35);
        this.profile.setFitHeight(35);

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
