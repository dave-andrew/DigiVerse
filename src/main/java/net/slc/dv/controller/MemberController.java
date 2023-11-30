package net.slc.dv.controller;

import net.slc.dv.database.MemberQuery;
import net.slc.dv.model.ClassroomMember;

import java.util.ArrayList;

public class MemberController {

    private final MemberQuery memberQuery;

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
