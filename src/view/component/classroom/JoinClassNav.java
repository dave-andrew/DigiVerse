package view.component.classroom;

import controller.ClassController;
import helper.StageManager;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import view.Home;

public class JoinClassNav extends HBox {

    private ClassController classController;

    private HBox leftNav;
    private Image closeImg;
    private ImageView close;
    private Label title;
    private Button joinBtn;
    private Button closeBtn;

    private void initialize() {
        classController = new ClassController();

        leftNav = new HBox(20);

        closeImg = new Image("file:resources/icons/close.png");
        close = new ImageView(closeImg);
        close.setFitWidth(20);
        close.setPreserveRatio(true);

        closeBtn = new Button();
        closeBtn.setGraphic(close);
        closeBtn.getStyleClass().add("image-button");

        title = new Label("Join Class");
        title.getStyleClass().add("title");

        joinBtn = new Button("Join");
        joinBtn.getStyleClass().add("primary-button");

        this.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(title, Priority.ALWAYS);
        HBox.setHgrow(joinBtn, Priority.NEVER);


        leftNav.getChildren().addAll(closeBtn, title);
        leftNav.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(leftNav, Priority.ALWAYS);

        this.getChildren().addAll(leftNav, joinBtn);
    }

    public void actions() {
        Stage stage = StageManager.getInstance();
        closeBtn.setOnMouseClicked(e -> {
            new Home(stage);
        });
    }

    public JoinClassNav(Stage stage) {
        initialize();
        actions();
        this.getStyleClass().add("nav-bar");
    }

    public Button getJoinBtn() {
        return joinBtn;
    }

}
