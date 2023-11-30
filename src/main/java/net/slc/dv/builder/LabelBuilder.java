package net.slc.dv.builder;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class LabelBuilder {
	private Label label;
	private LabelBuilder(String text) {
		this.label = new Label(text);
	}

	private LabelBuilder(Label label) {
		this.label = label;
	}

	public static LabelBuilder create() {
		return new LabelBuilder("");
	}

	public static LabelBuilder create(String text) {
		return new LabelBuilder(text);
	}

	public static LabelBuilder create(Label label) {
		return new LabelBuilder(label);
	}

	public LabelBuilder setStyle(String style){
		this.label.setStyle(style);

		return this;
	}

	public LabelBuilder setStyleClass(String styleClass){
		this.label.getStyleClass().add(styleClass);

		return this;
	}

	public LabelBuilder setPadding(int padding1, int padding2, int padding3, int padding4) {
		this.label.setPadding(new Insets(padding1, padding2, padding3, padding4));

		return this;
	}

	public LabelBuilder setMargin(int margin1, int margin2, int margin3, int margin4) {
		HBox.setMargin(this.label, new Insets(margin1, margin2, margin3, margin4));

		return this;
	}

	public LabelBuilder setHgrow(Priority priority) {
		HBox.setHgrow(this.label, priority);

		return this;
	}

	public Label build() {
		return this.label;
	}
}
