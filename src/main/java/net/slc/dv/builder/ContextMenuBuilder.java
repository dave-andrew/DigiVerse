package net.slc.dv.builder;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class ContextMenuBuilder {

	private ContextMenu contextMenu;

	private ContextMenuBuilder() {
		contextMenu = new ContextMenu();
	}

	public static ContextMenuBuilder create() {
		return new ContextMenuBuilder();
	}

	public ContextMenuBuilder setStyleClass(String styleClass) {
		contextMenu.getStyleClass().add(styleClass);
		return this;
	}

	public ContextMenuBuilder addItems(MenuItem... items) {
		contextMenu.getItems().addAll(items);
		return this;
	}

	public ContextMenu build() {
		return contextMenu;
	}
}
