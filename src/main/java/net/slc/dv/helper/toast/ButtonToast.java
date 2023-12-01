package net.slc.dv.helper.toast;

import javafx.scene.control.Button;
import net.slc.dv.builder.ButtonBuilder;

import java.util.function.Consumer;

public class ButtonToast extends Toast {

    private Button button;
    private boolean closeOnClick = true;
    private Consumer<ButtonToast> action;

    public ButtonToast() {
        super();
    }

    public ButtonToast setButtonText(String text) {
        this.button = ButtonBuilder
                .create(text)
                .setStyleClass("primary-button")
                .setStyle("-fx-text-fill: white;")
                .build();
        return this;
    }

    public ButtonToast setButtonAction(Consumer<ButtonToast> action) {
        this.action = action;
        return this;
    }

    public ButtonToast setOnClickClose(boolean closeOnClick) {
        this.closeOnClick = closeOnClick;
        return this;
    }

    @Override
    public void build() {
        button.setOnAction(event -> {
            if (action != null) {
                action.accept(this);
            }

            if (closeOnClick) {
                toastStage.close();
            }
        });

        box.getChildren().add(button);
        super.build();
    }
}
