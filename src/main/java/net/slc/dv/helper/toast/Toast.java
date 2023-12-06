package net.slc.dv.helper.toast;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import net.slc.dv.helper.ScreenManager;
import net.slc.dv.helper.StageManager;
import net.slc.dv.helper.ThemeManager;

public class Toast {

    protected Stage toastStage;
    protected HBox root;
    protected VBox box;
    private int fadeInDelay = 500;
    private int toastDelay = 2000;
    private int fadeOutDelay = 500;

    public Toast() {
        init();
    }

    public static void makeError(Stage ownerStage, String errorMsg, int toastDelay, int fadeInDelay, int fadeOutDelay) {
        Stage errorStage = new Stage();
        errorStage.initOwner(ownerStage);
        errorStage.setResizable(false);
        errorStage.initStyle(StageStyle.TRANSPARENT);

        errorStage.setX(ScreenManager.SCREEN_WIDTH / 2 - 130);
        errorStage.setY(0);

        Text text = new Text(errorMsg);
        text.setFill(Color.RED);

        VBox box = new VBox(text);
        VBox.setVgrow(box, Priority.NEVER);
        box.setStyle("-fx-background-radius: 5px; -fx-background-color: rgba(255, 255, 255, 1); -fx-padding: 10px 30px; -fx-min-width: 210px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0)");

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

        showToast(toastDelay, fadeInDelay, fadeOutDelay, errorStage);
    }

    private static void showToast(int toastDelay, int fadeInDelay, int fadeOutDelay, Stage errorStage) {
        // Fade in timeline
        Timeline fadeInTimeline = new Timeline();
        KeyValue fadeInValue = new KeyValue(errorStage.getScene().getRoot().opacityProperty(), 1);
        KeyFrame fadeInKey = new KeyFrame(Duration.millis(fadeInDelay), fadeInValue);
        fadeInTimeline.getKeyFrames().add(fadeInKey);

        // Fade out timeline
        Timeline fadeOutTimeline = new Timeline();
        KeyValue fadeOutValue = new KeyValue(errorStage.getScene().getRoot().opacityProperty(), 0);
        KeyFrame fadeOutKey = new KeyFrame(Duration.millis(fadeOutDelay), fadeOutValue);
        fadeOutTimeline.getKeyFrames().add(fadeOutKey);
        fadeOutTimeline.setOnFinished((evt) -> errorStage.close());

        // Pause Transition
        PauseTransition pause = new PauseTransition(Duration.millis(toastDelay));
        pause.setOnFinished((unused) -> fadeOutTimeline.play());

        // Start fadeInTimeline
        fadeInTimeline.setOnFinished((unused) -> pause.play());
        fadeInTimeline.play();
    }

    protected void init() {
        toastStage = new Stage();
        toastStage.initOwner(StageManager.getInstance());
        toastStage.setResizable(false);
        toastStage.initStyle(StageStyle.TRANSPARENT);

        toastStage.setX(ScreenManager.SCREEN_WIDTH / 2 - 130);
        toastStage.setY(0);

        this.root = new HBox();
    }

    public void build() {
        Scene scene = new Scene(root);
        ThemeManager.getInstance().getTheme(scene);
        scene.setFill(Color.TRANSPARENT);
        toastStage.setScene(scene);

        toastStage.show();
        animate();
    }

    private void animate() {
        showToast(toastDelay, fadeInDelay, fadeOutDelay, toastStage);
    }

    public Toast setText(String toastMsg) {
        Text text = new Text(toastMsg);
        text.setFont(Font.font("Nunito", 18));
        text.setFill(Color.BLACK);

        this.box = new VBox(text);
        VBox.setVgrow(box, Priority.NEVER);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-background-radius: 5px; -fx-background-color: rgba(255, 255, 255, 1); -fx-padding: 10px 30px; -fx-min-width: 210px; -fx-effect: dropshadow(gaussian, grey, 15.0, 0.5, 0, 0);");

        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: transparent;");
        root.setOpacity(0);

        root.getChildren().removeAll();
        root.getChildren().add(box);

        return this;
    }

    public Toast setFadeInDelay(int fadeInDelay) {
        this.fadeInDelay = fadeInDelay;
        return this;
    }

    public Toast setToastDelay(int toastDelay) {
        this.toastDelay = toastDelay;
        return this;
    }

    public Toast setFadeOutDelay(int fadeOutDelay) {
        this.fadeOutDelay = fadeOutDelay;
        return this;
    }

}
