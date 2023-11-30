package net.slc.dv.builder;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.HBox;

public class HBoxBuilder {
	private HBox hBox;
	private HBoxBuilder() {
		this.hBox = new HBox();
	}

	private HBoxBuilder(HBox vBox) {
		this.hBox = vBox;
	}

	public static HBoxBuilder create() {
		return new HBoxBuilder();
	}

	public static HBoxBuilder create(HBox vBox) {
		return new HBoxBuilder(vBox);
	}

	public HBoxBuilder setSpacing(int spacing) {
		this.hBox.setSpacing(spacing);

		return this;
	}

	public HBoxBuilder setMargin(int margin1, int margin2, int margin3, int margin4) {
		HBox.setMargin(this.hBox, new Insets(margin1, margin2, margin3, margin4));

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

	public HBoxBuilder setAlignment(Pos pos) {
		this.hBox.setAlignment(pos);

		return this;
	}

	public HBoxBuilder setStyleClass(String styleClass){
		this.hBox.getStyleClass().add(styleClass);

		return this;
	}

	public HBoxBuilder setHgrow(Priority priority){
		HBox.setHgrow(this.hBox, priority);

		return this;
	}

	public HBox build() {
		return this.hBox;
	}
}
