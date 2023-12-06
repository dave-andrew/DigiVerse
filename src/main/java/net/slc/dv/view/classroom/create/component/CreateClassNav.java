package net.slc.dv.view.classroom.create.component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import net.slc.dv.resources.Icon;
import net.slc.dv.resources.IconStorage;

public class CreateClassNav extends HBox {

    private Button closeBtn;

    public CreateClassNav() {
        initialize();
        actions();
        this.getStyleClass().add("nav-bar");
    }

    private void initialize() {
        HBox leftNav = new HBox(20);

        Image closeImg = IconStorage.getIcon(Icon.CLOSE);
        ImageView close = new ImageView(closeImg);
        close.setFitWidth(17);
        close.setFitHeight(17);

        closeBtn = new Button();
        closeBtn.setGraphic(close);
        closeBtn.getStyleClass().add("image-button");

        Label title = new Label("Create Class");
        title.getStyleClass().add("title");
        title.setPadding(new Insets(15, 0, 15, 0));

        Button joinBtn = new Button("Create");
        joinBtn.getStyleClass().add("primary-button");

        this.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(title, Priority.ALWAYS);
        HBox.setHgrow(joinBtn, Priority.NEVER);


        leftNav.getChildren().addAll(closeBtn, title);
        leftNav.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(leftNav, Priority.ALWAYS);

        this.getChildren().addAll(leftNav);
    }

    private void actions() {
        closeBtn.setOnMouseClicked(e -> {
            Stage createClassStage = (Stage) getScene().getWindow();
            createClassStage.close();
        });
    }

}
