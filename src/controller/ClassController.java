package controller;

import database.ClassQuery;
import model.Classroom;

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

}
