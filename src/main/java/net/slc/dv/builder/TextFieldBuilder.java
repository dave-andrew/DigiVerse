package net.slc.dv.builder;

import javafx.scene.control.TextField;
import lombok.Getter;

@Getter
public class TextFieldBuilder {

	private TextField textField;
	private TextFieldBuilder() {
		this.textField = new TextField();
	}
	public static TextFieldBuilder create() {
		return new TextFieldBuilder();
	}

	public TextFieldBuilder setPromptText(String promptText) {
		this.textField.setPromptText(promptText);
		return this;
	}

	public TextFieldBuilder setStyle(String style) {
		this.textField.setStyle(style);
		return this;
	}

	public TextFieldBuilder setEditable(boolean editable) {
		this.textField.setEditable(editable);
		return this;
	}

	public TextField build() {
		return this.textField;
	}
}
