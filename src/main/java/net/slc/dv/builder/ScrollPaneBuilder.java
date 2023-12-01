package net.slc.dv.builder;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;

public class ScrollPaneBuilder {

	private ScrollPane scrollPane;
	private ScrollPaneBuilder() {
		this.scrollPane = new ScrollPane();
	}
	public static ScrollPaneBuilder create() {
		return new ScrollPaneBuilder();
	}

	public ScrollPaneBuilder setContent(Node node) {
		this.scrollPane.setContent(node);
		return this;
	}

	public ScrollPaneBuilder setPadding(Insets insets) {
		this.scrollPane.setPadding(insets);
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

	public ScrollPane build() {
		return this.scrollPane;
	}
}
