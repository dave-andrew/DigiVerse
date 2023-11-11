package view.homeview.task;

import controller.AnswerController;
import controller.MemberController;
import helper.ImageManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Classroom;
import model.ClassroomMember;
import model.Task;
import model.User;
import view.component.classdetail.component.ClassMemberItem;

public class AnswerDetail extends HBox {

    private MemberController memberController;
    private AnswerController answerController;

    private Task task;
    private Classroom classroom;

    private VBox memberContainer, fileContainer;
    private VBox memberList;

    public AnswerDetail(Task task, Classroom classroom) {
        this.memberController = new MemberController();
        this.answerController = new AnswerController();
        this.task = task;
        this.classroom = classroom;
        init();
        setLayout();
    }

    private void init() {
        this.memberContainer = new VBox();
        this.memberList = new VBox();
        this.memberList.setPadding(new Insets(20));

        this.memberContainer.setPrefWidth(500);
        this.memberContainer.setStyle("-fx-border-color: transparent #e0e0e0 transparent transparent;");

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(memberList);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);

        this.memberContainer.getChildren().add(scrollPane);

        this.fileContainer = new VBox();
        this.fileContainer.setPadding(new Insets(20));

        Label fileTitle = new Label("Student Answer");

        this.fileContainer.getChildren().addAll(fileTitle);

        this.getChildren().addAll(memberContainer, fileContainer);
    }

    private void setLayout() {

        fetchMember();

    }

    private void fetchMember() {

        memberController.getClassMember(classroom.getClassId()).forEach(member -> {
            if(member.getRole().equals("Student")) {
                memberList.getChildren().add(memberItem(member));
            }
        });

    }


    private Image profileImg;
    private ImageView profile;

    private HBox memberItem(ClassroomMember member) {
        HBox memberItem = new HBox();

        if(member.getUser().getBlobProfile() != null) {
            profileImg = ImageManager.convertBlobImage(member.getUser().getBlobProfile());
        } else {
            profileImg = new Image("file:resources/icons/user.png");
        }

        this.profile = new ImageView(profileImg);
        this.profile.setFitWidth(35);
        this.profile.setPreserveRatio(true);

        HBox userBox = new HBox();
        userBox.setAlignment(Pos.CENTER_LEFT);
        userBox.setPadding(new Insets(0, 0, 0, 10));

        Label userNameLbl = new Label(member.getUser().getUsername());
        userNameLbl.setStyle("-fx-font-size: 18px;");

        userBox.getChildren().addAll(userNameLbl);

        memberItem.getChildren().addAll(profile, userBox);
        memberItem.getStyleClass().add("task-item");

        memberItem.setOnMouseClicked(e -> {
            fetchAnswer(member);
        });

        return memberItem;
    }

    private void fetchAnswer(ClassroomMember member) {

        answerController.getMemberAnswer(this.task.getId(), member.getUser().getId()).forEach(answer -> {
            System.out.println(answer.getName());
        });

    }

}
