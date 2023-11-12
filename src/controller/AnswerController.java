package controller;

import database.AnswerQuery;
import helper.StageManager;
import helper.Toast;
import model.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class AnswerController {

    private AnswerQuery answerQuery;

    public AnswerController() {
        this.answerQuery = new AnswerQuery();
    }

    public ArrayList<File> getMemberAnswer(String taskid, String userid) {
        return answerQuery.getMemberAnswer(taskid, userid);
    }

    public void downloadAnswer(File file) {
        String userHome = System.getProperty("user.home");
        Path downloadsDirectoryPath = Paths.get(userHome, "Downloads");

        try {
            Files.createDirectories(downloadsDirectoryPath);

            Path targetFilePath = downloadsDirectoryPath.resolve(file.getName()).toAbsolutePath();

            Files.copy(file.toPath(), targetFilePath, StandardCopyOption.REPLACE_EXISTING);

            Toast.makeText(StageManager.getInstance(), "File Downloaded!", 2000, 500, 500);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
