package view.component;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.LoggedUser;

public class ChangeAccountBox extends VBox {

    private LoggedUser loggedUser;

    private VBox userBox;
    private HBox userHbox;
    private Label userInfoLbl, userNameLbl, userEmailLbl;
    private ImageView userImg;
    private Button changeAccountBtn;

    private void initialize() {
        loggedUser = LoggedUser.getInstance();

        this.setSpacing(10);

        userBox = new VBox();
        userHbox = new HBox(10);

        userInfoLbl = new Label("Logged As:");

        Image image = new Image("file:resources/icons/user.png");
        userImg = new ImageView(image);
        userImg.setFitWidth(40);
        userImg.setPreserveRatio(true);

        if(loggedUser != null) {
            userNameLbl = new Label(loggedUser.getUsername());
            userEmailLbl = new Label(loggedUser.getEmail());
        }

        changeAccountBtn = new Button("Change");
        changeAccountBtn.getStyleClass().add("secondary-button");

        userBox.getChildren().addAll(userNameLbl, userEmailLbl);
        userHbox.getChildren().addAll(userImg, userBox, changeAccountBtn);
        userHbox.setAlignment(Pos.TOP_CENTER);
        this.getChildren().addAll(userInfoLbl, userHbox);
    }

    private void actions() {
        changeAccountBtn.setOnAction(e -> {
            System.out.println("Change Account");
        });
    }

    public ChangeAccountBox() {
        initialize();
        actions();
    }

}
