package net.slc.dv.builder;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ComboBoxBuilder<T> {

	private final ComboBox<T> comboBox;
	private ComboBoxBuilder() {
		this.comboBox = new ComboBox<>();
	}
	public static <T> ComboBoxBuilder<T> create() {
		return new ComboBoxBuilder<T>();
	}

	public ComboBoxBuilder<T> setPromptText(String promptText) {
		this.comboBox.setPromptText(promptText);
		return this;
	}

	@SafeVarargs
	public final ComboBoxBuilder<T> setItems(T... items) {
		this.comboBox.getItems().addAll(items);
		return this;
	}

	public ComboBoxBuilder<T> bindWidthProperty(VBox container, double subtract) {
		this.comboBox.prefWidthProperty().bind(container.widthProperty().subtract(subtract));
		return this;
	}

	public ComboBoxBuilder<T> setHgrow(Priority priority) {
		HBox.setHgrow(this.comboBox, priority);
		return this;
	}

	public ComboBoxBuilder<T> setStyle(String... styles) {
		this.comboBox.setStyle(String.join(";", styles));
		return this;
	}

	public ComboBoxBuilder<T> setStyleClass(String styleClass) {
		this.comboBox.getStyleClass().add(styleClass);
		return this;
	}

	public ComboBoxBuilder<T> setValue(T data) {
		this.comboBox.setValue(data);
		return this;
	}

	public ComboBoxBuilder<T> setMargin(double top, double right, double bottom, double left) {
		HBox.setMargin(this.comboBox, new Insets(top, right, bottom, left));
		return this;
	}

	public ComboBoxBuilder<T> setPrefWidth(double width) {
		this.comboBox.setPrefWidth(width);
		return this;
	}

	public ComboBoxBuilder<T> setOnAction(EventHandler<ActionEvent> eventHandler) {
		this.comboBox.setOnAction(eventHandler);
		return this;
	}

	public ComboBox<T> build() {
		return this.comboBox;
	}
}
