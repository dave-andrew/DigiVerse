package net.slc.dv.view.component.classtask;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.io.File;

public class FileItem extends HBox {

    private Button removeBtn;

    public FileItem(File file) {

        if (getFileType(file).equals("pdf")) {
            Image img = new Image("file:resources/icons/pdf.png");
            ImageView imgView = new ImageView(img);
            imgView.setFitWidth(20);
            imgView.setFitHeight(20);

            this.getChildren().add(imgView);
        } else if (getFileType(file).equals("png") || getFileType(file).equals("jpg") || getFileType(file).equals("jpeg")) {
            Image img = new Image("file:resources/icons/image.png");
            ImageView imgView = new ImageView(img);
            imgView.setFitWidth(20);
            imgView.setFitHeight(20);

            this.getChildren().add(imgView);
        } else {
            Image img = new Image("file:resources/icons/file.png");
            ImageView imgView = new ImageView(img);
            imgView.setFitWidth(20);
            imgView.setFitHeight(20);

            this.getChildren().add(imgView);
        }

        Label fileName = new Label(file.getName());
        fileName.setStyle("-fx-font-size: 14px;");
        this.getChildren().add(fileName);

        Image closeImg = new Image("file:resources/icons/close.png");
        ImageView closeIcon = new ImageView(closeImg);

        this.removeBtn = new Button();
        removeBtn.setGraphic(closeIcon);
        removeBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;-fx-border-style: none; -fx-border-color: transparent; -fx-border-width: 0;");

        closeIcon.setFitWidth(10);
        closeIcon.setFitHeight(10);

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        this.getChildren().addAll(spacer, removeBtn);

        HBox.setMargin(fileName, new Insets(0, 0, 0, 10));

        this.getStyleClass().add("card");
        this.setPadding(new Insets(3, 5, 3, 5));
        this.setPrefWidth(225);
        this.setAlignment(Pos.CENTER_LEFT);
    }

    private String getFileType(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');

        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }

        return "";
    }

    public Button getRemoveBtn() {
        return removeBtn;
    }

}
