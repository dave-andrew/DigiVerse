package net.slc.dv.controller;

import net.slc.dv.database.FileQuery;
import net.slc.dv.model.AnswerFile;
import net.slc.dv.model.LoggedUser;

import java.io.File;
import java.util.List;

public class FileController {

    private final FileQuery fileQuery;

    public FileController() {
        this.fileQuery = new FileQuery();
    }

    public void uploadTaskAnswer(List<File> fileList, String taskid) {

        AnswerFile answer = new AnswerFile(taskid, LoggedUser.getInstance().getId(), false, fileList, null);

        this.fileQuery.uploadTaskAnswer(answer);
    }


}
