package helper;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public final class Toast {

    private static final Executor executor = Executors.newFixedThreadPool(4);

    public static void makeText(Stage ownerStage, String toastMsg, int toastDelay, int fadeInDelay, int fadeOutDelay) {
        Stage toastStage = initStage(ownerStage);
        Text text = initText(toastMsg, Color.BLACK);

        VBox box = new VBox(text);
        VBox.setVgrow(box, Priority.NEVER);
        box.setStyle("-fx-background-radius: 5px; -fx-background-color: rgba(255, 255, 255, 1); -fx-padding: 10px 30px; -fx-min-width: 210px; -fx-effect: dropshadow(gaussian, grey, 15.0, 0.5, 0, 0);");

        initContentStyle(box, toastStage);
        display(toastDelay, fadeInDelay, fadeOutDelay, toastStage);
    }

    public static void makeError(Stage ownerStage, String errorMsg, int toastDelay, int fadeInDelay, int fadeOutDelay) {
        Stage errorStage = initStage(ownerStage);
        Text text = initText(errorMsg, Color.RED);

        VBox box = new VBox(text);
        VBox.setVgrow(box, Priority.NEVER);
        box.setStyle("-fx-background-radius: 5px; -fx-background-color: rgba(255, 255, 255, 1); -fx-padding: 10px 30px; -fx-min-width: 210px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0)");

        initContentStyle(box, errorStage);
        display(toastDelay, fadeInDelay, fadeOutDelay, errorStage);
    }

    private static Stage initStage(Stage ownerStage) {
        Stage errorStage = new Stage();
        errorStage.initOwner(ownerStage);
        errorStage.setResizable(false);
        errorStage.initStyle(StageStyle.TRANSPARENT);

        errorStage.setX(ScreenManager.SCREEN_WIDTH / 2 - 130);
        errorStage.setY(0);
        return errorStage;
    }

    private static Text initText(String errorMsg, Color color) {
        Text text = new Text(errorMsg);
        text.setFont(Font.font("Verdana", 18));
        text.setFill(color); // Set the text color for error messages
        return text;
    }

    private static void initContentStyle(VBox box, Stage errorStage) {
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
    }

    private static void display(int toastDelay, int fadeInDelay, int fadeOutDelay, Stage stage) {
        Timeline fadeInTimeline = new Timeline();
        KeyFrame fadeInKey = new KeyFrame(Duration.millis(fadeInDelay), new KeyValue(stage.getScene().getRoot().opacityProperty(), 1));
        fadeInTimeline.getKeyFrames().add(fadeInKey);
        fadeInTimeline.setOnFinished((ae) ->
                CompletableFuture.runAsync(() -> {
                    try {
                        Thread.sleep(toastDelay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }, executor).thenRun(() -> {
                    Timeline fadeOutTimeline = new Timeline();
                    KeyFrame fadeOutKey = new KeyFrame(Duration.millis(fadeOutDelay), new KeyValue(stage.getScene().getRoot().opacityProperty(), 0));
                    fadeOutTimeline.getKeyFrames().add(fadeOutKey);
                    fadeOutTimeline.setOnFinished((aeb) -> stage.close());
                    fadeOutTimeline.play();
                }));
        fadeInTimeline.play();
    }

}
