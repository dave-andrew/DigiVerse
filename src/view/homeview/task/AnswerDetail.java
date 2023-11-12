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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Classroom;
import model.ClassroomMember;
import model.Task;

import java.util.concurrent.atomic.AtomicInteger;

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

        Label fileTitle = new Label("Choose a student to see the answer!");
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
        fileContainer.getChildren().clear();

        Label fileTitle = new Label("Student Answer");
        this.fileContainer.getChildren().addAll(fileTitle);
        VBox.setMargin(fileTitle, new Insets(0, 0, 20, 0));

        GridPane fileList = new GridPane();
        fileList.setHgap(10);  // Horizontal gap between file items
        fileList.setVgap(10);  // Vertical gap between file items

        AtomicInteger rowIndex = new AtomicInteger();
        AtomicInteger colIndex = new AtomicInteger();

        answerController.getMemberAnswer(this.task.getId(), member.getUser().getId()).forEach(answer -> {

            HBox fileItem = new HBox();

            if(answer.getName().endsWith(".pdf")) {
                Image image = new Image("file:resources/icons/pdf.png");
                ImageView icon = new ImageView(image);

                icon.setFitWidth(25);
                icon.setPreserveRatio(true);

                fileItem.getChildren().addAll(icon);

            } else if(answer.getName().endsWith(".png") || answer.getName().endsWith(".jpg") || answer.getName().endsWith(".jpeg")) {

                Image image = new Image("file:resources/icons/image.png");
                ImageView icon = new ImageView(image);

                icon.setFitWidth(25);
                icon.setPreserveRatio(true);

                fileItem.getChildren().addAll(icon);

            } else {

                Image image = new Image("file:resources/icons/file.png");
                ImageView icon = new ImageView(image);

                icon.setFitWidth(25);
                icon.setPreserveRatio(true);

                fileItem.getChildren().addAll(icon);

            }

            Label fileName = new Label(answer.getName());
            HBox.setMargin(fileName, new Insets(0, 0, 0, 10));

            fileItem.getChildren().addAll(fileName);
            fileItem.getStyleClass().add("card");

            fileList.add(fileItem, colIndex.get(), rowIndex.get());

            colIndex.getAndIncrement();

            // Move to the next row if the current row is filled
            if (colIndex.get() >= 3) {
                colIndex.set(0);
                rowIndex.getAndIncrement();
            }

            fileItem.setOnMouseClicked(e -> {
                answerController.downloadAnswer(answer);
            });
        });

        this.fileContainer.getChildren().addAll(fileList);
    }
}
