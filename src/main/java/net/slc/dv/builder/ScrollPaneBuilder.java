package net.slc.dv.builder;

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


	public ScrollPane build() {
		return this.scrollPane;
	}
}
