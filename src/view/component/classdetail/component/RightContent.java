package view.component.classdetail.component;

import controller.ForumController;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import model.Classroom;
import model.LoggedUser;

public class RightContent extends VBox {

    private TextField postInput;
    private ForumController forumController;
    private Classroom classroom;

    private void init() {
        this.forumController = new ForumController();
    }

    private void setLayout() {
        this.getChildren().add(Container("post"));
        this.getChildren().add(Container("display"));
        this.getChildren().add(Container("display"));
        this.getChildren().add(Container("display"));
        this.getChildren().add(Container("display"));
        this.getChildren().add(Container("display"));
        this.getChildren().add(Container("display"));
        this.getChildren().add(Container("display"));
        this.getChildren().add(Container("display"));
    }

    private HBox Container(String type) {
        HBox container = new HBox(5);
        container.getStyleClass().add("border");
        container.setPadding(new Insets(10, 10, 10, 10));

        if(LoggedUser.getInstance() == null || LoggedUser.getInstance().getProfileImage() == null) {
            Image profile = new Image("file:resources/icons/user.png");
            ImageView profileImage = new ImageView(profile);
            profileImage.setFitWidth(30);
            profileImage.setFitHeight(30);

            profileImage.getStyleClass().add("profile");
            container.getChildren().add(profileImage);
        }
        Image profile = LoggedUser.getInstance().getProfileImage();
        ImageView profileImage = new ImageView(profile);
        profileImage.getStyleClass().add("profile");

        if(type.equals("post")) {
            container.getChildren().add(profileImage);
            postInput = new TextField();
            postInput.setPromptText("What's on your mind " + LoggedUser.getInstance().getUsername() + "?");

            container.getChildren().add(postInput);

            postInput.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (event.getCode() == KeyCode.ENTER) {
                        forumController.createForum(postInput.getText(), classroom.getClassId());
                        postInput.clear();
                    }
                }
            });

            HBox.setHgrow(postInput, Priority.ALWAYS);

        } else if(type.equals("display")) {
            container.getChildren().add(profileImage);

            Label label = new Label("Gtw isi postnya pokoknya");
            container.getChildren().add(label);
        }

        container.setAlignment(Pos.CENTER_LEFT);

        return container;
    }

    public RightContent(Classroom classroom) {
        this.classroom = classroom;
        init();
        setLayout();


        this.setSpacing(20);
    }

}
