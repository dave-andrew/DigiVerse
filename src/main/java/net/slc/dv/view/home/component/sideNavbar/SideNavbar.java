package net.slc.dv.view.home.component.sideNavbar;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.Getter;
import net.slc.dv.builder.HBoxBuilder;
import net.slc.dv.builder.ImageViewBuilder;
import net.slc.dv.builder.LabelBuilder;
import net.slc.dv.builder.VBoxBuilder;
import net.slc.dv.constant.Icon;
import net.slc.dv.controller.AuthController;
import net.slc.dv.helper.StageManager;
import net.slc.dv.model.LoggedUser;
import net.slc.dv.view.LoginView;

@Getter
public class SideNavbar extends VBox {
    private final List<SideNavbarButton> sideButtons;
    private final AuthController authController;
    private final SideNavbarButton homeButton;
    private final SideNavbarButton calendarButton;
    private final HBox logoutButton;

    public SideNavbar() {
        this.sideButtons = new ArrayList<>();
        this.authController = new AuthController();
        this.homeButton = new SideNavbarButton(Icon.HOME, "Home");
        this.calendarButton = new SideNavbarButton(Icon.CALENDAR, "Calendar");

        this.sideButtons.add(this.homeButton);
        this.sideButtons.add(this.calendarButton);

        this.logoutButton = this.LogoutButton();

        Label logOutLbl = new Label("Log Out");
        logOutLbl.setStyle("-fx-font-size: 16px; -fx-text-fill: #d70000;");

        HBox.setMargin(logOutLbl, new Insets(0, 0, 0, 10));

        VBox sidebarSpacer = VBoxBuilder.create().setVgrow(Priority.ALWAYS).build();

        VBoxBuilder.modify(this)
                .addChildren(this.sideButtons.stream().map(Node.class::cast).toArray(Node[]::new))
                .addChildren(sidebarSpacer, logoutButton)
                .setStyleClass("side-nav")
                .build();

        this.sideButtons.get(0).setActive();
    }

    public HBox LogoutButton() {

        ImageView icon = ImageViewBuilder.create()
                .setImage(new Image(Icon.LOGOUT))
                .setFitWidth(20)
                .setFitHeight(20)
                .setPreserveRatio(true)
                .build();

        Label label = LabelBuilder.create("Log Out")
                .setStyle("-fx-font-size: 16px; -fx-text-fill: #d70000;")
                .setHMargin(0, 0, 0, 10)
                .build();

        return HBoxBuilder.create()
                .addChildren(icon, label)
                .setSpacing(10)
                .setPrefWidth(200)
                .setMargin(0, 0, 40, 0)
                .setStyleClass("side-nav-item")
                .setOnMouseClicked(e -> logout())
                .setAlignment(Pos.CENTER_LEFT)
                .build();
    }

    private void logout() {
        LoggedUser.getInstance().logout();

        this.authController.removeAuth();

        new LoginView(StageManager.getInstance());
    }

    public void setActive(SideNavbarButton sideNavbarButton) {
        this.sideButtons.forEach(button -> {
            System.out.println("nya");
            if (button.equals(sideNavbarButton)) {
                button.setActive();
            } else {
                button.setInactive();
            }
        });
    }

}
