package net.slc.dv.model;

import lombok.Getter;
import lombok.Setter;
import net.slc.dv.enums.TaskType;
import net.slc.dv.helper.DateManager;
import net.slc.dv.view.component.classdetail.component.TaskItem;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Getter
@Setter
public class Task {
    private String id;
    private String userid;
    private User user;
    private String title;
    private String description;
    private String deadlineAt;
    private String createdAt;
    private boolean scored;
    private TaskType taskType;

    private Classroom classroom;

    public Task(ResultSet resultSet){
        this(resultSet, null, null);
    }

    public Task(ResultSet resultSet, User user){
        this(resultSet, user, null);
    }

    public Task(ResultSet resultSet, Classroom classroom){
        this(resultSet, null, classroom);
    }

    public Task(ResultSet resultSet, @Nullable User user, @Nullable Classroom classroom){
        try {
            this.id = resultSet.getString("TaskId");
            this.userid = resultSet.getString("UserId");
            this.title = resultSet.getString("TaskTitle");
            this.description = resultSet.getString("TaskDesc");
            this.deadlineAt = resultSet.getString("DeadlineAt");
            this.createdAt = resultSet.getString("CreatedAt");
            this.scored = resultSet.getBoolean("Scored");
            this.taskType = TaskType.valueOf(resultSet.getString("TaskType"));
            this.user = user;
            this.classroom = classroom;
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Task(User user, String title, String description, String deadlineAt, boolean scored, TaskType taskType) {
        this.id = UUID.randomUUID().toString();
        this.userid = user.getId();
        this.user = user;
        this.title = title;
        this.description = description;
        this.deadlineAt = deadlineAt;
        this.createdAt = DateManager.getNow();
        this.scored = scored;
        this.taskType = taskType;
    }

}
