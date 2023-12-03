package net.slc.dv.builder;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class StackPaneBuilder {
	private StackPane stackPane;
	private StackPaneBuilder() {
		this.stackPane = new StackPane();
	}

	private StackPaneBuilder(StackPane stackPane) {
		this.stackPane = stackPane;
	}

	public static StackPaneBuilder create() {
		return new StackPaneBuilder();
	}

	public static StackPaneBuilder modify(StackPane stackPane) {
		return new StackPaneBuilder(stackPane);
	}

	public StackPaneBuilder setStyleClass(String styleClass) {
		this.stackPane.getStyleClass().add(styleClass);
		return this;
	}

	public StackPaneBuilder setStyle(String style) {
		this.stackPane.setStyle(style);
		return this;
	}

	public StackPaneBuilder setPrefWidth(double width) {
		this.stackPane.setPrefWidth(width);
		return this;
	}

	public StackPaneBuilder setPrefHeight(double height) {
		this.stackPane.setPrefHeight(height);
		return this;
	}

	public StackPaneBuilder setMaxWidth(double width) {
		this.stackPane.setMaxWidth(width);
		return this;
	}

	public StackPaneBuilder setMaxHeight(double height) {
		this.stackPane.setMaxHeight(height);
		return this;
	}

	public StackPaneBuilder addChildren(Node... children) {
		this.stackPane.getChildren().addAll(children);
		return this;
	}

	public StackPaneBuilder removeAllChildren() {
		this.stackPane.getChildren().removeAll(this.stackPane.getChildren());
		return this;
	}

	public StackPaneBuilder setAlignment(Pos pos) {
		this.stackPane.setAlignment(pos);
		return this;
	}

	public StackPane build() {
		return new StackPane();
	}
}
