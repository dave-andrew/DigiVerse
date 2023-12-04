package net.slc.dv.view.task.add.component;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;
import net.slc.dv.builder.HBoxBuilder;
import net.slc.dv.builder.LabelBuilder;
import net.slc.dv.builder.VBoxBuilder;

@Getter
public class CreateGeneralTask extends VBox {

    private final TextField titleField;
    private final TextArea descriptionField;
    private final Label errorLbl;

    public CreateGeneralTask() {
        this.titleField = new TextField();
        this.descriptionField = new TextArea();
        this.errorLbl = LabelBuilder.create()
                .setStyle("-fx-text-fill: red;")
                .build();

        Label title = LabelBuilder.create("Task Title").build();

        Label description = LabelBuilder.create("Task Description")
                .setMargin(30, 0, 0, 0)
                .build();

        HBox errorContainer = HBoxBuilder.create()
                .setAlignment(Pos.CENTER)
                .addChildren(errorLbl)
                .build();

        VBox content = VBoxBuilder.create()
                .addChildren(title, titleField, description, descriptionField, errorContainer)
                .setSpacing(5)
                .setStyleClass("card")
                .build();

        HBox center = HBoxBuilder.create()
                .addChildren(content)
                .setAlignment(Pos.CENTER)
                .build();

        VBoxBuilder.modify(this)
                .addChildren(center)
                .setAlignment(Pos.CENTER)
                .build();
    }
}
