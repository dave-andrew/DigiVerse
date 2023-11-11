package controller;

import database.FileQuery;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileController {

    private FileQuery fileQuery;

    public FileController() {
        this.fileQuery = new FileQuery();
    }

    public void uploadTaskAnswer(List<File> fileList, String taskid) {
        this.fileQuery.uploadTaskAnswer(fileList, taskid);
    }


}
