package view.component.classdetail;

import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class ClassForum extends ScrollPane {

    private VBox forumContainer;

    public ClassForum() {

        forumContainer = new VBox();


        forumContainer.setAlignment(Pos.CENTER);
        forumContainer.setMinWidth(1000);

        this.setContent(forumContainer);
    }

}
