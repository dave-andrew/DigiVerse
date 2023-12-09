package net.slc.dv.view.classroom.detail.component;

import net.slc.dv.controller.MemberController;
import net.slc.dv.enums.Role;
import net.slc.dv.helper.ImageManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import net.slc.dv.model.Classroom;
import net.slc.dv.resources.Text;
import net.slc.dv.storage.TextStorage;

public class ClassForum extends ClassBase {

    private HBox forumContainer;
    private VBox forumContent;
    private StackPane forumStack;
    private HBox forumHBox;

    public ClassForum(Classroom classroom) {
        super(classroom);

        init();
        setLayout();

        this.setContent(forumContainer);
        this.setPannable(false);
    }

    private void setLayout() {

        HBox.setHgrow(forumStack, Priority.ALWAYS);
        forumContainer.setAlignment(Pos.CENTER);

        forumContainer.prefWidthProperty().bind(this.widthProperty());

        forumContent.getChildren().addAll(forumStack, forumHBox);
        HBox.setHgrow(forumHBox, Priority.ALWAYS);

        forumContainer.getChildren().add(forumContent);
    }

    @Override
    public void init() {
        forumContent = new VBox(20);

        forumStack = new StackPane();
        forumStack.prefWidthProperty().bind(forumContent.widthProperty());

        forumContainer = new HBox();

        Rectangle blueBackground = new Rectangle(1000, 250);
        blueBackground.setStyle("-fx-fill: #377fee");
        blueBackground.setArcWidth(20);
        blueBackground.setArcHeight(20);
        blueBackground.isSmooth();

        ImageView classBanner = new ImageView();

        if (classroom == null || classroom.getClassImage() == null) {
            forumStack.getChildren().add(blueBackground);
        } else {
            Image classImage = ImageManager.convertBlobImage(classroom.getClassImage());
            classBanner.setImage(classImage);
            forumStack.getChildren().add(classBanner);
        }

        classBanner.getStyleClass().add("class-banner");

        Label className;
        if (classroom == null) {
            className = new Label(TextStorage.getText(Text.SOMETHING_UNEXPECTED_HAPPENED));

            VBox labelsVBox = new VBox(className);
            labelsVBox.setAlignment(Pos.TOP_CENTER);
            forumStack.getChildren().add(labelsVBox);
        } else {
            className = new Label(classroom.getClassName());
            className.setStyle("-fx-text-fill: white; -fx-font-size: 40px; -fx-font-family: 'Nunito'");
            Label classDesc = new Label(classroom.getClassDesc());
            classDesc.setStyle("-fx-text-fill: white; -fx-font-size: 20px");

            VBox labelsVBox = new VBox(className, classDesc);
            labelsVBox.setPadding(new Insets(20, 20, 20, 20));
            forumStack.getChildren().add(labelsVBox);

            forumHBox = new HBox(20);
//            forumHBox.setStyle("-fx-background-color: #f5f5f5");


            HBox leftContentContainer = new HBox();
            Role userRole = new MemberController().getRole(classroom.getClassId());
            LeftContent leftContent = new LeftContent(userRole, this.classroom);
            leftContentContainer.getChildren().add(leftContent);

            RightContent rightContent = new RightContent(classroom);
            HBox.setHgrow(rightContent, Priority.ALWAYS);
            rightContent.setPadding(new Insets(0, 0, 40, 0));

            forumHBox.getChildren().addAll(leftContentContainer, rightContent);
//            forumContent.setStyle("-fx-background-color: #000000");
        }

    }
}
