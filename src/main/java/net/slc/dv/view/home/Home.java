package net.slc.dv.view.home;

import java.util.ArrayList;
import java.util.Objects;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.slc.dv.builder.*;
import net.slc.dv.constant.Icon;
import net.slc.dv.helper.ImageManager;
import net.slc.dv.helper.ScreenManager;
import net.slc.dv.helper.ThemeManager;
import net.slc.dv.model.Classroom;
import net.slc.dv.model.LoggedUser;
import net.slc.dv.view.CreateClass;
import net.slc.dv.view.JoinClass;
import net.slc.dv.view.home.component.Calendar;
import net.slc.dv.view.home.component.Navbar;
import net.slc.dv.view.home.component.Profile;
import net.slc.dv.view.home.component.sideNavbar.SideNavbar;
import net.slc.dv.view.home.component.sideNavbar.SideNavbarButton;
import net.slc.dv.view.homeview.ClassroomList;

public class Home {


	public static ArrayList<Classroom> teacherClassList = new ArrayList<>();
	public static ArrayList<Classroom> studentClassList = new ArrayList<>();
	private Stage stage;
	private Scene scene;
	private BorderPane borderPane;
	private GridPane classGrid;
	private ImageView userImg;

	private StackPane mainPane;
	private ScrollPane scrollPane;

	private Button plusBtn;
	private SideNavbar sideNavbar;
	private Navbar navbar;


	public Home(Stage stage) {
		this.stage = stage;
		initialize();
		scene = setLayout();

		stage.setScene(scene);
		stage.setTitle("DigiVerse");
	}

	private void fetchClass() {

		mainPane.getChildren().clear();
		classGrid = new ClassroomList(mainPane, this.navbar::setLeftNavigation);
		scrollPane.setContent(classGrid);

		mainPane.getChildren().add(scrollPane);
	}

	private void initialize() { // semua komponen yang di ho

		borderPane = new BorderPane();
		mainPane = new StackPane();


		scrollPane = new ScrollPane();
		mainPane.getChildren().add(scrollPane);

		this.navbar = new Navbar(stage, this::onNavbarButtonClick);
		this.sideNavbar = new SideNavbar(this::onSidebarButtonClick);

		classGrid = new ClassroomList(mainPane, this.navbar::setLeftNavigation);
		scrollPane.setContent(classGrid);
	}

	private Scene setLayout() { // masuk2in nya
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

		scrollPane.setPannable(true);

		//        TODO: Add class with role as teacher and student



		borderPane.setCenter(mainPane);
		borderPane.setLeft(sideNavbar);
		borderPane.setTop(navbar);

		scene = new Scene(borderPane, ScreenManager.SCREEN_WIDTH, ScreenManager.SCREEN_HEIGHT);
		ThemeManager.getTheme(scene);

		return scene;
	}

	private void onSidebarButtonClick(Node button){
		this.navbar.setLeftNavigation(null);
		if(button == this.sideNavbar.getHomeButton()){
			this.sideNavbar.setActive((SideNavbarButton) button);
			mainPane.getChildren().clear();
			fetchClass();
			return;
		}
		if(button == this.sideNavbar.getCalendarButton()){
			this.sideNavbar.setActive((SideNavbarButton) button);
			mainPane.getChildren().clear();
			mainPane.getChildren().add(new Calendar(mainPane, this.navbar::setLeftNavigation));
			return;
		}
	}

	private void onNavbarButtonClick(Node button) {
		mainPane.getChildren().clear();

		if(button == this.navbar.getIconButton()) {
			this.navbar.setLeftNavigation(null);
			this.sideNavbar.setActive(this.sideNavbar.getHomeButton());
			fetchClass();
			return;
		}
		if(button == this.navbar.getUserButton()) {
			this.navbar.setLeftNavigation(null);
			this.sideNavbar.setActive(null);
			profilePage();
			return;
		}
		if(button == this.navbar.getThemeSwitchButton()){
			//TODO idk
			ThemeManager.toggleTheme(scene, (ToggleButton) button);
			return;
		}
	}


	public void profilePage() {
		VBox profile = new Profile(userImg, mainPane, this.navbar::setLeftNavigation);

		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setContent(profile);

		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

		scrollPane.prefWidthProperty().bind(mainPane.widthProperty());
		profile.prefWidthProperty().bind(scrollPane.widthProperty());

		mainPane.getChildren().add(scrollPane);
	}

}
