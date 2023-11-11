package controller;

import database.MemberQuery;
import model.ClassroomMember;

import java.util.ArrayList;

public class MemberController {

    private MemberQuery memberQuery;

    public MemberController() {
        this.memberQuery = new MemberQuery();
    }
    public ArrayList<ClassroomMember> getClassMember(String classCode) {
        return memberQuery.getClassMember(classCode);
    }

    public String getRole(String classCode) {
        return memberQuery.getRole(classCode);
    }

}
