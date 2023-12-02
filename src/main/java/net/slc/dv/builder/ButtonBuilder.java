package net.slc.dv.builder;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

public class ButtonBuilder {

    private final Button button;

    private ButtonBuilder(String text) {
        this.button = new Button(text);
    }

    private ButtonBuilder(Button button) {
        this.button = button;
    }

    public static ButtonBuilder create(Button button) {
        return new ButtonBuilder(button);
    }

    public static ButtonBuilder create(String text) {
        return new ButtonBuilder(text);
    }

    public static ButtonBuilder create() {
        return new ButtonBuilder("");
    }

    public ButtonBuilder setGraphic(ImageView image) {
        this.button.setGraphic(image);

        return this;
    }

    public ButtonBuilder setStyle(String style) {
        this.button.setStyle(style);

        return this;
    }

    public ButtonBuilder setStyleClass(String styleClass) {
        this.button.getStyleClass().add(styleClass);

        return this;
    }

    public ButtonBuilder bindPrefWidth(VBox vBox) {
        this.button.prefWidthProperty().bind(vBox.widthProperty());

        return this;
    }

    public ButtonBuilder setPrefWidth(int width) {
        this.button.setPrefWidth(width);

        return this;
    }

    public ButtonBuilder setVMargin(int margin1, int margin2, int margin3, int margin4) {
        VBox.setMargin(this.button, new Insets(margin1, margin2, margin3, margin4));

        return this;
    }

    public ButtonBuilder setOnAction(Consumer consumer) {
        this.button.setOnAction(e -> consumer.accept(this.button));

        return this;
    }

    public ButtonBuilder setPrefSize(int width, int height) {
        this.button.setPrefSize(width, height);

        return this;
    }

    public ButtonBuilder setAlignment(Pos pos) {
        this.button.setAlignment(pos);

        return this;
    }
    public Button build() {
        return this.button;
    }


}
