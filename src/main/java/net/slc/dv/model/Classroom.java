package net.slc.dv.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

import java.sql.Blob;
import java.sql.ResultSet;
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


    public Classroom(ResultSet resultSet) {
        try {

            this.classId = resultSet.getString("ClassID");
            this.className = resultSet.getString("CLassName");
            this.classDesc = resultSet.getString("ClassDesc");
            this.classCode = resultSet.getString("ClassCode");
            this.classSubject = resultSet.getString("ClassSubject");
            this.classImage = resultSet.getBlob("ClassImage");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Classroom(String className, String classDesc, String classCode, String classSubject) {
        this.classId = UUID.randomUUID().toString();
        this.className = className;
        this.classDesc = classDesc;
        this.classCode = classCode;
        this.classSubject = classSubject;
    }

}
