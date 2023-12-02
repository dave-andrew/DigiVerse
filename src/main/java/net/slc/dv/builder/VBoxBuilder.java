package net.slc.dv.builder;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class VBoxBuilder {
    private final VBox vBox;

    private VBoxBuilder() {
        this.vBox = new VBox();
    }

    private VBoxBuilder(VBox vBox) {
        this.vBox = vBox;
    }

    public static VBoxBuilder create() {
        return new VBoxBuilder();
    }

    public static VBoxBuilder create(VBox vBox) {
        return new VBoxBuilder(vBox);
    }

    public VBoxBuilder setSpacing(int spacing) {
        this.vBox.setSpacing(spacing);

        return this;
    }

    public VBoxBuilder setMargin(int margin1, int margin2, int margin3, int margin4) {
        VBox.setMargin(this.vBox, new Insets(margin1, margin2, margin3, margin4));

        return this;
    }

    public VBoxBuilder setPadding(int top, int right, int bottom, int left) {
        this.vBox.setPadding(new Insets(top, right, bottom, left));

        return this;
    }

    public VBoxBuilder setPadding(int padding) {
        this.vBox.setPadding(new Insets(padding));

        return this;
    }

    public VBoxBuilder addChildren(Node... nodes) {
        this.vBox.getChildren().addAll(nodes);

        return this;
    }

    public VBoxBuilder removeChildren(Node... nodes) {
        this.vBox.getChildren().removeAll(nodes);

        return this;
    }

    public VBoxBuilder removeAll(){
        this.vBox.getChildren().removeAll(this.vBox.getChildren());

        return this;
    }

    public VBoxBuilder setAlignment(Pos pos) {
        this.vBox.setAlignment(pos);

        return this;
    }

    public VBoxBuilder setStyleClass(String styleClass) {
        this.vBox.getStyleClass().add(styleClass);

        return this;
    }

    public VBoxBuilder setVgrow(Priority priority) {
        VBox.setVgrow(this.vBox, priority);

        return this;
    }

    public VBox build() {
        return this.vBox;
    }
}