package view.homeview;

import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import view.component.classroom.ClassCard;

public class ClassroomList extends GridPane {

    public ClassroomList() {
        StackPane sp = new ClassCard("Hello", "World");
        StackPane sp2 = new ClassCard("Hello", "World");
        StackPane sp3 = new ClassCard("Hello", "World");
        StackPane sp4 = new ClassCard("Hello", "World");
        StackPane sp5 = new ClassCard("Hello", "World");
        StackPane sp6 = new ClassCard("Hello", "World");

        this.add(sp, 0, 0);
        this.add(sp2, 1, 0);
        this.add(sp3, 2, 0);
        this.add(sp4, 3, 0);
        this.add(sp5, 0, 1);
        this.add(sp6, 1, 1);

        this.setPadding(new Insets(20));
        this.setHgap(20);
        this.setVgap(20);
    }

}
