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
        } else {
            Classroom classroom = classQuery.joinClass(groupCode);
            if(classroom == null) {
                return "Group code not found!";
            } else {
                return "Join Class Success!";
            }
        }
    }

    public ArrayList<Classroom> getUserClassroom() {
        return classQuery.getUserClassrooom();
    }

    public ArrayList<ClassroomMember> getClassMember(String classCode) {
        return classQuery.getClassMember(classCode);
    }
}
