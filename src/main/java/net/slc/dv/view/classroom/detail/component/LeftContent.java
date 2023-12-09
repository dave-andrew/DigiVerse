package net.slc.dv.view.classroom.detail.component;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import net.slc.dv.enums.Role;
import net.slc.dv.helper.toast.Toast;
import net.slc.dv.helper.toast.ToastBuilder;
import net.slc.dv.model.Classroom;
import net.slc.dv.resources.Text;
import net.slc.dv.storage.TextStorage;

public class LeftContent extends HBox {

    private final Label classCode;

    public LeftContent(Role role, Classroom classroom) {
        VBox container = new VBox();

        Label classCodeTitle;
        if (role.equals(Role.TEACHER)) {
            classCodeTitle = new Label(TextStorage.getText(Text.CLASS_CODE) + ":");
            classCode = new Label(classroom.getClassCode());
            classCode.getStyleClass().add("title");

            classCode.setOnMouseEntered(e -> {
                classCode.setStyle("-fx-cursor: hand;");
            });

            classCode.setOnMouseExited(e -> {
                classCode.setStyle("-fx-cursor: default;");
            });

            classCode.setOnMouseClicked(e -> {
                Clipboard clipboard = Clipboard.getSystemClipboard();
                ClipboardContent content = new ClipboardContent();
                content.putString(classCode.getText());
                clipboard.setContent(content);
                ToastBuilder.buildNormal()
                        .setText(TextStorage.getText(Text.CLASS_CODE_COPIED))
                        .build();
            });

            container.getChildren().addAll(classCodeTitle, classCode);
        } else {
            classCodeTitle = new Label(TextStorage.getText(Text.ASK_THE_TEACHER));
            classCode = new Label(TextStorage.getText(Text.FOR_THE_CLASS_CODE));
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
