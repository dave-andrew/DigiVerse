package model;

import helper.DateManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Answer {

    private String id;
    private String taskid;
    private String userid;
    private int score;
    private List<File> fileList;
    private String createdAt;

    public Answer(String id, String taskid, String userid, List<File> fileList, int score, String createdAt) {
        this.id = id;
        this.taskid = taskid;
        this.userid = userid;
        this.score = score;
        this.fileList = fileList;
        this.createdAt = createdAt;
    }

    public Answer(String taskid, String userid, List<File> fileList, int score) {
        this.id = UUID.randomUUID().toString();
        this.taskid = taskid;
        this.userid = userid;
        this.score = score;
        this.fileList = fileList;
        this.createdAt = DateManager.getNow();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<File> getFileList() {
        return fileList;
    }

    public void setFileList(List<File> fileList) {
        this.fileList = fileList;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
