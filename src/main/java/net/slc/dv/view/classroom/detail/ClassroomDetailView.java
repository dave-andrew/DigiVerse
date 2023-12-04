package net.slc.dv.view.classroom.detail;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import lombok.Getter;
import net.slc.dv.builder.StackPaneBuilder;
import net.slc.dv.model.Classroom;
import net.slc.dv.view.classroom.detail.component.ClassDetailNav;

public class ClassroomDetailView extends BorderPane {

    @Getter
    private final Classroom classroom;
    private final String userRole;
    @Getter
    private StackPane mainPane;

    public ClassroomDetailView(StackPane mainPane, Classroom classroom, String userRole) {
        this.mainPane = mainPane;
        this.classroom = classroom;
        this.userRole = userRole;

        init();

//        StackPaneBuilder.modify(mainPane)
//                .removeAllChildren()
//                .addChildren(this)
//                .build();
    }

    private void init() {
        if (!this.userRole.isEmpty()) {
            HBox topBar = new ClassDetailNav(this.userRole, this);
            topBar.getStyleClass().add("nav-bar");
            this.setTop(topBar);
        }
    }

	public void setMainPane(StackPane mainPane) {
        this.mainPane = mainPane;
    }
}
