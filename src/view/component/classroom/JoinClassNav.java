package view.component.classroom;

import controller.ClassController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class JoinClassNav extends HBox {

    private Button joinBtn;
    private Button closeBtn;

    private void initialize() {
        HBox leftNav = new HBox(20);

        Image closeImg = new Image("file:resources/icons/close.png");
        ImageView close = new ImageView(closeImg);
        close.setFitWidth(17);
        close.setFitHeight(17);

        closeBtn = new Button();
        closeBtn.setGraphic(close);
        closeBtn.getStyleClass().add("image-button");

        Label title = new Label("Join Class");
        title.getStyleClass().add("title");
        title.setPadding(new Insets(15, 0, 15, 0));

        joinBtn = new Button("Join");
        joinBtn.getStyleClass().add("primary-button");
        joinBtn.setStyle("-fx-text-fill: white");

        this.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(title, Priority.ALWAYS);
        HBox.setHgrow(joinBtn, Priority.NEVER);

        leftNav.getChildren().addAll(closeBtn, title);
        leftNav.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(leftNav, Priority.ALWAYS);

        this.getChildren().addAll(leftNav, joinBtn);
    }

    public void actions() {
        closeBtn.setOnMouseClicked(e -> {
            Stage currentStage = (Stage) getScene().getWindow();
            currentStage.close();
        });
    }

    public JoinClassNav() {
        initialize();
        actions();
        this.getStyleClass().add("nav-bar");
    }

    public Button getJoinBtn() {
        return joinBtn;
    }
}
