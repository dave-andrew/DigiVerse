package net.slc.dv.view.component.classdetail.component;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import net.slc.dv.model.Classroom;

public class LeftContent extends HBox {

    private final Label classCode;

    public LeftContent(String role, Classroom classroom) {
        VBox container = new VBox();

        Label classCodeTitle;
        if (role.equals("Teacher")) {
            classCodeTitle = new Label("Class Code:");
            classCode = new Label(classroom.getClassCode());
            classCode.getStyleClass().add("title");

            classCode.setOnMouseClicked(e -> {
                Clipboard clipboard = Clipboard.getSystemClipboard();
                ClipboardContent content = new ClipboardContent();
                content.putString(classCode.getText());
                clipboard.setContent(content);
            });

            container.getChildren().addAll(classCodeTitle, classCode);
        } else {
            classCodeTitle = new Label("Ask the teacher");
            classCode = new Label("for the class code!");
            classCodeTitle.setStyle("-fx-font-size: 14px");
            classCode.setStyle("-fx-font-size: 14px");

            container.getChildren().addAll(classCodeTitle, classCode);
            container.setAlignment(Pos.CENTER);
        }

        VBox.setVgrow(container, Priority.NEVER);

        this.getChildren().add(container);

        HBox.setHgrow(this, Priority.ALWAYS);

        container.getStyleClass().add("small-container");
    }
}
