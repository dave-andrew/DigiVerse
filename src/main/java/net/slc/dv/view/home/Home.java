package net.slc.dv.view.home;

import java.util.ArrayList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import net.slc.dv.helper.ScreenManager;
import net.slc.dv.helper.ThemeManager;
import net.slc.dv.model.Classroom;
import net.slc.dv.view.calendar.Calendar;
import net.slc.dv.view.home.component.Navbar;
import net.slc.dv.view.home.component.Profile;
import net.slc.dv.view.home.component.SideNavbar;
import net.slc.dv.view.home.component.SideNavbarButton;
import net.slc.dv.view.classroom.list.ClassroomListView;

public class Home {

    public static ArrayList<Classroom> teacherClassList = new ArrayList<>();
    public static ArrayList<Classroom> studentClassList = new ArrayList<>();
    private final Stage stage;
    private Scene scene;
    private BorderPane borderPane;
    private static GridPane classGrid;

    private static StackPane mainPane;
    private static ScrollPane scrollPane;
    private SideNavbar sideNavbar;
    private static Navbar navbar;

    public Home(Stage stage) {
        this.stage = stage;
        initialize();
        scene = setLayout();

        stage.setScene(scene);
        stage.setTitle("DigiVerse");
    }

    public static void fetchClass() {
        mainPane.getChildren().clear();
        classGrid = new ClassroomListView(mainPane, navbar::setLeftNavigation);
        scrollPane.setContent(classGrid);

        mainPane.getChildren().add(scrollPane);
    }

    private void initialize() { // semua komponen yang di ho

        borderPane = new BorderPane();
        mainPane = new StackPane();

        scrollPane = new ScrollPane();
        mainPane.getChildren().add(scrollPane);

        this.navbar = new Navbar(mainPane, stage, this::onNavbarButtonClick);
        this.sideNavbar = new SideNavbar(this::onSidebarButtonClick);

        classGrid = new ClassroomListView(mainPane, this.navbar::setLeftNavigation);
        scrollPane.setContent(classGrid);
    }

    private Scene setLayout() { // masuk2in nya
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        scrollPane.setPannable(true);

        borderPane.setCenter(mainPane);
        borderPane.setLeft(sideNavbar);
        borderPane.setTop(navbar);

        scene = new Scene(borderPane, ScreenManager.SCREEN_WIDTH, ScreenManager.SCREEN_HEIGHT);
        ThemeManager.getInstance().getTheme(scene);

        return scene;
    }

    private void onSidebarButtonClick(Node button) {
        this.navbar.setLeftNavigation(null);
        if (button == this.sideNavbar.getHomeButton()) {
            this.sideNavbar.setActive((SideNavbarButton) button);
            mainPane.getChildren().clear();
            fetchClass();
            return;
        }
        if (button == this.sideNavbar.getCalendarButton()) {
            this.sideNavbar.setActive((SideNavbarButton) button);

            new Calendar(mainPane, this.navbar::setLeftNavigation);
        }
    }

    private void onNavbarButtonClick(Node button) {

        if (button == this.navbar.getIconButton()) {
            mainPane.getChildren().clear();
            this.navbar.setLeftNavigation(null);
            this.sideNavbar.setActive(this.sideNavbar.getHomeButton());
            fetchClass();
            return;
        }
        if (button == this.navbar.getUserButton()) {
            mainPane.getChildren().clear();
            this.navbar.setLeftNavigation(null);
            this.sideNavbar.setActive(null);
            profilePage((ImageView) this.navbar.getUserButton().getGraphic());
            return;
        }
        if (button == this.navbar.getThemeSwitchButton()) {
            ThemeManager.getInstance().toggleTheme(scene, (ToggleButton) button);
        }
    }


    public void profilePage(ImageView userImg) {
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
