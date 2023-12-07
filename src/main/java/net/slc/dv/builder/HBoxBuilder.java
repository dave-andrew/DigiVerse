package net.slc.dv.builder;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class HBoxBuilder {
    private final HBox hBox;

    private HBoxBuilder() {
        this.hBox = new HBox();
    }

    private HBoxBuilder(HBox vBox) {
        this.hBox = vBox;
    }

    public static HBoxBuilder create() {
        return new HBoxBuilder();
    }

    public static HBoxBuilder modify(HBox hBox) {
        return new HBoxBuilder(hBox);
    }

    public HBoxBuilder setSpacing(int spacing) {
        this.hBox.setSpacing(spacing);

        return this;
    }

    public HBoxBuilder setMargin(int margin1, int margin2, int margin3, int margin4) {
        VBox.setMargin(this.hBox, new Insets(margin1, margin2, margin3, margin4));

        return this;
    }

    public HBoxBuilder setLayoutX(double x) {
        this.hBox.setLayoutX(x);

        return this;
    }

    public HBoxBuilder setLayoutY(double y) {
        this.hBox.setLayoutY(y);

        return this;
    }

    public HBoxBuilder setPadding(int padding) {
        this.hBox.setPadding(new Insets(padding));

        return this;
    }

    public HBoxBuilder setPadding(int padding1, int padding2, int padding3, int padding4) {
        this.hBox.setPadding(new Insets(padding1, padding2, padding3, padding4));

        return this;
    }

    public HBoxBuilder addChildren(Node... nodes) {
        this.hBox.getChildren().addAll(nodes);

        return this;
    }

    public HBoxBuilder removeChildren(Node... nodes) {
        this.hBox.getChildren().removeAll(nodes);

        return this;
    }

    public HBoxBuilder removeAllChildren() {
        this.hBox.getChildren().removeAll(this.hBox.getChildren());

        return this;
    }

    public HBoxBuilder setAlignment(Pos pos) {
        this.hBox.setAlignment(pos);

        return this;
    }

    public HBoxBuilder setStyle(String... styleClass) {
        this.hBox.getStyleClass().addAll(styleClass);

        return this;
    }

    public HBoxBuilder setStyleClass(String... styleClass) {
        this.hBox.getStyleClass().addAll(styleClass);

        return this;
    }

    public HBoxBuilder setHgrow(Priority priority) {
        HBox.setHgrow(this.hBox, priority);

        return this;
    }

    public HBoxBuilder setPrefWidth(double width) {
        this.hBox.setPrefWidth(width);

        return this;
    }

    public HBoxBuilder setOnMouseClicked(EventHandler<Event> eventHandler) {
        this.hBox.setOnMouseClicked(eventHandler);

        return this;
    }

    public HBox build() {
        return this.hBox;
    }
}
