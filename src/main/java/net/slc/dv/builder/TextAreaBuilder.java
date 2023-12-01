package net.slc.dv.builder;

import javafx.scene.control.TextArea;

public class TextAreaBuilder {

	private TextArea textArea;

	private TextAreaBuilder() {
		this.textArea = new TextArea();
	}

	public static TextAreaBuilder create() {
		return new TextAreaBuilder();
	}

	public TextAreaBuilder setWrapText(boolean wrapText) {
		this.textArea.setWrapText(wrapText);
		return this;
	}

	public TextAreaBuilder setMaxHeight(double maxHeight) {
		this.textArea.setMaxHeight(maxHeight);
		return this;
	}

	public TextAreaBuilder setPromptText(String promptText) {
		this.textArea.setPromptText(promptText);
		return this;
	}

	public TextArea build() {
		return this.textArea;
	}
}
