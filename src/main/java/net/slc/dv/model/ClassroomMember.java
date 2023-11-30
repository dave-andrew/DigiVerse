package net.slc.dv.model;

public class ClassroomMember {

    private String classId;
    private User user;
    private String role;

    public ClassroomMember(String classId, User user, String role) {
        this.classId = classId;
        this.user = user;
        this.role = role;
    }


    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
