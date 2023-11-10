package helper;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public final class Toast
{
    public static void makeText(Stage ownerStage, String toastMsg, int toastDelay, int fadeInDelay, int fadeOutDelay) {
        Stage toastStage = new Stage();
        toastStage.initOwner(ownerStage);
        toastStage.setResizable(false);
        toastStage.initStyle(StageStyle.TRANSPARENT);

        toastStage.setX(ScreenManager.SCREEN_WIDTH / 2 - 130);
        toastStage.setY(0);

        Text text = new Text(toastMsg);
        text.setFont(Font.font("Verdana", 18));
        text.setFill(Color.BLACK);

        VBox box = new VBox(text);
        VBox.setVgrow(box, Priority.NEVER);
        box.setStyle("-fx-background-radius: 5px; -fx-background-color: rgba(255, 255, 255, 1); -fx-padding: 10px 30px; -fx-min-width: 210px; -fx-effect: dropshadow(gaussian, grey, 15.0, 0.5, 0, 0);");

        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        StackPane root = new StackPane(box, spacer);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: transparent;");
        root.setOpacity(0);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        toastStage.setScene(scene);
        toastStage.show();

        Timeline fadeInTimeline = new Timeline();
        KeyFrame fadeInKey1 = new KeyFrame(Duration.millis(fadeInDelay), new KeyValue(toastStage.getScene().getRoot().opacityProperty(), 1));
        fadeInTimeline.getKeyFrames().add(fadeInKey1);
        fadeInTimeline.setOnFinished((ae) -> {
            new Thread(() -> {
                try {
                    Thread.sleep(toastDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Timeline fadeOutTimeline = new Timeline();
                KeyFrame fadeOutKey1 = new KeyFrame(Duration.millis(fadeOutDelay), new KeyValue(toastStage.getScene().getRoot().opacityProperty(), 0));
                fadeOutTimeline.getKeyFrames().add(fadeOutKey1);
                fadeOutTimeline.setOnFinished((aeb) -> toastStage.close());
                fadeOutTimeline.play();
            }).start();
        });
        fadeInTimeline.play();
    }

    public static void makeError(Stage ownerStage, String errorMsg, int toastDelay, int fadeInDelay, int fadeOutDelay) {
        Stage errorStage = new Stage();
        errorStage.initOwner(ownerStage);
        errorStage.setResizable(false);
        errorStage.initStyle(StageStyle.TRANSPARENT);

        errorStage.setX(ScreenManager.SCREEN_WIDTH / 2 - 130);
        errorStage.setY(0);

        Text text = new Text(errorMsg);
        text.setFont(Font.font("Verdana", 18));
        text.setFill(Color.RED); // Set the text color for error messages

        VBox box = new VBox(text);
        VBox.setVgrow(box, Priority.NEVER);
        box.setStyle("-fx-background-radius: 5px; -fx-background-color: rgba(255, 255, 255, 1); -fx-padding: 10px 30px; -fx-min-width: 210px; -fx-effect: dropshadow(gaussian, grey, 15.0, 0.5, 0, 0);");

        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        StackPane root = new StackPane(box, spacer);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: transparent;");
        root.setOpacity(0);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        errorStage.setScene(scene);
        errorStage.show();

        Timeline fadeInTimeline = new Timeline();
        KeyFrame fadeInKey1 = new KeyFrame(Duration.millis(fadeInDelay), new KeyValue(errorStage.getScene().getRoot().opacityProperty(), 1));
        fadeInTimeline.getKeyFrames().add(fadeInKey1);
        fadeInTimeline.setOnFinished((ae) -> {
            new Thread(() -> {
                try {
                    Thread.sleep(toastDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Timeline fadeOutTimeline = new Timeline();
                KeyFrame fadeOutKey1 = new KeyFrame(Duration.millis(fadeOutDelay), new KeyValue(errorStage.getScene().getRoot().opacityProperty(), 0));
                fadeOutTimeline.getKeyFrames().add(fadeOutKey1);
                fadeOutTimeline.setOnFinished((aeb) -> errorStage.close());
                fadeOutTimeline.play();
            }).start();
        });
        fadeInTimeline.play();
    }


}
