package view.component.classroom;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ClassCard extends StackPane {

    public ClassCard(String className, String classCode) {
        this.setPrefSize(300, 250);
        this.getStyleClass().add("class-card");

        VBox cardContent = new VBox(10);

        Label classNameLbl = new Label(className);
        classNameLbl.getStyleClass().add("bold-text");

        Label classCodeLbl = new Label(classCode);
        classCodeLbl.setWrapText(true);
        cardContent.getStyleClass().add("blue-bg");

        classNameLbl.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");
        classCodeLbl.setStyle("-fx-text-fill: white; -fx-font-size: 12px;");

        cardContent.setMaxHeight(100);
        cardContent.setPadding(new Insets(20));

        cardContent.getChildren().addAll(classNameLbl, classCodeLbl);

        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        this.getChildren().addAll(cardContent, spacer);
        setAlignment(cardContent, Pos.TOP_CENTER);
    }

}
