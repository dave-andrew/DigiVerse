package net.slc.dv.view.component.classtask;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import net.slc.dv.builder.HBoxBuilder;
import net.slc.dv.builder.LabelBuilder;
import net.slc.dv.builder.VBoxBuilder;

public class CreateFileTask {

	private TextField titleField;
	private TextArea descriptionField;
	private Label errorLbl;
	private VBox root;


	public CreateFileTask(){

		this.titleField = new TextField();
		this.descriptionField = new TextArea();
		this.errorLbl = LabelBuilder.create()
				.setStyle("-fx-text-fill: red;")
				.build();

		Label title = LabelBuilder.create("Task Title").build();


		Label description = LabelBuilder.create("Task Description")
				.setMargin(30, 0, 0, 0)
				.build();

		Label addFileBtn = LabelBuilder.create()
				.setMargin(100, 0, 0, 0)
				.build();

		HBox errorContainer = HBoxBuilder.create()
				.setAlignment(Pos.CENTER)
				.addChildren(errorLbl)
				.build();

		VBox content = VBoxBuilder.create()
				.addChildren(title, titleField, description, descriptionField, errorContainer)
				.setSpacing(5)
				.setStyleClass("card")
				.build();

		HBox center = HBoxBuilder.create()
				.addChildren(content)
				.setAlignment(Pos.CENTER)
				.build();


		this.root = VBoxBuilder.create()
				.addChildren(center, addFileBtn)
				.setAlignment(Pos.CENTER)
				.build();
	}

	public VBox getRoot() {
		return this.root;
	}

	public TextField getTitleField() {
		return this.titleField;
	}

	public TextArea getDescriptionField() {
		return this.descriptionField;
	}

	public Label getErrorLbl() {
		return this.errorLbl;
	}


}
