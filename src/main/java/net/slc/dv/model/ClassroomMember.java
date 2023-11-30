package net.slc.dv.model;

import lombok.Data;

@Data
public class ClassroomMember {

    private String classId;
    private User user;
    private String role;

    public ClassroomMember(String classId, User user, String role) {
        this.classId = classId;
        this.user = user;
        this.role = role;
    }

}
