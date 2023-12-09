package net.slc.dv.controller;

import net.slc.dv.database.ClassQuery;
import net.slc.dv.model.Classroom;
import net.slc.dv.model.ClassroomMember;
import net.slc.dv.model.User;
import net.slc.dv.resources.Text;
import net.slc.dv.storage.TextStorage;

import java.util.ArrayList;

public class ClassController {

    private final ClassQuery classQuery;

    public ClassController() {
        this.classQuery = new ClassQuery();
    }

    public String checkCreateClass(String className, String classDesc, String classCode, String classSubject) {
        if (className.isEmpty() || classDesc.isEmpty() || classCode.isEmpty() || classSubject.isEmpty()) {
            return TextStorage.getText(Text.PLEASE_FILL_ALL_THE_FIELDS);
        }

        if (classCode.length() > 8) {
            return TextStorage.getText(Text.CLASS_CODE_MUST_BE_LESS_THAN_8_CHARACTERS);
        }

        Classroom classroom = new Classroom(className, classDesc, classCode, classSubject);

        classQuery.createClass(classroom);

        return TextStorage.getText(Text.CLASS_CREATED_SUCCESSFULLY);
    }

    public String checkJoinClass(String groupCode) {
        if (groupCode.isEmpty()) {
            return TextStorage.getText(Text.PLEASE_FILL_THE_GROUP_CODE);
        }
        String classroom = classQuery.joinClass(groupCode);

        if (classroom.equals("no data")) {
            return TextStorage.getText(Text.GROUP_CODE_NOT_FOUND);
        }

        if (classroom.equals("ingroup")) {
            return TextStorage.getText(Text.YOU_ARE_ALREADY_IN_THE_GROUP);
        }

        return TextStorage.getText(Text.CLASS_JOINED_SUCCESSFULLY);
    }

    public ArrayList<Classroom> getUserClassroom() {
        return classQuery.getUserClassroom();
    }
}
