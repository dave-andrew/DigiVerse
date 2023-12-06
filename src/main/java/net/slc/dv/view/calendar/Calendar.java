package net.slc.dv.view.calendar;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import net.slc.dv.builder.*;
import net.slc.dv.resources.Icon;
import net.slc.dv.resources.IconStorage;
import net.slc.dv.controller.MemberController;
import net.slc.dv.controller.TaskController;
import net.slc.dv.enums.Days;
import net.slc.dv.model.Classroom;
import net.slc.dv.model.Task;
import net.slc.dv.view.task.task.TaskBase;

public class Calendar extends ScrollPane {

    private VBox calendarContainer;
    private final StackPane mainPane;
    private TaskController taskController;
    private LocalDateTime date;
    private GridPane calendarGrid;
    private Label monthLbl;
    private HBox calendarHeader;
    private Consumer<Classroom> setNavigation;

    public Calendar(StackPane mainPane, Consumer<Classroom> setNavigation) {
        this.mainPane = mainPane;
        this.setNavigation = setNavigation;
        this.taskController = new TaskController();
        init();

        StackPaneBuilder.modify(mainPane)
                .removeAllChildren()
                .addChildren(this)
                .build();
    }

    private void init() {

        date = LocalDateTime.now();

        this.calendarGrid =
                GridPaneBuilder.create().setAlignment(Pos.TOP_CENTER).build();

        this.monthLbl = LabelBuilder.create(date.getMonth() + " " + date.getYear())
                .setStyle("-fx-font-size: 20px")
                .setStyleClass("bold-text")
                .setAlignment(Pos.TOP_CENTER)
                .setFont(Font.font(16))
                .build();

        ImageView leftArrowView = ImageViewBuilder.create()
                .setImage(IconStorage.getIcon(Icon.LEFT_NAV_ARROW))
                .setFitWidth(20)
                .setPreserveRatio(true)
                .build();

        Button prevMonthBtn = ButtonBuilder.create()
                .setGraphic(leftArrowView)
                .setAlignment(Pos.TOP_CENTER)
                .setStyleClass("image-button")
                .setOnAction(e -> updateCalendar(-1))
                .build();

        ImageView rightArrowView = ImageViewBuilder.create()
                .setImage(IconStorage.getIcon(Icon.RIGHT_NAV_ARROW))
                .setFitWidth(20)
                .setPreserveRatio(true)
                .build();

        Button nextMonthBtn = ButtonBuilder.create()
                .setGraphic(rightArrowView)
                .setAlignment(Pos.TOP_CENTER)
                .setStyleClass("image-button")
                .setOnAction(e -> updateCalendar(1))
                .build();

        this.calendarHeader = HBoxBuilder.create()
                .addChildren(prevMonthBtn, monthLbl, nextMonthBtn)
                .setAlignment(Pos.TOP_CENTER)
                .setPadding(20, 0, 20, 0)
                .setSpacing(50)
                .build();

        this.createCalendarLayout();

        VBox calendarContainer = VBoxBuilder.create()
                .addChildren(calendarHeader, calendarGrid)
                .setAlignment(Pos.TOP_CENTER)
                .setPadding(0, 0, 200, 0)
                .setStyleClass("card")
                .setStyle("-fx-effect: null")
                .build();

        ScrollPaneBuilder.modify(this)
                .setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER)
                .setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED)
                .setPrefWidth(1000)
                .setPadding(20, 20, 20, 20)
                .bindPrefWidthProperty(mainPane.widthProperty().subtract(10))
                .setFitToWidth(true)
                .setContent(calendarContainer)
                .build();
    }

    private void createCalendarLayout() {
        calendarGrid.getChildren().clear();

        this.createCalendarHeader(calendarGrid);

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
                    continue;
                }
                if (day <= daysInMonth) {

                    Label dayText = LabelBuilder.create(String.valueOf(day))
                            .setAlignment(Pos.TOP_CENTER)
                            .build();

                    VBox dayPane = VBoxBuilder.create()
                            .setStyleClass("calendar-box")
                            .setPadding(5, 5, 5, 5)
                            .addChildren(dayText)
                            .setPrefHeight(110)
                            .setPrefWidth(160)
                            .setAlignment(Pos.TOP_LEFT)
                            .build();

                    if (col == 0) {
                        VBoxBuilder.modify(dayPane)
                                .setStyleClass("first-column")
                                .build();
                    } else if (row == 1) {
                        VBoxBuilder.modify(dayPane).setStyleClass("first-row").build();
                    } else if (String.valueOf(day).equals("1")) {
                        VBoxBuilder.modify(dayPane)
                                .setStyleClass("first-column")
                                .build();
                    }

                    ArrayList<Task> taskList =
                            this.taskController.fetchTaskByDate(day, yearMonth.getMonthValue(), yearMonth.getYear());

                    this.setDayPaneTask(dayPane, taskList);

                    calendarGrid.add(dayPane, col, row);
                    day++;
                }
            }
        }
    }

    private void createCalendarHeader(GridPane calendarGrid) {
        Days[] daysOfWeek = Days.values();
        for (int i = 0; i < 7; i++) {
            Label dayLabel = LabelBuilder.create(daysOfWeek[i].getDay()).build();

            VBox dayBox = VBoxBuilder.create()
                    .addChildren(dayLabel)
                    .setPrefHeight(40)
                    .setPrefWidth(160)
                    .setAlignment(Pos.TOP_CENTER)
                    .setStyleClass("calendar-day")
                    .build();

            calendarGrid.add(dayBox, i, 0);
        }
    }

    private void setDayPaneTask(VBox dayPane, ArrayList<Task> taskList) {
        for (Task task : taskList) {
            Label taskTitle = LabelBuilder.create(" \u2022 " + task.getTitle())
                    .setWrapText(true)
                    .setMaxWidth(150)
                    .setPadding(0, 0, 0, 5)
                    .setStyleClass("calendar-item")
                    .build();

            LabelBuilder.modify(taskTitle)
                    .setOnMouseEntered(e -> taskTitle.setTextFill(Color.web("#FFFFFF")))
                    .setOnMouseExited(e -> taskTitle.setTextFill(Color.web("#36454F")))
                    .setOnMouseClicked(e -> {
                        String userRole = new MemberController()
                                .getRole(task.getClassroom().getClassId());
                        new TaskBase(mainPane, task, task.getClassroom(), userRole);

                        this.setNavigation.accept(task.getClassroom());
                    })
                    .build();

            VBoxBuilder.modify(dayPane).addChildren(taskTitle).build();
        }
    }

    private void updateCalendar(int direction) {
        date = date.plusMonths(direction);
        monthLbl.setText(date.getMonth() + " " + date.getYear());

        createCalendarLayout();
    }

}
