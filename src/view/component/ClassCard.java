package view.component;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ClassCard extends StackPane {

    public ClassCard(String className, String classCode) {
        this.setPrefSize(250, 250);
        this.getStyleClass().add("card-background");

        VBox cardContent = new VBox(10);

        Label classNameLbl = new Label(className);
        Label classCodeLbl = new Label(classCode);

        cardContent.getChildren().addAll(classNameLbl, classCodeLbl);

        this.getChildren().add(cardContent);

    }

}
