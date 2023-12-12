package net.slc.dv.view.classroom.join.component;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import net.slc.dv.resources.Text;
import net.slc.dv.storage.TextStorage;

public class GroupCodeForm extends VBox {

    private final TextField groupCodeField;

    public GroupCodeForm(Label errorLbl, HBox joinbtn) {
        this.setSpacing(20);
        this.setPadding(new Insets(20));

        Label groupCodeLbl = new Label(TextStorage.getText(Text.GROUP_CODE));
        groupCodeLbl.setStyle("-fx-font-size: 20px;");

        HBox titleBox = new HBox();
        titleBox.getChildren().add(groupCodeLbl);
        titleBox.getStyleClass().add("bottom-border");

        Label groupCodeDesc = new Label(TextStorage.getText(Text.ENTER_THE_GROUP_CODE));

        groupCodeField = new TextField();
        groupCodeField.setPromptText(TextStorage.getText(Text.GROUP_CODE));

        this.getChildren().addAll(titleBox, groupCodeDesc, groupCodeField, joinbtn, errorLbl);

    }

    public String getGroupCode() {
        return groupCodeField.getText();
    }

}
