package view.component.classroom;

import controller.ClassController;
import helper.StageManager;
import helper.Toast;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.Home;

import java.util.ArrayList;

public class CreateClassForm extends VBox {

    private ClassController classController;

    private Label classNameLbl, classDescLbl, classCodeLbl, classSubjectLbl;
    private TextField classNameField, classDescField, classCodeField;
    private ComboBox<String> classSubjectField;

    private VBox classNameBox, classDescBox, classCodeBox, classSubjectBox;

    private HBox btnBox;
    private Button createBtn, cancelBtn;

    private ArrayList<String> subjects = new ArrayList<>();

    private StackPane stackPane;

    private void initialize() {
        classController = new ClassController();

        stackPane = new StackPane();
        this.getChildren().add(stackPane);

        this.setSpacing(20);
        this.setPadding(new Insets(20));

        classNameLbl = new Label("Class Name");
        classDescLbl = new Label("Class Description");
        classCodeLbl = new Label("Class Code");
        classSubjectLbl = new Label("Class Subject");

        classNameField = new TextField();
        classDescField = new TextField();
        classCodeField = new TextField();
        classSubjectField = new ComboBox<>();

        classNameBox = new VBox(5);
        classDescBox = new VBox(5);
        classCodeBox = new VBox(5);
        classSubjectBox = new VBox(5);

        createBtn = new Button("Create");
        cancelBtn = new Button("Cancel");

        btnBox = new HBox(10);

        subjects.add("Algorithm and Data Structure");
        subjects.add("Database");
        subjects.add("Java");
        subjects.add("Web Design");
        subjects.add("Web Programming");
        subjects.add("Computer Network");
    }

    private void actions() {
        createBtn.setOnAction(e -> {
            String className = classNameField.getText();
            String classDesc = classDescField.getText();
            String classCode = classCodeField.getText();
            String classSubject = classSubjectField.getValue();

            String message = classController.checkCreateClass(className, classDesc, classCode, classSubject);
            Toast.makeText((Stage) stackPane.getScene().getWindow(), message, 2000, 500, 500);

            new Home(StageManager.getInstance());
        });

        cancelBtn.setOnAction(e -> {
            new Home(StageManager.getInstance());
        });
    }

    public CreateClassForm() {
        initialize();
        actions();

        cancelBtn.getStyleClass().add("secondary-button");
        createBtn.getStyleClass().add("primary-button");

        btnBox.getChildren().addAll(cancelBtn, createBtn);
        btnBox.setPadding(new Insets(30, 0, 0, 0));
        btnBox.setAlignment(Pos.CENTER_RIGHT);

        classSubjectField.getItems().addAll(subjects);
        classSubjectField.getSelectionModel().selectFirst();

        classNameBox.getChildren().addAll(classNameLbl, classNameField);
        classDescBox.getChildren().addAll(classDescLbl, classDescField);
        classCodeBox.getChildren().addAll(classCodeLbl, classCodeField);
        classSubjectBox.getChildren().addAll(classSubjectLbl, classSubjectField);

        this.getChildren().addAll(classNameBox, classDescBox, classCodeBox, classSubjectBox, btnBox);
    }

}
