package helper;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class ToastManager {

    public static void showMessage(StackPane toastContainer, String message) {
        Label toastLabel = new Label(message);
        toastLabel.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-text-fill: white;");
        toastLabel.setMinWidth(300);

        StackPane toastPane = new StackPane(toastLabel);
        toastPane.setMouseTransparent(true);

        toastContainer.getChildren().add(toastPane);

        Timeline timeline = new Timeline();
        KeyFrame fadeIn = new KeyFrame(Duration.millis(200), new KeyValue(toastPane.opacityProperty(), 1));
        KeyFrame fadeOut = new KeyFrame(Duration.millis(2000), new KeyValue(toastPane.opacityProperty(), 0));
        timeline.getKeyFrames().addAll(fadeIn, fadeOut);

        timeline.setOnFinished(e -> {
            toastContainer.getChildren().remove(toastPane);
        });

        timeline.play();
    }

}
