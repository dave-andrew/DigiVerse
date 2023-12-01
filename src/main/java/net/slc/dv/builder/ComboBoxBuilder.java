package net.slc.dv.builder;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;

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

	public ComboBoxBuilder<T> setStyleClass(String styleClass) {
		this.comboBox.getStyleClass().add(styleClass);
		return this;
	}

	public ComboBoxBuilder<T> setValue(T data) {
		this.comboBox.setValue(data);
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
