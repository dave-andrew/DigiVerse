package helper;

import javafx.application.Platform;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ObservableVariable<T> {
    private final ArrayList<Consumer<T>> listeners = new ArrayList<>();
    private T value;

    public ObservableVariable(T value) {
        this.value = value;
    }

    public void addListener(Consumer<T> listener) {
        listeners.add(listener);
    }

    public void removeListener(Consumer<T> listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for (Consumer<T> listener : listeners) {
            Platform.runLater(() -> listener.accept(value));
        }
    }

    public T getValue() {
        return value;
    }

    public void setValue(T newValue) {
        if (value == null || !value.equals(newValue)) {
            value = newValue;
            notifyListeners();
        }
    }
}
