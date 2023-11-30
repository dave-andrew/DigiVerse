package net.slc.dv.controller;

import net.slc.dv.database.FileQuery;
import net.slc.dv.model.Answer;
import net.slc.dv.model.LoggedUser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileController {

    private final FileQuery fileQuery;

    public FileController() {
        this.fileQuery = new FileQuery();
    }

    public void uploadTaskAnswer(List<File> fileList, String taskid) {

        Answer answer = new Answer(taskid, LoggedUser.getInstance().getId(), fileList, 0);

        this.fileQuery.uploadTaskAnswer(answer);
    }


}
