package net.slc.dv.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskComment extends Comment {

    private String taskid;
    private Task task;

    public TaskComment(String id, String text, String userid, User user, String createdAt, String taskid, Task task) {
        super(id, text, userid, user, createdAt);

        this.taskid = taskid;
        this.task = task;
    }

    public TaskComment(String text, String userid, User user, String taskid) {
        super(text, userid, user);
        this.taskid = taskid;
    }

    public TaskComment(String replyid, String text, String userid, User user) {
        super(replyid, text, userid, user);
    }

}
