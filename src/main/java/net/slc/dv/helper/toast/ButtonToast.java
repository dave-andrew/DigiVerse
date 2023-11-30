package net.slc.dv.helper.toast;

import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class ButtonToast extends Toast {

	private Button button;
	private boolean closeOnClick = true;
	private Consumer action;
	public ButtonToast() {
		super();
	}

	public ButtonToast setButtonText(String text) {
		button = new Button(text);

		return this;
	}

	public ButtonToast setButtonAction(Consumer action) {
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
