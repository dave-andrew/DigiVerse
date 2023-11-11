package view.component.classtask;

import helper.StageManager;
import helper.ThemeManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UploadFileModal {
    private Scene scene;

    private VBox mainVbox;
    private List<File> uploadedFiles = new ArrayList<>();

    public UploadFileModal() {
        initialize();
        setLayout();
        showAndWait();
    }

    private void initialize() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(StageManager.getInstance());
        dialogStage.initStyle(StageStyle.TRANSPARENT);

        mainVbox = new VBox(20);
        mainVbox.getStyleClass().add("modal");

        scene = new Scene(mainVbox, 600, 600);
        ThemeManager.getTheme(scene);
        dialogStage.setScene(scene);
    }

    private void setLayout() {
        mainVbox.setAlignment(Pos.CENTER);
        VBox.setMargin(mainVbox, new Insets(20));

        Image uploadImg = new Image("file:resources/icons/close.png");
        ImageView uploadIcon = new ImageView(uploadImg);
        uploadIcon.setFitWidth(10);
        uploadIcon.setFitHeight(10);

        HBox closeBox = new HBox();
        closeBox.setAlignment(Pos.CENTER);

        Label uploadLabel = new Label("Upload File");
        uploadLabel.getStyleClass().add("title");

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button closeBtn = new Button();
        closeBtn.setGraphic(uploadIcon);
        closeBtn.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0;");

        closeBox.getChildren().addAll(uploadLabel, spacer, closeBtn);

        closeBox.setAlignment(Pos.TOP_RIGHT);

        closeBtn.setOnMouseClicked(e -> {
            Stage stage = (Stage) mainVbox.getScene().getWindow();
            stage.close();
        });

        mainVbox.getChildren().add(closeBox);

        VBox uploadBox = new VBox(10);
        uploadBox.setAlignment(Pos.CENTER);
        uploadBox.setPadding(new Insets(120, 0, 120, 0));

        Label dropLabel = new Label("Drop your file here");
        uploadBox.setOnDragOver(this::handleDragOver);
        uploadBox.setOnDragDropped(this::handleDragDropped);

        uploadBox.getChildren().add(dropLabel);
        uploadBox.getStyleClass().add("upload-box");

        uploadBox.setOnMouseClicked(e -> {
            chooseFile();
        });

        Button uploadBtn = new Button("+ Upload");
        uploadBtn.prefWidthProperty().bind(mainVbox.widthProperty());
        VBox.setMargin(mainVbox, new Insets(0, 0, 20, 0));

        uploadBtn.setOnMouseClicked(e -> {

        });

        uploadBtn.getStyleClass().add("primary-button");

        mainVbox.getChildren().addAll(uploadBox, uploadBtn);
        mainVbox.setAlignment(Pos.TOP_CENTER);
        mainVbox.setPadding(new Insets(20));
    }

    private void handleDragOver(DragEvent event) {
        if (event.getGestureSource() != event.getTarget() && event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY);
        }
        event.consume();
    }

    private void handleDragDropped(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;

        if (db.hasFiles()) {
            success = true;
            List<File> droppedFiles = db.getFiles();
            uploadedFiles.addAll(droppedFiles);
        }

        event.setDropCompleted(success);
        event.consume();
    }

    private void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Files");
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(mainVbox.getScene().getWindow());

        if (selectedFiles != null) {
            uploadedFiles.addAll(selectedFiles);
        } else {
            System.out.println("No files selected.");
        }
    }

    private void showAndWait() {
        Stage dialogStage = (Stage) mainVbox.getScene().getWindow();
        dialogStage.setTitle("Upload File");
        dialogStage.showAndWait();
    }
}
