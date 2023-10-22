package view.component.classroom;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class GroupCodeForm extends VBox {

    private Label groupCodeLbl, groupCodeDesc;
    private TextField groupCodeField;

    public GroupCodeForm() {
        this.setSpacing(20);
        this.setPadding(new Insets(20));

        groupCodeLbl = new Label("Group Code");
        groupCodeDesc = new Label("Enter the group code given by your teacher");

        groupCodeField = new TextField();
        groupCodeField.setPromptText("Group Code");

        this.getChildren().addAll(groupCodeLbl, groupCodeDesc, groupCodeField);

    }

}
