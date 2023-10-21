package view.component;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ClassCard {

    public static StackPane createCard(String className, String classCode) {
        StackPane card = new StackPane();
        card.setPrefSize(250, 250);
        card.getStyleClass().add("card-background");

        VBox cardContent = new VBox(10);

        Label classNameLbl = new Label(className);
        Label classCodeLbl = new Label(classCode);

        cardContent.getChildren().addAll(classNameLbl, classCodeLbl);

        card.getChildren().add(cardContent);

        return card;
    }

}
