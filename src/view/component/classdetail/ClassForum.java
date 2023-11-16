package view.component.classdetail;

import controller.ForumController;
import controller.MemberController;
import helper.ImageManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.Classroom;
import view.component.classdetail.component.LeftContent;
import view.component.classdetail.component.RightContent;

public class ClassForum extends ClassBase {

    private HBox forumContainer;
    private VBox forumContent;
    private StackPane forumStack;
    private Image classImage;
    private ImageView classBanner;
    private Label className, classDesc;


    HBox forumHBox;
    LeftContent leftContent;
    RightContent rightContent;

    private void setLayout() {

        HBox.setHgrow(forumStack, Priority.ALWAYS);
        forumContainer.setAlignment(Pos.CENTER);

        forumContainer.setMinWidth(1300);

        forumContent.getChildren().addAll(forumStack, forumHBox);
        HBox.setHgrow(forumHBox, Priority.ALWAYS);

        forumContainer.getChildren().add(forumContent);
    }

    @Override
    public void init() {
        forumContent = new VBox(20);

        forumStack = new StackPane();

        forumContainer = new HBox();

        Rectangle blueBackground = new Rectangle(1000, 250);
        blueBackground.setFill(Color.BLUE);
        blueBackground.setArcWidth(20);
        blueBackground.setArcHeight(20);
        blueBackground.isSmooth();

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
            className = new Label(classroom.getClassName());
            className.setStyle("-fx-text-fill: white; -fx-font-size: 35px");
            classDesc = new Label(classroom.getClassDesc());
            classDesc.setStyle("-fx-text-fill: white; -fx-font-size: 20px");

            VBox labelsVBox = new VBox(className, classDesc);
            labelsVBox.setPadding(new Insets(20, 20, 20, 20));
            forumStack.getChildren().add(labelsVBox);

            forumHBox = new HBox(20);
//            forumHBox.setStyle("-fx-background-color: #f5f5f5");



            HBox leftContentContainer = new HBox();
            String userRole = new MemberController().getRole(classroom.getClassId());
            leftContent = new LeftContent(userRole, this.classroom);
            leftContentContainer.getChildren().add(leftContent);

            rightContent = new RightContent(classroom);
            HBox.setHgrow(rightContent, Priority.ALWAYS);
            rightContent.setPadding(new Insets(0, 0, 40, 0));

            forumHBox.getChildren().addAll(leftContentContainer, rightContent);
//            forumContent.setStyle("-fx-background-color: #000000");
        }

    }

    public ClassForum(Classroom classroom) {
        super(classroom);

        init();
        setLayout();

        this.setContent(forumContainer);
        this.setPannable(true);
    }
}
