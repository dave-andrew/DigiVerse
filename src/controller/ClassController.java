package controller;

import database.ClassQuery;
import model.Classroom;
import model.ClassroomMember;
import model.User;

import java.util.ArrayList;

public class ClassController {

    private ClassQuery classQuery;

    public ClassController() {
        this.classQuery = new ClassQuery();
    }

    public String checkCreateClass(String className, String classDesc, String classCode, String classSubject) {
        if(className.isEmpty() || classDesc.isEmpty() || classCode.isEmpty() || classSubject.isEmpty()) {
            return "Please fill all the fields!";
        } else {
            Classroom classroom = new Classroom(className, classDesc, classCode, classSubject);

            classQuery.createClass(classroom);
            return "Create Class Success!";
        }
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
        return classQuery.getUserClassrooom();
    }
}
