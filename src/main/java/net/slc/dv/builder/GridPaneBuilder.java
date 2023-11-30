package net.slc.dv.builder;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class GridPaneBuilder {

	private GridPane gridPane;
	private GridPaneBuilder() {
		this.gridPane = new GridPane();
	}
	public static GridPaneBuilder create() {
		return new GridPaneBuilder();
	}

	public GridPaneBuilder addChildren(Node node, int col, int row) {
		this.gridPane.add(node, col, row);
		return this;
	}

	public GridPaneBuilder setHGap(double hGap) {
		this.gridPane.setHgap(hGap);
		return this;
	}

	public GridPaneBuilder setVGap(double vGap) {
		this.gridPane.setVgap(vGap);
		return this;
	}

	public GridPane build() {
		return this.gridPane;
	}
}
