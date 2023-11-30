package net.slc.dv.helper;

import javafx.stage.Stage;

public class StageManager extends Stage {

    private static Stage stage;

    public static Stage getInstance() {
        if(stage == null) {
            stage = new Stage();
        }
        return stage;
    }

}
