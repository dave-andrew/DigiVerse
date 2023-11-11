package controller;

import database.AnswerQuery;
import model.Task;

import java.io.File;
import java.util.ArrayList;

public class AnswerController {

    private AnswerQuery answerQuery;

    public AnswerController() {
        this.answerQuery = new AnswerQuery();
    }

    public ArrayList<File> getMemberAnswer(String taskid, String userid) {
        return answerQuery.getMemberAnswer(taskid, userid);
    }

}
