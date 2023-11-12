package view.component.classdetail;

import controller.MemberController;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import model.Classroom;
import view.component.classdetail.component.MemberItem;

import java.util.concurrent.atomic.AtomicInteger;

public class ClassScore extends ClassBase {

    private MemberController memberController;

    private HBox mainContainer;
    private VBox memberContainer, scoreContainer;
    private VBox memberList;
    private ScrollPane members;

    public ClassScore(Classroom classroom) {
        super(classroom);

        setLayout();
    }

    @Override
    public void init() {
        this.memberController = new MemberController();

        this.members = new ScrollPane();

        this.mainContainer = new HBox();
        this.memberContainer = new VBox();
        this.scoreContainer = new VBox();

        this.memberList = new VBox();
        this.memberList.setPadding(new Insets(20));

        this.memberContainer.setPrefWidth(500);
        this.memberContainer.setStyle("-fx-border-color: transparent #e0e0e0 transparent transparent;");

        this.members.setContent(memberList);
        this.memberContainer.getChildren().add(members);

        this.mainContainer.getChildren().addAll(memberContainer, scoreContainer);

        this.setContent(mainContainer);
    }

    private void setLayout() {
        fetchMember();
    }

    private void fetchMember() {
        this.memberList.getChildren().clear();

        this.memberController.getClassMember(classroom.getClassId()).forEach(member -> {
            if(member.getRole().equals("Student")) {
                HBox scoreContainer = new HBox();
                Label score = new Label("Score: ");
                Label scoreValue = new Label("0");

                scoreContainer.getChildren().addAll(score, scoreValue);

                HBox spacer = new HBox();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                MemberItem memberItem = new MemberItem(member);
                this.memberList.getChildren().add(memberItem);
            }
        });
    }

}
