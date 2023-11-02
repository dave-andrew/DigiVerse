package view.component.classdetail;

import helper.ImageManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.Classroom;

public class ClassForum extends ScrollPane {

    private Classroom classroom;

    private HBox forumContainer;
    private VBox forumContent;
    private StackPane forumStack;
    private Image classImage;
    private ImageView classBanner;
    private Label className, classDesc;

    private void setLayout() {
        forumContent.setAlignment(Pos.CENTER);

        HBox.setHgrow(forumStack, Priority.ALWAYS);
        forumContainer.setAlignment(Pos.CENTER);

        forumContainer.setMinWidth(1300);

        forumContent.getChildren().add(forumStack);
        forumContainer.getChildren().add(forumContent);
    }

    private void init() {
        forumContent = new VBox();

        forumStack = new StackPane();

        forumContainer = new HBox();

        Rectangle blueBackground = new Rectangle(1000, 250);
        blueBackground.setFill(Color.BLUE);
        blueBackground.setArcWidth(15);
        blueBackground.setArcHeight(15);

        classBanner = new ImageView();

        if (classroom == null || classroom.getClassImage() == null) {
            forumStack.getChildren().add(blueBackground);
        } else {
            classImage = ImageManager.convertBlobImage(classroom.getClassImage());
            classBanner.setImage(classImage);
            forumStack.getChildren().add(classBanner);
        }
        classBanner.getStyleClass().add("class-banner");

        if (classroom == null) {
            className = new Label("Something Unexpected Happened");

            VBox labelsVBox = new VBox(className);
            labelsVBox.setAlignment(Pos.TOP_CENTER);
            forumStack.getChildren().add(labelsVBox);
        } else {
            className = new Label("TESsTTTTTT");
            className.setStyle("-fx-text-fill: white");
            classDesc = new Label("fdjsajlfhdsjklfhjkdsahfjkds");
            classDesc.setStyle("-fx-text-fill: white");

            VBox labelsVBox = new VBox(className, classDesc);
            labelsVBox.setPadding(new Insets(20, 20, 20, 20));
            forumStack.getChildren().add(labelsVBox);
        }
    }

    public ClassForum(Classroom classroom) {
        this.classroom = classroom;

        init();
        setLayout();

        this.setContent(forumContainer);
    }
}
