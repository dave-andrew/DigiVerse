package net.slc.dv.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Blob;
import java.util.UUID;

@Getter
@Setter
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

}
