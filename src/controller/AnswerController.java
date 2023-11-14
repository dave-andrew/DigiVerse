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

    public Integer getAnswerScore(String taskid, String userid) {
        return answerQuery.getAnswerScore(taskid, userid);
    }

    public boolean checkAnswer(String taskid, String userid) {
        return answerQuery.checkAnswer(taskid, userid);
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

    public void downloadAllAnswer(ArrayList<File> fileList) {
        String userHome = System.getProperty("user.home");
        Path downloadsDirectoryPath = Paths.get(userHome, "Downloads");

        try {
            Files.createDirectories(downloadsDirectoryPath);

            for (File file : fileList) {
                Path targetFilePath = downloadsDirectoryPath.resolve(file.getName()).toAbsolutePath();

                Files.copy(file.toPath(), targetFilePath, StandardCopyOption.REPLACE_EXISTING);
            }

            Toast.makeText(StageManager.getInstance(), "Files Downloaded!", 2000, 500, 500);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void markAsDone(String taskid, String userid) {
        answerQuery.markAsDone(taskid, userid);
    }

    public void markUndone(String taskid, String userid) {
        answerQuery.markUndone(taskid, userid);
    }

    public boolean scoreAnswer(String taskid, String userid, String score) {
        if(score.isEmpty()) {
            Toast.makeText(StageManager.getInstance(), "Score cannot be empty!", 2000, 500, 500);
            return false;
        }

        if(!score.matches("[0-9]+")) {
            Toast.makeText(StageManager.getInstance(), "Score must be a number!", 2000, 500, 500);
            return false;
        }

        if(Integer.parseInt(score) < 0 || Integer.parseInt(score) > 100) {
            Toast.makeText(StageManager.getInstance(), "Score must be between 0 and 100!", 2000, 500, 500);
            return false;
        }

        return answerQuery.scoreAnswer(taskid, userid, Integer.parseInt(score));
    }
}
