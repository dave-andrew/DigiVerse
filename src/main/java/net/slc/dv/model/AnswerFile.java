package net.slc.dv.model;

import lombok.Getter;
import lombok.Setter;
import net.slc.dv.helper.DateManager;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class AnswerFile {

    private String id;
    private String taskid;
    private String userid;
    private boolean finished;
    private Integer score;
    private List<File> fileList;
    private String createdAt;
    private String finishedAt;

    public AnswerFile(String taskid, String userid, boolean finished, List<File> fileList, Integer score) {
        this.id = UUID.randomUUID().toString();
        this.taskid = taskid;
        this.userid = userid;
        this.finished = finished;
        this.score = score;
        this.fileList = fileList;
        this.createdAt = DateManager.getNow();
        this.finishedAt = "";
    }

    public boolean getFinished() {
        return this.finished;
    }

}
