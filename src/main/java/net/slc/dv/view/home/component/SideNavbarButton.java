package net.slc.dv.view.home.component;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import net.slc.dv.builder.HBoxBuilder;
import net.slc.dv.builder.ImageViewBuilder;
import net.slc.dv.builder.LabelBuilder;

import java.util.function.Consumer;

public class SideNavbarButton extends HBox {
	public SideNavbarButton(String imagePath, String labelString, Consumer<SideNavbarButton> onButtonClick) {
		super();

		ImageView icon = ImageViewBuilder.create()
				.setImage(new Image(imagePath))
				.setFitHeight(25)
				.setPreserveRatio(true)
				.build();

		Label label = LabelBuilder.create(labelString)
				.setStyle("-fx-font-size: 16px;")
				.build();

		HBoxBuilder.modify(this)
				.addChildren(icon, label)
				.setSpacing(10)
				.setPrefWidth(200)
				.setStyleClass("side-nav-item")
				.setAlignment(Pos.CENTER_LEFT)
				.setOnMouseClicked(e -> onButtonClick.accept((SideNavbarButton) e.getSource()))
				.build();
	}

	public void setActive() {
		this.getStyleClass().add("active");
	}

	public void setInactive() {
		this.getStyleClass().remove("active");
	}
}
