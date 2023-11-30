package net.slc.dv.model;

import java.sql.Blob;
import java.util.UUID;

public class Classroom {

    private String classId;
    private String className;
    private String classDesc;
    private String classCode;
    private String classSubject;
    private Blob classImage;

    public Classroom(String classId, String className, String classDesc, String classCode, String classSubject, Blob classImage) {
        this.classId = classId;
        this.className = className;
        this.classDesc = classDesc;
        this.classCode = classCode;
        this.classSubject = classSubject;
        this.classImage = classImage;
    }

    public Classroom(String className, String classDesc, String classCode, String classSubject) {
        this.classId = UUID.randomUUID().toString();
        this.className = className;
        this.classDesc = classDesc;
        this.classCode = classCode;
        this.classSubject = classSubject;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassDesc() {
        return classDesc;
    }

    public void setClassDesc(String classDesc) {
        this.classDesc = classDesc;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getClassSubject() {
        return classSubject;
    }

    public void setClassSubject(String classSubject) {
        this.classSubject = classSubject;
    }

    public Blob getClassImage() {
        return classImage;
    }

    public void setClassImage(Blob classImage) {
        this.classImage = classImage;
    }
}
