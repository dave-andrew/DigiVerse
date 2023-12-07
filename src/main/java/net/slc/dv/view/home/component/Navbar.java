package net.slc.dv.view.home.component;

import java.util.Objects;
import java.util.function.Consumer;

import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lombok.Getter;
import net.slc.dv.builder.*;
import net.slc.dv.resources.Icon;
import net.slc.dv.resources.IconStorage;
import net.slc.dv.controller.MemberController;
import net.slc.dv.helper.ImageManager;
import net.slc.dv.model.Classroom;
import net.slc.dv.model.LoggedUser;
import net.slc.dv.view.classroom.create.CreateClassView;
import net.slc.dv.view.classroom.detail.ClassroomDetailView;
import net.slc.dv.view.classroom.join.JoinClassView;

public class Navbar extends HBox {

	private final ContextMenu plusMenu;
	private final Consumer<Node> onButtonClick;
	private final Stage stage;
	private final HBox leftNav;

	private final StackPane mainPane;
	@Getter
	private final Button userButton;

	@Getter
	private Button plusButton;

	@Getter
	private Button iconButton;

	@Getter
	private ToggleButton themeSwitchButton;

	@Getter
	private MenuItem createClass;

	@Getter
	private MenuItem joinClass;

	public Navbar(StackPane mainPane, Stage stage, Consumer<Node> onButtonClick) {
		this.mainPane = mainPane;
		this.stage = stage;
		this.onButtonClick = onButtonClick;
		this.plusMenu = createPlusMenu();
		this.userButton = createUserButton();
		this.plusButton = getPlusButton();
		this.leftNav = createLeftNav();
		HBox rightNav = createRightNav();

		HBoxBuilder.modify(this)
				.addChildren(leftNav, rightNav)
				.setStyleClass("nav-bar")
				.build();
	}

	public HBox createLeftNav() {
		ImageView icon = ImageViewBuilder.create()
				.bindImageProperty(IconStorage.getIcon(Icon.LOGO))
				.setFitHeight(40)
				.setPreserveRatio(true)
				.build();

		this.iconButton = ButtonBuilder.create()
				.setGraphic(icon)
				.setOnAction(onButtonClick::accept)
				.setStyle(
						"-fx-cursor: hand;-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0;")
				.build();

		return HBoxBuilder.create()
				.addChildren(iconButton)
				.setAlignment(Pos.CENTER_LEFT)
				.setSpacing(15)
				.setHgrow(Priority.ALWAYS)
				.build();
	}

	public HBox createRightNav() {

		this.themeSwitchButton = new ToggleButton();
		themeSwitchButton.setPrefWidth(30);
		themeSwitchButton.setPrefHeight(30);
		themeSwitchButton.setOnAction(e -> {
			onButtonClick.accept((ToggleButton) e.getSource());
		});

		ImageView sun = ImageViewBuilder.create()
				.bindImageProperty(IconStorage.getIcon(Icon.SUN))
				.setFitHeight(20)
				.setFitWidth(20)
				.build();
		themeSwitchButton.setGraphic(sun);

		sun.setFitWidth(30);
		sun.setFitHeight(30);

		themeSwitchButton.getStyleClass().add("image-button");

		ImageView plus = ImageViewBuilder.create()
				.bindImageProperty(IconStorage.getIcon(Icon.PLUS))
				.setFitHeight(20)
				.setFitWidth(20)
				.setPreserveRatio(true)
				.build();

		this.plusButton = ButtonBuilder.create()
				.setGraphic(plus)
				.setStyleClass("image-button")
				.setOnMouseClick(e -> plusMenu.show((Node) e.getSource(), e.getScreenX() - 150, e.getScreenY()))
				.build();

		return HBoxBuilder.create()
				.addChildren(themeSwitchButton, plusButton, userButton)
				.setAlignment(Pos.CENTER_RIGHT)
				.setSpacing(25)
				.setHgrow(Priority.NEVER)
				.build();
	}

	private ContextMenu createPlusMenu() {
		this.createClass = MenuItemBuilder.create()
				.setText("Create Class")
				.setStyleClass("item")
				.setOnAction(e -> new CreateClassView(stage))
				.build();
		this.joinClass = MenuItemBuilder.create()
				.setText("Join Class")
				.setStyleClass("item")
				.setOnAction(e -> new JoinClassView(stage))
				.build();

		return ContextMenuBuilder.create()
				.addItems(createClass, joinClass)
				.setStyleClass("context-menu")
				.build();
	}

	private Button createUserButton() {
		LoggedUser loggedUser = LoggedUser.getInstance();
		ImageView userImageView;

		if (loggedUser != null) {
			Image userImage = loggedUser.getProfile();
			userImageView = ImageViewBuilder.create()
					.bindImageProperty(Objects.isNull(userImage) ? IconStorage.getIcon(Icon.USER) : new SimpleObjectProperty<>(userImage))
					.setFitWidth(40)
					.setFitHeight(40)
					.setPreserveRatio(true)
					.build();
		} else {
			userImageView = ImageViewBuilder.create()
					.bindImageProperty(IconStorage.getIcon(Icon.USER))
					.setFitWidth(40)
					.setFitHeight(40)
					.setPreserveRatio(true)
					.build();
		}

		ImageManager.makeCircular(userImageView, 20);

		return ButtonBuilder.create()
				.setGraphic(userImageView)
				.setStyleClass("image-button")
				.setOnAction(this.onButtonClick::accept)
				.build();
	}

	public void setLeftNavigation(Classroom classroom) {
		if (classroom == null) {
			HBoxBuilder.modify(leftNav)
					.removeAllChildren()
					.addChildren(iconButton)
					.build();
			return;
		}

		ImageView icon = ImageViewBuilder.create()
				.bindImageProperty(IconStorage.getIcon(Icon.RIGHT_ARROW))
				.setFitHeight(18)
				.setPreserveRatio(true)
				.build();

		Label label = LabelBuilder.create(classroom.getClassName())
				.setStyle("-fx-font-size: 16px;")
				.setOnMouseEntered(e -> e.setStyle("-fx-underline: true;-fx-cursor: hand;"))
				.setOnMouseExited(e -> e.setStyle("-fx-underline: false;"))
				.setOnMouseClicked(e -> {
					mainPane.getChildren().clear();
					mainPane.getChildren().add(new ClassroomDetailView(mainPane, classroom, new MemberController().getRole(classroom.getClassId())));
				})
				.build();

		HBoxBuilder.modify(leftNav)
				.removeAllChildren()
				.addChildren(iconButton, icon, label)
				.build();
	}
}
