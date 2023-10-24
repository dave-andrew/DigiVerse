package view.homeview;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.time.LocalDateTime;
import java.time.YearMonth;

public class Calendar extends VBox {

    private LocalDateTime date;

    private GridPane calendarGrid;
    private Label monthLbl;

    private HBox calendarHeader;
    private Button prevMonthBtn, nextMonthBtn;

    public Calendar() {
        init();
        createCalendarLayout();
    }

    private void init() {
        date = LocalDateTime.now();

        calendarHeader = new HBox(50);

        calendarGrid = new GridPane();

        monthLbl = new Label(date.getMonth() + " " + date.getYear());

        monthLbl.setAlignment(Pos.TOP_CENTER);
        monthLbl.setFont(Font.font(16));

        Image leftArrow = new Image("file:resources/icons/left-nav.png");
        ImageView leftArrowView = new ImageView(leftArrow);
        leftArrowView.setFitWidth(20);
        leftArrowView.setPreserveRatio(true);
        prevMonthBtn = new Button();
        prevMonthBtn.setGraphic(leftArrowView);
        prevMonthBtn.setAlignment(Pos.TOP_CENTER);
        prevMonthBtn.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0;-fx-cursor: hand");

        Image rightArrow = new Image("file:resources/icons/right-nav.png");
        ImageView rightArrowView = new ImageView(rightArrow);
        rightArrowView.setFitWidth(20);
        rightArrowView.setPreserveRatio(true);
        nextMonthBtn = new Button();
        nextMonthBtn.setGraphic(rightArrowView);
        nextMonthBtn.setAlignment(Pos.TOP_CENTER);
        nextMonthBtn.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0;-fx-cursor: hand");

        calendarHeader.getChildren().addAll(prevMonthBtn, monthLbl, nextMonthBtn);
        calendarHeader.setAlignment(Pos.TOP_CENTER);
        calendarHeader.setPadding(new Insets(20, 0, 20, 0));

        calendarGrid.setAlignment(Pos.CENTER);

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
            Label dayLabel = new Label(daysOfWeek[i]);
            dayLabel.setStyle("-fx-font-weight: bold");
            calendarGrid.add(dayLabel, i, 0);
        }

        YearMonth yearMonth = YearMonth.of(date.getYear(), date.getMonthValue());
        int daysInMonth = yearMonth.lengthOfMonth();
        int dayOfWeek = yearMonth.atDay(1).getDayOfWeek().getValue();

        int day = 1;

        for (int row = 1; row < 7; row++) {
            for (int col = 0; col < 7; col++) {
                if (row == 1 && col < dayOfWeek) {
                    StackPane emptyCell = new StackPane();
                    calendarGrid.add(emptyCell, col, row);
                } else if (day <= daysInMonth) {
                    StackPane dayPane = new StackPane();
                    dayPane.setStyle("-fx-border-color: #C0C0C0; -fx-border-width: 1; -fx-background-color: #FFFFFF;");
                    Label dayText = new Label(String.valueOf(day));
                    dayPane.getChildren().add(dayText);
                    dayPane.setPrefWidth(160);
                    dayPane.setPrefHeight(110);
                    dayPane.setAlignment(Pos.TOP_LEFT);

                    dayText.setStyle("-fx-font-weight: bold");
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

    private void updateCalendar() {
        monthLbl.setText(date.getMonth() + " " + date.getYear());

        createCalendarLayout();
    }

}
