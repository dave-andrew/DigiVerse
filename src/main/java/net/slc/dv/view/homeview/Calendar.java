package net.slc.dv.view.homeview;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import net.slc.dv.controller.MemberController;
import net.slc.dv.controller.TaskController;
import net.slc.dv.model.Classroom;
import net.slc.dv.model.Task;
import net.slc.dv.view.homeview.task.TaskBase;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;

public class Calendar extends VBox {

    private TaskController taskController;

    private LocalDateTime date;

    private GridPane calendarGrid;
    private Label monthLbl;

    private HBox calendarHeader;
    private Button prevMonthBtn, nextMonthBtn;

    private StackPane mainPane;
    private HBox leftNav;
    private Button iconBtn;

    public Calendar(StackPane mainPane, HBox leftNav, Button iconBtn) {
        this.mainPane = mainPane;
        this.leftNav = leftNav;
        this.iconBtn = iconBtn;
        init();
        createCalendarLayout();
    }

    private void init() {
        this.taskController = new TaskController();

        date = LocalDateTime.now();

        calendarHeader = new HBox(50);

        calendarGrid = new GridPane();

        monthLbl = new Label(date.getMonth() + " " + date.getYear());
        monthLbl.getStyleClass().add("bold-text");
        monthLbl.setStyle("-fx-font-size: 20px");

        monthLbl.setAlignment(Pos.TOP_CENTER);
        monthLbl.setFont(Font.font(16));

        Image leftArrow = new Image("file:resources/icons/left-nav.png");
        ImageView leftArrowView = new ImageView(leftArrow);
        leftArrowView.setFitWidth(20);
        leftArrowView.setPreserveRatio(true);
        prevMonthBtn = new Button();
        prevMonthBtn.setGraphic(leftArrowView);
        prevMonthBtn.setAlignment(Pos.TOP_CENTER);
        prevMonthBtn.getStyleClass().add("image-button");

        Image rightArrow = new Image("file:resources/icons/right-nav.png");
        ImageView rightArrowView = new ImageView(rightArrow);
        rightArrowView.setFitWidth(20);
        rightArrowView.setPreserveRatio(true);
        nextMonthBtn = new Button();
        nextMonthBtn.setGraphic(rightArrowView);
        nextMonthBtn.setAlignment(Pos.TOP_CENTER);
        nextMonthBtn.getStyleClass().add("image-button");

        calendarHeader.getChildren().addAll(prevMonthBtn, monthLbl, nextMonthBtn);
        calendarHeader.setAlignment(Pos.TOP_CENTER);
        calendarHeader.setPadding(new Insets(20, 0, 20, 0));

        calendarGrid.setAlignment(Pos.TOP_CENTER);

        prevMonthBtn.setOnAction(e -> {
            date = date.minusMonths(1);
            updateCalendar();
        });

        nextMonthBtn.setOnAction(e -> {
            date = date.plusMonths(1);
            updateCalendar();
        });
    }

    private void createCalendarLayout() {
        calendarGrid.getChildren().clear();
        String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (int i = 0; i < 7; i++) {
            VBox dayBox = new VBox();
            dayBox.setPrefWidth(160);
            dayBox.setPrefHeight(40);
            dayBox.setAlignment(Pos.TOP_CENTER);

            dayBox.getStyleClass().add("calendar-day");

            Label dayLabel = new Label(daysOfWeek[i]);

            dayBox.getChildren().add(dayLabel);

            calendarGrid.add(dayBox, i, 0);
        }

        YearMonth yearMonth = YearMonth.of(date.getYear(), date.getMonthValue());
        int daysInMonth = yearMonth.lengthOfMonth();
        int dayOfWeek = yearMonth.atDay(1).getDayOfWeek().getValue();

        int day = 1;

        for (int row = 1; row < 7; row++) {
            for (int col = 0; col < 7; col++) {
                if (row == 1 && col < dayOfWeek) {
                    StackPane emptyCell = new StackPane();
                    if (dayOfWeek - 1 == col) {
                        emptyCell.getStyleClass().add("rb");
                    } else {
                        emptyCell.getStyleClass().add("r");
                    }
                    calendarGrid.add(emptyCell, col, row);
                } else if (day <= daysInMonth) {
                    VBox dayPane = new VBox();
                    dayPane.getStyleClass().add("calendar-box");

                    dayPane.setPadding(new Insets(5));
                    Label dayText = new Label(String.valueOf(day));
                    dayPane.getChildren().add(dayText);
                    dayPane.setPrefWidth(160);
                    dayPane.setPrefHeight(110);
                    dayPane.setAlignment(Pos.TOP_LEFT);

                    if (col == 0) {
                        dayPane.getStyleClass().add("first-column");
                    } else if (row == 1) {
                        dayPane.getStyleClass().add("first-row");
                    } else if (String.valueOf(day).equals("1")) {
                        dayPane.getStyleClass().add("first-column");
                    }

                    ArrayList<Task> taskList = fetchTask(day, yearMonth.getMonthValue(), yearMonth.getYear());

                    for (Task task : taskList) {
                        Label taskTitle = new Label(" • " + task.getTitle());
                        taskTitle.setWrapText(true);
                        taskTitle.setMaxWidth(150);
                        taskTitle.setPadding(new Insets(0, 0, 0, 5));
                        taskTitle.getStyleClass().add("calendar-item");

                        taskTitle.setOnMouseEntered(e -> {
                            taskTitle.setTextFill(Color.web("#FFFFFF"));
                        });

                        taskTitle.setOnMouseExited(e -> {
                            taskTitle.setTextFill(Color.web("#36454F"));
                        });

                        taskTitle.setOnMouseClicked(e -> {
                            this.mainPane.getChildren().clear();

                            String userRole = new MemberController().getRole(task.getClassroom().getClassId());
                            this.mainPane.getChildren().add(new TaskBase(task, task.getClassroom(), userRole));

                            setLeftNav(task.getClassroom());
                        });

                        dayPane.getChildren().add(taskTitle);
                    }

                    dayText.setAlignment(Pos.TOP_CENTER);

                    calendarGrid.add(dayPane, col, row);
                    day++;
                }
            }
        }

        this.getChildren().add(calendarHeader);
        this.getChildren().add(calendarGrid);
        this.setAlignment(Pos.TOP_CENTER);
    }

    private ArrayList<Task> fetchTask(int day, int month, int year) {

        String date = year + "-" + month + "-" + day;

        return this.taskController.fetchTaskByDate(date);
    }

    private void updateCalendar() {
        monthLbl.setText(date.getMonth() + " " + date.getYear());

        createCalendarLayout();
    }

    private void setLeftNav(Classroom classroom) {
        Image image = new Image("file:resources/icons/right-arrow.png");
        ImageView icon = new ImageView(image);

        icon.setFitWidth(25);
        icon.setPreserveRatio(true);

        Label lbl = new Label(classroom.getClassName());
        lbl.setStyle("-fx-font-size: 16px;");

        lbl.setOnMouseEntered(e -> {
            lbl.setStyle("-fx-underline: true;-fx-cursor: hand;");
        });

        lbl.setOnMouseExited(e -> {
            lbl.setStyle("-fx-underline: false;");
        });

        lbl.setOnMouseClicked(e -> {
            String userRole = new MemberController().getRole(classroom.getClassId());
            BorderPane classDetail = new ClassroomDetail(classroom, userRole, mainPane);

            setLeftNav(classroom);

            mainPane.getChildren().clear();
            mainPane.getChildren().add(classDetail);
        });

        this.leftNav.getChildren().clear();

        this.leftNav.getChildren().addAll(iconBtn, icon, lbl);
    }
}
