package net.slc.dv.builder;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
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

	public TextFieldBuilder setDisable(boolean disable) {
		this.textField.setDisable(disable);
		return this;
	}

	public TextFieldBuilder setText(String text) {
		this.textField.setText(text);
		return this;
	}

	public TextFieldBuilder setTextFormatter(TextFormatter formatter) {
		this.textField.setTextFormatter(formatter);
		return this;
	}

	public TextField build() {
		return this.textField;
	}
}
