package view.homeview;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.time.LocalDateTime;
import java.time.YearMonth;

public class Calendar extends VBox {

    private LocalDateTime date;

    private GridPane calendarGrid;
    private Label monthLbl;

    public Calendar() {
        init();
        createCalendarLayout();
    }

    private void init() {
        date = LocalDateTime.now();
        calendarGrid = new GridPane();
        monthLbl = new Label(date.getMonth() + " " + date.getYear());
        monthLbl.setAlignment(Pos.TOP_CENTER);
        monthLbl.setFont(Font.font(16));

        // Adjust the grid's properties
//        calendarGrid.setHgap(5);
//        calendarGrid.setVgap(5);
        calendarGrid.setAlignment(Pos.CENTER);
    }

    private void createCalendarLayout() {
        String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (int i = 0; i < 7; i++) {
            Label dayLabel = new Label(daysOfWeek[i]);
            dayLabel.setStyle("-fx-font-weight: bold");
            calendarGrid.add(dayLabel, i, 0);
        }

        YearMonth yearMonth = YearMonth.of(date.getYear(), date.getMonthValue() - 1);
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

        this.getChildren().add(monthLbl);
        this.getChildren().add(calendarGrid);
        this.setAlignment(Pos.TOP_CENTER);
    }
}
