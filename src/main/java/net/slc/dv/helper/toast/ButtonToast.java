package net.slc.dv.helper.toast;

import javafx.scene.control.Button;

import java.util.function.Consumer;

public class ButtonToast extends Toast {

    private Button button;
    private boolean closeOnClick = true;
    private Consumer<ButtonToast> action;

    public ButtonToast() {
        super();
    }

    public ButtonToast setButtonText(String text) {
        this.button = new Button(text);
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

        root.getChildren().add(button);
        super.build();
    }
}
