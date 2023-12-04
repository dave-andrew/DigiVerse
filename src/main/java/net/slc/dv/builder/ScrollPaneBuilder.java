package net.slc.dv.builder;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;

public class ScrollPaneBuilder {

	private ScrollPane scrollPane;
	private ScrollPaneBuilder() {
		this.scrollPane = new ScrollPane();
	}

	private ScrollPaneBuilder(ScrollPane scrollPane) {
		this.scrollPane = scrollPane;
	}
	public static ScrollPaneBuilder create() {
		return new ScrollPaneBuilder();
	}

	public static ScrollPaneBuilder modify(ScrollPane scrollPane) {
		return new ScrollPaneBuilder(scrollPane);
	}

	public ScrollPaneBuilder setContent(Node node) {
		this.scrollPane.setContent(node);
		return this;
	}

	public ScrollPaneBuilder setPadding(int top, int right, int bottom, int left) {
		this.scrollPane.setPadding(new Insets(top, right, bottom, left));
		return this;
	}

	public ScrollPaneBuilder setPannable(boolean pannable) {
		this.scrollPane.setPannable(pannable);
		return this;
	}

	public ScrollPaneBuilder setFitToWidth(boolean fitToWidth) {
		this.scrollPane.setFitToWidth(fitToWidth);
		return this;
	}

	public ScrollPaneBuilder setHbarPolicy(ScrollPane.ScrollBarPolicy policy) {
		this.scrollPane.setHbarPolicy(policy);
		return this;
	}

	public ScrollPaneBuilder setVbarPolicy(ScrollPane.ScrollBarPolicy policy) {
		this.scrollPane.setVbarPolicy(policy);
		return this;
	}

	public ScrollPaneBuilder setStyleClass(String ...styleClass) {
		this.scrollPane.getStyleClass().addAll(styleClass);
		return this;
	}

	public ScrollPaneBuilder setStyle(String style) {
		this.scrollPane.setStyle(style);
		return this;
	}

	public ScrollPaneBuilder setPrefWidth(double width) {
		this.scrollPane.setPrefWidth(width);
		return this;
	}

	public ScrollPaneBuilder setPrefHeight(double height) {
		this.scrollPane.setPrefHeight(height);
		return this;
	}

	public ScrollPaneBuilder bindPrefWidthProperty(ObservableValue value) {
		this.scrollPane.prefWidthProperty().bind(value);
		return this;
	}

	public ScrollPaneBuilder bindPrefHeightProperty(ObservableValue value) {
		this.scrollPane.prefHeightProperty().bind(value);
		return this;
	}

	public ScrollPaneBuilder setToWidthProperty(boolean value) {
		this.scrollPane.fitToWidthProperty().set(value);
		return this;
	}

	public ScrollPane build() {
		return this.scrollPane;
	}
}
