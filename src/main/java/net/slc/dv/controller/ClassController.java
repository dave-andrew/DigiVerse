package net.slc.dv.controller;

import net.slc.dv.database.ClassQuery;
import net.slc.dv.model.Classroom;
import net.slc.dv.model.ClassroomMember;
import net.slc.dv.model.User;

import java.util.ArrayList;

public class ClassController {

    private ClassQuery classQuery;

    public ClassController() {
        this.classQuery = new ClassQuery();
    }

    public String checkCreateClass(String className, String classDesc, String classCode, String classSubject) {
        if(className.isEmpty() || classDesc.isEmpty() || classCode.isEmpty() || classSubject.isEmpty()) {
            return "Please fill all the fields!";
        }

        if(classCode.length() > 8) {
            return "Class code must be less than 8 characters!";
        }

        Classroom classroom = new Classroom(className, classDesc, classCode, classSubject);

        classQuery.createClass(classroom);

        return "Create Class Success!";
    }

    public String checkJoinClass(String groupCode) {
        if(groupCode.isEmpty()) {
            return "Please fill the group code!";
        }
        String classroom = classQuery.joinClass(groupCode);

        if(classroom.equals("no data")) {
            return "Group code not found!";
        }

        if(classroom.equals("ingroup")) {
            return "You are already in the group!";
        }

        return "Class Joined!";
    }

    public ArrayList<Classroom> getUserClassroom() {
        return classQuery.getUserClassroom();
    }
}
