package net.slc.dv.view.component.classroom;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GroupCodeForm extends VBox {

    private Label groupCodeLbl, groupCodeDesc;
    private TextField groupCodeField;

    public GroupCodeForm(Label errorLbl) {
        this.setSpacing(20);
        this.setPadding(new Insets(20));

        groupCodeLbl = new Label("Group Code");
        groupCodeLbl.setStyle("-fx-font-size: 20px;");

        HBox titleBox = new HBox();
        titleBox.getChildren().add(groupCodeLbl);
        titleBox.getStyleClass().add("bottom-border");

        groupCodeDesc = new Label("Enter the group code given by your teacher");

        groupCodeField = new TextField();
        groupCodeField.setPromptText("Group Code");

        this.getChildren().addAll(titleBox, groupCodeDesc, groupCodeField, errorLbl);

    }

    public String getGroupCode() {
        return groupCodeField.getText();
    }

}
