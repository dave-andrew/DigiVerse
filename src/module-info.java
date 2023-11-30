module DigiVerse {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.media;
    requires javafx.web;
    requires javafx.swing;
    requires javafx.swt;

    requires java.sql;

    opens main;
    exports main;
}
